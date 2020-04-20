package com.hebeiUniversity.aile;

/**
 * @author aile
 * @date 2020/4/8 22:59
 */
import java.io.*;

import java.util.*;


public class UsedCarNnalysis {

    public static void main(String[] args) throws Exception  {
        UsedCarNnalysis usedCarNnalysis = new UsedCarNnalysis();
        //获取检测样本
        List<Sample> list = usedCarNnalysis.readFile("汽车价格离群值检测/dataset/accord_sedan_testing.csv");
        //求每个样本值的第K距离
        for (int i = 0;i < list.size();i++){
            usedCarNnalysis.distanceK(list, i);
        }
    }
    //    独热编码，对某一列进行编码
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
//            list.set(i, list.get(i) + ","+array2string(a));
        }

        return list;
    }

    private void distanceK(List<Sample> list, int index){
        int left = index - 1;  //第k距离为从该点向两边扩散
        int right = index + 1;  //第k距离为从该点向两边扩散
        Integer pPrice = list.get(index).getPrice(); //P点的价格
        List<Sample> leftList = new ArrayList<>();  //存储P点左边的相对价格
        List<Sample> rightList = new ArrayList<>(); //存储P点右边的相对价格
        for (int i = 0; i < list.size();i++) {
            if (i <= left) {
                Sample sample = list.get(i);
                sample.setIndex(i);
                sample.setRelativePrice(pPrice - list.get(i).getPrice());//左边点价格，都比他小
                leftList.add(sample);
            } else if (i >= right) {
                Sample sample = list.get(i);
                sample.setIndex(i);
                sample.setRelativePrice(list.get(i).getPrice() - pPrice);//右边点价格，都比他大
                rightList.add(sample);
            }
        }
        List<Sample> newList = new ArrayList<>();  //用于存组合的点
    }

    private List<Sample> readFile(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        List<Sample> list = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(path));//换成你的文件名
            reader.readLine();//第一行信息，为标题信息，不用
            String line = null;
            while((line=reader.readLine())!=null){
                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
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
        Collections.sort(list);//从小到大排序
        return list;
    }
}