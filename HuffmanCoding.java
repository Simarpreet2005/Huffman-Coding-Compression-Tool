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
// It defines how two HuffmanNode objects are compared when sorting or inserting them into a PriorityQueue (min-heap)        
    }
}

public class HuffmanCoding {
    private Map<Character, String> huffmanCodes = new HashMap<>();
    private HuffmanNode root;

    // Build Huffman Tree
    private void buildHuffmanTree(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        // Print frequency table
        System.out.println("\nCharacter Frequency Table:");
        System.out.println("-------------------------");
        System.out.printf("%-10s %-10s%n", "Character", "Frequency");
//         %-10s → String, left-aligned (-) in a field of 10 characters.
//         %-10s (second one) → Same rule for the next string column.
//         %n → Platform-independent newline (better than \n in printf).


        System.out.println("-------------------------");
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            String charDisplay = (entry.getKey() == '\n') ? "\\n" : String.valueOf(entry.getKey());
            System.out.printf("%-10s %-10d%n", charDisplay, entry.getValue());
        }
//         If the character is a newline (\n), it replaces it with the string literal "\n" (two characters: backslash + n) so it displays visibly instead of making a line break
//         Otherwise, it converts the character to a string normally
        System.out.println("-------------------------");

        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.offer(new HuffmanNode(entry.getKey(), entry.getValue(), null, null));
        }

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode parent = new HuffmanNode('\0', left.frequency + right.frequency, left, right);
            pq.offer(parent);
        }

        root = pq.poll();
        generateCodes(root, "");

        // Print Huffman codes
        System.out.println("\nHuffman Codes:");
        System.out.println("-------------------------");
        System.out.printf("%-10s %-10s%n", "Character", "Code");
        System.out.println("-------------------------");
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            String charDisplay = (entry.getKey() == '\n') ? "\\n" : String.valueOf(entry.getKey());
            System.out.printf("%-10s %-10s%n", charDisplay, entry.getValue());
        }
        System.out.println("-------------------------");

        // Print tree
        System.out.println("\nHuffman Tree Visualization:");
        System.out.println("-------------------------");
        printHuffmanTree(root, 0);
        System.out.println("-------------------------");
    }

    private void generateCodes(HuffmanNode node, String code) {
        if (node == null) return;
        if (node.isLeaf()) {
            huffmanCodes.put(node.ch, code.length() > 0 ? code : "0");
            // special case for when the input text has only one unique character — in that case, Huffman assigns "0" as its code
        }
        // recursion
        generateCodes(node.left, code + "0"); // Going left adds a "0" to the current code
        generateCodes(node.right, code + "1"); // Going right adds a "1" to the current code
    }

    private void printHuffmanTree(HuffmanNode node, int level) {
        if (node == null) return;
        printHuffmanTree(node.right, level + 1);
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
        String display = node.isLeaf() ? "'" + (node.ch == '\n' ? "\\n" : node.ch) + "'(" + node.frequency + ")"
                : "(" + node.frequency + ")";
        System.out.println(display);
        printHuffmanTree(node.left, level + 1);
//      Right child is printed above
//      Left child is printed below
//      Indentation increases with tree depth
//      Leaf nodes display 'character'(frequency)
//      Internal nodes display (frequency)
//      the order is right → node → left so the right child appears above the current node in the sideways layout, and the left child appears below
    }

    public String encode(String text, long[] sizeMetrics) {
        buildHuffmanTree(text);
        StringBuilder encoded = new StringBuilder();
        long compressedBits = 0;
        for (char c : text.toCharArray()) {
            String code = huffmanCodes.get(c);
            encoded.append(code);
            compressedBits += code.length();
        }
        sizeMetrics[0] = text.length() * 8;
        sizeMetrics[1] = compressedBits;
        return encoded.toString();
    }

    // This decode() method takes a binary string (produced by your Huffman encoding) and reconstructs the original text by traversing the Huffman tree.
    public String decode(String encodedText) {
        StringBuilder decoded = new StringBuilder();
        HuffmanNode current = root;
        for (char bit : encodedText.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;
            if (current.isLeaf()) {
                decoded.append(current.ch);
                current = root; // Reset current to root to decode the next character
            }
        }
        return decoded.toString();
    }
    
     // Huffman coding program can export the results into files instead of just printing them to the console.
    public void saveToFile(String encodedText, String outputFilePath, String codesFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(encodedText);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(codesFilePath))) {
            for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
        }
    }
  

// reads text from input file
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

    public static void main(String[] args) {
        try {
            HuffmanCoding huffman = new HuffmanCoding();

            String inputFile = "input.txt";
            System.out.println("Reading from: " + inputFile);
            String text = huffman.readFromFile(inputFile);
            System.out.println("\nOriginal Text:\n" + text);

            long[] sizeMetrics = new long[2];
           // Creates an array with 2 slots:
           // sizeMetrics[0] → original size in bits
           // sizeMetrics[1] → compressed size in bits
            String encoded = huffman.encode(text, sizeMetrics);
            System.out.println("\nEncoded Text:\n" + encoded);

            double originalBytes = sizeMetrics[0] / 8.0;
            double compressedBytes = sizeMetrics[1] / 8.0;
            double compressionRatio = (sizeMetrics[0] - sizeMetrics[1]) / (double) sizeMetrics[0] * 100;
            System.out.println("\nSize Analysis:");
            System.out.println("-------------------------");
            System.out.printf("Original Size: %.2f bytes (%d bits)%n", originalBytes, sizeMetrics[0]);
            System.out.printf("Compressed Size: %.2f bytes (%d bits)%n", compressedBytes, sizeMetrics[1]);
            System.out.printf("Size Reduction: %.2f%%%n", compressionRatio);
            System.out.println("-------------------------");

            huffman.saveToFile(encoded, "encoded.txt", "codes.txt");
            // encoded.txt → contains the compressed binary string
            // codes.txt → contains the Huffman code mapping for each character
            System.out.println("Encoded text saved to encoded.txt");
            System.out.println("Huffman codes saved to codes.txt");

            String decoded = huffman.decode(encoded);
            System.out.println("\nDecoded Text:\n" + decoded);
            System.out.println("\nCompression successful: " + text.equals(decoded));
            // If true → compression + decompression was lossless
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
