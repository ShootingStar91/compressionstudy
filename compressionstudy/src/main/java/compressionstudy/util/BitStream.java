
package compressionstudy.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arttu Kangas
 */
public class BitStream {
    
    List<CByte> stream;
    int pointer;
    
    
    public BitStream() {
        stream = new ArrayList<>();
        pointer = 0;
    }
    
    public boolean hasBit() {
        return stream.size() >= pointer / 8;
    }
    
    public void setBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            short value = (short)bytes[i];
            if (value < 0) {
                value += 256;
            }
            CByte cByte = new CByte();
            cByte.setValue(value);
            stream.add(cByte);
        }
    }
    
    public void setBytes(List<CByte> newStream) {
        stream = newStream;
        System.out.println("stream size " + stream.size());
    }
    
    // return bit at current pointer
    public int readBit() {
        int index = pointer / 8;
        if (index >= stream.size()) {
            return -1;
        }
        int bit = stream.get(pointer / 8).getBit(pointer % 8);
        pointer++;
        return bit;
    }
    
    public void writeNumber(int number, int bytes) {
        String binary = Integer.toBinaryString(number);
        if (binary.length() < (bytes * 8)) {
            int zeroesToAdd = bytes * 8 - binary.length();
            for (int i = 0; i < zeroesToAdd; i++) {
                binary = "0" + binary;
            }
        }
        writeBits(binary);
    }
    
    public void writeBit(int newBit) {
        int index = pointer / 8;
        if (index >= stream.size()) {
            stream.add(new CByte());
        }
        stream.get(index).setBit(pointer % 8, newBit);
        pointer++;
    }
    
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
    
    public int readNumber(int bytes) {
        String numString = "";
        for (int i = 0; i < bytes * 8; i++) {
            int bit = readBit();
            numString += Integer.toString(bit);
        }
        return Integer.parseInt(numString, 2);
    }
    
    public byte[] getByteArray() {
        byte[] arr = new byte[stream.size()];
        for (int i = 0; i < stream.size(); i++) {
            int value = stream.get(i).getValue();
            if (value > 127) {
                value -= 256;
            }
            arr[i] = (byte)value;
        }
        return arr;
    }
    
}
