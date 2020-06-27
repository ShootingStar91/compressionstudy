# Testing document

## Unit tests

Unit tests are created for most of the methods of the code, both utility classes and the main algorithms. Some methods are omitted, as they are tested so thoroughly with both integration tests and the other method tests already. Dysfunctionality in these methods would be found if all other method tests do not raise error but the integration tests do not work.

Entry-class does not need tests, as it contains only of simple getters and setters. Code for user interface or main-method also do not require testing.


## Performance testing

Files used in the testing:

+ The files used for the *speed testing* are text files combined from [Calgary Corpus](http://corpus.canterbury.ac.nz/descriptions/) and cut into specific sizes close to 0.5 MB, 1 MB, and 1.5 MB.
  + The sizes are close to the megabytes they are named after, actual sizes are following:
    + testfile_0_5mb: 498 292 bytes
    + testfile_1mb: 981 616 bytes
    + testfile_1_5mb: 1 470 897 bytes
  + The content of these files is largely similar to each other. They contain natural English words, but also pieces of program code.
  + The performance tests are run in following fashion:
    + Each test run runs the algorithms 50 times on the same file, and sorting the time results, and then chooses the 25th time on the list, which is the median.
    + I ran the complete tests on these three files 3 times separately, and saved the average of the median results.
+ The following files are used to further analyze *compression ratios* for different types of files:
    + Two text files from Canterbury Corpus (from same address as above for Calgary Corpus):
        + alice29.txt, containing Alice in Wonderland by Lewis Carrol in text format, size 152 089 bytes
        + random.txt, containing 100 000 bytes of very random characters
    + A bitmap file made by me, testDrawing.bmp, size 307 274 bytes

### Compression speed

| File size in bytes | Algorithm              | Compressed size in relation to original size   | Time (milliseconds) |
| -------------------| :--------------------: | -----------------:  | ------------------: |
| 498 292            | Huffman coding         | 60.01 %             | 370                 |
| 498 292            | Lempel-Ziv-Welch       | 44.31 %             | 402                 |
| 981 616            | Huffman coding         | 59.99 %             | 923                 |
| 981 616            | Lempel-Ziv-Welch       | 45.75 %             | 709                 |
| 1 470 897          | Huffman coding         | 59.29 %             | 1464                |
| 1 470 897          | Lempel-Ziv-Welch       | 45.86 %             | 1351                |


