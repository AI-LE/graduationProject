package com.hbu.aile;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author aile
 * @date 2020/4/16 10:15
 */
public class Sample implements Comparable<Sample>{
    private Integer price;  //样本价格
    private Integer kDistance;  //第K距离
    private Integer NKP;    //第k距离领域点的个数
    private List<Integer> reachDistance;  //第k距离领域点的可达距离
    private BigDecimal localReachAbilityDensity;    //局部可达密度
    private BigDecimal LOF; //局部离群因子；小于1说明该点为正常值，大于1，说明该点为异常值
    private Integer relativePrice;   //与参与计算的点的相对价格，也就相当于P,O亮点的距离
    private int index;  // 不属于样本属性，辅助字段

    @Override   //从小到大排序
    public int compareTo(Sample o) {
        return this.price - o.price;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getkDistance() {
        return kDistance;
    }

    public void setkDistance(Integer kDistance) {
        this.kDistance = kDistance;
    }

    public Integer getNKP() {
        return NKP;
    }

    public void setNKP(Integer NKP) {
        this.NKP = NKP;
    }

    public List<Integer> getReachDistance() {
        return reachDistance;
    }

    public void setReachDistance(List<Integer> reachDistance) {
        this.reachDistance = reachDistance;
    }

    public BigDecimal getLocalReachAbilityDensity() {
        return localReachAbilityDensity;
    }

    public void setLocalReachAbilityDensity(BigDecimal localReachAbilityDensity) {
        this.localReachAbilityDensity = localReachAbilityDensity;
    }

    public BigDecimal getLOF() {
        return LOF;
    }

    public void setLOF(BigDecimal LOF) {
        this.LOF = LOF;
    }

    public Integer getRelativePrice() {
        return relativePrice;
    }

    public void setRelativePrice(Integer relativePrice) {
        this.relativePrice = relativePrice;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
