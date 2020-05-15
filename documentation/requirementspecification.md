# Requirement specification


### Description of project aims

This project will implement **lossless file compression algorithms** in Java and test their performance.

### List of requirements for final project

+ Implement [Huffman coding](https://en.wikipedia.org/wiki/Huffman_coding)
+ Implement [Lempel-Ziv-Welch](https://en.wikipedia.org/wiki/Lempel-Ziv-Welch)
+ Test and document their performance
  + If one is better or faster, explain what causes this, and what kind of input (file content) possibly effects this
+ The input can be any type of file

### Time complexity and other requirements

According to [University of Aucklands webpage](https://www.cs.auckland.ac.nz/software/AlgAnim/huffman.html), the time complexity of Huffman coding is O(n log n). Other sources, however, give different information, and it seems that the time complexity of compression algorithms is not straightforward, likely because the *type* of the input may affect the performance greatly. For this reason I will compare the functioning algorithms to existing file compression software. The speed of my implementations should be similar to existing programs that use the same algorithms, provided the compressed file is exactly same. Slight variation of speed will be allowed here, if it can be explained for example by the language choice (Java). The following measurements will be used in defining whether the implementations of this project are successful:

+ Compression speed
  + How long it takes to run the compression
+ Compression percentage
  + How many percent the file shrinks in compression
  + This should be very similar in my implementations to existing programs
    + As an example, Lempel-Ziv-Welch algorithm, according to its wikipedia entry, can compress a large English language text file typically by about 50%.

### Datastructures used

At least a binary tree and a heap will be implemented and used for these algorithms, possibly many more data structures.

### Possible project extensions

If the above will be implemented fully before the course is over, the project will be extended by implementing another file compression algorithm, either completely separate or a modification of the ones above.
