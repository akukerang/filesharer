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
    JButton uploadButton = new JButton("Upload a File");
    JButton downloadButton = new JButton("View your Files");
    JButton shareButton = new JButton("Share a file");
    JButton viewButton = new JButton("View Shared Files");
    JPanel centerPanel = new JPanel(new GridLayout(4, 1));

    public Home(String username) {
        super("File Sharer");
        this.username = username;
        setSize(500 , 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        topPanel.add(titleLabel);

        centerPanel.add(uploadButton);
        centerPanel.add(shareButton);
        centerPanel.add(downloadButton);
        centerPanel.add(viewButton);

        uploadButton.addActionListener(this);
        shareButton.addActionListener(this);
        downloadButton.addActionListener(this);
        viewButton.addActionListener(this);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        setContentPane(mainPanel);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
       if(e.getSource() == uploadButton){
            new UploadFile(this.username);
            this.setVisible(false);
       }
    }


    public static void main(String[] args){
        new Home("akukerang2");
    }




}
