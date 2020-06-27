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
public class CByteTest {
    
    public CByteTest() {
        
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
