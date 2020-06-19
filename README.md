# Compression study

A Java implementation of different file compression algorithms, and a study of their efficiency.

This project is a practice project by Arttu Kangas for a course at University of Helsinki.

+ [Requirement specification](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/requirementspecification.md)
+ [Implementation document](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/implementation.md)
+ [Testing document](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/testing.md)
+ [Week report 1](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/weekreport1.md)
+ [Week report 2](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/weekreport2.md)
+ [Week report 3](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/weekreport3.md)
+ [Week report 4](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/weekreport4.md)
+ [Week report 5](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/weekreport5.md)
+ [Week report 6](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/weekreport6.md)

### How to use it

At this moment, running the program will read the "alice29.txt" -file from the folder where the src-folder is located. It will compress the file into "compressedAlice". Then it will decompress that into "decodedAlice.txt". These filenames can only be changed by changing the code in the main-method currently. The program will also print how many percents the compressed file is from the original.

### Command line

Run tests and create jacoco report (make sure you are in the /compressionstudy folder):

```
mvn test jacoco:report
```

You will find it from /target/site/jacoco/

Create checkstyle report:
```
mvn jxr:jxr checkstyle:checkstyle
```

Find it at /target/site/checkstyle.html

