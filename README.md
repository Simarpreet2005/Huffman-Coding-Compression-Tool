Huffman Coding Compression Tool 📜💾

A Java-based implementation of Huffman Coding for lossless text file compression and decompression, achieving up to 50% file size reduction for repetitive inputs. This project showcases efficient data compression using variable-length prefix codes, with interactive console visualizations for enhanced understanding.

✨ Features





Lossless Compression 📉: Compresses text files using Huffman Coding, reducing file sizes by up to 50% for optimal inputs, verified through detailed size analysis.



Console Visualizations 🖥️: Displays character frequency tables, Huffman codes, and a text-based Huffman tree for clear insight into the compression process.



File I/O Operations 📂: Reads input text, saves encoded binary strings, and stores Huffman codes for seamless decoding.



Robust Decoding 🔄: Ensures 100% accurate reconstruction of original text from compressed data.



Efficient Data Structures ⚙️: Utilizes PriorityQueue for Huffman tree construction and HashMap for code storage, optimizing performance.

🛠️ Technologies Used





Language: Java ☕



Key Concepts: Huffman Coding, Priority Queues, Binary Trees, File I/O

🚀 Usage





📋 Place input text in input.txt.



▶️ Run the program to:





Generate and display character frequencies, Huffman codes, and tree structure.



Encode the text and show the compressed size (in bits/bytes) and reduction percentage.



Save encoded text to encoded.txt and codes to codes.txt.



Decode and verify the output against the original text.



👀 Check the console for detailed visualizations and size metrics.

📊 Example

For input aaaaaa\n:





Original size: 56 bits (7 bytes)



Compressed size: 7 bits (0.88 bytes)



Size reduction: 87.50% 🎉

🌟 Future Enhancements





🗂️ Support for binary file compression.



🖼️ GUI for interactive visualization of the Huffman tree.



📈 Integration with other compression algorithms for performance comparison.

Explore the code to see how Huffman Coding optimizes data storage with variable-length encoding! 🚀
