package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

public class viewFiles extends JFrame implements ActionListener
{
    private static String URL = "jdbc:mysql://localhost/files?" +
    "user=root&password=password";
    private String username;
    private Object[][] data;



    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel topPanel = new JPanel();
    private JPanel tablePanel = new JPanel();
    private JPanel bottomPanel = new JPanel();

    JButton uploadButton = new JButton("Upload");
    JButton shareButton = new JButton("Share");
    JButton downloadButton = new JButton("Download");
    JButton deleteButton = new JButton("Delete");
    JButton refreshButton = new JButton("Refresh");

    JLabel titleLabel = new JLabel("Your Files");
    JTable fileTable = new JTable();
    JScrollPane scrollPane = new JScrollPane();

    private static String[] columnNames = {"ID", "Name", "Date Created"};
    public viewFiles(String username){
        this.username = username;
        setSize(500 , 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        try {
            this.data = updateFileList(this.username);
            this.fileTable = new JTable(data, columnNames);
        } catch (SQLException e){
            System.out.println("error");
        }

        this.scrollPane = new JScrollPane(this.fileTable);

        tablePanel.add(this.scrollPane);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);
        
        uploadButton.addActionListener(this);
        shareButton.addActionListener(this);
        downloadButton.addActionListener(this);
        deleteButton.addActionListener(this);
        refreshButton.addActionListener(this);

        bottomPanel.add(uploadButton);
        bottomPanel.add(shareButton);
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
        if (e.getSource() == uploadButton) {
            JFileChooser fileChooser = new JFileChooser("F:/repos/Filesharer/src/files");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String fileName = selectedFile.getName();
                try {
                    uploadFile(selectedFile, fileName, this.username);
                    this.data = updateFileList(this.username);
                    DefaultTableModel model = new DefaultTableModel(this.data, columnNames);
                    this.fileTable.setModel(model);
                } catch (SQLException error){
                    System.out.println(error.getMessage());
                }
            }   
        } else if(e.getSource() == refreshButton){
            try {
                this.data = updateFileList(this.username);
                DefaultTableModel model = new DefaultTableModel(this.data, columnNames);
                this.fileTable.setModel(model);
            } catch (SQLException err){
                System.out.println(err.getMessage());
            }
        }
    }
    

    public static void uploadFile(File file, String filename, String username) throws SQLException{
        byte[] bytes = new byte[(int) file.length()];
        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
            Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement("INSERT INTO files (filename, filedata, username, dateCreated) VALUES (?, ?, ?, CURRENT_TIMESTAMP)");
            statement.setString(1, filename);
            statement.setBytes(2, bytes);
            statement.setString(3, username);
            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (IOException ex) {
            System.out.println("error opening file");
        }
    }

    public static Object[][] updateFileList(String username) throws SQLException{
        ArrayList<Object[]> files = new ArrayList<Object[]>();
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement statement = conn.prepareStatement("SELECT id, filename, dateCreated FROM FILES WHERE USERNAME = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        while(rs.next()){
            Object[] temp = new Object[3];
            temp[0] = rs.getInt("id");
            temp[1] = rs.getString("filename");
            temp[2] = rs.getDate("datecreated");
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
        new viewFiles("akukerang2");
    }

}
