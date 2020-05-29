
package compressionstudy.compression;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This class provides Huffman Encoding, through encode(input) and
 * decode() methods. Get unpacked data then by get()
 * @author Arttu Kangas
 */
public class HuffmanCoder {

    private class Entry {
        Character character;
        Integer frequency;
        String code;
        Entry leftChild;
        Entry rightChild;

        public Entry(Character character, Integer frequency) {
            this.character = character;
            this.frequency = frequency;
            this.code = "";
        }

        public Entry(Character character, String code) {
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

        public Character getCharacter() {
            return character;
        }
        
        public String getCode() {
            return code;
        }
        
        public void setCode(String code) {
            this.code = code;
        }

        public void setCharacter(Character character) {
            this.character = character;
        }

        public Integer getFrequency() {
            return frequency;
        }

        public void setFrequency(Integer frequency) {
            this.frequency = frequency;
        }
        
        public String toString() {
            if (frequency == null) {
                return "(" + this.character + " " + code + ")";
            }
            return "(" + this.character + " " + this.frequency + " " + " " + code + ")";
        }
        
    }
    

    
    String encoded = "";
    String signs;
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
     * @param input Input data to be encoded
     */
    public void encode(String input) {
        
        HashMap<Character, Integer> frequencies = new HashMap<>();
        
        // Create a table of frequencies and sort it
        for (int i = 0; i < input.length(); i++) {
            Character newChar = input.charAt(i);
            if (frequencies.containsKey(newChar)) {
                frequencies.put(newChar, frequencies.get(newChar) + 1);
            } else {
                frequencies.put(newChar, 1);
            }
        }
        
        List<Entry> table = new ArrayList<>();
        
        for (Character character : frequencies.keySet()) {
            table.add(new Entry(character, frequencies.get(character)));
        }
        
        sort(table);
        
        
        List<Character> charList = new ArrayList<>();
        for (Entry e : table) {
            charList.add(e.getCharacter());
        }
 
        root = createTree(table);

        codes = new ArrayList<>();
        
        createCodes(root, "");
        
        // create dict
        HashMap<Character, String> dict = new HashMap<>();
        for (Entry e : codes) {
            dict.put(e.getCharacter(), e.getCode());
        }
        
        Collections.sort(codes, (Entry e1, Entry e2) -> {
            return e1.getCode().length() - e2.getCode().length();
        });
        
        // Save the bitlengths and characters, so they can be used in decoding 
        bitLengths = new ArrayList<>();
        for (int i = 0; i < codes.size(); i++) {
            String code = codes.get(i).getCode();
            int bits = code.length();
            if (i > 0) {
                if (bits != codes.get(codes.size() - 1).getCode().length()) {
                    bitLengths.add(createBitString(bits));
                }
            } else {
                bitLengths.add(createBitString(bits));
            }
        }


        // actual encoding:
        for (int i = 0; i < input.length(); i++) {
            Character character = input.charAt(i);
            String code = dict.get(character);
            encoded += code;
        }

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
                if (e1.getCharacter() == null || e2.getCharacter() == null) {
                    return 0;
                }
                difference = e1.getCharacter() - e2.getCharacter();
                return difference;
            }
        });
        
        
    }
    
    /**
     * Decodes the data earlier encoded with encode()-method.
     * 
     */
    public void decode() {
        
        /* Not ready, commented out temporarily:
        // construct dictionary from the signs-string and bitLengths-list
        int index = 0;
        decoded = "";
        int longestCode = bitLengths.get(0).length();
        
        ArrayList<Entry> dict = new ArrayList<>();
        int currentLength = 0;
        while (true) {
            String input;
            if (index + longestCode >= encoded.length()) {
                input = encoded.substring(index);
                for (int i = 0; i < longestCode - input.length(); i++) {
                    input += "0";
                }
            } else {
                input = encoded.substring(index, index + longestCode);
            }
            // handle input
        }
        */
        
        // Decoding using the already-built tree, this will be removed once the
        // dictionary building code is ready
        Entry node = root;
        decoded = "";
        for (int i = 0; i < encoded.length(); i++) {

            Character c = encoded.charAt(i);
            if (c == '0') {
                node = node.getLeftChild();
            } else if (c == '1') {
                node = node.getRightChild();
            } else {
                System.out.println("Fatal error, unrecognized sign in encoded data: " + c);
            }
            if (node == null) {
                break;
            }
            if (node.leaf()) {
                decoded += node.getCharacter();
                node = root;
            }
            
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
