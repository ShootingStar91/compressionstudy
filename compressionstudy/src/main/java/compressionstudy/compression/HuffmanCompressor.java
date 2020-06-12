
package compressionstudy.compression;


import compressionstudy.util.BitStream;
import compressionstudy.util.CByte;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This class performs file compression using the Huffman Coding
 * @author Arttu Kangas
 */
public class HuffmanCompressor {
    /**
     * Performs huffman coding on the input data, and returns it in byte[] form
     * @param rawInput Input data to be encoded in byte[] form
     * @return Compressed data in byte[] form
     */
    public byte[] encode(byte[] rawInput) {
        // Translate the input from byte-array to array of CBytes
        CByte[] input = process(rawInput);
        // Create table
        List<Entry> table = createTable(input);
        Entry root = createTree(table);
        // Create the codes from the tree structure
        ArrayList<Entry> codes = new ArrayList<>();
        createCodes(root, "", codes);
        // Sort codes by their length
        sortCodeTable(codes);
        // create dictionary of bytes and their codes
        HashMap<String, String> dict = createDictionary(codes);
        int[] lengthAmounts = new int[codes.get(codes.size() - 1).getCode().length() + 1];
        // count how many codes are for each code length
        countLengthAmounts(codes, lengthAmounts);
        BitStream stream = new BitStream();
        // add size of file to first (4 bytes)
        stream.writeNumber(input.length, 4);
        // add how many unique bytes there are (two bytes)
        stream.writeNumber(codes.size(), 2);
        // add all unique bytes
        writeUniqueBytes(codes, stream, lengthAmounts);
        // For each code length, write how many of them exists, and then all the codes of that length
        writeCodeData(codes, stream, lengthAmounts);
        // Actual encoding
        writeCodes(input, dict, stream);
        return stream.getByteArray();
    }
    
    private HashMap<String, String> createDictionary(ArrayList<Entry> codes) {
        HashMap<String, String> dict = new HashMap<>();
        for (Entry entry : codes) {
            dict.put(entry.getCByte().toString(), entry.getCode());
        }
        return dict;
    }
    
    private void sortCodeTable(ArrayList<Entry> codes) {
        // Sort codes-table by their code length and get the longest code length
        Collections.sort(codes, (Entry e1, Entry e2) -> {
            return e1.getCode().length() - e2.getCode().length();
        });
        
    }
    
    private void countLengthAmounts(ArrayList<Entry> codes, int[] lengthAmounts) {
        for (int i = 0; i < codes.size(); i++) {
            String code = codes.get(i).getCode();
            int bits = code.length();
            lengthAmounts[bits]++;
        }
    }
    
    private void writeUniqueBytes(ArrayList<Entry> codes, BitStream stream, int[] lengthAmounts) {
        for (int i = 0; i < lengthAmounts.length; i++) {
            for (int j = 0; j < codes.size(); j++) {
                if (codes.get(j).getCode().length() == i) {
                    stream.writeBits(codes.get(j).getCByte().toString());
                }
            }
        }
    }
    
    private void writeCodeData(ArrayList<Entry> codes, BitStream stream, int[] lengthAmounts) {
        // add data about codes
        for (int i = 2; i < lengthAmounts.length; i++) {
            // how many of this code length exists
            stream.writeNumber(lengthAmounts[i], 1);

            for (int j = 0; j < codes.size(); j++) {
                if (codes.get(j).getCode().length() == i) {
                    stream.writeBits(codes.get(j).getCode());
                }
            }
        }
    }
    
    // Writes the original file's bytes into bitstream as huffman codes
    private void writeCodes(CByte[] input, HashMap<String, String> dict, BitStream stream) {
        for (int i = 0; i < input.length; i++) {
            CByte cbyte = new CByte();
            cbyte.setValue(input[i].getValue());
            String code = dict.get(cbyte.toString());
            stream.writeBits(code);
        }
    }

    private CByte[] process(byte[] rawinput) {
        CByte[] result = new CByte[rawinput.length];
        for (int i = 0; i < rawinput.length; i++) {
            short value = (short)rawinput[i];
            if (value < 0) {
                value += 256;
            }
            result[i] = new CByte(value);
        }
        
        return result;
    }
    
    /*
    Goes through all bytes in the file, counting how often they appear, and
    returns a list of them which is sorted by the frequencies
    */
    private List<Entry> createTable(CByte[] input) {
        HashMap<String, Integer> frequencies = new HashMap<>();
        for (CByte newByte : input) {
            if (frequencies.containsKey(newByte.toString())) {
                frequencies.put(newByte.toString(), frequencies.get(newByte.toString()) + 1);
            } else {
                frequencies.put(newByte.toString(), 1);
            }
        }
        List<Entry> table = new ArrayList<>();
        for (String string : frequencies.keySet()) {
            table.add(new Entry(new CByte(string), frequencies.get(string)));
        }
        sort(table);
        return table;
    }

    private void createCodes(Entry node, String code, ArrayList<Entry> codes) {
        if (node.getLeftChild() != null) {
            createCodes(node.getLeftChild(), code + "0", codes);
        }
        if (node.getRightChild() != null) {
            createCodes(node.getRightChild(), code + "1", codes);
        } 
        if (node.leaf()) {
            saveCode(node, code, codes);
        }
    }
    
    private void saveCode(Entry node, String code, ArrayList<Entry> codes) {
        node.setCode(code);
        codes.add(node);
    }
    
    private Entry createTree(List<Entry> table) {
        while (table.size() > 1) {
            Entry e1 = table.get(0);
            Entry e2 = table.get(1);
            table.remove(1);
            table.remove(0);
            Entry e = new Entry();
            e1.setCode(e1.getCode() + "0");
            e2.setCode(e2.getCode() + "1");
            e.setFrequency(e1.getFrequency() + e2.getFrequency());
            e.setLeftChild(e1);
            e.setRightChild(e2);
            table.add(e);
            if (table.size() == 1) {
                break;
            }
            sort(table);
        }
        return table.get(0);
    }

    private void sort(List<Entry> table) {
        Collections.sort(table, (Entry e1, Entry e2) -> {
            int difference = e1.compareTo(e2);
            if (difference != 0) {
                return difference;
            } else {
                if (e1.getCByte() == null || e2.getCByte() == null) {
                    return 0;
                }
                difference = e1.getCByte().getValue() - e2.getCByte().getValue();
                return difference;
            }
        });
    }
    
    
}
