
package compressionstudy.compression;

import compressionstudy.util.BitStream;
import compressionstudy.util.CByte;
import java.util.ArrayList;
import java.util.List;

/**
 * This class decompresses a byte[] array file that has been compressed with
 * HuffmanCompressor.java class
 * @author Arttu Kangas
 */
public class HuffmanDecompressor {

    /**
     * Decompresses a file compressed with HuffmanCompressor class
     * @param encoded byte[] array containing compressed file
     * @return Decompressed file in byte[] array
     */
    public byte[] decompress(byte[] encoded) {
        // translate encoded byte[]-array to CBytes with BitStream
        BitStream stream = new BitStream();
        stream.setBytes(encoded);
        // read initial data
        int fileSize = stream.readNumber(4);
        int uniqueAmount = stream.readNumber(2);
        // read unique bytes
        List<CByte> uniqueBytes = readUniqueBytes(stream, uniqueAmount);
        ArrayList<Entry> codes = new ArrayList<>();
        readCodes(stream, codes, uniqueBytes, uniqueAmount);
        // create tree
        Entry root = createTree(codes);
        List<CByte> decodedBytes = new ArrayList<>();
        // use tree to decode
        decode(stream, root, decodedBytes, fileSize);
        // translate back to byte[]-form
        BitStream decodedStream = new BitStream();
        decodedStream.setBytes(decodedBytes);
        return decodedStream.getByteArray();
    }
    
    private void decode(BitStream stream, Entry root, List<CByte> decodedBytes, int fileSize) {
        Entry node = root;
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
    }
    
    /*
    Create tree out of the codes
    */
    private Entry createTree(ArrayList<Entry> codes) {
        Entry root = new Entry();
        Entry node = root;
        for (Entry entry : codes) {
            String code = entry.getCode();
            node = processCode(code, node);
            node.setCode(code);
            node.setCByte(entry.getCByte());
            node = root;
        }
        return root;
    }
    
    /*
    Read the code and take steps in the tree, creating new branches/leafs
    when necessary
    */
    private Entry processCode(String code, Entry node) {
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
        return node;
    }
    
    private ArrayList<CByte> readUniqueBytes(BitStream stream, int uniqueAmount) {
        ArrayList<CByte> uniqueBytes = new ArrayList<>();
        for (int i = 0; i < uniqueAmount; i++) {
            uniqueBytes.add(stream.readCByte());
        }
        return uniqueBytes;
    }
    
    private void readCodes(BitStream stream, ArrayList<Entry> codes, List<CByte> uniqueBytes, int uniqueAmount) {
        int codeLength = 1;
        int uniqueIndex = 0;
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

    }
}
