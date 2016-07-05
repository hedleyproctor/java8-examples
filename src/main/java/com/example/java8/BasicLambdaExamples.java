package com.example.java8;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BasicLambdaExamples {

    // https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html

    public void filter() {
        File myDirectory = new File("/tmp");

        // old
        File[] textFiles = myDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });

        // new
        textFiles = myDirectory.listFiles(
            (dir,name) -> name.endsWith(".txt")
        );
    }

    public void comparator() {
        File file1 = new File("/tmp/file1.txt");
        File file2 = new File("/tmp/file2.txt");

        List<File> fileList = new ArrayList<>();
        fileList.add(file1);
        fileList.add(file2);

        // old
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        // new
        Collections.sort(fileList, (f1,f2) -> f1.getName().compareTo(f2.getName()));

    }
}
