package com.hebeiUniversity.aile;


import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;
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
        denseX = DenseMatrix.Factory.zeros(rowoffile, columnoffile - 4);
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
            denseY.setAsDouble(oneHotSamples.get(i).getPrice(), i, 0);
        }
        denseXt = denseX.transpose();
    }

    public void result(){
        Matrix denseXtX = denseXt.mtimes(denseX);
        Matrix denseXtXInv = denseXtX.inv();
        Matrix denseXtXInvXt = denseXtXInv.mtimes(denseXt);
        Matrix denseXtXInvXtY = denseXtXInvXt.mtimes(denseY);
        System.out.println("常数项w0："+ denseXtXInvXtY.getAsDouble(0, 0));
        System.out.println("Engine4Cyl w1："+ denseXtXInvXtY.getAsDouble(1, 0));
        System.out.println("Mileage w2："+ denseXtXInvXtY.getAsDouble(2, 0));
        System.out.println("TransmissionAutomatic w3："+ denseXtXInvXtY.getAsDouble(3, 0));
        System.out.println("TrimEx w4："+ denseXtXInvXtY.getAsDouble(4, 0));
        System.out.println("TrimExl w5："+ denseXtXInvXtY.getAsDouble(5, 0));
        int count = 0;
        for (int i = 0;i < oneHotSamples.size();i++) {
            OneHotSample oneHotSample = oneHotSamples.get(i);
            double price = denseXtXInvXtY.getAsDouble(0, 0) +
                    denseXtXInvXtY.getAsDouble(1, 0) * oneHotSample.getEngine4Cyl() +
                    denseXtXInvXtY.getAsDouble(2, 0) * oneHotSample.getMileage() +
                    denseXtXInvXtY.getAsDouble(3, 0) * oneHotSample.getTransmissionAutomatic() +
                    denseXtXInvXtY.getAsDouble(4, 0) * oneHotSample.getTrimEx() +
                    denseXtXInvXtY.getAsDouble(5, 0) * oneHotSample.getTrimExl();
            double abs = Math.abs(price - oneHotSample.getPrice());
            double allow = denseY.getAsDouble(i, 0) * 0.05;
            System.out.println(price);
            System.out.println(abs);
            System.out.println(allow);
            if (abs  > denseY.getAsDouble(i, 0) * 0.05) {
                count++;
            }
            System.out.println();
        }
        System.out.println(count);
    }

    public static void main(String[] args) throws Exception {
        LinearRegression m = new LinearRegression();
        m.result();
    }
}