# Week report 2

This week, I set up pom.xml, checkstyle.xml, studied compression algorithms in detail, mostly focusing on Huffman. Then I wrote the huffman encoding core using Java's util-classes like ArrayList and HashMap as an aid, this will be changed later to my own data structures. The huffman algorithm is in quite good condition, however I did not have time to write it completely, it does not build the tree when decoding, but uses the tree already built while encoding. It also only deals with simple strings at this point and outputs a string of 1's and 0's, but I believe the most important part of the algorithm is ready, and I learned a lot writing it.

Then I wrote simple tests, there are technically only two integration tests that test the entirety of the HuffmanCoder-class, but the coverage is very high when checking with Jacoco. 

I also wrote a little javadoc comments. I did not focus very much on style yet because I got in a bit hurry.

### What next

Next I will finish the Huffman Coder algorithm, and start writing LZ77 (Lempel-Ziv) algorithm. I should also look at the checkstyle report and clean up any mistakes.
