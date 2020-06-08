
package compressionstudy.dao;


import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Arttu Kangas
 */
public class Dao {
    
    byte[] fileBytes;
    
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
    
    public void write(String fileName, byte[] fileContent) {
        try (FileOutputStream fileStream = new FileOutputStream(fileName)) {
            fileStream.write(fileContent);
        } catch (Exception e) {
            System.out.println(e);    
        }
    }
    
}
