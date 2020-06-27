package compressiontest;

import compressionstudy.util.Entry;
import compressionstudy.compression.HuffmanCompressor;
import compressionstudy.compression.HuffmanDecompressor;
import compressionstudy.dao.Dao;
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
        byte[] testMaterial = "Text for testing_äöä'*ÄÖ*Äöäpqwe1§23!#%áéŵ".getBytes();
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] compressed = compressor.encode(testMaterial);
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        byte[] decompressed = decompressor.decompress(compressed);
        boolean error = false;
        if (decompressed.length != testMaterial.length) {
            error = true;
        }
        for (int i = 0; i < testMaterial.length; i++) {
            if (decompressed[i] != testMaterial[i]) {
                error = true;
            }
        }
        assertTrue(!error);
    }    
    
    @Test
    public void testHuffmanLong() {
        Dao dao = new Dao("Test material/testfile_1_5mb");
        byte[] testMaterial = dao.getContent();
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] compressed = compressor.encode(testMaterial);
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        byte[] decompressed = decompressor.decompress(compressed);
        boolean error = false;
        if (decompressed.length != testMaterial.length) {
            error = true;
        }
        for (int i = 0; i < testMaterial.length; i++) {
            if (decompressed[i] != testMaterial[i]) {
                error = true;
            }
        }
        assertTrue(!error);
    }
    
    @Test
    public void testProcess() {
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] rawInput = "helloworld".getBytes();
        CByte[] bytes = compressor.process(rawInput);
        assertTrue(bytes[0].toString().equals("01101000"));
        assertTrue(bytes[2].toString().equals("01101100"));
        assertTrue(bytes[3].toString().equals("01101100"));
        assertTrue(bytes[9].toString().equals("01100100"));
    }
    
    @Test
    public void testCreateTable() {
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] rawInput = "helloworld".getBytes();
        CByte[] bytes = compressor.process(rawInput);
        CList<Entry> table = compressor.createTable(bytes);
        assertTrue(table.get(0).getCByte().toString().equals("01100100"));
        assertTrue(table.get(6).getCByte().toString().equals("01101100"));
        assertTrue(table.get(0).getFrequency() == 1);
        assertTrue(table.get(6).getFrequency() == 3);
    }
    
    @Test
    public void testCreateTree() {
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] rawInput = "helloworld".getBytes();
        CByte[] bytes = compressor.process(rawInput);
        CList<Entry> table = compressor.createTable(bytes);
        Entry root = compressor.createTree(table);
        Entry leftNode = root.getLeftChild();
        leftNode = leftNode.getLeftChild();
        leftNode = leftNode.getLeftChild();
        Entry rightNode = root.getRightChild();
        rightNode = rightNode.getRightChild();
        rightNode = rightNode.getRightChild();
        assertTrue(leftNode.getCByte().toString().equals("01100100"));
        assertTrue(rightNode.getCByte().toString().equals("01101111"));
    }
    
    @Test
    public void testCreateCodes() {
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] rawInput = "helloworld".getBytes();
        CByte[] bytes = compressor.process(rawInput);
        CList<Entry> table = compressor.createTable(bytes);
        Entry root = compressor.createTree(table);
        CList<Entry> codes = new CList<>();
        compressor.createCodes(root, "", codes);
        assertTrue(codes.get(0).getCByte().toString().equals("01100100"));
        assertTrue(codes.get(0).getCode().equals("000"));
        assertTrue(codes.get(6).getCByte().toString().equals("01101111"));
        assertTrue(codes.get(6).getCode().equals("111"));
    }
    
    @Test
    public void testSortList() {
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] rawInput = "helloworld".getBytes();
        CByte[] bytes = compressor.process(rawInput);
        CList<Entry> table = compressor.createTable(bytes);
        Entry root = compressor.createTree(table);
        CList<Entry> codes = new CList<>();
        compressor.createCodes(root, "", codes);
        compressor.sortList(codes, true);
        assertTrue(codes.get(0).getCByte().toString().equals("01101100"));
        assertTrue(codes.get(0).getCode().equals("10"));
    }
    
    @Test
    public void testCreateDictionary() {
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] rawInput = "helloworld".getBytes();
        CByte[] bytes = compressor.process(rawInput);
        CList<Entry> table = compressor.createTable(bytes);
        Entry root = compressor.createTree(table);
        CList<Entry> codes = new CList<>();
        compressor.createCodes(root, "", codes);
        compressor.sortList(codes, true);
        HashMap<String, String> dict = compressor.createDictionary(codes);
        assertTrue(dict.get("01101000").equals("010"));
        assertTrue(dict.get("01101111").equals("111"));
    }
    
    @Test
    public void testLengthAmounts() {
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] rawInput = "helloworld".getBytes();
        CByte[] bytes = compressor.process(rawInput);
        CList<Entry> table = compressor.createTable(bytes);
        Entry root = compressor.createTree(table);
        CList<Entry> codes = new CList<>();
        compressor.createCodes(root, "", codes);
        compressor.sortList(codes, true);
        HashMap<String, String> dict = compressor.createDictionary(codes);
        int[] lengthAmounts = new int[codes.size()];
        compressor.countLengthAmounts(codes, lengthAmounts);
        assertTrue(lengthAmounts[0] == 0);
        assertTrue(lengthAmounts[1] == 0);
        assertTrue(lengthAmounts[2] == 1);
        assertTrue(lengthAmounts[3] == 6);
    }
    
    @Test
    public void testDecompressorsCreateTree() {
        CList<Entry> codes = new CList<>();
        Entry code1 = new Entry();
        code1.setCByte(new CByte("00000001"));
        code1.setCode("1");
        Entry code2 = new Entry();
        code2.setCByte(new CByte("00010001"));
        code2.setCode("0");
        codes.add(code1);
        codes.add(code2);
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        Entry root = decompressor.createTree(codes);
        assertTrue(root.getLeftChild().getCByte().toString().equals("00010001"));
        assertTrue(root.getRightChild().getCByte().toString().equals("00000001"));
    }
    
    @Test
    public void testReadUniqueBytes() {
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] compressed = compressor.encode("helloworld".getBytes());
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        BitStream stream = new BitStream();
        stream.setBytes(compressed);
        stream.readNumber(4);
        int uniqueAmount = stream.readNumber(2);
        CList<CByte> uniqueBytes = decompressor.readUniqueBytes(stream, uniqueAmount);
        assertTrue(uniqueBytes.get(0).toString().equals("01101100"));
        assertTrue(uniqueBytes.size() == uniqueAmount);
    }
}
