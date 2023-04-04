package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;



import encryption.AES;
public class UploadFile extends JFrame implements ActionListener {
    private JButton browseButton = new JButton("Browse");
    AES a = new AES("10000001000000011000001000000010100000110000001110000100000001001000010100000101100001100000011010000111000001111");
    public UploadFile() {
        super("Upload File");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(browseButton);
        panel.add(topPanel, BorderLayout.NORTH);
        browseButton.addActionListener(this);
        setContentPane(panel);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == browseButton) {
            JFileChooser fileChooser = new JFileChooser("F:/repos/Filesharer/src/files");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                byte[] bytes = new byte[(int) selectedFile.length()];
                try(FileInputStream fis = new FileInputStream(selectedFile)) {
                    fis.read(bytes);
                    byte[] encryptedBytes = a.encryptFile(bytes);
                    String fileName = selectedFile.getName();
                    String parent = selectedFile.getParent();
                    try {
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/files?" +
                        "user=root&password=password");
                        String sql = "INSERT INTO files (filename, filedata) VALUES (?, ?)";
                        PreparedStatement statement = conn.prepareStatement(sql);
                        statement.setString(1, fileName);
                        statement.setBytes(2, encryptedBytes);
                        // int rowsInserted = statement.executeUpdate();
                        // if (rowsInserted > 0) {
                        //     System.out.println("File data inserted successfully");
                        // }                        
                        
                        sql = "SELECT * FROM files WHERE id = 1";
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);
                        if(rs.next()){
                            String outputFilename = rs.getString("filename");
                            byte[] fileData = rs.getBytes("fileData");
                            byte[] decryptedBytes = a.decryptFile(fileData);
                            Files.write(Paths.get(parent+"\\encrypted"), fileData);                         
                            Files.write(Paths.get(parent+"\\"+outputFilename+"_decrypted.png"), decryptedBytes);
                        }
                        statement.close();
                        stmt.close();
                        conn.close();
                    } catch (SQLException sqlE){
                        System.out.println(sqlE.getMessage());
                    }

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static void main(String[] args) {
        new UploadFile();
    }
}
