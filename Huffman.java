package image;

import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Huffman {

    // Calculate frequency 
    public static int[] getFrequency(int[] data) {
        int[] freq = new int[256];  // array for grayscale values

        for (int i = 0; i < data.length; i++) {
            freq[data[i]]++;   // increase count
        }

        return freq;
    }

    //  Build Huffman Tree 
    public static Node buildTree(int[] freq) {

        // min heap 
        PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
            public int compare(Node a, Node b) {
                return a.freq - b.freq;
            }
        });

        // create nodes for non-zero frequencies
        for (int i = 0; i < 256; i++) {
            if (freq[i] > 0) {
                pq.add(new Node(i, freq[i]));
            }
        }

        // combine nodes until one root remains
        while (pq.size() > 1) {
            Node left = pq.poll();   
            Node right = pq.poll();  

            Node parent = new Node(-1, left.freq + right.freq);
            parent.left = left;
            parent.right = right;

            pq.add(parent);
        }

        return pq.poll();  
    }

    //  Generate Huffman 
    public static void getCodes(Node root, String code, String[] codes) {
        if (root == null) return;

        // if leaf node → assign code
        if (root.left == null && root.right == null) {
            codes[root.value] = code;
            return;
        }

        getCodes(root.left, code + "0", codes);
        getCodes(root.right, code + "1", codes);
    }

    //  Compress 
    public static Node compress(String input, String output, int[] size) throws Exception {

        BufferedImage img = ImageIO.read(new File(input));

        int w = img.getWidth();
        int h = img.getHeight();

        size[0] = w;  
        size[1] = h; 

        int[] pixels = new int[w * h];
        int k = 0;

        //  grayscale 
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                int rgb = img.getRGB(x, y);

                // take red value as grayscale
                int gray = (rgb >> 16) & 255;

                pixels[k++] = gray;
            }
        }

        // build Huffman process
        int[] freq = getFrequency(pixels);
        Node root = buildTree(freq);

        String[] codes = new String[256];
        getCodes(root, "", codes);

        // encode pixels
        String encoded = "";

        for (int i = 0; i < pixels.length; i++) {
            encoded += codes[pixels[i]];
        }

  
        BufferedWriter bw = new BufferedWriter(new FileWriter(output));
        bw.write(encoded);
        bw.close();

        System.out.println("Compression done!");

        return root;
    }

    // Decompress 
    public static void decompress(String input, Node root, int[] size, String output) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(input));

        String encoded = "";
        String line;

        
        while ((line = br.readLine()) != null) {
            encoded += line;
        }
        br.close();

        ArrayList<Integer> pixels = new ArrayList<>();

        Node curr = root;

        
        for (int i = 0; i < encoded.length(); i++) {

            char bit = encoded.charAt(i);

            if (bit == '0') curr = curr.left;
            else curr = curr.right;

            // if leaf node → get pixel value
            if (curr.left == null && curr.right == null) {
                pixels.add(curr.value);
                curr = root;
            }
        }

        int w = size[0];
        int h = size[1];

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);

        int index = 0;

        
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                int val = pixels.get(index++);

                int rgb = (val << 16) | (val << 8) | val;

                img.setRGB(x, y, rgb);
            }
        }

        // save 
        ImageIO.write(img, "png", new File(output));

        System.out.println("Decompression done!");
    }
}
