package compressiontest;

import compressionstudy.compression.HuffmanCompressor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author arkangas
 */
public class HuffmanTest {
    
    HuffmanCompressor encoder;
    
    public HuffmanTest() {
        
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


    public void testEncodingAndDecoding(String data) {
      //  encoder = new HuffmanCoder();
       // byte[] data = encoder.encode();
      //  encoder.decode();
      //  assertTrue(encoder.get().equals(data));
    }
    
    @Test 
    public void testShortMessage() {
        testEncodingAndDecoding("This is a short message. Not long at all. Quite short.");
    }
    
    @Test
    public void testLongMessage() {
        testEncodingAndDecoding("Longer message. This message is very long. Let's see how " +
                " the algorithm will do with this. Does it still stay the same after " +
                " being compressed? Nobody knows. Maybe China knows, you should ask them.");
    }
    
}
