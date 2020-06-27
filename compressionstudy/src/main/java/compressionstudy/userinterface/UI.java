
package compressionstudy.userinterface;

import compressionstudy.compression.HuffmanCompressor;
import compressionstudy.compression.HuffmanDecompressor;
import compressionstudy.compression.LZW;
import compressionstudy.dao.Dao;
import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 *
 * @author Arttu Kangas
 */
public class UI extends Application {
    
    File file;
    Stage window;
    Dao dao;
    Label resultLabel;
    
    
    public static void main(String args[]) {
        launch();
    }
    
    @Override
    public void start(Stage mainWindow) {
        
        window = mainWindow;
        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane);
        gridPane.setMinSize(600, 800);
        gridPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        window.setScene(scene);
        Button chooseFileButton = new Button();
        Label fileLabel = new Label("No file chosen");
        resultLabel = new Label();
        
        chooseFileButton.setText("Choose file");
        chooseFileButton.setOnAction(e -> {
            chooseFile();
            if (file == null) {
                return;
            }
            dao = new Dao(file.getAbsolutePath());
            fileLabel.setText(file.getName() + "   (" + dao.getContent().length + " bytes)");
        });
        gridPane.add(chooseFileButton, 0, 0);
        gridPane.add(fileLabel, 0, 1);
        Button runButton = new Button();
        runButton.setText("Run");
        ToggleGroup modeSelection = new ToggleGroup();
        RadioButton testMode = new RadioButton("Test Huffman");
        testMode.setToggleGroup(modeSelection);
        testMode.setSelected(true);
        RadioButton fileMode = new RadioButton("Run Huffman");
        fileMode.setToggleGroup(modeSelection);
        RadioButton lzwTestMode = new RadioButton("Test LZW");
        lzwTestMode.setToggleGroup(modeSelection);
        RadioButton lzwFileMode = new RadioButton("Run LZW");
        lzwFileMode.setToggleGroup(modeSelection);
        RadioButton compareMode = new RadioButton("Run performance test on both");
        compareMode.setToggleGroup(modeSelection);
        runButton.setOnAction(e -> {
            if (dao == null) {
               System.out.println("Choose file first!");
               return;
            }   
            RadioButton selection = (RadioButton) modeSelection.getSelectedToggle();
            if (selection.getText().equals("Test Huffman")) {
                testHuffman();
            } else if (selection.getText().equals("Run Huffman")) {
                runHuffman();
            } else if (selection.getText().equals("Test LZW")) {
                testLZW();
            } else if (selection.getText().equals("Run LZW")) {
                runLZW();
            } else {
                runComparison();
            }
        });
        Button clearButton = new Button("Clear text");
        gridPane.add(clearButton, 2, 12);
        clearButton.setOnAction(e ->{
            resultLabel.setText("");
        });
        gridPane.add(testMode, 0, 3);
        gridPane.add(fileMode, 0, 4);
        gridPane.add(lzwTestMode, 0, 5);
        gridPane.add(lzwFileMode, 0, 6);
        gridPane.add(compareMode, 0, 7);
        gridPane.add(runButton, 0, 12);
        gridPane.add(resultLabel, 0, 14);
        
        window.show();
    }
    
    private void testLZW() {
        byte[] originalFile = dao.getContent();
        long originalLength = originalFile.length;
        LZW lzw = new LZW();
        byte[] compressedFile = lzw.compress(originalFile);
        long compressedLength = compressedFile.length;
        byte[] decompressedFile = lzw.decompress(compressedFile);
        checkSimilarity(originalFile, decompressedFile);
        double compressionRate = compressedLength * 1.00 / originalLength * 100;
        String percentString = Double.toString(compressionRate).substring(0, 5);
        System.out.println("Original size: " + originalLength + " bytes");
        System.out.println("Compressed into: " + compressedFile.length + " bytes");
        System.out.println("LZW compression rate: " + percentString + " %");
    }
    
    private void runLZW() {
        byte[] originalFile = dao.getContent();
        LZW lzw = new LZW();
        byte[] compressedFile = lzw.compress(originalFile);
        dao.write("compressed_" + file.getName(), compressedFile);
        byte[] decompressedFile = lzw.decompress(compressedFile);
        dao.write("decompressed_" + file.getName(), decompressedFile);
    }

    private void runComparison() {
        long comparisonStartTime = System.nanoTime();
        byte [] originalFile = dao.getContent();
        long originalLength = originalFile.length;
        int repeats = 50;
        long[] times = new long[repeats];
        byte[] compressedFile = new byte[1];

        double huffmanRatio = 0.0;
        double lzwRatio = 0.0;
        for (int i = 0; i < repeats; i++) {
            long startTime = System.nanoTime();
            HuffmanCompressor compressor = new HuffmanCompressor();
            compressedFile = compressor.encode(originalFile);
            times[i] = System.nanoTime() - startTime;
            huffmanRatio = compressedFile.length * 1.00 / originalLength * 100;
        }
        long huffCompressionTime = times[repeats / 2] / 1000000;
        System.out.println("1/4");
        for (int i = 0; i < repeats; i++) {
            long startTime = System.nanoTime();
            HuffmanDecompressor decompressor = new HuffmanDecompressor();
            decompressor.decompress(compressedFile);
            times[i] = System.nanoTime() - startTime;
        }
        long huffDecompressionTime = times[repeats / 2] / 1000000;
        System.out.println("2/4");
        for (int i = 0; i < repeats; i++) {
            long startTime = System.nanoTime();
            LZW lzw = new LZW();
            compressedFile = lzw.compress(originalFile);
            times[i] = System.nanoTime() - startTime;
            lzwRatio = compressedFile.length * 1.00 / originalLength * 100;

        }
        long lzwCompressionTime = times[repeats / 2] / 1000000;
        System.out.println("3/4");
        for (int i = 0; i < repeats; i++) {
            long startTime = System.nanoTime();
            LZW lzw = new LZW();
            lzw.decompress(compressedFile);
            times[i] = System.nanoTime() - startTime;
        }
        long lzwDecompressionTime = times[repeats / 2] / 1000000;
        long totalTime = System.nanoTime() - comparisonStartTime;
        addText("Huffman compression:       \t" + huffCompressionTime + " ms");
        addText("Huffman decompression:     \t" + huffDecompressionTime + " ms");
        addText("LZW compression:           \t" + lzwCompressionTime + " ms");
        addText("LZW decompression:         \t" + lzwDecompressionTime + " ms");
        addText("Huffman ratio:             \t" + (""+huffmanRatio).substring(0, 5) + " %");
        addText("LZW ratio:                 \t" + (""+lzwRatio).substring(0, 5) + " %");
        addText("Entire testing took:       \t" + (totalTime / 1000000000) + " s");
    }
    
    private void addText(String text) {
        resultLabel.setText(resultLabel.getText() + text + "\n");
    }
    
    private void runHuffman() {
        byte[] originalFile = dao.getContent();
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] compressedFile = compressor.encode(originalFile);
        dao.write("compressed_" + file.getName(), compressedFile);
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        byte[] decompressedFile = decompressor.decompress(compressedFile);
        dao.write("decompressed_" + file.getName(), decompressedFile);
    }
    
    private void testHuffman() {

        byte[] originalFile = dao.getContent();
        long originalLength = originalFile.length;
        HuffmanCompressor compressor = new HuffmanCompressor();
        byte[] compressedFile = compressor.encode(originalFile);
        long compressedLength = compressedFile.length;
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        byte[] decompressedFile = decompressor.decompress(compressedFile);
        checkSimilarity(originalFile, decompressedFile);
        double compressionRate = compressedLength * 1.00 / originalLength * 100;
        String percentString = Double.toString(compressionRate).substring(0, 5);
        System.out.println("Original size: " + originalLength + " bytes");
        System.out.println("Compressed into: " + compressedFile.length + " bytes");
        System.out.println("Huffman compression rate: " + percentString + " %");
        
    }
    
    private void checkSimilarity(byte[] original, byte[] decompressed) {
        if (original.length != decompressed.length) {
            System.out.println("ERROR: Lengths not same");
        }
        for (int i = 0; i < original.length; i++) {
            if (original[i] != decompressed[i]) {
                System.out.println("ERROR: Not same at " + i);
                break;
            }
        }
    }
    
    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file to compress");
        file = fileChooser.showOpenDialog(window);
        
    }

    
}
