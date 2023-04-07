package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.Driver;

import java.awt.*;
import Helper.FileReturn;
import encryption.AES;
import encryption.RSA;
import encryption.Keys;

public class viewShared extends JFrame implements ActionListener
{
    private static String URL = "jdbc:mysql://localhost/files?" +
    "user=root&password=password";
    private String username;
    private Object[][] data;
    private RSA r;
    private Keys key;

    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel topPanel = new JPanel();
    private JPanel tablePanel = new JPanel();
    private JPanel bottomPanel = new JPanel();


    JButton downloadButton = new JButton("Download");
    JButton deleteButton = new JButton("Delete");
    JButton refreshButton = new JButton("Refresh");

    JLabel titleLabel = new JLabel("Your Shared Files");
    JTable fileTable = new JTable();
    JScrollPane scrollPane = new JScrollPane();

    private static String[] columnNames = {"ID", "Name", "Shared by", "Date created"};
    public viewShared(String username){
        this.username = username;
        setSize(500 , 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        try {
            getKeys();
            this.r = new RSA(this.key);
            this.data = updateFileList(this.username, this.r);
            this.fileTable = new JTable(data, columnNames);
        } catch (SQLException e){
            System.out.println("error");
        }

        this.scrollPane = new JScrollPane(this.fileTable);

        tablePanel.add(this.scrollPane);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);
        

        downloadButton.addActionListener(this);
        deleteButton.addActionListener(this);
        refreshButton.addActionListener(this);


        bottomPanel.add(downloadButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(refreshButton);

        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.PAGE_START);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);

        

        setContentPane(mainPanel);
        setVisible(true);

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refreshButton){
            try {
                this.data = updateFileList(this.username, this.r);
                // updates table data
                DefaultTableModel model = new DefaultTableModel(this.data, columnNames);
                this.fileTable.setModel(model);
            } catch (SQLException err){
                System.out.println(err.getMessage());
            }
        } else if(e.getSource() == downloadButton){
            // open a file dialog, with the selected file of the table
            int selectedRow = fileTable.getSelectedRow();
            if(selectedRow != -1){
                try {
                    getRowData(selectedRow);
                } catch (SQLException eee){
                    System.out.println(eee.getMessage());
                }
             } else {
                System.out.println("select");
             }
        } else if(e.getSource() == deleteButton){
            // delete selected entry, then refresh table 
        } 
    }
    
    public void getKeys() throws SQLException{
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement statement = conn.prepareStatement("SELECT PUBLICKEY, PRIVATEKEY FROM USERS WHERE USERNAME = ?");
        statement.setString(1, this.username);
        ResultSet rs = statement.executeQuery();
        rs.next();
        String privatekey = rs.getString("PRIVATEKEY");
        String publicKey = rs.getString("PUBLICKEY");
        this.key = new Keys(publicKey, privatekey);
        rs.close();
        statement.close();
        conn.close();
    }



    public FileReturn getRowData(int selectedRow) throws SQLException{
        String id = fileTable.getValueAt(selectedRow, 0).toString();
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement statement = conn.prepareStatement("SELECT filename, filedata, masterkey FROM SHARED WHERE ID = ?");
        statement.setString(1, id);
        ResultSet rs = statement.executeQuery();
        rs.next();
        String decryptKey = r.decryptBlock(rs.getString("masterkey"));
        AES a = new AES(decryptKey);
        String decryptedName = a.decryptString(rs.getString("filename"));
        byte[] decryptedBytes = a.decryptFile(rs.getBytes("filedata"));
        FileReturn output = new FileReturn(decryptedName, decryptedBytes);
        System.out.println(decryptedName);
        conn.close();
        rs.close();
        statement.close();
        return output;
    }

    public static Object[][] updateFileList(String username, RSA r) throws SQLException{
        ArrayList<Object[]> files = new ArrayList<Object[]>();
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement statement = conn.prepareStatement("SELECT id, filename, sender, datecreated, masterkey FROM SHARED WHERE RECIEVER = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        while(rs.next()){
            String decryptKey = r.decryptBlock(rs.getString("masterkey"));
            AES a = new AES(decryptKey);
            Object[] temp = new Object[4];
            String encryptedFile = rs.getString("filename");
            temp[0] = rs.getInt("id");
            temp[1] = a.decryptString(encryptedFile);
            temp[2] = rs.getString("sender");
            temp[3] = rs.getDate("datecreated");
            files.add(temp);
        }
        Object[][] output = new Object[files.size()][3];
        for(int i = 0; i < files.size(); i++){
            output[i] = files.get(i);
        }

        rs.close();
        statement.close();
        conn.close();
        return output;
    }


    public static void main(String[] args) {
        new viewShared("gabriel2");
    }

}
