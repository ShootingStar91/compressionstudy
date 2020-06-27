
package compressionstudy.dao;


import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Dao-class provides getContent() to get content from file,
 * and write(fileName, fileContent) to write into files
 * @author Arttu Kangas
 */
public class Dao {
    
    byte[] fileBytes;
    
    /**
     * Give filename as parameter
     * @param fileName Filename to open with this Dao
     */
    public Dao(String fileName) {
        Path path = Paths.get(fileName);
        try {
            fileBytes = Files.readAllBytes(path);
        } catch (Exception e) { 
        
        }
    }
    
    public byte[] getContent() {
        return fileBytes;
    }
    
    /**
     * Write the given file content into specific file
     * @param fileName name of the file
     * @param fileContent byte[] to write into file
     */
    public void write(String fileName, byte[] fileContent) {
        try (FileOutputStream fileStream = new FileOutputStream(fileName)) {
            fileStream.write(fileContent);
        } catch (Exception e) {
            System.out.println(e);    
        }
    }
    
}
