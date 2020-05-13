package com.hbu.aile;

/**
 * @author aile
 * @date 2020/4/8 22:59
 */
import java.io.*;

import java.util.*;


public class UsedCarNnalysis {

    public List<OneHotSample> readFile2(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        List<OneHotSample> list = new ArrayList<>();
        try{
            //换成你的文件名
            BufferedReader reader = new BufferedReader(new FileReader(path));
            reader.readLine();//第一行信息，为标题信息，不用
            String line = null;
            while((line=reader.readLine())!=null){
                //CSV格式文件为逗号分隔符文件，这里根据逗号切分
                String item[] = line.split(",");
                OneHotSample sample = new OneHotSample();
                sample.setPrice(Double.valueOf(item[0]));
                sample.setMileage(Double.valueOf(item[1]));
                sample.setYear(Double.valueOf(item[2]));
                switch (item[3]) {
                    case "ex" :
                        sample.setTrimEx(1d);
                        break;
                    case "lx":
                        sample.setTrimLx(1d);
                        break;
                    default:
                        sample.setTrimExl(1d);
                }
                switch (item[4]) {
                    case "4 Cyl" :
                        sample.setEngine4Cyl(1d);
                        break;
                    default:
                        sample.setEngine6Cyl(1d);
                }
                switch (item[5]) {
                    case "Automatic" :
                        sample.setTransmissionAutomatic(1d);
                        break;
                    default:
                        sample.setTransmissionManual(1d);
                }
                list.add(sample);
            }
        } catch (Exception  e){
            System.out.println("文件转换出错:" + e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e){
                    System.out.println("输入流关闭异常");
                }
            }
        }
        return list;
    }
}