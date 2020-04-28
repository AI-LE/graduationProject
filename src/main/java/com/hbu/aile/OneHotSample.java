package com.hbu.aile;

/**
 * @author aile
 * @date 2020/4/20 16:48
 */
public class OneHotSample {
    private Double price;
    private Double mileage;
    private Double year;
    private Double trimEx;
    private Double trimLx;
    private Double trimExl;
    private Double engine4Cyl;
    private Double engine6Cyl;
    private Double transmissionManual;
    private Double transmissionAutomatic;

    /**
     * 初始化非数值化的 全为0
     */
    public OneHotSample() {
        trimEx = 0d;
        trimLx = 0d;
        trimExl = 0d;
        engine4Cyl = 0d;
        engine6Cyl = 0d;
        transmissionManual = 0d;
        transmissionAutomatic = 0d;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Double getYear() {
        return year;
    }

    public void setYear(Double year) {
        this.year = year;
    }

    public Double getTrimEx() {
        return trimEx;
    }

    public void setTrimEx(Double trimEx) {
        this.trimEx = trimEx;
    }

    public Double getTrimLx() {
        return trimLx;
    }

    public void setTrimLx(Double trimLx) {
        this.trimLx = trimLx;
    }

    public Double getTrimExl() {
        return trimExl;
    }

    public void setTrimExl(Double trimExl) {
        this.trimExl = trimExl;
    }

    public Double getEngine4Cyl() {
        return engine4Cyl;
    }

    public void setEngine4Cyl(Double engine4Cyl) {
        this.engine4Cyl = engine4Cyl;
    }

    public Double getEngine6Cyl() {
        return engine6Cyl;
    }

    public void setEngine6Cyl(Double engine6Cyl) {
        this.engine6Cyl = engine6Cyl;
    }

    public Double getTransmissionManual() {
        return transmissionManual;
    }

    public void setTransmissionManual(Double transmissionManual) {
        this.transmissionManual = transmissionManual;
    }

    public Double getTransmissionAutomatic() {
        return transmissionAutomatic;
    }

    public void setTransmissionAutomatic(Double transmissionAutomatic) {
        this.transmissionAutomatic = transmissionAutomatic;
    }

    @Override
    public String toString() {
        return "OneHotSample{" +
                "price=" + price +
                ", mileage=" + mileage +
                ", year=" + year +
                ", trimEx=" + trimEx +
                ", trimLx=" + trimLx +
                ", trimExl=" + trimExl +
                ", engine4Cyl=" + engine4Cyl +
                ", engine6Cyl=" + engine6Cyl +
                ", transmissionManual=" + transmissionManual +
                ", transmissionAutomatic=" + transmissionAutomatic +
                '}';
    }
}
