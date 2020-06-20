
package compressionstudy.compression;


import compressionstudy.util.BitStream;
import compressionstudy.util.CByte;
import compressionstudy.util.CList;
import compressionstudy.util.HashMap;

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
        CList<Entry> table = createTable(input);
        Entry root = createTree(table);
        // Create the codes from the tree structure
        CList<Entry> codes = new CList<>();
        createCodes(root, "", codes);
        // Sort codes by their length
        sortList(codes, true);
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
    
    private HashMap<String, String> createDictionary(CList<Entry> codes) {
        HashMap<String, String> dict = new HashMap<>();
        for (int i = 0; i < codes.size(); i++) {
            Entry entry = codes.get(i);
            dict.put(entry.getCByte().toString(), entry.getCode());
        }
        return dict;
    }
    

    private void countLengthAmounts(CList<Entry> codes, int[] lengthAmounts) {
        for (int i = 0; i < codes.size(); i++) {
            String code = codes.get(i).getCode();
            int bits = code.length();
            lengthAmounts[bits]++;
        }
    }
    
    private void writeUniqueBytes(CList<Entry> codes, BitStream stream, int[] lengthAmounts) {
        for (int i = 0; i < lengthAmounts.length; i++) {
            for (int j = 0; j < codes.size(); j++) {
                if (codes.get(j).getCode().length() == i) {
                    stream.writeBits(codes.get(j).getCByte().toString());
                }
            }
        }
    }
    
    private void writeCodeData(CList<Entry> codes, BitStream stream, int[] lengthAmounts) {
        // add data about codes
        for (int i = 1; i < lengthAmounts.length; i++) {
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
            short value = (short) rawinput[i];
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
    private CList<Entry> createTable(CByte[] input) {
        HashMap<String, Integer> frequencies = new HashMap<>();
        for (CByte newByte : input) {
            if (frequencies.containsKey(newByte.toString())) {
                frequencies.put(newByte.toString(), frequencies.get(newByte.toString()) + 1);
            } else {
                frequencies.put(newByte.toString(), 1);
            }
        }
        CList<Entry> table = new CList<>();
        for (int i = 0; i < frequencies.keySet().size(); i++) {
            String string = frequencies.keySet().get(i);
            table.add(new Entry(new CByte(string), frequencies.get(string)));
        }
        sortList(table, false);
        return table;
    }

    private void createCodes(Entry node, String code, CList<Entry> codes) {
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
    
    private void saveCode(Entry node, String code, CList<Entry> codes) {
        node.setCode(code);
        codes.add(node);
    }
    
    private Entry createTree(CList<Entry> table) {
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
            sortList(table, false);
        }
        return table.get(0);
    }
    
    public void testSorting() {
        Entry e1 = new Entry();
        e1.setCByte(new CByte("00100001"));
        e1.setFrequency(10);     
        
        Entry e2 = new Entry();
        e2.setCByte(new CByte("00000010"));
        e2.setFrequency(300);        
        
        Entry e3 = new Entry();
        e3.setCByte(new CByte("00000100"));
        e3.setFrequency(10);     
        
        Entry e4 = new Entry();
        e4.setCByte(new CByte("00000011"));
        e4.setFrequency(100);
        
        CList<Entry> list = new CList<>();
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        sortList(list, false);
        for (int i = 0; i< 4; i++) {
            Entry e = list.get(i);
            System.out.println(e.getFrequency() + "   ,   " + e.getCByte().getValue());
        }
    }
    
    /**
     * Call this with true in the codeLength parameter,
     * to sort based on codeLengths. If it's false,
     * sorting will happen primarily by frequencies and
     * secondarily by CByte values.
     * @param list
     * @param codeLength 
     */
    private void sortList(CList<Entry> list, boolean codeLength) {
        quickSort(list, 0, list.size()-1, codeLength);
    }
    
    private int part(CList<Entry> list, int low, int high, boolean codeLength) {
        Entry pivot = list.get(high);
        int index = low - 1;
        for (int i = low; i < high; i++) {
            if (compare(list.get(i), pivot, codeLength)) {
                index++;
                Entry temp = list.get(index);
                list.replace(index, list.get(i));
                list.replace(i, temp);
                
            }
        }
        Entry temp = list.get(index + 1);
        list.replace(index + 1, list.get(high));
        list.replace(high, temp);
        return index + 1;
    }
    
    private boolean compare(Entry e1, Entry e2, boolean codeLength) {
        if (codeLength) {
            return e1.getCode().length() < e2.getCode().length();
        }
        //if (e1 == null || e2 == null) return true;
        if (e1.frequency != e2.frequency) {
            return e1.frequency <= e2.frequency;
        } else {
            if (e1.getCByte() == null || e2.getCByte() == null) {
                return true;
            }
            return e1.getCByte().getValue() <= e2.getCByte().getValue();
        }
    }
    
    private void quickSort(CList<Entry> list, int low, int high, boolean codeLength) {
        if (low < high) {
            int index = part(list, low, high, codeLength);
            quickSort(list, low, index - 1, codeLength);
            quickSort(list, index + 1, high, codeLength);
        }
    }
    
    
}


