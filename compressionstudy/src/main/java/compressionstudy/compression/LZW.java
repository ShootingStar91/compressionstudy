
package compressionstudy.compression;

import compressionstudy.util.BitStream;
import compressionstudy.util.HashMap;

/**
 * Performs Lempel-Ziv-Welch compression and decompression on a byte[] file
 * @author Arttu Kangas
 */
public class LZW {
    
    int dictLimit = 65536;
    
    /**
     * Compress given file with LZW
     * @param rawInput byte[] of the file to be compressed
     * @return Compressed file in byte-array
     */
    public byte[] compress(byte[] rawInput) {
        int bitLength = 16;
        BitStream stream = new BitStream();
        stream.setBytes(rawInput);
        int dictSize = 256;
        HashMap<String, Integer> dict = createDictionary();
        BitStream compressed = new BitStream();
        compressed.writeNumber(rawInput.length, 4);
        String w = "";
        for (int z = 0; z < rawInput.length; z++) {
            String c = "" + Integer.toString(stream.readNumberFromBits(8));
            if (c.length() == 2) {
                c = "0" + c;
            } else if (c.length() == 1) {
                c = "00" + c;
            }
            String wc = w + c;
            if (dict.containsKey(wc)) {
                w = wc;
            } else {
                compressed.writeNumberToBits(dict.get(w), bitLength);
                dict.put(wc, dictSize);
                dictSize++;
                if (dictSize >= dictLimit - 1) {
                    dictSize = 256;
                    dict = createDictionary();
                }
                w = c;
            }
        }
        compressed.writeNumberToBits(dict.get(w), bitLength);
        return compressed.getByteArray();
    }
    
    /**
     * Create a dictionary for compression
     * @return HashMap dictionary of string-keys and Integer-values
     */
    public HashMap<String, Integer> createDictionary() {
        HashMap<String, Integer> dict = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            String intStr = Integer.toString(i);
            while (intStr.length() < 3) {
                intStr = "0" + intStr;
            }
            dict.put(intStr, i);
        }
        return dict;
    }
    
    /**
     * Create a dictionary for decompression
     * @return HashMap dictionary of Integer, String pairs
     */
    public HashMap<Integer, String> createDictionary2() {
        HashMap<Integer, String> dict = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dict.put(i, Integer.toString(i));
        }
        return dict;
    }
    
    /**
     * Decompresses a file from byte[]-array that has been compressed
     * with this classes compress-method
     * @param rawInput byte[] array containing compressed file
     * @return byte[] array containing decompressed file
     */
    public byte[] decompress(byte[] rawInput) {
        int bitLength = 16;
        int dictSize = 256;
        BitStream stream = new BitStream();
        stream.setBytes(rawInput);
        int fileSize = stream.readNumber(4);
        HashMap<Integer, String> dict = createDictionary2();
        BitStream decompressed = new BitStream();
        String w = Integer.toString(stream.readNumberFromBits(bitLength));
        decompressed.writeNumber(Integer.parseInt(w), 1);
        for (int i = 0; i < rawInput.length; i++) {
            int a = stream.readNumberFromBits(bitLength);
            String entry = "";
            if (dict.containsKey(a)) {
                entry = dict.get(a);
            } else if (a == dictSize) {
                entry = w + "_" + pickFirst(w);
            } else {
                throw new IllegalArgumentException();
            }
            writeNumbers(decompressed, entry);
            if (decompressed.getPointer() >= fileSize * 8 - 1) {
                break;
            }
            dict.put(dictSize, "" + w + "_" + pickFirst(entry));
            dictSize++;
            if (dictSize >= dictLimit - 1) {
                dictSize = 256;
                dict = createDictionary2();
            }
            w = entry;
        }
        return decompressed.getByteArray();
    }
    
    /**
     * Returns the first number before an underscore
     * @param string String to pick number from
     * @return String of a number
     */
    public String pickFirst(String string) {
        String[] numbers = string.split("_");
        return numbers[0];
    }
    
    /**
     * Writes numbers split with "_" in a string, to given stream
     * @param stream stream to write numbers into
     * @param entry String that has numbers separated by underscore
     */
    public void writeNumbers(BitStream stream, String entry) {
        String[] numbers = entry.split("_");
        for (String number : numbers) {
            stream.writeNumber(Integer.parseInt(number), 1);
        }
    }
    
}
