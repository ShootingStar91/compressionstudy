
package compressionstudy.compression;

import compressionstudy.util.Entry;
import compressionstudy.util.BitStream;
import compressionstudy.util.CByte;
import compressionstudy.util.CList;

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
        CList<CByte> uniqueBytes = readUniqueBytes(stream, uniqueAmount);
        CList<Entry> codes = new CList<>();
        readCodes(stream, codes, uniqueBytes, uniqueAmount);
        // create tree
        Entry root = createTree(codes);
        CList<CByte> decodedBytes = new CList<>();
        // use tree to decode
        decode(stream, root, decodedBytes, fileSize);
        // translate back to byte[]-form
        BitStream decodedStream = new BitStream();
        decodedStream.setBytes(decodedBytes);
        return decodedStream.getByteArray();
    }
    
    /**
     * Decodes the file with the given codes
     * @param stream stream to read from
     * @param root Root node of the Huffman Tree
     * @param decodedBytes This will be the result
     * @param fileSize The size of original file in bytes
     */
    private void decode(BitStream stream, Entry root, CList<CByte> decodedBytes, int fileSize) {
        Entry node = root;
        while (stream.hasBit()) {
            int bit = stream.readBit();
            if (bit == 1) {
                node = node.getRightChild();
            } else {
                node = node.getLeftChild();
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
    
    /**
     * Reconstruct Huffman tree
     * @param codes Contains codes and their corresponding bytes
     * @return Root node of the constructed tree
     */
    public Entry createTree(CList<Entry> codes) {
        Entry root = new Entry();
        Entry node = root;
        for (int i = 0; i < codes.size(); i++) {
            Entry entry = codes.get(i);
            String code = entry.getCode();
            node = processCode(code, node);
            node.setCode(code);
            node.setCByte(entry.getCByte());
            node = root;
        }
        return root;
    }
    
    /**
    * Read the code and take steps in the tree, creating new branches/leafs
    * when necessary
    * @param code Code to process
    * @param node root node
    */
    public Entry processCode(String code, Entry node) {
        for (int i = 0; i < code.length(); i++) {
            Entry newNode = new Entry();
            if (code.charAt(i) == '0') {
                if (node.getLeftChild() == null) {
                    node.setLeftChild(newNode);
                    node = newNode;
                } else {
                    node = node.getLeftChild();
                }
            } else {
                if (node.getRightChild() == null) {
                    node.setRightChild(newNode);
                    node = newNode;
                } else {
                    node = node.getRightChild();
                }                
            }
        }
        return node;
    }

    /**
     * Reads all unique bytes from the stream
     * @param stream stream to read from
     * @param uniqueAmount How many uniques
     * @return Unique bytes in a CList of CBytes
     */
    public CList<CByte> readUniqueBytes(BitStream stream, int uniqueAmount) {
        CList<CByte> uniqueBytes = new CList<>();
        for (int i = 0; i < uniqueAmount; i++) {
            uniqueBytes.add(stream.readCByte());
        }
        return uniqueBytes;
    }
    
    /**
     * Read codes from stream
     * @param stream stream to read from
     * @param codes Where codes are saved
     * @param uniqueBytes All unique bytes
     * @param uniqueAmount How many uniques there are
     */
    private void readCodes(BitStream stream, CList<Entry> codes, CList<CByte> uniqueBytes, int uniqueAmount) {
        int codeLength = 0;
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
