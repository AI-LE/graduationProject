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
            denseX.setAsDouble(oneHotSamples.get(i).getMileage(), i, 2);
            denseX.setAsDouble(oneHotSamples.get(i).getTransmissionAutomatic(), i, 3);
            denseX.setAsDouble(oneHotSamples.get(i).getTrimEx(), i, 4);
            denseX.setAsDouble(oneHotSamples.get(i).getTrimExl(), i, 5);
            denseX.setAsDouble(oneHotSamples.get(i).getEngine6Cyl(), i, 6);
            denseX.setAsDouble(oneHotSamples.get(i).getTransmissionManual(), i, 7);
            denseX.setAsDouble(oneHotSamples.get(i).getTrimLx(), i, 8);
            denseX.setAsDouble(oneHotSamples.get(i).getYear(), i, 9);
            denseY.setAsDouble(oneHotSamples.get(i).getPrice(), i, 0);
        }
        denseXt = denseX.transpose();
    }

    public void result() throws Exception {

        Matrix denseXtX = denseXt.mtimes(denseX);
        Matrix denseXtXInv = denseXtX.pinv();
        Matrix denseXtXInvXt = denseXtXInv.mtimes(denseXt);
        Matrix denseXtXInvXtY = denseXtXInvXt.mtimes(denseY);
        System.out.println(denseXtXInvXtY);
    }

    public static void main(String[] args) throws Exception {
        LinearRegression m = new LinearRegression();
        m.result();
    }
}