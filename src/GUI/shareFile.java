package GUI;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import encryption.RSA;
import encryption.AES;
import encryption.Helper;
import Helper.FileReturn;
public class shareFile extends JFrame implements ActionListener
{
    private String username;
    private FileReturn selected;
    private JPanel main = new JPanel(new GridBagLayout());
    private static String URL = "jdbc:mysql://localhost/files?" +
    "user=root&password=password";


    JLabel title = new JLabel("Share file");
    JLabel recipientLabel = new JLabel("Recipient: ");
    JLabel response = new JLabel("");

    JTextField recipientField = new JTextField(20);

    JButton shareButton = new JButton("Share");
    JButton cancelButton = new JButton("Cancel");


    public shareFile(String username, FileReturn selected){
        super("Share Selected File");
        this.username = username;
        this.selected = selected;
        title.setFont(new Font("Arial", Font.BOLD, 16));

        setSize(300, 200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cancelButton.addActionListener(this);
        shareButton.addActionListener(this);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 0, 5, 0);
        c.gridx = 0;
        c.gridy = 0;
        main.add(title, c);
        c.gridy++;
        main.add(recipientLabel, c);
        c.gridy++;
        c.gridwidth = 2;
        main.add(recipientField, c);
        c.gridwidth = 1;
        c.gridy++;
        main.add(cancelButton, c);
        c.gridx++;
        main.add(shareButton, c);
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        main.add(response, c);
        response.setVisible(false);
        setContentPane(main);
        setVisible(true);
    }

    public void share(String recipient) throws SQLException{
        // Select user key and encrypt
        // SQL Insert on shareFile Table
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement statement = conn.prepareStatement("SELECT PUBLICKEY FROM USERS WHERE USERNAME = ?");
        statement.setString(1, recipient);
        ResultSet rs = statement.executeQuery();
        rs.next();
        String[] recipPublic = rs.getString(1).split(" "); //Gets recipient public key
        // Generates random key for AES
        String masterKey = Helper.randomBigInt(new BigInteger("3"), new BigInteger("340282366920938463463374607431768211455")).toString(10);
        // Encryption init
        AES a = new AES(masterKey);
        RSA r = new RSA(recipPublic);

        
        byte[] encryptedBytes = a.encryptFile(this.selected.data);
        String encryptedName = a.encryptString(this.selected.name);
        String encryptedKey = r.encryptBlock(masterKey);
        System.out.println(encryptedKey.length());
        statement = conn.prepareStatement("INSERT INTO SHARED (filename, filedata, sender, reciever, masterkey, datecreated) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)");
        statement.setString(1, encryptedName);
        statement.setBytes(2, encryptedBytes);
        statement.setString(3, this.username);
        statement.setString(4, recipient);
        statement.setString(5, encryptedKey);
        statement.executeUpdate();


        statement.close();
        rs.close();
        conn.close();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == shareButton){
            try{
                share(recipientField.getText());
                response.setText("Successfully shared");
                response.setVisible(true);
            } catch (SQLException eee){
                response.setText("User not found");
                response.setVisible(true);
            }
        } else {
            this.setVisible(false);
        }
    }
    
}
