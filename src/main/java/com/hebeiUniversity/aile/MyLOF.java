package com.hebeiUniversity.aile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aile
 * @date 2020/4/26 19:10
 */
public class MyLOF {

    private List<double[]> sample;

    public static void main(String[] args) throws Exception {
        MyLOF myLOF = new MyLOF();
        UsedCarNnalysis usedCarNnalysis = new UsedCarNnalysis();
        List<OneHotSample> oneHotSamples = usedCarNnalysis.readFile2("汽车价格离群值检测/dataset/accord_sedan_training.csv");
        for (OneHotSample oneHotSample : oneHotSamples) {
            myLOF.sample.add(new double[]{oneHotSample.getPrice(), oneHotSample.getMileage()});
        }

    }
}
