
package compressionstudy.main;

import compressionstudy.compression.HuffmanCompressor;
import compressionstudy.compression.HuffmanDecompressor;
import compressionstudy.compression.LZW;
import compressionstudy.dao.Dao;
import compressionstudy.util.BitStream;
import java.util.Arrays;

/**
 * 
 * @author Arttu Kangas
 */
public class Main {

    public static void main(String [] args) {
        //fileTest();
        timeTest();
        //LZWTest();
        //LZWFileTest();
    }
    
    public static void LZWFileTest() {
        Dao dao = new Dao("alice29.txt");
        byte[] original =  dao.getContent();

        LZW lzw = new LZW();
        byte[] compressed = lzw.compress(original);
        BitStream stream = new BitStream();
        stream.setBytes(compressed);
        stream.readNumber(4);
        System.out.println("LZW compressed to: " + compressed.length + " from " + original.length);
        byte[] result = lzw.decompress(compressed);
        for (int i = 0; i < original.length; i++) {
            if (result[i] != original[i]) {System.out.println("ERROR !!! AT " + i);
                System.out.println("original: " + original[i] + " decompressed: " + result[i]);
            }
        }
        dao.write("aliceResult.txt", result);
    }
    
    public static void LZWTest() {
        Dao dao = new Dao("alice29.txt");
        byte[] original = dao.getContent();
        System.out.println("original:");
        for (int i = 0; i < original.length; i++) {
            System.out.println(original[i]);
        }
        LZW lzw = new LZW();
        byte[] compressed = lzw.compress(original);
        BitStream stream = new BitStream();
        stream.setBytes(compressed);
        stream.readNumber(4);
        while(true) {
            int number = stream.readNumberFromBits(12);
            if (number == 0) break;
            System.out.println(number);
        }
        System.out.println("LZW compressed to: " + compressed.length);
        byte[] result = lzw.decompress(compressed);
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }
        dao.write("aliceDecompressedLZW.txt", result);
        for (int i = 0; i < original.length; i++) {
            if (original[i] != result[i]) {
                System.out.println("error at " + i);
            }
        }
        if (original.length != result.length) System.out.println("LENGTH ERROR !!!");
    }
    
    /**
     * Run this to test the algorithm saving the compressed file and loading 
     * it again for decompression and then saving decompressed.
     */
    public static void fileTest() {
        String originalFileName = "alice29.txt";
        String newFileName = "aliceDecoded.txt";
        String compressedFileName = "compressedAlice";
        runHuffman(originalFileName, compressedFileName);
        decompress(compressedFileName, newFileName);
    }
    
    /**
     * Run this to test the speed of the algorithm. Runs 50 times and prints
     * median time. Should take about 5-15 seconds to run.
     */
    public static void timeTest() {
        Dao dao = new Dao("alice29.txt");
        byte[] original = dao.getContent();
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] compressed = new byte[1];
        long[] times = new long[50];
        for (int i = 0; i < 50; i++) {
            long time = System.nanoTime();
            compressed = compressor.encode(original);
            times[i] = System.nanoTime() - time;
        }
        Arrays.sort(times);
        System.out.println("Encoding median time: " + (times[24] / 1000000) + " ms");
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        for (int i = 0; i < 50; i++) {
            long time = System.nanoTime();
            byte[] decompressed = decompressor.decompress(compressed);
            times[i] = System.nanoTime() - time;
        
        }
        Arrays.sort(times);
        System.out.println("Decoding median time: " + (times[24] / 1000000) + " ms");
    }
    
    public static void runLZW(String fileName) {
        Dao dao = new Dao(fileName);
        byte[] file = dao.getContent();
        LZW lzw = new LZW();
        byte[] arr = lzw.compress(file);
        System.out.println("size: " + file.length);
        System.out.println("Compressed: " + arr.length);
        System.out.println("Unpacking...");
        byte[] decomp = lzw.decompress(arr);
        for (int i = 0; i < file.length; i++) {
            if (arr[i] != decomp[i]) {
                System.out.println("ERROR AT " + i);
            }
        }
        dao.write("LZWResult.txt", decomp);
    }
    
    public static void runHuffman(String fileName, String compressedFileName) {
        System.out.println("Compressing " + fileName + "...");
        Dao dao = new Dao(fileName);
        byte[] orig = dao.getContent();
        int sizeBefore = orig.length;
        HuffmanCompressor hc = new HuffmanCompressor();
        byte[] encodedFile = hc.encode(orig);
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
        byte[] decodedFile = decompressor.decompress(data);
        dao.write(newFileName, decodedFile);
    }
    
}
