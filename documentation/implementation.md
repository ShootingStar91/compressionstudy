# Implementation document

### Basic structure

Main-package contains the main class which is ran first. It contains methods to run the Huffman compression and decompression, and also a performance test which will run this repeatedly for 50 times and print out the average time.

Compression-package contains the classes HuffmanCompressor and HuffmanDecompression, which are used to compress and decompress bytefiles with Huffman coding. It also contains LZW-class for Lempel-Ziv-Welch compression.

Util-package contains classes used for utility, such as a list class, and a custom byte class, and a bitstream class used in the algorithms to read and write individual bits.

Test-package contains unit and integration tests.


