
package compressionstudy.util;

import compressionstudy.util.CByte;

/**
 * Represents an entry in the Huffman Coding table and tree.
 * Used in both compression and decompression.
 * Contains code as string, CByte that represents the original byte, and
 * the frequency. These might not be all used at all times.
 * @author Arttu Kangas
 */
public class Entry {
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

    /**
     * Returns true if this node has no children-nodes (is leaf in a tree)
     * @return True if this node is a leaf
     */
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
}
