# Testing document

### What is tested

The performance tests are ran 50 times and the times are saved in an array. The average time is then printed out.

### What kind of input was tested

The performance test uses a 152089-byte text file "alice29.txt" which has text from the book "Alice in Wonderland". That is good test material, because it is something that might very well be compressed by a program, it is therefore realistic.

Compression currently takes on average around 120 ms. Decompression takes around 24 ms.

