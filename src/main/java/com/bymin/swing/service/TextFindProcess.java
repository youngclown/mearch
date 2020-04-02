package com.bymin.swing.service;

import com.bymin.swing.search.FileInSearch;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

public class TextFindProcess extends FileInSearch {


    static String pattern = "^[ ©\\-()?!\\]\\[“”>.,가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\\s]*$";   // 특수문자포함

    public TextFindProcess(String readPath, String outPath, String[] inKeyword, String[] orKeyword, String[] exceptKeyword, boolean[] logArray, String splitArray, int splitPos, int sample) {
        super(readPath, outPath, inKeyword, orKeyword, exceptKeyword, logArray, splitArray, splitPos, sample);
    }

    @Override
    public void checkPattern(String line) {

        byte[]  bytes  = new byte[0];
        try {
            bytes = line.getBytes("EUC-KR");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String type = new String(bytes);
        boolean i = Pattern.matches(pattern, line);
        if(i)
        {
//            System.out.println(line+"는 패턴에 일치함.");
        }
        else
        {
            System.out.println(line+"패턴 일치하지 않음.");
        }
    }

    public static void main(String[] args) {

//        boolean i = Pattern.matches(pattern, "패턴 일치하지 않음.>");
//        System.out.println(i);
        TextFindProcess textFindProcess  = new TextFindProcess("C:\\Users\\bymin\\Downloads\\새 폴더 (3)",null,null,null,null,null,"",0,100);
        textFindProcess.returnFileInSearch();

    }
}
