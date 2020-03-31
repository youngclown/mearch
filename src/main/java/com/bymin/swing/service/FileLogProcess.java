package com.bymin.swing.service;

import com.bymin.swing.search.FileInSearch;
import lombok.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class FileLogProcess extends FileInSearch {

    Map<String, Integer> map = new HashMap<>();

    public FileLogProcess(){
        super("","",null,null,null,null,"",0,0);
    }

    public static void main(String[] args) {
        FileLogProcess fileLogProcess = new FileLogProcess();
        fileLogProcess.returnFileInSearch();
        if (fileLogProcess.getOutPath() != null) {
            int onlyMapCount = 0;
            StringBuilder builder2 = new StringBuilder();
            for (String key : fileLogProcess.getMap().keySet()) {
                onlyMapCount++;
                builder2.append(key).append("\t").append(fileLogProcess.getMap().get(key)).append("\n");
            }
            try
            {
                FileWriter fw = new FileWriter(fileLogProcess.getOutPath()); // 절대주소 경로 가능
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(onlyMapCount +"숫자\n"+builder2.toString());
                bw.newLine(); // 줄바꿈
                bw.close();
            }
            catch (IOException e)
            {
                System.err.println(e); // 에러가 있다면 메시지 출력
                System.exit(1);
            }
        }
    }

    @Override
    public void checkPattern(String line) {
        if (getLogArray()[1]) {
            String[] temp = line.split(getSplitArray());
            getMap().merge(temp[getSplitPos()], 1, Integer::sum);
        }
    }
}
