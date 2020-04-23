package com.hebeiUniversity.aile;


import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;
import java.util.List;

/**
 * @author 艾乐
 */
public class LinearRegression {
    /*
     * 训练数据示例：
     *   x0        x1        x2        y
        1.0       1.0       2.0       7.2
        1.0       2.0       1.0       4.9
        1.0       3.0       0.0       2.6
        1.0       4.0       1.0       6.3
        1.0       5.0      -1.0       1.0
        1.0       6.0       0.0       4.7
        1.0       7.0      -2.0      -0.6
        注意！！！！x1，x2，y三列是用户实际输入的数据，x0是为了推导出来的公式统一，特地补上的一列。
        x0,x1,x2是“特征”，y是结果

        h(x) = theta0 * x0 + theta1* x1 + theta2 * x2

        theta0,theta1,theta2 是想要训练出来的参数

         此程序采用“梯度下降法”

     *
     */

    Matrix denseX;
    Matrix denseXT;
    Matrix denseY;
    private List<OneHotSample> oneHotSamples;

    public LinearRegression() throws Exception {
        UsedCarNnalysis usedCarNnalysis = new UsedCarNnalysis();
        oneHotSamples = usedCarNnalysis.readFile2("汽车价格离群值检测/dataset/accord_sedan_training.csv");
        //获取输入训练数据文本的   行数
        int rowoffile=oneHotSamples.size();
        //获取输入训练数据文本的   列数
        int columnoffile = OneHotSample.class.getDeclaredFields().length;
        denseX = DenseMatrix.Factory.zeros(rowoffile, columnoffile);
//        denseXT = DenseMatrix.Factory.zeros(columnoffile, rowoffile);
        denseY = DenseMatrix.Factory.zeros(rowoffile, 1);
        loadTrainDataFromFile(rowoffile);
    }
    private void loadTrainDataFromFile(int row){
        //trainData的第一列全部置为1.0（feature x0）
        for (int i = 0; i < oneHotSamples.size();i++) {
            denseX.setAsDouble(1.0, i, 0);
            denseX.setAsDouble(oneHotSamples.get(i).getEngine4Cyl(), i, 1);
            denseX.setAsDouble(oneHotSamples.get(i).getEngine6Cyl(), i, 2);
            denseX.setAsDouble(oneHotSamples.get(i).getMileage(), i, 3);
            denseX.setAsDouble(oneHotSamples.get(i).getTransmissionAutomatic(), i, 4);
            denseX.setAsDouble(oneHotSamples.get(i).getTransmissionManual(), i, 5);
            denseX.setAsDouble(oneHotSamples.get(i).getTrimEx(), i, 6);
            denseX.setAsDouble(oneHotSamples.get(i).getTrimExl(), i, 7);
            denseX.setAsDouble(oneHotSamples.get(i).getTrimLx(), i, 8);
            denseX.setAsDouble(oneHotSamples.get(i).getYear(), i, 9);
            denseY.setAsDouble(oneHotSamples.get(i).getPrice(), i, 0);
        }
        denseXT = denseX.transpose();
    }

    public void result(){
        Matrix denseXTX = denseXT.mtimes(denseX);
        Matrix denseXTXInv = denseXTX.inv();
        Matrix A = denseXT.mtimes(denseY);
        Matrix AA = denseXTXInv.mtimes(A);
        System.out.println(AA);
//        Matrix denseXTX = denseXT.mtimes(denseX);
//        Matrix AA = denseXTX.solve(denseXT.mtimes(denseY));
//        System.out.println(AA);
    }

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        LinearRegression m = new LinearRegression();

        m.result();
//        m.printTrainData();
//        for (int i = 0; i < m.XT.length; i++) {
//            for (int i1 = 0; i1 < m.XT[i].length; i1++) {
//                System.out.printf(m.XT[i][i1]+" ");
//            }
//        }
//        m.printTheta();
    }

}