package com.jhxaa.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtil {


    public static void main(String[] args) throws IOException {
        File directory = new File("config/filter.config");// 参数为空
        String courseFile = directory.getCanonicalPath();
        System.out.println(courseFile);

    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static ArrayList<String> readFileByLines(String fileName) {
//        String path = FileUtil.class.getResource(fileName).getFile();
//        System.out.println(path);
//        File file = new File(fileName);
        File file = new File("config/filter.config");// 参数为空
        BufferedReader reader = null;
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
//                line++;
                arrayList.add(tempString.trim());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return arrayList;
    }

}
