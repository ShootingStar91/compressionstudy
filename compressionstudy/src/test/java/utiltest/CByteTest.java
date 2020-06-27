package utiltest;

import compressionstudy.util.CByte;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author Arttu Kangas
 */
public class CByteTest {
    
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
    
    @Test
    public void testSetValueAndToString() {
        CByte cbyte = new CByte();
        cbyte.setValue(5);
        assertTrue(cbyte.toString().equals("00000101"));        
        cbyte.setValue(255);
        assertTrue(cbyte.toString().equals("11111111"));
    }

}
