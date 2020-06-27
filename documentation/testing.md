# Testing document

## Unit tests

Unit tests are created for most of the methods of the code, both utility classes and the main algorithms. Some methods are omitted, as they are tested so thoroughly with both integration tests and the other method tests already. Dysfunctionality in these methods would be found if all other method tests do not raise error but the integration tests do not work.

Entry-class does not need tests, as it contains only of simple getters and setters. Code for user interface or main-method also do not require testing.

Test coverage is good, only some errors which should not happen, and couple automatically generated getters and setters were out of the coverage.

![Test coverage](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/testcov.png)


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

| File size in bytes | Algorithm              | Time (milliseconds) |
| -------------------| :--------------------: | ------------------: |
| 498 292            | Huffman coding         | 370                 |
| 498 292            | Lempel-Ziv-Welch       | 402                 |
| 981 616            | Huffman coding         | 923                 |
| 981 616            | Lempel-Ziv-Welch       | 709                 |
| 1 470 897          | Huffman coding         | 1464                |
| 1 470 897          | Lempel-Ziv-Welch       | 1351                |

![Compression speeds](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/compressionspeeds.png)

In compression, Huffman Coding seems to work better in the smaller filesizes, but LZW is clearly faster at 1 MB. However the difference seems to start to even out at higher sizes.

### Decompression speed

| File size in bytes | Algorithm              | Time (milliseconds) |
| -------------------| :--------------------: | ------------------: |
| 498 292            | Huffman coding         | 191                 |
| 498 292            | Lempel-Ziv-Welch       | 343                 |
| 981 616            | Huffman coding         | 366                 |
| 981 616            | Lempel-Ziv-Welch       | 676                 |
| 1 470 897          | Huffman coding         | 753                 |
| 1 470 897          | Lempel-Ziv-Welch       | 1175                |

![Decompression speeds](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/decompressionspeeds.png)

In decompression, Huffman Coding is clearly faster, and the difference seems to grow the larger the file size is.

### Compression ratios

Compression ratio describes how many percents the compressed file size is from the original file size. Ratio of below 100% has achieved compression, the lower the better.

Compression ratios for the files used in the speed testing above are as follows:

| File size in bytes | Algorithm              | Compression ratio   |
| -------------------| :--------------------: | ------------------: |
| 498 292            | Huffman coding         | 60.01 %             |
| 498 292            | Lempel-Ziv-Welch       | 44.31 %             |
| 981 616            | Huffman coding         | 59.99 %             |
| 981 616            | Lempel-Ziv-Welch       | 45.75 %             |
| 1 470 897          | Huffman coding         | 59.29 %             |
| 1 470 897          | Lempel-Ziv-Welch       | 45.86 %             |

![Compression ratios](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/compressionratios.png)

Lempel-Ziv-Welch performs better in terms of compression ratio, however Huffman Coding seems to catch up the difference, if only slightly. However, the compression ratio is highly dependent of the data, as we will see in the later tests.

Compression ratios for the extra files:

| File                    | Algorithm              | Compression ratio   |
| ------------------------| :--------------------: | ------------------: |
| alice29.txt (152 kb)    | Huffman coding         | 57.77 %             |
| alice29.txt (152 kb)    | Lempel-Ziv-Welch       | 46.12 %             |
| random.txt (100 kb)     | Huffman coding         | 75.12 %             |
| random.txt (100 kb)     | Lempel-Ziv-Welch       | 100.2 %             |
| testDrawing.bmp (307 kb)| Huffman coding         | 20.11 %             |
| testDrawing.bmp (307 kb)| Lempel-Ziv-Welch       | 3.984 %             |

These results show the difference in the algorithms very well. In natural language of Alice in Wonderland, Lempel-Ziv-Welch is more effective. However, when compressing a file with random data, there is no repeating patterns for LZW to find, and thus it fails to compress the file at all, whereas Huffman Coding gets the file to 75.12%, since it's more interested in individual bytes and their frequencies.

The bitmap file achieves great compression ratios from both algorithms, LZW manages to get it to less than 4% from the original size, which is impressive. The file is very simple drawing with some brush strokes and only a couple different colours. If the file had a lot more colours and varying shapes, the compression would certainly be lower.

### Comparison to existing implementations

On my Cubbli Linux, I right-clicked the 1.5 MB test file, and compressed it. The resulting .zip was 526.3 kilobytes, which is 35.7% of the original size. My own ratios were 59.29% and 45.86%. The .bmp file compressed to 2.5%, which is also considerably less than my 3.984% with LZW.

I do not have clear benchmark of other algorithms with the exact same files. However, I will look at the results of this [UCDavis compression benchmark](https://fiehnlab.ucdavis.edu/staff/kind/collector/benchmark/7zip-benchmark) with my own results in very rought terms. My algorithms perform compression in roughly 1 MB/s, and decompression slightly faster. On the UCDavis benchmark the algorithms compared vary in speed from 4 MB/s to well above a hundred megabytes per second. So my algorithm is much slower.
