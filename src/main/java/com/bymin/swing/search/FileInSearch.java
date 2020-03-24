package com.bymin.swing.search;

import com.bymin.swing.util.FileUtil;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.io.*;

@Getter
@ToString
public abstract class FileInSearch implements FileSearch {
    String readPath = "";
    String outPath = "";
    String[] inKeyword = {","};
    String[] orKeyword = {};
    String[] exceptKeyword = {};

    // 1. Log collection
    // 2. Log array pattern
    boolean[] logArray = {true, true}; // log array count, log collect

    // if log arry
    String splitArray = ",";    // tap , "" 등
    int splitPos = 0;

    // deafult : -1
    int sample = 10;    // sample count > -1 is count playing

    public void returnFileInSearch(
            final String PATH,
            final String[] inKeyword,
            final String[] orKeyword,
            final String[] exceptKeyword) {

        var listOfFiles = FileUtil.fileList(PATH);

        if (listOfFiles == null) {
            var file = new File(PATH);
            filePlay(file, inKeyword, orKeyword, exceptKeyword);
        } else {
            for (var file : listOfFiles) {
                filePlay(file, inKeyword, orKeyword, exceptKeyword);
            }
        }
    }

    public void filePlay(File file,
                         final String[] inKeyword,
                         final String[] orKeyword,
                         final String[] exceptKeyword) {
        int count = 0;
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufReader = new BufferedReader(fileReader)){
            String line;

            BufferedWriter bw = null;
            if (StringUtils.isEmpty(outPath)) {
                FileWriter fw = new FileWriter(outPath); // 절대주소 경로 가능
                bw = new BufferedWriter(fw);
            }
            while ((line = bufReader.readLine()) != null) {

                if (getSample() != -1) {
                    count++;
                    if (count >= getSample()) break;
                }

                boolean patternOn = false;  // 패턴에 맞을 경우

                if (orKeyword != null) {
                    // 해당 배열이 or 조건으로 존재하면 무조건 true
                    for (String keyword : orKeyword) {
                        if (line.contains(keyword)) {
                            patternOn = true;
                            break;
                        }
                    }
                }

                if (inKeyword != null) {
                    // 해당 배열이 전부 있어야 함.
                    for (String keyword : inKeyword) {
                        // or 조건절에서 true가 있다면, and 조건절의 keyword가 없더라도  true를 유지한다.
                        // 키워드가 없거나, or 조건절의 true가 없다면... false 처리.
                        if (!line.contains(keyword) || !patternOn) {
                            patternOn = false;
                            break;
                        }
                    }
                }

                // 해당 배열이 하나라도 있으면 false
                if (exceptKeyword != null && patternOn && exceptKeyword.length > 0) {
                    for (String keyword : exceptKeyword) {
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
