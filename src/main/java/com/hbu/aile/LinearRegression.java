package com.hbu.aile;


import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LinearRegression {

    Matrix denseX;
    Matrix denseXt;
    Matrix denseY;
    String[] str = new String[10];
    private List<OneHotSample> oneHotSamples;

    public LinearRegression() throws Exception {
        UsedCarNnalysis usedCarNnalysis = new UsedCarNnalysis();
        oneHotSamples = usedCarNnalysis.readFile2("汽车价格离群值检测/dataset/accord_sedan_training.csv");
        //获取输入训练数据文本的   行数
        int rowoffile=oneHotSamples.size();
        //获取输入训练数据文本的   列数
        int columnoffile = OneHotSample.class.getDeclaredFields().length;
        //去掉某些会造成线性相关的列
        denseX = DenseMatrix.Factory.zeros(rowoffile, columnoffile - 1);
        denseY = DenseMatrix.Factory.zeros(rowoffile, 1);
        initMatrix();
    }
    private void initMatrix(){
        for (int i = 0; i < oneHotSamples.size();i++) {
            denseX.setAsDouble(1.0, i, 0);
            denseX.setAsDouble(oneHotSamples.get(i).getEngine4Cyl(), i, 1);
            str[0] = "getEngine4Cyl";
            denseX.setAsDouble(oneHotSamples.get(i).getMileage(), i, 2);
            str[1] = "getMileage";
            denseX.setAsDouble(oneHotSamples.get(i).getTransmissionAutomatic(), i, 3);
            str[2] = "getTransmissionAutomatic";
            denseX.setAsDouble(oneHotSamples.get(i).getTrimEx(), i, 4);
            str[3] = "getTrimEx";
            denseX.setAsDouble(oneHotSamples.get(i).getTrimExl(), i, 5);
            str[4] = "getTrimExl";
            denseX.setAsDouble(oneHotSamples.get(i).getEngine6Cyl(), i, 6);
            str[5] = "getEngine6Cyl";
            denseX.setAsDouble(oneHotSamples.get(i).getTransmissionManual(), i, 7);
            str[6] = "getTransmissionManual";
            denseX.setAsDouble(oneHotSamples.get(i).getTrimLx(), i, 8);
            str[7] = "getTrimLx";
            denseX.setAsDouble(oneHotSamples.get(i).getYear(), i, 9);
            str[8] = "getYear";
            denseY.setAsDouble(oneHotSamples.get(i).getPrice(), i, 0);
        }
        denseXt = denseX.transpose();
    }

    public void result() throws Exception {

        Matrix denseXtX = denseXt.mtimes(denseX);
        Matrix denseXtXInv = denseXtX.pinv();
        Matrix denseXtXInvXt = denseXtXInv.mtimes(denseXt);
        Matrix denseXtXInvXtY = denseXtXInvXt.mtimes(denseY);
        for (int i = 0; i < denseXtXInvXtY.getRowCount();i++){
            System.out.println(denseXtXInvXtY.getAsDouble(i, 0));
            if (str[i] != null)
            System.out.print(str[i].substring(3) + " : ");
        }
        int count = 0;
        for (int i = 0;i < oneHotSamples.size();i++) {
            OneHotSample oneHotSample = oneHotSamples.get(i);
            double price = denseXtXInvXtY.getAsDouble(0, 0);
            for (int j = 0; j < denseXtXInvXtY.getRowCount() - 1;j++) {
                price += denseXtXInvXtY.getAsDouble(j+1, 0) *
                        (Double)OneHotSample.class.getMethod(str[j]).invoke(oneHotSample);
            }
            oneHotSample.setDeviation(Math.abs(price - oneHotSample.getPrice()));
        }
        Collections.sort(oneHotSamples);
        double deviation = oneHotSamples.get((int)(oneHotSamples.size() * 0.95)).getDeviation();
        System.out.println(oneHotSamples.get((int)(oneHotSamples.size() * 0.90)).getDeviation() + "111111111111111111111111");
        System.out.println(oneHotSamples.get((int)(oneHotSamples.size() * 0.85)).getDeviation() + "111111111111111111111111");
        System.out.println(oneHotSamples.get((int)(oneHotSamples.size() * 0.80)).getDeviation() + "111111111111111111111111");
        System.out.println(oneHotSamples.get((int)(oneHotSamples.size() * 0.5)).getDeviation() + "111111111111111111111111");
        Class oneHotSampleClass = OneHotSample.class;
        for (OneHotSample oneHotSample : oneHotSamples) {
            if (oneHotSample.getDeviation() > deviation) {
                System.out.print(oneHotSample.getPrice() + "\t" + oneHotSample.getMileage() + "\t");
                System.out.println();
                for (String s : str) {
                    if (!(s == null || "".equals(s))) {
                        Method method = oneHotSampleClass.getMethod(s);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        LinearRegression m = new LinearRegression();
        m.result();
    }
}