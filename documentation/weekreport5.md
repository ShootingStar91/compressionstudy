# Week report 5

I started early this week and got the Huffman algorithm to work with byte files. I haven't yet done extensive testing or any unit tests for it, but I will now commit the current development as it managed to compress a text file (Lorem ipsum copypaste) into about 54% from its original size, and also back to original form exactly as it was before.

I created the classes CByte and BitStream which proved to be very useful for this.

Last commit divided the HuffmanCoder into three parts: private Entry-class became its own public class, and encoding and decoding are now two separate classes: HuffmanCompressor, and HuffmanDecompressor. The huffman main methods are also divided into separate smaller methods which cleaned the code up nicely. JavaDocs are not done yet but I will do them before the end of this week.

This document will be updated during the week as I will make more commits.
