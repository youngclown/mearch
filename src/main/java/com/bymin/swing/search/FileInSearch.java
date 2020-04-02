package com.bymin.swing.search;

import com.bymin.swing.util.FileUtil;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.io.*;

@Getter
@ToString
public abstract class FileInSearch implements FileSearch {
    final String readPath;
    final String outPath;
    final String[] inKeyword;
    final String[] orKeyword;
    final String[] exceptKeyword;

    // 1. Log collection
    // 2. Log array pattern
    final boolean[] logArray; // log array count, log collect

    // if log arry
    final String splitArray;    // tap , "" 등
    final int splitPos;

    // deafult : -1
    final int sample;    // sample count > -1 is count playing,  = 10

    public FileInSearch(String readPath,String outPath,String[] inKeyword,String[] orKeyword,String[] exceptKeyword,boolean[] logArray,String splitArray,int splitPos,int sample) {
        this.readPath = readPath;
        this.outPath = outPath;
        this.inKeyword = inKeyword;
        this.orKeyword = orKeyword;
        this.exceptKeyword = exceptKeyword;
        this.logArray = logArray; //  = {true, true}
        this.splitArray = splitArray; // ","
        this.splitPos = splitPos;
        this.sample = sample; //

    }

    public void returnFileInSearch() {

        var listOfFiles = FileUtil.fileList(getReadPath());

        if (listOfFiles == null) {
            var file = new File(getReadPath());
            filePlay(file);
        } else {
            for (var file : listOfFiles) {
                filePlay(file);
            }
        }
    }

    public void filePlay(File file) {
        int count = 0;
        try (FileReader fileReader = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(fileReader)){
            String line;

            BufferedWriter bw = null;
            if (!StringUtils.isEmpty(getOutPath())) {
                FileWriter fw = new FileWriter(getOutPath());
                bw = new BufferedWriter(fw);
            }

            while ((line = bufReader.readLine()) != null) {

                if (getSample() != -1) {
                    count++;
                    if (count >= getSample()) break;
                }

                boolean patternOn = true;  // 패턴에 맞을 경우

                if (getOrKeyword() != null) {
                    patternOn = false; // or 조건절이 있을 경우, 먼저 false 처리함.
                    // 해당 배열이 or 조건으로 존재하면 무조건 true
                    for (String keyword : getOrKeyword()) {
                        if (line.contains(keyword)) {
                            patternOn = true;
                            break;
                        }
                    }
                }

                if (getInKeyword() != null && !patternOn) {
                    // 해당 배열이 전부 있어야 함.
                    for (String keyword : getInKeyword()) {
                        // or 조건절에서 true가 있다면, and 조건절의 keyword가 없더라도  true를 유지한다.
                        // 키워드가 없거나, or 조건절의 true가 없다면... false 처리.
                        if (line.contains(keyword)) {
                            patternOn = true;
                            break;
                        }
                    }
                }

                // 해당 배열이 하나라도 있으면 false
                if (getExceptKeyword() != null && patternOn && getExceptKeyword().length > 0) {
                    for (String keyword : getExceptKeyword()) {
                        if (line.contains(keyword)) {
                            patternOn = false;
                            break;
                        }
                    }
                }

                if (patternOn) {
                    try {
                        if (bw != null && getLogArray()[0]) {
                            bw.write(line);
                            bw.newLine(); // 줄바꿈
                        }
                        checkPattern(line);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (bw != null) bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
