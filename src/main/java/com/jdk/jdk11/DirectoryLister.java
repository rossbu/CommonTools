package com.jdk.jdk11;

import java.nio.file.*;
import static java.lang.System.*;

public class DirectoryLister {
    public static void main(String[] args) throws Exception {
        var dirName = ".";

        if ( args == null || args.length< 1 ){
            err.println("Will list the current directory");
        } else {
            dirName = args[0];
        }

        Files
                .walk(Paths.get(dirName))
                .forEach(out::println);
    }
}