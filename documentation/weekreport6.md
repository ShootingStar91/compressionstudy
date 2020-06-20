# Week report 6

This week I finished LZW algorithm (although apparently it isn't finished, more on that later) and started writing sorting algorithm for the HuffmanCompressor-class. I implemented a quicksort. It works, but I started wondering if keeping an ordered list structure would work faster, since I wouldn't have to call the entire sorting algorithm on the list every time. I'm not sure yet but I should try it next week.

I also wrote a custom HashMap. There isn't tests for it yet, but it seems to work just as well as Java's own, for my purposes. The size of the hashmap might need tweaking later.

I used about 15 hours this week.

### Problems

I found an error from my LZW algorithm, and it seems to not be related to my HashMap as I first saw, but I just hadn't tested the LZW properly before. When compressing and decompressing the alice29.txt file, two bytes in the book get messed up. I think I got an idea what might cause this, but I'm not sure yet. I will try to fix it very quickly.

It is strange that the entire book was otherwise exactly the same but two adjacent bytes went slightly wrong. This only caused one character error in the text file, and the rest of the book was completely fine, but obviously a lossless algorithm can't have mistakes like that.

UPDATE: LZW error is now fixed.

### What next

Next week is the final week and I have lots of things left to do, but I am confident that I can do them in time, because I have 7 days free with no other obligations.

In this order:
+ Unit tests for all classes
+ Integration tests for both algorithms
+ Performance tests
+ Graphical User Interface
+ Documentation

