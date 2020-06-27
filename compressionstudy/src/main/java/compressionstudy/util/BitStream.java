
package compressionstudy.util;

import java.util.List;

/**
 * Provides utility for reading and writing bits across multiple CBytes.
 * Used when you need to consider individual bits or non-full bytes.
 * Uses a pointer that automatically moves when you read or write, but can
 * also be manipulated with setPointer method. Any method that reads or writes
 * numbers moves the pointer.
 * @author Arttu Kangas
 */
public class BitStream {
    
    CList<CByte> stream;
    int pointer;
    
    
    public BitStream() {
        stream = new CList<>();
        pointer = 0;
    }
    
    /**
     * Check if the stream has still bits unread/unwritten
     * @return False if pointer is at end of the stream, otherwise true
     */
    public boolean hasBit() {
        return stream.size() >= pointer / 8;
    }
    
    /**
     * Set the content of the stream from a byte[]-array.
     * @param bytes Bytes you want the stream to contain
     */
    public void setBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            short value = (short) bytes[i];
            if (value < 0) {
                value += 256;
            }
            CByte cByte = new CByte();
            cByte.setValue(value);
            stream.add(cByte);
        }
    }
    
    /**
     * Set the stream content from a CByte-list
     * @param newStream 
     */
    public void setBytes(CList<CByte> newStream) {
        stream = newStream;
    }
    
    /**
     * Reads one bit from the stream and moves the pointer one step forward.
     * @return The bit read
     */
    public int readBit() {
        int bit = stream.get(pointer / 8).getBit(pointer % 8);
        pointer++;
        return bit;
    }
    
    /**
     * Writes a number given in decimal form to the stream as bits.
     * Second parameter tells how many bytes the number will take.
     * @param number Value to be written in stream
     * @param bytes  How many bytes the number should take
     */
    public void writeNumber(int number, int bytes) {
        writeNumberToBits(number, 8 * bytes);
    }

    /**
     * Writes number in binary to stream, to supplied amount of bits
     * @param number Value to write in stream
     * @param bits How many bits the number is written into
     */
    public void writeNumberToBits(int number, int bits) { 
        String binary = Integer.toBinaryString(number);
        if (binary.length() < bits) {
            int zeroesToAdd = bits - binary.length();
            for (int i = 0; i < zeroesToAdd; i++) {
                binary = "0" + binary;
            }
        }
        writeBits(binary);
    }
    
    /**
     * Writes a single bit onto stream, pushing pointer one step forward
     * @param newBit Value of the new bit (use only 0 or 1)
     */
    public void writeBit(int newBit) {
        int index = pointer / 8;
        if (index >= stream.size()) {
            stream.add(new CByte());
        }
        stream.get(index).setBit(pointer % 8, newBit);
        pointer++;
    }
    
    /**
     * Write multiple bits from a string
     * @param bitString String of 1's and 0's to be written into stream
     */
    public void writeBits(String bitString) {
        for (int i = 0; i < bitString.length(); i++) {
            char bit = bitString.charAt(i);
            if (bit == '0') {
                writeBit(0);
            } else {
                writeBit(1);
            }
        }
    }
    
    /**
     * Reads one byte and returns it as CByte
     * @return CByte containing value of next 8 bits
     */
    public CByte readCByte() {
        int byteValue = readNumber(1);
        CByte cByte = new CByte();
        cByte.setValue(byteValue);
        return cByte;
    }
    
    public int getPointer() {
        return pointer;
    }
    
    public void setPointer(int newPointer) {
        pointer = newPointer;
    }
    
    /**
     * Read a value from given amount of next bytes
     * @param bytes How many bytes to read
     * @return Value of the bits as integer
     */
    public int readNumber(int bytes) {
        return readNumberFromBits(bytes * 8);
    }    
    
    /**
     * Reads number from given amount of next bits
     * @param bits How many bits are read
     * @return The value of the binary number read from bits
     */
    public int readNumberFromBits(int bits) {
        String numString = "";
        for (int i = 0; i < bits; i++) {
            int bit = readBit();
            numString += Integer.toString(bit);
        }
        int number = Integer.parseInt(numString, 2);
        return number;
    }
    
    /**
     * Returns the whole content as a byte[] array.
     * @return byte[] array containing the bytes of the stream
     */
    public byte[] getByteArray() {
        byte[] arr = new byte[stream.size()];
        for (int i = 0; i < stream.size(); i++) {
            int value = stream.get(i).getValue();
            if (value > 127) {
                value -= 256;
            }
            arr[i] = (byte) value;
        }
        return arr;
    }
    
}
