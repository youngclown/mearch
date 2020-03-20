package com.bymin.swing.util;

import java.io.File;

public class FileUtil {

    public static File[] fileList(String filename) {
        File folder = new File(filename);
        return folder.listFiles();
    }


}
