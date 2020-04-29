package com.hbu.aile;


import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;

import java.util.Arrays;
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
        denseX = DenseMatrix.Factory.zeros(rowoffile, columnoffile - 4);
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
//            denseX.setAsDouble(oneHotSamples.get(i).getEngine6Cyl(), i, 6);
//            str[5] = "getEngine6Cyl";
//            denseX.setAsDouble(oneHotSamples.get(i).getTransmissionManual(), i, 7);
//            str[6] = "getTransmissionManual";
//            denseX.setAsDouble(oneHotSamples.get(i).getTrimLx(), i, 8);
//            str[7] = "getTrimLx";
            denseY.setAsDouble(oneHotSamples.get(i).getPrice(), i, 0);
        }
        denseXt = denseX.transpose();
    }

    public void result() throws Exception {

        Matrix denseXtX = denseXt.mtimes(denseX);
        Matrix denseXtXInv = denseXtX.inv();
        Matrix denseXtXInvXt = denseXtXInv.mtimes(denseXt);
        Matrix denseXtXInvXtY = denseXtXInvXt.mtimes(denseY);
        System.out.println(denseXtXInvXtY);
        for (int i = 0; i < denseXtXInvXtY.getRowCount();i++){
            System.out.println(denseXtXInvXtY.getAsDouble(i, 0));
        }
        int count = 0;
        double[] deviations = new double[oneHotSamples.size()];
        for (int i = 0;i < oneHotSamples.size();i++) {
            OneHotSample oneHotSample = oneHotSamples.get(i);
            double price = denseXtXInvXtY.getAsDouble(0, 0);
            for (int j = 0; j < denseXtXInvXtY.getRowCount() - 1;j++) {
                price += denseXtXInvXtY.getAsDouble(j+1, 0) *
                        (Double)oneHotSample.getClass().getMethod(str[j]).invoke(oneHotSample);
            }
            deviations[i] = Math.abs(price - oneHotSample.getPrice());
        }
        Arrays.sort(deviations);
        System.out.println(count);
    }

    public static void main(String[] args) throws Exception {
        LinearRegression m = new LinearRegression();
        m.result();
    }
}