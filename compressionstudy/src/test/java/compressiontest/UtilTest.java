package compressiontest;

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
public class UtilTest {
    
    public UtilTest() {
        
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
    public void testListAddGetSize() {
        CList<Integer> list = new CList<>();
        int sum  = 0;
        for (int i = 0; i < 2000; i++) {
            list.add(i);
            sum += i;
        }
        int newSum = 0;
        for (int i = 0; i < list.size(); i++) {
            newSum += list.get(i);
        }
        assertTrue(newSum == sum);
    }
    
    @Test
    public void testListRemove() {
        CList<Integer> list = new CList<>();
        list.add(2);
        list.add(3);
        list.add(100);
        list.remove(1);
        assertTrue(list.size() == 2);
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
    public void testCByteSetBit() {
        CByte cbyte = new CByte();
        cbyte.setBit(6, 1);
        assertEquals(cbyte.getValue(), 2);
    }
    
    @Test
    public void testCByte() {
        CByte cbyte = new CByte();
        cbyte.setValue(20);
        assertEquals(cbyte.toString(), "00010100");
    }
}
