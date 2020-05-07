package com.jhxaa.util;

import com.alibaba.fastjson.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class FileUtil {


//    public static void main(String[] args) throws IOException {
//        File directory = new File("config/filter.config");// 参数为空
//        String courseFile = directory.getCanonicalPath();
//        System.out.println(courseFile);
//
//    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static ArrayList<String> readFileByLines(String fileName) {
//        String path = FileUtil.class.getResource(fileName).getFile();
//        System.out.println(path);
//        File file = new File(fileName);
        File file = new File(fileName);// 参数为空
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

    /**
     * @param filePath 文件将要保存的目录
     * @param method   请求方法，包括POST和GET
     * @param url      请求的路径
     * @return
     * @从制定URL下载文件并保存到指定目录
     */

    public static File saveUrlAs(String url, String filePath, String method) {
        //System.out.println("fileName---->"+filePath);
        //创建不同的文件夹目录
        File file = new File(filePath);
        //判断文件夹是否存在
        if (!file.exists()) {
            //如果文件夹不存在，则创建新的的文件夹
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            // 建立链接
            URL httpUrl = new URL(url);
            conn = (HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //判断文件的保存路径后面是否以/结尾
            if (!filePath.endsWith("/")) {
                filePath += "/";
            }
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            fileOut = new FileOutputStream(filePath + "123.png");
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while (length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }
        return file;
    }

    public static void main(String[] args) throws IOException {
        while (true) {
            //        File file = new File("config/filter.config");// 参数为空
//        String photoUrl = "code.png";   //文件URL地址
            String photoUrl = "https://login.51job.com/ajax/verifycode.php?type=33&from_domain=i&t=" + System.currentTimeMillis();   //文件URL地址
            String fileName = photoUrl.substring(photoUrl.lastIndexOf("/"));     //为下载的文件命名
            String filePath = "code/";      //保存目录
            File file = saveUrlAs(photoUrl, filePath, "GET");
            /**
             * https://login.51job.com/ajax/checkcode.php?
             * jsoncallback=jQuery18308649113878619732_1588767795524&verifycode=mb8z&from_domain=i&_=1588770091397
             *
             */
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入验证码：");
            String code = scanner.nextLine();
            Document document = Jsoup.connect("https://login.51job.com/ajax/checkcode.php")
                    .data("jsoncallback", "jQuery18308649113878619732_" + System.currentTimeMillis())
                    .data("verifycode", code)
                    .data("from_domain", "i")
                    .data("_", String.valueOf(System.currentTimeMillis()))
                    .cookie("guid", "461e4561987eb75d445365ee477c64bc")
                    .cookie("slife", "lowbrowser%3Dnot%26%7C%26")
                    .get();
            String html = document.body().html();
            html = html.substring(html.indexOf("(") + 1, html.lastIndexOf(")"));
            int intValue = JSON.parseObject(html).getIntValue("result");
            if (intValue == 0) {
                System.out.println("失败");
            } else if (intValue == 1) {
                System.out.println("成功");
            }
        }
    }

}
