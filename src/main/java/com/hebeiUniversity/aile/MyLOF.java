package com.hebeiUniversity.aile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author aile
 * @date 2020/4/26 19:10
 */
public class MyLOF {

    class Point {
        //点的下标，用于定位某个点
        int index;
        //p点的第K距离
        double dk_p = 0;
        //p点第k距离领域内的点的个数
        int Nk_p = 0;
        //p的可达距离
        double reach_dist;
        //记录在第k距离内领域的点的下标
        List<Integer> NkIndexs = new ArrayList<>();
        //局部可达密度
        double lrdk_p;
        //局部离群因子
        double lof;
    }

    public MyLOF() throws Exception {
        UsedCarNnalysis usedCarNnalysis = new UsedCarNnalysis();
        List<OneHotSample> oneHotSamples = usedCarNnalysis.readFile2("汽车价格离群值检测/dataset/accord_sedan_training.csv");
        sample = new ArrayList<>();
        for (OneHotSample oneHotSample : oneHotSamples) {
            sample.add(new double[]{oneHotSample.getPrice(), oneHotSample.getMileage()});
        }
        distance = new double[oneHotSamples.size()][oneHotSamples.size()];
        distance();
        k_distance();
        reach_distance();
        localReachabilityDensity();
        localOutlierFactor();
        int count = 0;
        for (Point point : points) {
            if (point.lof < 1) {
                count++;
            }
            System.out.println(point.lof);
        }
        System.out.println(count);
    }

    private List<double[]> sample;
    //记录每个点之间的距离
    private double[][] distance;
    private List<Point> points = new ArrayList<>();

    /**
     * 两点之间的距离
     */
    private void distance() {
        for (int i = 0;i < sample.size();i++) {
            for (int j = 0;j < sample.size();j++) {
                if (i == j) {
                    continue;
                }
                double temp = Math.pow(sample.get(i)[0] - sample.get(j)[0], 2) + Math.pow(sample.get(i)[1] - sample.get(j)[1], 2);
                //点i和点J之间的距离,保留两位小数
                DecimalFormat df = new DecimalFormat("#.00");
                temp = Math.sqrt(temp);
                distance[i][j] = Double.parseDouble(df.format(temp));
            }
        }
    }

    /**
     * 计算每个点的第K距离以及第k距离领域内的点的个数
     */
    private void k_distance() {
        //计算点i的第k距离
        for (int i = 0;i < sample.size();i++) {
            double[] p_distance = distance[i].clone();
            Arrays.sort(p_distance);
            //第k距离k选10
            double dk_p = p_distance[10];
            Point point = new Point();
            point.dk_p = dk_p;
            for (int k = 0;k < distance[i].length;k++) {
                if (i == k) {
                    continue;
                }
                if (distance[i][k] <= dk_p) {
                    point.NkIndexs.add(k);
                }
            }
            point.Nk_p = point.NkIndexs.size();
            points.add(point);
        }
    }

    /**
     * 可达距离之和
     */
    private void reach_distance() {
        for (int i = 0;i < points.size();i++) {
            double reach_distance = 0;
            List<Integer> tempNkIndexs = points.get(i).NkIndexs;
            for (int j = 0;j < tempNkIndexs.size();j++) {
                reach_distance += Math.max(distance[tempNkIndexs.get(j)][i], points.get(tempNkIndexs.get(j)).dk_p);
            }
            points.get(i).reach_dist = reach_distance;
        }
    }

    /**
     *  局部可达密度
     */
    private void localReachabilityDensity() {
        for (int i = 0;i < points.size();i++) {
            points.get(i).lrdk_p = 1 / (points.get(i).reach_dist / points.get(i).Nk_p);
        }
    }

    /**
     * 局部离群因子LOF
     */
    private void localOutlierFactor() {
        for (int i = 0;i < points.size();i++) {
            double lrdko_p = 0;
            for (Integer nkIndex : points.get(i).NkIndexs) {
                lrdko_p += points.get(nkIndex).lrdk_p;
            }
            points.get(i).lof = lrdko_p / points.get(i).Nk_p / points.get(i).lrdk_p;
        }
    }
    public static void main(String[] args) throws Exception {
        MyLOF myLOF = new MyLOF();
    }
}
