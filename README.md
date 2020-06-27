# Compression study

A Java implementation of different file compression algorithms, and a study of their efficiency.

This project is a practice project by Arttu Kangas for a course at University of Helsinki.

+ [Requirement specification](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/requirementspecification.md)
+ [Implementation document](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/implementation.md)
+ [Testing document](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/testing.md)
+ [Test material folder](https://github.com/ShootingStar91/compressionstudy/blob/master/compressionstudy/Test%20material/)
+ [Week report 1](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/weekreport1.md)
+ [Week report 2](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/weekreport2.md)
+ [Week report 3](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/weekreport3.md)
+ [Week report 4](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/weekreport4.md)
+ [Week report 5](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/weekreport5.md)
+ [Week report 6](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/weekreport6.md)
+ [Week report 7](https://github.com/ShootingStar91/compressionstudy/blob/master/documentation/weekreport7.md)

### How to use it

Run the program by either compiling it or creating jar with "mvn package" and running the .jar from /target/ -folder.

The user interface allows you to choose any file. I recommend one from the Test Material folder provided with the project.

To test the validity of compression and decompression, choose test and click Run. To do it on an actual file which will leave the compressed and decompressed files for you to examine, choose File.

To run performance tests click the last radio button and click Run. Depending on the file size and your computer, the testing might take anywhere from couple seconds to several minutes (for the 1.5MB test files that is most expected).

The results will appear on the user interface. If you run more tests, they will appear below the previous results, but when you run out of room, click Clear text button.

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

