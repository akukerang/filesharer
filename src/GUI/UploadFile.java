package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import encryption.AES;

public class UploadFile extends JFrame implements ActionListener {
    private static String URL = "jdbc:mysql://localhost/files?" +
    "user=root&password=password";
    private String username;

    private JButton browseButton = new JButton("Browse");
    private JButton submitButton = new JButton("Submit");
    private JButton backButton = new JButton("Back");

    private JLabel currentLabel = new JLabel("Current File: NO FILE SELECTED");

    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel topPanel = new JPanel(new FlowLayout());
    private JPanel buttonPanel = new JPanel(new GridBagLayout());

    File selectedFile;
    String fileName;


    AES a = new AES("10000001000000011000001000000010100000110000001110000100000001001000010100000101100001100000011010000111000001111");

    public UploadFile(String username) {
        super("Upload File");
        this.username = username;
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("Upload File");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);


        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 5, 10, 5);
        c.gridx = 0;
        c.gridy = 0;
        buttonPanel.add(backButton ,c );
        c.gridx++;
        buttonPanel.add(browseButton, c);
        c.gridx++;
        buttonPanel.add(submitButton, c);
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 3;
        buttonPanel.add(currentLabel, c);
        browseButton.addActionListener(this);
        backButton.addActionListener(this);
        submitButton.addActionListener(this);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.PAGE_START);
        setContentPane(mainPanel);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == browseButton) {
            JFileChooser fileChooser = new JFileChooser("F:/repos/Filesharer/src/files");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                this.selectedFile = fileChooser.getSelectedFile();
                this.fileName = this.selectedFile.getName();
                currentLabel.setText("Current File: "+this.fileName);
                this.revalidate();
            }
        } else if (e.getSource() == submitButton){
            byte[] bytes = new byte[(int) this.selectedFile.length()];
            try(FileInputStream fis = new FileInputStream(this.selectedFile)) {
                fis.read(bytes);
                try {
                    Connection conn = DriverManager.getConnection(URL);
                    PreparedStatement statement = conn.prepareStatement("INSERT INTO files (filename, filedata, username) VALUES (?, ?, ?)");
                    statement.setString(1, fileName);
                    statement.setBytes(2, bytes);
                    statement.setString(3, this.username);
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("File uploaded");
                    }                        
                    statement.close();
                    conn.close();
                } catch (SQLException sqlE){
                    System.out.println(sqlE.getMessage());
                }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            this.setVisible(false);
            new Home(this.username);
        }
    }

}
