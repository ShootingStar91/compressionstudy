package utiltest;

import compressionstudy.util.BitStream;
import compressionstudy.util.CByte;
import compressionstudy.util.CList;
import compressionstudy.util.HashMap;

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
public class HashMapTest {
    
    public HashMapTest() {
        
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
    public void testHashMapStringInteger() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < 200000; i++) {
            hashMap.put("key" + i, i*2);
        }
        for (int i = 0; i < 200000; i++) {
            assertTrue(hashMap.get("key" + i) == i * 2);
        }
    }    
    
    @Test
    public void testHashMapIntegerString() {
        HashMap<Integer, String> hashMap = new HashMap<>();
        for (int i = 0; i < 200000; i++) {
            hashMap.put(i * 2, "key" + i);
        }
        for (int i = 0; i < 200000; i++) {
            assertTrue(hashMap.get(i * 2).equals("key" + i));
        }
    }
    
    @Test
    public void testContains() {
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(100, "100");
        hashMap.put(2, "2");
        assertTrue(hashMap.containsKey(100) && hashMap.containsKey(2));
        assertTrue(!hashMap.containsKey(9));
    }
  
}
