package compressiontest;

import compressionstudy.compression.HuffmanCompressor;
import compressionstudy.compression.HuffmanDecompressor;
import compressionstudy.dao.Dao;
import java.util.Arrays;
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

    @Test
    public void testHuffman() {
        byte[] testMaterial = "Text for testing, simple short text".getBytes();
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] compressed = compressor.encode(testMaterial);
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        byte[] decompressed = decompressor.decompress(compressed);
        boolean error = false;
        for (int i = 0; i < testMaterial.length; i++) {
            if (decompressed[i] != testMaterial[i]) {
                error = true;
            }
        }
        assertTrue(!error);
    }
    
    
}
