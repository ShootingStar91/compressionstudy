package compressiontest;

import compressionstudy.util.List;

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
        List<Integer> list = new List<>();
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
        List<Integer> list = new List<>();
        list.add(2);
        list.add(3);
        list.add(100);
        list.remove(1);
        assertTrue(list.size() == 2);
    }
    
    

    
}
