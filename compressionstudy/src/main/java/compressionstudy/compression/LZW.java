
package compressionstudy.compression;

import compressionstudy.util.BitStream;
import compressionstudy.util.HashMap;

/**
 * Performs Lempel-Ziv-Welch compression  on a byte[] file
 * @author Arttu Kangas
 */
public class LZW {
    int dictLimit = 4096;
    public byte[] compress(byte[] rawInput) {
        int bitLength = 12;
        BitStream stream = new BitStream();
        stream.setBytes(rawInput);
        int dictSize = 256;
        // Initialize dictionary
        HashMap<String, Integer> dict = createDictionary();
        BitStream compressed = new BitStream();
        compressed.writeNumber(rawInput.length, 4);
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
        compressed.writeNumberToBits(dict.get(w), bitLength);
        return compressed.getByteArray();
    }
    
    private HashMap<String, Integer> createDictionary() {
        HashMap<String, Integer> dict = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dict.put(Integer.toString(i), i);
        }
        return dict;
    }
        private HashMap<Integer, String> createDictionary2() {
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
        int bitLength = 12;
        int dictSize = 256;
        BitStream stream = new BitStream();
        stream.setBytes(rawInput);
        int fileSize = stream.readNumber(4);
        HashMap<Integer, String> dict = createDictionary2();
        BitStream decompressed = new BitStream();
        
        String w = Integer.toString(stream.readNumberFromBits(bitLength));
        decompressed.writeNumber(Integer.parseInt(w), 1);
        for (int z = 0; z < rawInput.length; z++) {
            int a = stream.readNumberFromBits(bitLength);
            String entry = "";
            if (dict.containsKey(a)) {
             //   System.out.println("contained a ");
                entry = dict.get(a);
            } else if (a == dictSize) {
             //   System.out.println("didnt contain");
                entry = w + "_" + pickFirst(w);
            } else {
                System.out.println("ERROR");
            }
            if (entry.equals("")) {
                continue;
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
    
    public String pickFirst(String string) {
        String[] numbers = string.split("_");
        return numbers[0];
    }
    
    public void writeNumbers(BitStream decompressed, String entry) {
        String[] numbers = entry.split("_");
        for (String number : numbers) {
            decompressed.writeNumber(Integer.parseInt(number), 1);
        }
        
    }
    
}
