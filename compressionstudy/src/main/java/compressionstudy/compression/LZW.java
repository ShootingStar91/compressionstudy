
package compressionstudy.compression;

import compressionstudy.util.BitStream;
import java.util.HashMap;

/**
 * Performs Lempel-Ziv-Welch compression  on a byte[] file
 * @author Arttu Kangas
 */
public class LZW {

    public byte[] compress(byte[] rawInput) {
        int bitLength = 12;
        int dictLimit = 4096;
        BitStream stream = new BitStream();
        stream.setBytes(rawInput);
        int dictSize = 256;
        // Initialize dictionary
        HashMap<String, Integer> dict = createDictionary();
        BitStream compressed = new BitStream();
        String w = "";
        for (int z = 0; z < rawInput.length; z++) {
            String c = "" + Integer.toString(stream.readNumberFromBits(8));
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
        compressed.writeBit(dict.get(w));
        return compressed.getByteArray();
    }
    
    private HashMap<String, Integer> createDictionary() {
        HashMap<String, Integer> dict = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dict.put(Integer.toString(i), i);
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
        int bitLength = 12;
        int dictSize = 256;
        BitStream stream = new BitStream();
        stream.setBytes(rawInput);
        
        HashMap<String, Integer> dict = createDictionary();
        BitStream decompressed = new BitStream();
        String w = Integer.toString(stream.readNumberFromBits(bitLength));
        for (int z = 0; z < rawInput.length; z++) {
            String a = Integer.toString(stream.readNumberFromBits(12));
            String entry = "";
            if (dict.containsKey(a)) {
                entry = Integer.toString(dict.get(a));
            } else if (a.equals(Integer.toString(dict.size()))) {
                entry = w;
            } else {
                System.out.println("ERROR");
            }
            if (entry.equals("")) {
                continue;
            }
            decompressed.writeNumber(Integer.parseInt(entry), 1);
            dict.put(w + entry, dictSize);
            dictSize++;
            w = entry;
        }
        
        return decompressed.getByteArray();
        
    }
    
}
