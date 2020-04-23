package com.hebeiUniversity.aile;

/**
 * @author aile
 * @date 2020/4/8 22:59
 */
import java.io.*;

import java.util.*;


public class UsedCarNnalysis {

    private String[] engine = {"4Cyl", "6Cyl"};
    private String[] trim = {"ex", "exl", "lx"};
    private String[] transmission = {"Automatic", "exl", "lx"};

    public static void main(String[] args) throws Exception  {
        UsedCarNnalysis usedCarNnalysis = new UsedCarNnalysis();
        //获取检测样本
        List<Sample> list = usedCarNnalysis.readFile("汽车价格离群值检测/dataset/accord_sedan_training.csv");
        //求每个样本值的第K距离
        for (int i = 0;i < list.size();i++){
            usedCarNnalysis.distanceK(list, i);
        }
        List<OneHotSample> oneHotSamples = usedCarNnalysis.readFile2("汽车价格离群值检测/dataset/accord_sedan_training.csv");
        for (OneHotSample oneHotSample : oneHotSamples) {
            System.out.println(oneHotSample);
        }
    }

    /**
     * 独热编码，对某一列进行编码
     * @param list
     * @param index
     * @return
     * @throws Exception
     */
    public static ArrayList<String> oneHot(ArrayList<String> list, int index) throws Exception {

//      建立键值
        HashSet<String> set = new HashSet<>();
        for (String l : list) {
            set.add(l.split(",")[index]);
        }
        System.out.println("键值总数：");
        System.out.println(set.size());

//        为键值映射数组下表
        HashMap<String, Integer> toIndex = new HashMap<>();
        int ind = 0;
        for (String a : set) {
            toIndex.put(a, ind);
            ind++;
        }
//      开始编码
        for (int i=0; i<list.size(); i++) {
            int a[] = new int[set.size()];
            a[ toIndex.get( list.get(i).split(",")[index] ) ] = 1;
        }

        return list;
    }

    public void distanceK(List<Sample> list, int index){
        //第k距离为从该点向两边扩散
        int left = index - 1;
        //第k距离为从该点向两边扩散
        int right = index + 1;
        //P点的价格
        Integer pPrice = list.get(index).getPrice();
        //存储P点左边的相对价格
        List<Sample> leftList = new ArrayList<>();
        //存储P点右边的相对价格
        List<Sample> rightList = new ArrayList<>();
        for (int i = 0; i < list.size();i++) {
            if (i <= left) {
                Sample sample = list.get(i);
                sample.setIndex(i);
                //左边点价格，都比他小
                sample.setRelativePrice(pPrice - list.get(i).getPrice());
                leftList.add(sample);
            } else if (i >= right) {
                Sample sample = list.get(i);
                sample.setIndex(i);
                //右边点价格，都比他大
                sample.setRelativePrice(list.get(i).getPrice() - pPrice);
                rightList.add(sample);
            }
        }
        //用于存组合的点
        List<Sample> newList = new ArrayList<>();
    }

    public List<Sample> readFile(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        List<Sample> list = new ArrayList<>();
        try{
            //换成你的文件名
            BufferedReader reader = new BufferedReader(new FileReader(path));
            reader.readLine();//第一行信息，为标题信息，不用
            String line = null;
            while((line=reader.readLine())!=null){
                //CSV格式文件为逗号分隔符文件，这里根据逗号切分
                String item[] = line.split(",");
                Sample sample = new Sample();
                sample.setPrice(Integer.valueOf(item[0]));
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
        //从小到大排序
        Collections.sort(list);
        return list;
    }

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