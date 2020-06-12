# Week report 5

I started early this week and got the Huffman algorithm to work with byte files. I haven't yet done extensive testing or any unit tests for it, but I will now commit the current development as it managed to compress a text file (Lorem ipsum copypaste) into about 54% from its original size, and also back to original form exactly as it was before.

I created the classes CByte and BitStream which proved to be very useful for this.

I divided the HuffmanCoder into three parts: private Entry-class became its own public class, and encoding and decoding are now two separate classes: HuffmanCompressor, and HuffmanDecompressor. The huffman main methods are also divided into separate smaller methods which cleaned the code up nicely. JavaDocs are not done yet but I will do them before the end of this week.

I then started writing Lempel-Ziv-Welch into LZW-class. I finished compression-method, but I am not sure if it works because I did not yet finish decompression. It does however make the file smaller so that is a good sign.

I wrote tests for CByte and BitStream classes and Huffman classes - for Huffman I only have one integration test so far, I will have to think more about how to make the unit tests.

I added javadoc comments to all classes which are in use currently.

I have not yet made my own HashMap class. ArrayList replacement is ready, and there are some tests, but I did not yet test using it in the actual huffman algorithm. I will do that at the same time with HashMap next week.

I also started writing implementation and testing documents.

I used about 16 hours this week. Many of it went into studying and coding Lempel-Ziv-Welch.

# What next

Next week I will write a HashMap class and custom sorting method and replace ArrayList with my custom list class. I will also try to finish LZW decompression and write some more unit tests for individual methods.
