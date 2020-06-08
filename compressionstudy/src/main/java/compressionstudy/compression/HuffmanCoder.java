
package compressionstudy.compression;


import compressionstudy.util.BitStream;
import compressionstudy.util.CByte;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This class provides Huffman Encoding, through encode(input) and
 * decode(compressed file) methods.
 * @author Arttu Kangas
 */
public class HuffmanCoder {

    private class Entry {
        CByte character;
        Integer frequency;
        String code;
        Entry leftChild;
        Entry rightChild;

        public Entry(CByte character, Integer frequency) {
            this.character = character;
            this.frequency = frequency;
            this.code = "";
        }

        public Entry(CByte character, String code) {
            this.code = code;
        }
        
        public Entry() {
            this.code = "";
        }

        public Entry getLeftChild() {
            return leftChild;
        }
        
        public boolean leaf() {
            return leftChild == null && rightChild == null;
        }

        public void setLeftChild(Entry leftChild) {
            this.leftChild = leftChild;
        }

        public Entry getRightChild() {
            return rightChild;
        }

        public void setRightChild(Entry rightChild) {
            this.rightChild = rightChild;
        }
        
        
        
        public int compareTo(Entry entry) {
            return frequency - entry.frequency;
        }

        public CByte getCByte() {
            return character;
        }
        
        public String getCode() {
            return code;
        }
        
        public void setCode(String code) {
            this.code = code;
        }

        public void setCByte(CByte newChar) {
            this.character = newChar;
        }

        public Integer getFrequency() {
            return frequency;
        }

        public void setFrequency(Integer frequency) {
            this.frequency = frequency;
        }
        
        @Override
        public String toString() {
            if (frequency == null) {
                return "(" + this.character + " " + code + ")";
            }
            return "(" + this.character + " " + this.frequency + " " + " " + code + ")";
        }
        
    }
    

    String signs;
    int[] lengthAmounts;
    String decoded;
    Entry root;
    ArrayList<String> bitLengths;
    ArrayList<Entry> codes;
    
    public HuffmanCoder() {
        
    }
    
    /**
     * Performs huffman coding on the input data. Saves it into class variable.
     * Then call decode() to de-compress it, and then get() to get the
     * unpacked data.
     * @param rawInput Input data to be encoded
     * @return encoded list
     */
    public byte[] encode(byte[] rawInput) {
        
        CByte[] input = process(rawInput);
        
        HashMap<String, Integer> frequencies = new HashMap<>();
        
        
        // Create a table of frequencies and sort it
        for (int i = 0; i < input.length; i++) {
            CByte newByte = input[i];
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
        root = createTree(table);
        codes = new ArrayList<>();
        
        createCodes(root, "");
        
        List<CByte> byteList = new ArrayList<>();
        for (Entry e : codes) {
            CByte cbyte = e.getCByte();
            if (cbyte == null) {
                continue;
            }
            byteList.add(cbyte);
        }
        System.out.println("uniques when encoding " + byteList.size());
        // create dict
        HashMap<String, String> dict = new HashMap<>();
        for (Entry e : codes) {
            dict.put(e.getCByte().toString(), e.getCode());
        }
        
        Collections.sort(codes, (Entry e1, Entry e2) -> {
            return e1.getCode().length() - e2.getCode().length();
        });
        lengthAmounts = new int[codes.get(codes.size() - 1).getCode().length() + 1];

        // Save the bitlengths and characters, so they can be used in decoding 
        bitLengths = new ArrayList<>();
        String[] firstCodes = new String[codes.size()];
        for (int i = 0; i < codes.size(); i++) {
            String code = codes.get(i).getCode();
            int bits = code.length();
            lengthAmounts[bits]++;
            if (i > 0) {
                if (bits != codes.get(codes.size() - 1).getCode().length()) {
                    bitLengths.add(createBitString(bits));
                    firstCodes[bits] = codes.get(i).getCode();
                }
            } else {
                bitLengths.add(createBitString(bits));
            }
        }


        
        // add table about the codes in the beginning of the file
        BitStream stream = new BitStream();
        // add size of file to first (2 bytes)
        stream.writeNumber(input.length, 4);
        // add how many unique bytes there are (one byte)
        stream.writeNumber(codes.size(), 2);
        // add all unique bytes
        List<String> addedBytes = new ArrayList<>();
        
        for (int i = 0; i < lengthAmounts.length; i++) {
            for (int j = 0; j < codes.size(); j++) {
                if (codes.get(j).getCode().length() == i) {
                    stream.writeBits(codes.get(j).getCByte().toString());
                }
            }
        }
        
        //shortest code:
        for (int i = 2; i < lengthAmounts.length; i++) {
            if (lengthAmounts[i] > 0) {
                stream.writeNumber(i, 1);
                break;
            }
        }
        // longest code:
        stream.writeNumber(lengthAmounts.length - 1, 4);

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
        // actual encoding:
        for (int i = 0; i < input.length; i++) {
            CByte cbyte = new CByte();
            cbyte.setValue(input[i].getValue());
            String code = dict.get(cbyte.toString());
            stream.writeBits(code);
        }
        return stream.getByteArray();
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
    

    private String createBitString(int bits) {
        String bitString = "";
        for (int i = 0; i < codes.get(codes.size() - 1).getCode().length(); i++) {
            if (i < bits) {
                bitString += "1";
            } else {
                bitString += "0";
            }
        }
        return bitString;
    }
    

    private void createCodes(Entry node, String code) {
        if (node.getLeftChild() != null) {
            createCodes(node.getLeftChild(), code + "0");
        }
        if (node.getRightChild() != null) {
            createCodes(node.getRightChild(), code + "1");
        } 
        if (node.leaf()) {
            saveCode(node, code);
        }
    }
    
    private void saveCode(Entry node, String code) {
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
    
    
    /**
     * Decodes the data earlier encoded with encode()-method.
     * dictionary from the signs-string and bitLengths-list
     */
    public byte[] decode(byte[] encoded) {

        
        BitStream stream = new BitStream();
        stream.setBytes(encoded);

        int fileSize = stream.readNumber(4);
        int uniqueAmount = stream.readNumber(2);

        List<CByte> uniqueBytes = new ArrayList<>();
        for (int i = 0; i < uniqueAmount; i++) {
            uniqueBytes.add(stream.readCByte());
        }

        int shortestCode = stream.readNumber(1);
        int longestCode = stream.readNumber(4);
        codes = new ArrayList<>();
        int codeLength = 1;
        int uniqueIndex = 0;
        System.out.println("uniques " + uniqueBytes.size());
        while (codes.size() < uniqueAmount) {
            codeLength++;
            // how many of this length codes are there
            int lengthAmount = stream.readNumber(1);
            
            for (int i = 0; i < lengthAmount; i++) {
                String newCode = "";
                for (int j = 0; j < codeLength; j++) {
                    newCode += Integer.toString(stream.readBit());
                }
                // add the first code of this length
                Entry entry = new Entry();
                entry.setCode(newCode);
                entry.setCByte(uniqueBytes.get(uniqueIndex));
                uniqueIndex++;
                codes.add(entry);
            }
        }
        
        // create tree
        root = new Entry();
        Entry node = root;
        for (Entry entry : codes) {
            String code = entry.getCode();
            for (int i = 0; i < code.length(); i++) {
                if (code.charAt(i) == '0') {
                    if (node.getLeftChild() == null) {
                        Entry newNode = new Entry();
                        node.setLeftChild(newNode);
                        node = newNode;
                    } else {
                        node = node.getLeftChild();
                    }
                } else {
                    if (node.getRightChild() == null) {
                        Entry newNode = new Entry();
                        node.setRightChild(newNode);
                        node = newNode;
                    } else {
                        node = node.getRightChild();
                    }                
                }
            }
            node.setCode(code);
            node.setCByte(entry.getCByte());
            node = root;
            
        }
        List<CByte> decodedBytes = new ArrayList<>();
        
        // use tree to decode
        node = root;
        while (stream.hasBit()) {
            int bit = stream.readBit();
            if (bit == -1) {
                break;
            }
           
            if (bit == 1) {
                node = node.getRightChild();
            } else {
                node = node.getLeftChild();
            }
            if (node == null) {
                break;
            }
            if (node.leaf()) {
                CByte cByte = node.getCByte();
                decodedBytes.add(cByte);
                if (decodedBytes.size() >= fileSize) {
                    break;
                }
                node = root;
            }
        }
        BitStream decodedStream = new BitStream();
        decodedStream.setBytes(decodedBytes);
        return decodedStream.getByteArray();
    }
    

    public String incrementBinary(String binary) {
        int zeroes = 0;
        String zeroesString = "";
        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) == '0') {
                zeroes++;
            } else {
                break;
            }
        }
        zeroesString = binary.substring(0, zeroes);
        binary = binary.substring(zeroes);
        if (binary.length() == 0) {
            return zeroesString.substring(0, zeroesString.length() - 1) + "1";
        }
        binary = Integer.toBinaryString(Integer.parseInt(binary, 2) + 1);
        return zeroesString + binary;
    }

            
    public void buildTable() {
        codes = new ArrayList<>();
        for (int i = 0; i < signs.length(); i++) {
            int length = bitLengths.get(i).length();
            Entry entry = new Entry();
            //entry.setContent(signs.charAt(i));
            String code = "";
            for (int j = 0; j < length; j++) {
                code += "0";
            }
            codes.add(entry);
            
        }
        
    }
      
    /**
     * 
     * @return The decoded data, only call this if decode has been called prior.
     */
    public String get() {
        return decoded;
    }
    
}
