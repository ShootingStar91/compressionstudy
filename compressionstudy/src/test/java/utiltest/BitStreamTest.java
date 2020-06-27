package utiltest;

import compressionstudy.util.BitStream;
import compressionstudy.util.CByte;
import compressionstudy.util.CList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author Arttu Kangas
 */
public class BitStreamTest {
    
    public BitStreamTest() {
        
    }
    
    @BeforeAll
    public static void setUpClass() {

    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testBitStreamWriteBits() {
        String testBits = "00010100";
        BitStream stream = new BitStream();
        stream.writeBits(testBits);
        stream.setPointer(0);
        assertEquals(stream.readNumber(1), 20);
    }
    
    @Test
    public void testBitStreamReadNumberFromBits() {
        BitStream stream = new BitStream();
        stream.writeNumber(20, 1);
        stream.setPointer(0);
        assertEquals(stream.readNumberFromBits(8), 20);
    }
    
    @Test
    public void testSetBytes() {
        byte[] bytes = {10, 20, 30};
        BitStream stream = new BitStream();
        stream.setBytes(bytes);
        int a = stream.readCByte().getValue();
        int b = stream.readCByte().getValue() + stream.readCByte().getValue();
        assertTrue(a == 10 && b == 50);
    }

    @Test
    public void testReadNumberFromBits() {
        BitStream stream = new BitStream();
        byte[] bytes = {10, 20, 30};
        stream.setBytes(bytes);
        int number = stream.readNumberFromBits(8);
        assertTrue(number == 10);
        number = stream.readNumberFromBits(8);
        assertTrue(number == 20);
        
    }
    
    @Test
    public void testWriteNumberToBits() {
        BitStream stream = new BitStream();
        stream.writeNumberToBits(107, 13);
        stream.setPointer(0);
        int number = stream.readNumberFromBits(13);
        assertTrue(number == 107);
    }
    
}
