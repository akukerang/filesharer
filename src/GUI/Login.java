package GUI;

import javax.swing.JButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Login extends JFrame implements ActionListener{
    JLabel loginLabel = new JLabel("Login");
    JLabel usernameLabel = new JLabel("Username");
    JLabel passwordLabel = new JLabel("Password");
    JLabel errorMessage = new JLabel("");
    JTextField username = new JTextField(20);
    JPasswordField password = new JPasswordField(20);
    JButton loginButton = new JButton("Login");
    JButton createAccountButton = new JButton("Create Account");


    public Login(){
        super("File Sharer");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 0, 5, 0);
    
        // add components to panel using gridbag layout
        JPanel loginPanel = new JPanel(new GridBagLayout());
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
        c.gridy++;
        errorMessage.setVisible(false);
        loginPanel.add(errorMessage, c);
        errorMessage.setForeground(Color.red);




        setContentPane(loginPanel);
        setVisible(true);

    }

    private static boolean verifyField(String input){
        String validCharsRegex = "^[a-zA-Z0-9\\?!]*$";
        return input.matches(validCharsRegex);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == loginButton){
            String usernameData = username.getText();
            char[] passwordData = password.getPassword();
            String passwordDataString = new String(passwordData);
        
            if(usernameData.trim().isEmpty() || passwordDataString.trim().isEmpty()){
                errorMessage.setVisible(true);
                errorMessage.setText("Empty field");
            } else if(!verifyField(usernameData) || !verifyField(passwordDataString)){
                errorMessage.setVisible(true);
                errorMessage.setText("Invalid Characthers");
            } else {
                errorMessage.setVisible(false);
                System.out.println("Username: " + usernameData);
                System.out.println("Password: " + passwordDataString);
            }
        }


    }

    public static void main(String[] args) {
        new Login();
    }




}
