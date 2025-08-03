import java.io.*;
import java.util.*;

class HuffmanNode implements Comparable<HuffmanNode> {
    char ch;
    int frequency;
    HuffmanNode left, right;

    public HuffmanNode(char ch, int frequency, HuffmanNode left, HuffmanNode right) {
        this.ch = ch;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    @Override
    public int compareTo(HuffmanNode other) {
        return this.frequency - other.frequency;
    }
}

public class HuffmanCodingWithSizeVerification {
    private Map<Character, String> huffmanCodes = new HashMap<>();
    private HuffmanNode root;

    // Build Huffman Tree and calculate sizes
    private void buildHuffmanTree(String text) {
        // Count frequency of each character
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        // Visualize frequency table
        System.out.println("\nCharacter Frequency Table:");
        System.out.println("-------------------------");
        System.out.printf("%-10s %-10s%n", "Character", "Frequency");
        System.out.println("-------------------------");
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            String charDisplay = (entry.getKey() == '\n') ? "\\n" : String.valueOf(entry.getKey());
            System.out.printf("%-10s %-10d%n", charDisplay, entry.getValue());
        }
        System.out.println("-------------------------");

        // Create priority queue for Huffman nodes
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.offer(new HuffmanNode(entry.getKey(), entry.getValue(), null, null));
        }

        // Build the Huffman tree
        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode parent = new HuffmanNode('\0', left.frequency + right.frequency, left, right);
            pq.offer(parent);
        }

        root = pq.poll();
        generateCodes(root, "");
        
        // Visualize Huffman codes
        System.out.println("\nHuffman Codes:");
        System.out.println("-------------------------");
        System.out.printf("%-10s %-10s%n", "Character", "Code");
        System.out.println("-------------------------");
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            String charDisplay = (entry.getKey() == '\n') ? "\\n" : String.valueOf(entry.getKey());
            System.out.printf("%-10s %-10s%n", charDisplay, entry.getValue());
        }
        System.out.println("-------------------------");

        // Visualize Huffman tree
        System.out.println("\nHuffman Tree Visualization:");
        System.out.println("-------------------------");
        printHuffmanTree(root, 0);
        System.out.println("-------------------------");
    }

    // Generate Huffman codes
    private void generateCodes(HuffmanNode node, String code) {
        if (node == null) return;
        if (node.isLeaf()) {
            huffmanCodes.put(node.ch, code);
        }
        generateCodes(node.left, code + "0");
        generateCodes(node.right, code + "1");
    }

    // Print Huffman tree in console
    private void printHuffmanTree(HuffmanNode node, int level) {
        if (node == null) return;
        
        // Print right child
        printHuffmanTree(node.right, level + 1);
        
        // Print current node
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
        String nodeDisplay = node.isLeaf() ? 
            "'" + (node.ch == '\n' ? "\\n" : node.ch) + "'(" + node.frequency + ")" : 
            "(" + node.frequency + ")";
        System.out.println(nodeDisplay);
        
        // Print left child
        printHuffmanTree(node.left, level + 1);
    }

    // Encode the input text and calculate sizes
    public String encode(String text, long[] sizeMetrics) {
        buildHuffmanTree(text);
        StringBuilder encoded = new StringBuilder();
        long compressedBits = 0;
        for (char c : text.toCharArray()) {
            String code = huffmanCodes.get(c);
            encoded.append(code);
            compressedBits += code.length();
        }
        // Original size in bits (assuming 8 bits per character for ASCII/UTF-8)
        sizeMetrics[0] = text.length() * 8;
        // Compressed size in bits
        sizeMetrics[1] = compressedBits;
        return encoded.toString();
    }

    // Decode the encoded text
    public String decode(String encodedText) {
        StringBuilder decoded = new StringBuilder();
        HuffmanNode current = root;
        for (char bit : encodedText.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;
            if (current.isLeaf()) {
                decoded.append(current.ch);
                current = root;
            }
        }
        return decoded.toString();
    }

    // Save encoded text and Huffman codes to files
    public void saveToFile(String encodedText, String outputFilePath, String codesFilePath) throws IOException {
        // Save encoded text
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(encodedText);
        }

        // Save Huffman codes for decoding
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(codesFilePath))) {
            for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
        }
    }

    // Read text from file
    public String readFromFile(String filePath) throws IOException {
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
        }
        return text.toString();
    }

    // Example usage
    public static void main(String[] args) {
        try {
            HuffmanCodingWithSizeVerification huffman = new HuffmanCodingWithSizeVerification();
            
            // Read input text from file
            String inputFile = "input.txt";
            System.out.println("Reading input from: " + inputFile);
            String text = huffman.readFromFile(inputFile);
            System.out.println("\nOriginal Text:\n" + text);
            
            // Encode and calculate sizes
            long[] sizeMetrics = new long[2]; // [0]: original size, [1]: compressed size
            String encoded = huffman.encode(text, sizeMetrics);
            System.out.println("\nEncoded Text:\n" + encoded);
            
            // Calculate and display size reduction
            double originalSizeBytes = sizeMetrics[0] / 8.0;
            double compressedSizeBytes = sizeMetrics[1] / 8.0;
            double compressionRatio = (sizeMetrics[0] - sizeMetrics[1]) / (double) sizeMetrics[0] * 100;
            
            System.out.println("\nSize Analysis:");
            System.out.println("-------------------------");
            System.out.printf("Original Size: %.2f bytes (%d bits)%n", originalSizeBytes, sizeMetrics[0]);
            System.out.printf("Compressed Size: %.2f bytes (%d bits)%n", compressedSizeBytes, sizeMetrics[1]);
            System.out.printf("Size Reduction: %.2f%%%n", compressionRatio);
            System.out.println("-------------------------");
            
            // Save encoded text and codes
            huffman.saveToFile(encoded, "encoded.txt", "codes.txt");
            System.out.println("\nEncoded text saved to 'encoded.txt'");
            System.out.println("Huffman codes saved to 'codes.txt'");
            
            // Decode
            String decoded = huffman.decode(encoded);
            System.out.println("\nDecoded Text:\n" + decoded);
            
            // Verify correctness
            System.out.println("\nCompression successful: " + text.equals(decoded));
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}