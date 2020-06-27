# Implementation document

### Basic structure

Packages, classes, and their purposes are the following:

+ main
  + Main-class, which launches user interface
+ userinterface
  + UI-class, which shows and operates user interface and performance tests
+ compression
  + HuffmanCompressor-class, which has the Huffman Coding compression algorithm
  + HuffmanDecompressor-class, which has the Huffman Coding decompression
  + LZW-class, which has both compression and decompression of Lempel-Ziv-Welch algorithm
+ util
  + CByte-class, which is a custom byte class that has some useful methods for using unsigned bytes
  + CList-class, which is a custom list implementation, very similar to that of Java's ArrayList
  + BitStream-class, which contains a CList of CBytes and provides useful tools to read or write individual bits or numbers to specific amounts of bits
  + HashMap-class, which is a implementation similar to Java's HashMap
  + Entry-class, which contains CByte, String of a code, and an integer representing frequency. This class is only used by the Huffman classes.
+ dao
  + Dao-class, which is used to read files and write to them.


### Huffman coding

Huffman coding is implemented in the following manner. The sorting mentioned here is a quicksort implementation:

Compression:
+ Create a table from the input, which contains all unique codes, and their frequencies
+ Use the table to create a Huffman Tree structure
+ Use the tree and the table to create codes of varying bit lengths, for example code "001" would be two steps to left from root node, and then one step to right, and that node will contain a CByte representing a unique byte
+ Sort the codes by their bit lengths
+ Create a dictionary where you can find a code for each unique byte
+ Calculate the length amounts into an array
+ Create a BitStream, and write the length of the original file in bytes to the first 4 bytes
+ Write into the stream the number of the unique bytes into the next two bytes
+ Write all unique bytes to the stream, in the order that they are in the sorted codetable (which is by their code lengths)
+ Write data about the codes: First a byte telling how many different codes of the length exists (starting from length 1), then all the different codes of that length. This is the same order as in the previous step
+ Use the dictionary created previously to write the original data in a compressed form using the codes created

Decompression:
+ In the same order, first read the original file size
+ Then read the unique bytes amount
+ Read all the unique bytes
+ Read all the codes and create a tree from them, adding the unique byte to the leaf node pointed to by the code
+ Use the tree to decode the data

### Lempel-Ziv-Welch

+ Simple implementation of what is described for example on the English wikipedia page: https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Welch
+ To signify patterns, I save the numbers into strings and separate them with underscore "_"

### Evaluation of implementation

My implementation worked well in the sense that it usually compressed whatever format of file, as long as the type of data can be sensibly compressed. Both algorithms got decent results. However, the results could have been better. Especially in speed performance. In Huffman Coding, I planned at some point to implement Canonical Huffman Coding, which I understand would have been faster and more efficient than the way I implemented it. In case of LZW, the usage of strings might be the thing that slows it down the most.



