package com.test.util;

import java.io.File;
import java.io.FileInputStream;

public class FileUtil {


    public static FileInputStream fetchFile() {
        FileInputStream outputFile = null;
        try {
            File f = new File(System.getProperty("outputFileGen"));

            if (!f.exists()) {
                outputFile = new FileInputStream(new File(System.getProperty("outputFile")));
            } else {
                outputFile = new FileInputStream(new File(System.getProperty("outputFileGen")));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return outputFile;
    }
}
