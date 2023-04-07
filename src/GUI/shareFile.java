package GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class shareFile extends JFrame implements ActionListener
{
    private String username;
    private Object[] selected;
    private JPanel main = new JPanel(new GridBagLayout());

    JLabel title = new JLabel("Share file");
    JLabel recipientLabel = new JLabel("Recipient: ");
    JLabel response = new JLabel("");

    JTextField recipientField = new JTextField(20);

    JButton shareButton = new JButton("Share");
    JButton cancelButton = new JButton("Cancel");


    public shareFile(String username, Object[] selected){
        super("Share Selected File");
        this.username = username;
        this.selected = selected;
        setSize(400, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    public void share(String recipient){
        // SQL Insert on shareFile Table

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == shareButton){
            share(recipientField.getText());
            response.setText("Successfully shared");
            response.setVisible(true);
        } else {
            this.setVisible(false);
        }
    }
    
    public static void main(String[] args) {
        Object[] temp = {1,2,3};
        new shareFile("akukerang2", temp);
    }
}
