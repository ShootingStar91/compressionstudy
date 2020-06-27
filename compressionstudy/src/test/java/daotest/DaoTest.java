package daotest;

import compressionstudy.dao.Dao;
import compressionstudy.util.BitStream;
import compressionstudy.util.CByte;
import compressionstudy.util.CList;
import compressionstudy.util.HashMap;
import java.io.File;

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
public class DaoTest {
    
    public DaoTest() {
        
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
    public void testDaoRead() {
        Dao dao = new Dao("Test material/random.txt");
        byte[] file = dao.getContent();
        String string = "";
        for (int i = 0; i < 5; i++) {
            string += "" + file[i];
        }
        assertTrue(string.equals("11974998753"));
    }
    
    @Test
    public void testDaoWrite() {
        Dao writeDao = new Dao("Test material/random.txt");
        writeDao.write("testFile.txt", "testText123".getBytes());
        Dao dao = new Dao("testFile.txt");
        byte[] file = dao.getContent();
        String string = "";
        for (int i = 0; i < file.length; i++) {
            string += (char)file[i];
        }
        assertTrue(string.equals("testText123"));
        new File("testFile.txt").delete();
    }
    
    
}

