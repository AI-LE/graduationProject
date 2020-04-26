package com.hebeiUniversity.aile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author aile
 * @date 2020/4/6 17:04
 */
public class LOF {
    //数据列分隔符
    public static String ITEM_SPLIT = ",";
    //数据维度
    public static int ATRIBUTE_NUMBER = 4;
    //数据之间的欧式距离
    public Map<Integer, List<Distance>> distances = new HashMap<>();
    //list索引及数据的索引
    public List<Result> results;
    public static void main(String[] args) throws Exception {
        //设置维度之间分隔符及维度数量
        ITEM_SPLIT = ",";
        ATRIBUTE_NUMBER = 4;
        ArrayList<double[]> data = readFile("data/iris.txt");
        dataNormalize(data);
        LOF lof = new LOF();
        int k = 9;
        lof.run(data, k);
        lof.printResult();

        //搜索结果k=9
        //搜索超参数k
		/*Set<Integer> targetIndices = new HashSet<>();
		targetIndices.add(41);
		targetIndices.add(106);
		targetIndices.add(109);
		targetIndices.add(117);
		targetIndices.add(131);
		lof.gridSearch(2, 40, targetIndices, data);*/
    }
    //读取数据
    private static ArrayList<double[]> readFile(String fileAdd) throws Exception {
        ArrayList<double[]> arrayList = new ArrayList<>();
        File file = new File(fileAdd);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 需要注意读入空行
                String[] strings = tempString.split(ITEM_SPLIT);
                double[] array = new double[ATRIBUTE_NUMBER];
                for (int i = 0; i < ATRIBUTE_NUMBER; i++) {
                    array[i] = Double.parseDouble(strings[i]);
                }
                arrayList.add(array);
            }
            reader.close();
            UsedCarNnalysis usedCarNnalysis = new UsedCarNnalysis();
            List<OneHotSample> oneHotSamples = usedCarNnalysis.readFile2("汽车价格离群值检测/dataset/accord_sedan_training.csv");
            ArrayList<double[]> data = new ArrayList<double[]>();
            for (OneHotSample oneHotSample : oneHotSamples) {
                data.add(new double[]{oneHotSample.getPrice(), oneHotSample.getMileage()});
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    System.out.println("关闭失败");
                }
            }
        }
        return null;
    }

    public void run(ArrayList<double[]> data, int k) {
        //1.计算各数据之间的欧式距离
        distancesBetweenSamples(data);
        //2.计算每个点p的局部可达密度（包括了第k领域以及第k距离的计算）
        kDisAndReachDisAndLRD(data, k);
        //3.计算局部离群因子
        LOF();
    }

    /**
     * 搜索超参数k，让给定的targetIndexes的lof值尽快排在前面
     * @param ks k最小值
     * @param ke k最大值
     * @param targetIndexes 目标数据索引
     * @param data 数据
     */
    public void gridSearch(int ks, int ke, Set<Integer> targetIndexes, ArrayList<double[]> data) {
        //排名index的和最小
        int min = Integer.MAX_VALUE;
        int bestK = 0;
        for(int i = ks; i <= ke; i++) {
            System.out.println("searching...k="+i);
            run(data, i);
            int rankSum = 0;
            for(int j = 0; j < results.size(); j++) {
                if(targetIndexes.contains(results.get(j).p)) {
                    rankSum += j;
                }
            }
            if(rankSum < min) {
                min = rankSum;
                bestK = i;
            }
        }
        System.out.println("best K:"+bestK);

    }

    public void printResult() {
        System.out.println("可能的异常点:");
        System.out.println("index\tlof");
        int c = 0;
        for(Result result : results) {
            //lof越接近1，越小于1都是正常，越大于1则可能是异常点
            if(result.lof <= 1.1)
                break;
            c++;
            System.out.println(result.p+"  "+result.lof);
        }
        System.out.println("共"+c+"个点");
    }

    //计算局部离群因子并由大到小排序
    public void LOF() {
        for(int i = 0; i < results.size(); i++) {
            //p与邻域内点的局部可达密度之比的平均值
            Result result = results.get(i);
            double avg = 0;
            for(int j = 0; j < result.neighbors.size(); j++) {
                int index = result.neighbors.get(j);
                avg += results.get(index).lrd;
            }
            avg /= result.lrd;
            result.lof =avg / result.neighbors.size();
        }
        //有个麻烦问题就是如果这里排序了那么索引就乱了，Result还是得保存p的index
        Collections.sort(results);
    }


    // k <= data.size - 1
    public void kDisAndReachDisAndLRD(ArrayList<double[]> data, int k) {
        results = new ArrayList<>(data.size());
        for(int i = 0; i < data.size(); i++) {
            Result knia = new Result();
            knia.p = i;
            //1.获取点p到其他点的距离
            List<Distance> p2Others = new ArrayList<>(data.size()-1);
            for(int j = 0; j < data.size(); j++) {
                if(i!=j) {
                    //bug:如果i > j，比如i=2,j=1,那么实际调用getDistance时获取的时i=1,j=2
                    //问题就是返回的对象中distance.j就变成了2，实际上应该是1
                    //最好的办法就需要动态设置distance对象中的属性
                    p2Others.add( getDistance(i, j));
                }
            }
            //2.对距离降序排列，选取前k个，如果k+1到p的距离等于k到p，那么加进去，依次类推，k+2,k+3
            Collections.sort(p2Others);
            //0..k-1
            double p2k = p2Others.get(k-1).dis;
            int m = k;
            while(p2Others.get(m).dis == p2k) m++;
            //System.out.println("m==k?"+(m==k));
            //将这些邻域点加进去,并保存k距离
            List<Integer> indices = new ArrayList<>(m);
            for(int c = 0; c < m; c++) {
                indices.add(p2Others.get(c).j);
            }
            knia.neighbors = indices;
            knia.kDis = p2k;
            results.add(knia);
        }
        for(int i = 0; i < data.size(); i++) {
            //3.计算p到其他点的可达距离
            Result ki = results.get(i);
            double avg = 0;
            for(int j = 0; j < ki.neighbors.size(); j++) {
                int index = ki.neighbors.get(j);
                avg += Math.max(results.get(index).kDis, getDistance(i, index).dis);
            }
            //4.计算p的局部可达密度:邻域内平均可达距离的倒数
            ki.lrd = 1/(avg / ki.neighbors.size());
        }
    }

    public void distancesBetweenSamples(ArrayList<double[]> data){
        //实际保存的距离数量
        //int len = (data.size()*(data.size()-1))/2;
        if(distances != null && distances.size() > 0)
            return;
        for(int i = 0; i <data.size()-1; i++) {
            List<Distance> dis = new ArrayList<>(data.size() -i - 1);
            distances.put(i, dis);
            for(int j = i+1; j < data.size(); j++) {
                //dis[j-i-1] = distance(data.get(i), data.get(j));
                Distance distance = new Distance();
                distance.j = j;
                distance.dis = distance(data.get(i), data.get(j));
                dis.add(distance);
            }
        }
    }

    public Distance getDistance(int i, int j) {
        //索引必须小的在前面
        Distance distance = null;
        //对j索引做偏移，j-i-1
        if(i > j) {
            int tmp = i;
            i = j;
            j = tmp;
            distance  = distances.get(i).get(j-i-1);
            distance.j = i;
        } else {
            distance  = distances.get(i).get(j-i-1);
            distance.j = j;
        }
        return distance;
    }

    //欧式距离
    public double distance(double[] v1, double[] v2) {
        double sum = 0;
        for(int i = 0; i < v1.length; i++) {
            sum += Math.pow(v1[i]-v2[i], 2);
        }
        return Math.sqrt(sum);
    }

    //数据点p的第k邻域数据索引及可达密度及局部离群因子等计算结果保存
    public static class Result implements Comparable<Result>{
        public int p;
        //p的局部可达密度
        public double lrd;
        //p的k距离
        public double kDis;
        //局部离群因子
        public double lof;
        //邻域点的索引
        public List<Integer> neighbors;
        @Override
        public int compareTo(Result o) {
            return o.lof > this.lof? 1 : -1;
        }
    }

    public static class Distance implements Comparable<Distance>{
        //数据i到j的欧式距离，这里j为数据索引，i存储到map中的key
        public int j;
        public double dis;
        @Override
        public int compareTo(Distance o) {
            return o.dis > this.dis ? -1 : 1;
        }
    }
    /**
     * 数据归一化
     * 如果不做归一化并且不修改weka中DBSCAN的设置那么结果将大不一样
     * x = (x - min)/(max - min)
     */
    public static void dataNormalize(ArrayList<double[]> inputValues) {
        //x = (x - min)/(max - min)
        double[] mins = new double[ATRIBUTE_NUMBER];
        double[] maxs = new double[ATRIBUTE_NUMBER];
        for(int i = 0; i < ATRIBUTE_NUMBER;i++) {
            mins[i] = Double.MAX_VALUE;
            maxs[i] = Double.MIN_VALUE;
        }
        for(int i = 0; i < ATRIBUTE_NUMBER; i++) {
            for(int j = 0; j < inputValues.size();j++) {
                mins[i] = Math.min(inputValues.get(j)[i], mins[i]);
                maxs[i] = Math.max(inputValues.get(j)[i], maxs[i]);
            }
        }
        double[] maxsReduceMins = new double[ATRIBUTE_NUMBER];
        for(int i = 0; i < ATRIBUTE_NUMBER;i++) {
            maxsReduceMins[i] = maxs[i] - mins[i];
        }
        for(int i = 0; i < ATRIBUTE_NUMBER; i++) {
            for(int j = 0; j < inputValues.size();j++) {
                inputValues.get(j)[i] = (inputValues.get(j)[i] - mins[i])/(maxsReduceMins[i]);
            }
        }
    }
}
