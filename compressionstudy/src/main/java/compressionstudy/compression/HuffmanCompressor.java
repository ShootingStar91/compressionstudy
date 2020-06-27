
package compressionstudy.compression;


import compressionstudy.util.Entry;
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
    
    /**
     * Create dictionary from codes, that has strings of unique bytes as keys,
     * and their corresponding codes as values
     * @param codes List of codes and their bytes
     * @return HasMap<String, String> Dictionary of bytes and codes
     */
    public HashMap<String, String> createDictionary(CList<Entry> codes) {
        HashMap<String, String> dict = new HashMap<>();
        for (int i = 0; i < codes.size(); i++) {
            Entry entry = codes.get(i);
            dict.put(entry.getCByte().toString(), entry.getCode());
        }
        return dict;
    }
    
    /**
     * Calculate how many of each code length exists in the codes
     * @param codes The previously made codes
     * @param lengthAmounts int array that the results are put into
     */
    public void countLengthAmounts(CList<Entry> codes, int[] lengthAmounts) {
        for (int i = 0; i < codes.size(); i++) {
            String code = codes.get(i).getCode();
            int bits = code.length();
            lengthAmounts[bits]++;
        }
    }
    
    /**
     * Writes all the unique bytes into the BitStream
     * @param codes All unique bytes
     * @param stream stream to write to
     * @param lengthAmounts Amounts of each code length
     */
    private void writeUniqueBytes(CList<Entry> codes, BitStream stream, int[] lengthAmounts) {
        for (int i = 0; i < lengthAmounts.length; i++) {
            for (int j = 0; j < codes.size(); j++) {
                if (codes.get(j).getCode().length() == i) {
                    stream.writeBits(codes.get(j).getCByte().toString());
                }
            }
        }
    }
    
    /**
     * Writes data about the codes into the stream. First writes how many
     * numbers of the code exists, and then the actual codes. Starts from length 1
     * @param codes Codes to write
     * @param stream Stream to write into
     * @param lengthAmounts Amounts of each code length
     */
    private void writeCodeData(CList<Entry> codes, BitStream stream, int[] lengthAmounts) {
        for (int i = 1; i < lengthAmounts.length; i++) {
            stream.writeNumber(lengthAmounts[i], 1);
            for (int j = 0; j < codes.size(); j++) {
                if (codes.get(j).getCode().length() == i) {
                    stream.writeBits(codes.get(j).getCode());
                }
            }
        }
    }
    
    /**
     * Write the original files bytes into the stream, but as the codes created
     * earlier into dict
     * @param input The original file
     * @param dict The dictionary containing bytes as keys and values as their codes
     * @param stream Stream to write into
     */
    private void writeCodes(CByte[] input, HashMap<String, String> dict, BitStream stream) {
        for (int i = 0; i < input.length; i++) {
            CByte cbyte = new CByte();
            cbyte.setValue(input[i].getValue());
            String code = dict.get(cbyte.toString());
            stream.writeBits(code);
        }
    }
    
    /**
     * Creates a CByte-array out of the original byte[] input
     * @param rawinput the original file to compress
     * @return The original file as CByte-array
     */
    public CByte[] process(byte[] rawinput) {
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
    
    /**
     * Goes through the original file to find all unique bytes, and counts
     * their frequencies, sorts it by frequencies and byte-values
     * @param input Original file in CByte-array
     * @return Sorted CList of Entries that contain all unique bytes and their frequencies
     */
    public CList<Entry> createTable(CByte[] input) {
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

    /**
     * Create the codes out of the tree structure, recursively
     * @param node Current node to check, on first call give root node
     * @param code Current code, set empty "" on first call
     * @param codes List of codes where the codes will be written into
     */
    public void createCodes(Entry node, String code, CList<Entry> codes) {
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
    
    /**
     * Save the code created into the codes-list
     * @param node
     * @param code
     * @param codes 
     */
    private void saveCode(Entry node, String code, CList<Entry> codes) {
        node.setCode(code);
        codes.add(node);
    }
    
    /**
     * Create the Huffman tree
     * @param table CList containing all CBytes and their frequencies
     * @return Root node of the tree
     */
    public Entry createTree(CList<Entry> table) {
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
    
    /**
     * Call this with true in the codeLength parameter,
     * to sort based on codeLengths. If it's false,
     * sorting will happen primarily by frequencies and
     * secondarily by CByte values.
     * @param list
     * @param codeLength 
     */
    public void sortList(CList<Entry> list, boolean codeLength) {
        quickSort(list, 0, list.size() - 1, codeLength);
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
        if (e1.getFrequency() != e2.getFrequency()) {
            return e1.getFrequency() <= e2.getFrequency();
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


