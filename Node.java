package image;

class Node {
 int value;      // pixel value
 int freq;       // frequency
 Node left, right;


 Node(int v, int f) {
     value = v;
     freq = f;
     left = null;
     right = null;
 }
}