
package compressionstudy.main;

import compressionstudy.compression.HuffmanCompressor;
import compressionstudy.compression.HuffmanDecompressor;
import compressionstudy.dao.Dao;

/**
 *
 * @author Arttu Kangas
 */
public class Main {

    public static void main(String [] args) {
          String originalFileName = "alice29.txt";
          String newFileName = "aliceDecoded.txt";
          String compressedFileName = "compressedAlice";
          runHuffman(originalFileName, compressedFileName);
          decompress(compressedFileName, newFileName);
    }
    
    public static void runHuffman(String fileName, String compressedFileName) {
        System.out.println("Compressing " + fileName + "...");
        Dao dao = new Dao(fileName);
        int sizeBefore = dao.getContent().length;
        HuffmanCompressor hc = new HuffmanCompressor();
        byte[] encodedFile = hc.encode(dao.getContent());
        int sizeAfter = encodedFile.length;
        double compressionRate = sizeAfter * 1.00 / sizeBefore * 100;
        String percentString = Double.toString(compressionRate).substring(0, 5);
        System.out.println("File size before compression: " + sizeBefore);
        System.out.println("File size after compression: " + sizeAfter);
        System.out.println("Compression percentage: " + percentString + " %");
        System.out.println("");
        dao.write(compressedFileName, encodedFile);
        
    }
    
    public static void decompress(String fileName, String newFileName) {
        Dao dao = new Dao(fileName);
        byte[] data = dao.getContent();
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        byte[] decodedFile = decompressor.decode(data);
        dao.write(newFileName, decodedFile);
    }
    
}
