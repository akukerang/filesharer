package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import java.awt.*;
import Helper.FileReturn;
import Helper.Keys;
import encryption.AES;
import encryption.RSA;

public class viewShared extends JFrame implements ActionListener {
    private static String URL = "jdbc:mysql://localhost/files?" +
            "user=root&password=password";
    private String username;
    private Object[][] data;
    private RSA r;
    private Keys key;
    private FileReturn selectedFile;

    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel topPanel = new JPanel();
    private JPanel tablePanel = new JPanel();
    private JPanel bottomPanel = new JPanel(new GridBagLayout());

    JButton downloadButton = new JButton("Download");
    JButton deleteButton = new JButton("Delete");
    JButton refreshButton = new JButton("Refresh");
    JLabel errorMsg = new JLabel("");

    JLabel titleLabel = new JLabel("Your Shared Files");
    JTable fileTable = new JTable();
    JScrollPane scrollPane = new JScrollPane();

    private static String[] columnNames = { "ID", "Name", "Shared by", "Date created" };

    public viewShared(String username) {
        this.username = username;
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        try {
            getKeys();
            this.r = new RSA(this.key);
            this.data = updateFileList(this.username, this.r);
            this.fileTable = new JTable(data, columnNames);
        } catch (SQLException e) {
            System.out.println("error");
        }

        this.scrollPane = new JScrollPane(this.fileTable);

        tablePanel.add(this.scrollPane);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);

        downloadButton.addActionListener(this);
        deleteButton.addActionListener(this);
        refreshButton.addActionListener(this);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 3, 10, 3);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        bottomPanel.add(errorMsg, c);
        c.gridwidth = 1;
        c.gridy++;
        bottomPanel.add(downloadButton, c);
        c.gridx++;
        bottomPanel.add(deleteButton, c);
        c.gridx++;
        bottomPanel.add(refreshButton, c);

        errorMsg.setVisible(false);

        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.PAGE_START);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);

        setContentPane(mainPanel);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == refreshButton) {
            try {
                errorMsg.setVisible(false);
                this.data = updateFileList(this.username, this.r);
                // updates table data
                DefaultTableModel model = new DefaultTableModel(this.data, columnNames);
                this.fileTable.setModel(model);
            } catch (SQLException err) {
                errorMsg.setVisible(true);
                errorMsg.setText(err.getMessage());
            }
        } else if (e.getSource() == downloadButton) {
            // open a file dialog, with the selected file of the table
            int selectedRow = fileTable.getSelectedRow();
            if (selectedRow != -1) {
                errorMsg.setVisible(false);
                try {
                    this.selectedFile = getRowData(selectedRow);
                    File selectedName = new File(this.selectedFile.name);
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setSelectedFile(selectedName);
                    int result = fileChooser.showSaveDialog(this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selected = fileChooser.getSelectedFile();
                        try {
                            FileOutputStream fileWriter = new FileOutputStream(selected);
                            fileWriter.write(this.selectedFile.data);
                            fileWriter.close();
                        } catch (IOException err) {
                            System.out.println();
                        }
                    }
                } catch (SQLException eee) {
                    errorMsg.setVisible(true);
                    errorMsg.setText(eee.getMessage());   
                }
            } else {
                errorMsg.setVisible(true);
                errorMsg.setText("Select a row");   
            }
        } else if (e.getSource() == deleteButton) {
            int selectedRow = fileTable.getSelectedRow();
            if (selectedRow != -1) {
                errorMsg.setVisible(false);
                try{
                    deleteRow(selectedRow);
                    this.data = updateFileList(this.username, this.r);
                    // updates table data
                    DefaultTableModel model = new DefaultTableModel(this.data, columnNames);
                    this.fileTable.setModel(model);
                } catch (SQLException e3){
                    errorMsg.setVisible(true);
                    errorMsg.setText(e3.getMessage());   
                }
            } else {
                errorMsg.setVisible(true);
                errorMsg.setText("Select a row");   
            }
        }
    }


    private void deleteRow(int row) throws SQLException{
        int id = Integer.parseInt(fileTable.getValueAt(row, 0).toString());
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM SHARED WHERE ID = ?");
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    private void getKeys() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement statement = conn
                .prepareStatement("SELECT PUBLICKEY, PRIVATEKEY FROM USERS WHERE USERNAME = ?");
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

    private FileReturn getRowData(int selectedRow) throws SQLException {
        String id = fileTable.getValueAt(selectedRow, 0).toString();
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement statement = conn
                .prepareStatement("SELECT FILENAME, FILEDATA, MASTERKEY FROM SHARED WHERE ID = ?");
        statement.setString(1, id);
        ResultSet rs = statement.executeQuery();
        rs.next();
        String decryptKey = r.decryptBlock(rs.getString("MASTERKEY"));
        AES a = new AES(decryptKey);
        String decryptedName = a.decryptString(rs.getString("FILENAME"));
        byte[] decryptedBytes = a.decryptFile(rs.getBytes("FILEDATA"));
        FileReturn output = new FileReturn(decryptedName, decryptedBytes);
        conn.close();
        rs.close();
        statement.close();
        return output;
    }

    private static Object[][] updateFileList(String username, RSA r) throws SQLException {
        ArrayList<Object[]> files = new ArrayList<Object[]>();
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement statement = conn
                .prepareStatement("SELECT ID, FILENAME, SENDER, DATECREATED, MASTERKEY FROM SHARED WHERE RECIEVER = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            String decryptKey = r.decryptBlock(rs.getString("MASTERKEY"));
            AES a = new AES(decryptKey);
            Object[] temp = new Object[4];
            String encryptedFile = rs.getString("FILENAME");
            temp[0] = rs.getInt("ID");
            temp[1] = a.decryptString(encryptedFile);
            temp[2] = rs.getString("SENDER");
            temp[3] = rs.getDate("DATECREATED");
            files.add(temp);
        }
        Object[][] output = new Object[files.size()][3];
        for (int i = 0; i < files.size(); i++) {
            output[i] = files.get(i);
        }

        rs.close();
        statement.close();
        conn.close();
        return output;
    }

}
