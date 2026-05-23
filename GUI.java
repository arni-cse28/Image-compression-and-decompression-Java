package image;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GUI {

    public static void create() {

        // window
        JFrame frame = new JFrame("Huffman Image Compression");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Buttons
        JButton selectBtn = new JButton("Select Image");
        JButton compressBtn = new JButton("Compress");
        JButton decompressBtn = new JButton("Decompress");

        // Status label
        JLabel status = new JLabel("No file selected");

        // Variables to store data
        String[] path = new String[1];     // image path
        Node[] root = new Node[1];         // Huffman tree
        int[][] size = new int[1][2];      // width & height

        
        selectBtn.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(frame);

            if (res == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                path[0] = file.getAbsolutePath();

                status.setText("Selected: " + file.getName());
            }
        });

        //COMPRESS
        compressBtn.addActionListener(e -> {

            try {
                if (path[0] == null) {
                    status.setText("Select image first!");
                    return;
                }

                
                root[0] = Huffman.compress(path[0], "compressed.txt", size[0]);

                status.setText("Compressed successfully!");

            } catch (Exception ex) {
                status.setText("Error during compression");
                ex.printStackTrace();
            }
        });

        // DECOMPRESS
        decompressBtn.addActionListener(e -> {

            try {
                if (root[0] == null) {
                    status.setText("Compress first!");
                    return;
                }

                
                Huffman.decompress("compressed.txt", root[0], size[0], "output.png");

                status.setText("Image saved as output.png");

            } catch (Exception ex) {
                status.setText("Error during decompression");
                ex.printStackTrace();
            }
        });

        // Add components to frame
        frame.add(selectBtn);
        frame.add(compressBtn);
        frame.add(decompressBtn);
        frame.add(status);

        frame.setVisible(true);
    }
}