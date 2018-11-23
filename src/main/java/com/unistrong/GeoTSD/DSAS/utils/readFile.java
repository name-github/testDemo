package com.unistrong.GeoTSD.DSAS.utils;

import java.io.*;

/**
 * @Author:
 * @Description
 * @Data:$time$ $date$
 */
public class readFile {
    public static String reader(String filePath) {
        String resultstr = "";
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                   resultstr = resultstr + lineTxt;
                }
                bufferedReader.close();
            }
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            System.out.println("文件不存在!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("文件读取错误!");
            e.printStackTrace();
        }
        return resultstr;
    }
}
