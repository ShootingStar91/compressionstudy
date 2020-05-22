
package compressionstudy.main;

import compressionstudy.compression.HuffmanCoder;

/**
 *
 * @author Arttu Kangas
 */
public class Main {
    
    public static void main(String [] args) {
        runHuffman("First message, very short, nothing special.");
        runHuffman("Longer message. This message is very long. Let's see how " +
                "the compression algorithm by mr. huffman will survive compressing " +
                " this longer message. Oh boy it just keeps going on and on and " + 
                " on and on and on! What a loooooong message!");
    }
    
    public static void runHuffman(String input) {
        HuffmanCoder huffman = new HuffmanCoder();
        huffman.encode(input);
        huffman.decode();
        System.out.println("DECODED: " + huffman.get());
        
    }
    
}
