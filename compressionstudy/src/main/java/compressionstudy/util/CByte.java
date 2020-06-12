
package compressionstudy.util;

/**
 * Custom Byte class, which allows individual bits to be written and read
 * easily.
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
        for (int i = 0; i < 8; i++) {
            if (value.charAt(i) == '1') {
                bits[i] = 1;
            }
        }
    }
    
    /**
     * Get a bit at the given index
     * @param index Integer in range 0-7, index of bit
     * @return Bit at given index
     */
    public int getBit(int index) {
        return bits[index];
    }
    
    /**
     * Sets the bit at given index
     * @param index Index to be changed
     * @param newBit New bit value
     */
    public void setBit(int index, int newBit) {
        bits[index] = (short) newBit;
    }
    
    /**
     * Adds the given bit value to the first spot which is not written to yet
     * @param newBit New bit value
     * @return False if all the bits have been written to before and
     * nothing was written
     */
    public boolean append(int newBit) {
        if (usedBits >= 8) {
            return false;
        }
        bits[usedBits] = (short) newBit;
        usedBits++;
        return true;
    }
    
    /**
     * Increments the value of this byte by one
     */
    public void increment() {
        setValue(getValue() + 1);
    }
    
    public int getUsedBits() {
        return usedBits;
    }
 
    /**
     * Sets the value of this byte to given integer value
     * @param value Value in range 0-255
     */
    public void setValue(int value) {
        bits = new short[8];
        String valueString = Integer.toBinaryString(value);
        for (int i = valueString.length() - 1; i >= 0; i--) {
            short newBit = 0;
            if (valueString.charAt(i) == '1') {
                newBit = 1;
            }
            bits[7 - ((valueString.length() - 1) - i)] = newBit;
        }
        usedBits = 8;
    }

    /**
     * Returns the value of the byte
     * @return Value of the byte as integer (0-255)
     */
    public int getValue() {
        return Integer.parseInt(toString(), 2);
    }
    
    /**
     * Returns the byte as bits, including potential zeroes in front 
     * @return String of bits, length is always 8
     */
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
