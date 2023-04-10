package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
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

public class viewFiles extends JFrame implements ActionListener
{
    private static String URL = "jdbc:mysql://localhost/files?" +
    "user=root&password=password";
    private String username;
    private Object[][] data;
    private FileReturn selectedFile;


    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel topPanel = new JPanel();
    private JPanel tablePanel = new JPanel();
    private JPanel bottomPanel = new JPanel(new GridBagLayout());

    JButton uploadButton = new JButton("Upload");
    JButton shareButton = new JButton("Share");
    JButton downloadButton = new JButton("Download");
    JButton deleteButton = new JButton("Delete");
    JButton refreshButton = new JButton("Refresh");

    JLabel titleLabel = new JLabel("Your Files");
    JTable fileTable = new JTable();
    JScrollPane scrollPane = new JScrollPane();
    JLabel errorMsg = new JLabel("");

    private static String[] columnNames = {"ID", "Name", "Date Created"};
    public viewFiles(String username){
        this.username = username;
        setSize(500 , 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 3, 10, 3);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 5;
        bottomPanel.add(errorMsg, c);
        c.gridy++;
        c.gridwidth = 1;
        bottomPanel.add(uploadButton, c);
        c.gridx++;
        bottomPanel.add(shareButton, c);
        c.gridx++;
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
        if (e.getSource() == uploadButton) {
            JFileChooser fileChooser = new JFileChooser("F:/repos/Filesharer/src/files");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String fileName = selectedFile.getName();
                try {
                    errorMsg.setVisible(false);
                    uploadFile(selectedFile, fileName, this.username);
                    // updates table data
                    this.data = updateFileList(this.username);
                    DefaultTableModel model = new DefaultTableModel(this.data, columnNames);
                    this.fileTable.setModel(model);
                } catch (SQLException error){
                    errorMsg.setVisible(true);
                    errorMsg.setText(error.getMessage());             
               }
            }   
        } else if(e.getSource() == refreshButton){
            try {
                errorMsg.setVisible(false);
                this.data = updateFileList(this.username);
                // updates table data
                DefaultTableModel model = new DefaultTableModel(this.data, columnNames);
                this.fileTable.setModel(model);
            } catch (SQLException err){
                errorMsg.setVisible(true);
                errorMsg.setText(err.getMessage());
            }
        } else if(e.getSource() == downloadButton){
            // open a file dialog, with the selected file of the table
            int selectedRow = fileTable.getSelectedRow();
            if (selectedRow != -1) {
                errorMsg.setVisible(false);
                try {
                    this.selectedFile = getRowData(selectedRow);
                    File selectedName = new File(this.selectedFile.name);
                    JFileChooser fileChooser = new JFileChooser("F:/repos/Filesharer/src/files");
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
                    System.out.println(eee.getMessage());
                }
            } else {
                errorMsg.setVisible(true);
                errorMsg.setText("Select a row");
            }
        } else if(e.getSource() == deleteButton){
            // delete selected entry, then refresh table
            int selectedRow = fileTable.getSelectedRow();
            if (selectedRow != -1) {
                errorMsg.setVisible(false);
                try{
                    deleteRow(selectedRow);
                    this.data = updateFileList(this.username);
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

        } else {
            // share, open popup window to share selected file
            // popup window should have two buttons cancel, confirm
            // and a textfield for the recipient's name
            // new shareFile(this.username. this.selected);
            int selectedRow = fileTable.getSelectedRow();
            if(selectedRow != -1){
                errorMsg.setVisible(false);
                try {
                    new shareFile(this.username, getRowData(selectedRow));
                } catch (SQLException eee){
                    System.out.println(eee.getMessage());
                }
             } else {
                errorMsg.setVisible(true);
                errorMsg.setText("Select a row");
             }
        }
    }
    
    public void deleteRow(int row) throws SQLException{
        int id = Integer.parseInt(fileTable.getValueAt(row, 0).toString());
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM FILES WHERE ID = ?");
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    public FileReturn getRowData(int selectedRow) throws SQLException{
        String id = fileTable.getValueAt(selectedRow, 0).toString();
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement statement = conn.prepareStatement("SELECT filename, filedata FROM FILES WHERE ID = ?");
        statement.setString(1, id);
        ResultSet rs = statement.executeQuery();
        rs.next();
        FileReturn output = new FileReturn(rs.getString("filename"), rs.getBytes("filedata"));
        conn.close();
        rs.close();
        statement.close();
        return output;
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

}
