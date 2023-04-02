import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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

                    ArrayList<String[][]> ciphers = a.encryptFile(bytes);
                    String decrypt = a.decryptFile(ciphers);
                    byte[] encryptedBytes = AES.toByteArray(ciphers);
                    byte[] decryptedBytes = AES.toByteArray(decrypt);


                    String fileName = selectedFile.getName();
                    String parent = selectedFile.getParent();
                    System.out.println(parent+"test");
                    Files.write(Paths.get(parent+"\\encryped"), encryptedBytes);
                    Files.write(Paths.get(parent+"\\"+fileName+"_decrypted.png"), decryptedBytes);




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
