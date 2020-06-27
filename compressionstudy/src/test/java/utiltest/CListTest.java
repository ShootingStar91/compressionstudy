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
public class CListTest {
    
    public CListTest() {
        
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
    public void testListAddGetSizeResize() {
        CList<Integer> list = new CList<>();
        int sum  = 0;
        for (int i = 0; i < 20000; i++) {
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
    
}
