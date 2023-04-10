package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import Helper.Keys;
import encryption.Hash;
import encryption.RSA;

public class editProfile extends JFrame implements ActionListener {
    private static String URL = "jdbc:mysql://localhost/files?" +
            "user=root&password=password";
    private String username;
    JLabel confirmLabel = new JLabel("Enter current password");
    JLabel newLabel = new JLabel("Enter new password");

    JPasswordField confirmField = new JPasswordField();
    JPasswordField passwordField = new JPasswordField();

    JButton passwordButton = new JButton("Change Password");
    JButton keyButton = new JButton("Change Keys");

    JPanel formPanel = new JPanel(new GridLayout(6, 1));
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel topPanel = new JPanel();
    JPanel bottomPanel = new JPanel();

    JLabel errorMessage = new JLabel("");
    JLabel titleLabel = new JLabel("Edit Profile");

    public editProfile(String username) {
        this.username = username;
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        setSize(300, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formPanel.add(confirmLabel);
        formPanel.add(confirmField);
        formPanel.add(newLabel);
        formPanel.add(passwordField);
        formPanel.add(passwordButton);
        formPanel.add(keyButton);

        passwordButton.addActionListener(this);
        keyButton.addActionListener(this);

        topPanel.add(titleLabel);

        bottomPanel.add(errorMessage);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.PAGE_START);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);

        setContentPane(mainPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == passwordButton) {
            String passwordData = new String(confirmField.getPassword());
            String newPasswordData = new String(passwordField.getPassword());

            if (passwordData.trim().equals("") || newPasswordData.trim().equals("")) { // checks for empty fields
                errorMessage.setText("Empty fields");
                errorMessage.setVisible(true);
            } else {
                try {
                    String passwordHash = Hash.HashMessage(passwordData);
                    if (!checkPassword(this.username, passwordHash)) { // checks if current password correct
                        errorMessage.setText("Wrong password");
                        errorMessage.setVisible(true);
                    } else { // if current correct, check if new password matches requirements
                        if (!verifyField(newPasswordData)) {
                            errorMessage.setText("Invalid characthers");
                            errorMessage.setVisible(true);
                        } else {
                            String newPasswordHash = Hash.HashMessage(newPasswordData);
                            // update password
                            updatePassword(newPasswordHash);
                            errorMessage.setText("Password updated");
                            errorMessage.setVisible(true);
                        }
                    }
                } catch (SQLException e2) {
                    errorMessage.setText(e2.getMessage());
                    errorMessage.setVisible(true);
                }
            }
        } else { // generate new keys
            String passwordData = new String(confirmField.getPassword());
            if (passwordData.trim().equals("")) { // checks for empty fields
                errorMessage.setText("Empty fields");
                errorMessage.setVisible(true);
            } else {
                try {
                    String passwordHash = Hash.HashMessage(passwordData);
                    if (!checkPassword(this.username, passwordHash)) { // checks if current password correct
                        errorMessage.setText("Wrong password");
                        errorMessage.setVisible(true);
                    } else { // if current correct, generate new keys
                        generateKeys();
                        errorMessage.setText("New Keys Generated");
                        errorMessage.setVisible(true);

                    }
                } catch (SQLException e2) {
                    errorMessage.setText(e2.getMessage());
                    errorMessage.setVisible(true);
                }
        
                }
        }
    }

    private static boolean checkPassword(String username, String password) throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM USERS WHERE USERNAME = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String returnHash = rs.getString("passwordHash");
            if (returnHash.equals(password)) { // if password hash matches database hash return true
                return true;
            }
        }

        return false;
    }

    private static boolean verifyField(String input) {
        String validCharsRegex = "^[a-zA-Z0-9\\?!]*$";
        return input.matches(validCharsRegex);
    }

    private void updatePassword(String newHash) throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement stmt = conn.prepareStatement("UPDATE USERS SET PASSWORDHASH = ? WHERE USERNAME = ?");
        stmt.setString(1, newHash);
        stmt.setString(2, this.username);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    private void generateKeys() throws SQLException{
        Connection conn = DriverManager.getConnection(URL);
        Keys key = RSA.generateKeys();
        String publicKey = key.publicKey;
        String privateKey = key.privateKey;
        PreparedStatement stmt = conn.prepareStatement("UPDATE USERS SET PUBLICKEY = ?, PRIVATEKEY = ? WHERE USERNAME = ?");
        stmt.setString(1, publicKey);
        stmt.setString(2, privateKey);
        stmt.setString(3, this.username);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

}
