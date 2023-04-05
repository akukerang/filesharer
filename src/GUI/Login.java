package GUI;

import javax.swing.JButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import encryption.Hash;

public class Login extends JFrame implements ActionListener{
    private static String URL = "jdbc:mysql://localhost/files?" +
    "user=root&password=password";
    JLabel loginLabel = new JLabel("Login");
    JLabel createLabel = new JLabel("Create Account");
    JLabel usernameLabel = new JLabel("Username");
    JLabel passwordLabel = new JLabel("Password");
    JLabel usernameLabel2 = new JLabel("Username");
    JLabel passwordLabel2 = new JLabel("Password");
    JLabel confirmLabel = new JLabel("Confirm Password");
    JLabel errorMessage = new JLabel("");
    JLabel errorMessage2 = new JLabel("");
    JTextField username = new JTextField(20);
    JPasswordField password = new JPasswordField(20);
    JTextField username2 = new JTextField(20);
    JPasswordField password2 = new JPasswordField(20);
    JPasswordField confirmPassword = new JPasswordField(20);
    JButton loginButton = new JButton("Login");
    JButton createAccountButton = new JButton("Create Account");
    JButton submitButton = new JButton("Create Account");
    JButton backButton = new JButton("Back");

    JPanel loginPanel = new JPanel(new GridBagLayout());
    JPanel createPanel = new JPanel(new GridBagLayout());


    JLabel temp = new JLabel("temp");

    public Login(){
        super("File Sharer");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 0, 5, 0);
        // Login Panel        
        c.gridx = 0;
        c.gridy = 0;
        loginPanel.add(loginLabel, c);
        c.gridy++;
        loginPanel.add(usernameLabel, c);
        c.gridy++;
        loginPanel.add(username, c);
        c.gridy++;
        loginPanel.add(passwordLabel, c);
        c.gridy++;        
        loginPanel.add(password, c);
        c.gridy++;
        loginPanel.add(loginButton, c);
        loginButton.addActionListener(this);
        c.gridy++;
        loginPanel.add(createAccountButton, c);
        createAccountButton.addActionListener(this);
        c.gridy++;
        errorMessage.setVisible(false);
        loginPanel.add(errorMessage, c);
        errorMessage.setForeground(Color.red);

        //Create Account Panel
        c.gridx = 0;
        c.gridy = 0;
        createPanel.add(createLabel, c);
        c.gridy++;
        createPanel.add(usernameLabel2, c);
        c.gridy++;
        createPanel.add(username2, c);
        c.gridy++;
        createPanel.add(passwordLabel2, c);
        c.gridy++;
        createPanel.add(password2, c);
        c.gridy++;
        createPanel.add(confirmLabel, c);
        c.gridy++;
        createPanel.add(confirmPassword, c);
        c.gridy++;
        createPanel.add(submitButton, c);
        c.gridy++;
        createPanel.add(backButton,c);
        c.gridy++;
        errorMessage2.setVisible(false);
        createPanel.add(errorMessage2,c);
        errorMessage2.setForeground(Color.red);

        submitButton.addActionListener(this);
        backButton.addActionListener(this);



        setContentPane(loginPanel);
        setVisible(true);

    }

    private static boolean verifyField(String input){
        String validCharsRegex = "^[a-zA-Z0-9\\?!]*$";
        return input.matches(validCharsRegex);
    }

    public static boolean userExist(String username) throws SQLException{
        //checks if username already exists in the table
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE userName= ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        stmt.close();
        rs.close();
        conn.close();
        if(count == 0){
            return false;
        } //if count anything other than 0, user Exists
        return true;
    }

    public static void newUser(String username, String hash) throws SQLException{
        Connection conn = DriverManager.getConnection(URL);
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO USERS (username, passwordHash) VALUES (?, ?)");
        stmt.setString(1, username);
        stmt.setString(2, hash);
        stmt.executeUpdate();
        stmt.close();
        conn.close();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == loginButton){
            String usernameData = username.getText();
            String passwordData = new String(password.getPassword());
            if(usernameData.trim().isEmpty() || passwordData.trim().isEmpty()){
                errorMessage.setVisible(true);
                errorMessage.setText("Empty field");
            } else if(!verifyField(usernameData) || !verifyField(passwordData)){
                errorMessage.setVisible(true);
                errorMessage.setText("Invalid Characthers");
            } else {
                errorMessage.setVisible(false);
                System.out.println("Username: " + usernameData);
                System.out.println("Password: " + passwordData);
                // hash password
                // check if password matches SQL
                    // make function
                // SELECT * FROM users WHERE username=userNameData
                // if hash(passwordData) matches SQL return, login, else error message

            }
        } else if(e.getSource() == createAccountButton){
            this.setContentPane(createPanel);
            this.revalidate();
        } else if(e.getSource() == backButton) {
            this.setContentPane(loginPanel);
            this.revalidate();
        } else {
            String usernameData2 = username2.getText();
            String passwordData2 = new String(password2.getPassword());
            String confirmData = new String(confirmPassword.getPassword());
            String passwordHash = Hash.HashMessage(passwordData2);
            System.out.println("submit");
            if(usernameData2.trim().isEmpty() 
            || passwordData2.trim().isEmpty()
            || confirmData.trim().isEmpty()
            ){
                errorMessage2.setVisible(true);
                errorMessage2.setText("Empty field");
            } else if(
                !verifyField(usernameData2) 
                || !verifyField(passwordData2)
                || !verifyField(confirmData)
            ){
                errorMessage2.setVisible(true);
                errorMessage2.setText("Invalid Characthers");
            } else if(!confirmData.equals(passwordData2)){
                errorMessage2.setVisible(true);
                errorMessage2.setText("Passwords do not match");
            } 
            else {
                try {
                    if(userExist(usernameData2)){
                        errorMessage2.setText("User already exists");
                        errorMessage2.setVisible(true);
                    } else {
                        errorMessage2.setVisible(false);
                        newUser(usernameData2, passwordHash);
                    }
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                }

            }


        }


    }

    public static void main(String[] args) {
        new Login();
    }




}
