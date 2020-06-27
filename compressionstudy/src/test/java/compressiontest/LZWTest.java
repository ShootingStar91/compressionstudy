package compressiontest;

import compressionstudy.compression.LZW;
import compressionstudy.dao.Dao;
import compressionstudy.util.HashMap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class LZWTest {
    

    @Test
    public void testLZWshort() {
        LZW lzw = new LZW();
        byte[] content = "abcdefgKJäölkqåwpeo+09123?!ölaksdkfljoijuoiqwerrewq".getBytes();
        byte[] compressed = lzw.compress(content);
        byte[] decompressed = lzw.decompress(compressed);
        boolean errorFound = false;
        for (int i = 0; i < content.length; i++) {
            if (content[i] != decompressed[i]) {
                errorFound = true;
            }
            
        }
        
        assertTrue(!errorFound && content.length == decompressed.length);

    }
    
    @Test
    public void testLZWlong() {
        LZW lzw = new LZW();
        Dao dao = new Dao("Test material/testfile_1_5mb");
        byte[] content = dao.getContent();
        byte[] compressed = lzw.compress(content);
        byte[] decompressed = lzw.decompress(compressed);
        boolean errorFound = false;
        for (int i = 0; i < content.length; i++) {
            if (content[i] != decompressed[i]) {
                errorFound = true;
            } 
        }
        assertTrue(!errorFound && content.length == decompressed.length);

    }
    
    @Test
    public void testDictionary() {
        LZW lzw = new LZW();
        HashMap<String, Integer> dict = lzw.createDictionary();
        assertTrue(dict.get("001") == 1);
        assertTrue(dict.get("255") == 255);
    }
    
    @Test
    public void testDictionary2() {
        LZW lzw = new LZW();
        HashMap<Integer, String> dict = lzw.createDictionary2();
        assertTrue(dict.get(1).equals("1"));
        assertTrue(dict.get(255).equals("255"));
    }
    
    @Test
    public void testPickFirst() {
        String string = "101_200_301";
        LZW lzw = new LZW();
        assertTrue(lzw.pickFirst(string).equals("101"));
    }
    
}
