package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Home extends JFrame implements ActionListener{
    String username;
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel topPanel = new JPanel(new FlowLayout());
    
    JLabel titleLabel = new JLabel("File Sharer");
    JButton viewButton = new JButton("View Files");
    JButton sharedFilesButton = new JButton("View Shared");
    JButton profileButton = new JButton("Edit Profile");
    JButton logOutButton = new JButton("Log Out");

    JPanel centerPanel = new JPanel(new GridLayout(5, 1));

    public Home(String username) {
        super("File Sharer");
        this.username = username;
        setSize(500 , 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        topPanel.add(titleLabel);

        
        centerPanel.add(viewButton);
        centerPanel.add(sharedFilesButton);
        centerPanel.add(profileButton);
        centerPanel.add(logOutButton);
        
        viewButton.addActionListener(this);
        sharedFilesButton.addActionListener(this);
        profileButton.addActionListener(this);
        logOutButton.addActionListener(this);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        setContentPane(mainPanel);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
       if(e.getSource() == viewButton){
            new viewFiles(this.username);
       } else if (e.getSource() == sharedFilesButton){
            //open shared files, similar to viewFiles but with shared file table
            new viewShared(this.username);
       } else if(e.getSource() == profileButton){
            //open edit profile, can edit password and generate new keys
            // 1 textfield, and buttons for cancel, confirm, generate new keys
            // should user be able to see key values?
       } else {
            //logout
            this.dispose();
            new Login();
       }
    }


    public static void main(String[] args){
        new Home("gabriel");
    }




}
