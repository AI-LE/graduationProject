package com.hebeiUniversity.aile;

/**
 * @author aile
 * @date 2020/4/8 22:59
 */
import java.io.*;

import java.util.List;



public class Excel {

    public static void main(String[] args) throws Exception  {
        readFile("C:\\Users\\Administrator\\Desktop\\毕设\\汽车价格离群值检测\\dataset\\accord_sedan_testing.csv");
    }
    private static List<String[]> readFile(String path) throws Exception {
        InputStream is = new FileInputStream(path);
            //将file 转换成MultipartFile
            try{

                BufferedReader reader = new BufferedReader(new FileReader(path));//换成你的文件名
                reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉
                String line = null;
                while((line=reader.readLine())!=null){
                    String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                    for (String s : item) {
                        System.out.print(s + ",");

                    }
                    System.out.println();
                }
                return null;
            } catch (Exception  e){
                System.out.println("文件转换出错");
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e){
                        System.out.println("输入流关闭异常");
                    }
                }
            }
            return null;
        }
}