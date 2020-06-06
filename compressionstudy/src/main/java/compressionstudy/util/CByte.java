
package compressionstudy.util;

/**
 * CByte: Custom Byte class
 * @author Arttu Kangas
 */
public final class CByte {

    short[] bits;
    short usedBits;
    
    public CByte() {
        bits = new short[8];
        usedBits = 0;
    }
    
    public CByte(int value) {
        this();
        setValue(value);
    }
    
    public CByte(String value) {
        this();
        for (int i=0; i<8; i++) {
            if (value.charAt(i) == '1') {
                bits[i] = 1;
                
            }
        }
    }
    
    public int getBit(int index) {
        return bits[index];
    }
    
    public void setBit(int index, int newBit) {
        bits[index] = (short)newBit;
    }
    
    public boolean append(int newBit) {
        if (usedBits >= 8) {
            return false;
        }
        bits[usedBits] = (short)newBit;
        usedBits++;
        return true;
    }
    
    public void increment() {
        setValue(getValue() + 1);
    }
    
    public int getUsedBits() {
        return usedBits;
    }
 
    public void setValue(int value) {
        bits = new short[8];
        String valueString = Integer.toBinaryString(value);
        for (int i = valueString.length() - 1; i >= 0; i--) {
            short newBit = 0;
            if (valueString.charAt(i) == '1') {
                newBit = 1;
            }
            bits[7 - ((valueString.length()-1) - i)] = newBit;
        }
        usedBits = 8;
    }

    public int getValue() {

        return Integer.parseInt(toString(), 2);
    }
    
    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < 8; i++) {
            if (bits[i] == 0) {
                string += "0";
            } else {
                string += "1";
            }
        }
        return string;
    }

}
