package utiltest;

import compressionstudy.util.CList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author Arttu Kangas
 */
public class CListTest {

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
