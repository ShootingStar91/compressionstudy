
package compressionstudy.main;

import compressionstudy.compression.HuffmanCoder;
import compressionstudy.dao.Dao;
import compressionstudy.util.BitStream;

/**
 *
 * @author Arttu Kangas
 */
public class Main {
    
    public static void main(String [] args) {
      
          runHuffman("test2.txt");
          decompress("compressedFile", "decompressed.txt");
    }
    
    public static void runHuffman(String fileName) {
        System.out.println("Compressing " + fileName + "...");
        Dao dao = new Dao(fileName);
        int sizeBefore = dao.getContent().length;
        HuffmanCoder hc = new HuffmanCoder();
        byte[] encodedFile = hc.encode(dao.getContent());
        int sizeAfter = encodedFile.length;
        double compressionRate = sizeAfter * 1.00 / sizeBefore * 100;
        String percentString = Double.toString(compressionRate).substring(0, 5);
        System.out.println("File size before compression: " + sizeBefore);
        System.out.println("File size after compression: " + sizeAfter);
        System.out.println("Compression percentage: " + percentString + " %");
        System.out.println("");
        dao.write("compressedFile", encodedFile);
        
    }
    
    public static void decompress(String fileName, String newFileName) {
        Dao dao = new Dao(fileName);
        byte[] data = dao.getContent();
        HuffmanCoder hc = new HuffmanCoder();
        byte[] decodedFile = hc.decode(data);
        dao.write(newFileName, decodedFile);
    }
    
}
