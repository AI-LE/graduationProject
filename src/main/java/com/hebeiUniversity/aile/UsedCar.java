package com.hebeiUniversity.aile;

/**
 * @author aile
 * @date 2020/4/20 22:43
 */
public enum UsedCar {




    private String key;
    private String value;
    private UsedCar(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
