package com.bymin.swing.service;

import com.bymin.swing.search.FileInSearch;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class FileLogProcess extends FileInSearch {

    Map<String, Integer> map = new HashMap<>();

    public static void main(String[] args) {
        FileLogProcess fileLogProcess = new FileLogProcess();
        fileLogProcess.returnFileInSearch(fileLogProcess.getReadPath(), fileLogProcess.getInKeyword(), fileLogProcess.getOrKeyword(), fileLogProcess.getExceptKeyword());
    }

    @Override
    public void checkPattern(String line) {
        if (getLogArray()[1]) {
            String[] temp = line.split(getSplitArray());
            getMap().merge(temp[getSplitPos()], 1, Integer::sum);
        }
    }
}
