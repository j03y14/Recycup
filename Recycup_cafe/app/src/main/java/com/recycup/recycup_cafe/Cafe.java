package com.recycup.recycup_cafe;

public class Cafe {
    String headName;
    String cafeName;
    String material;


    private static class SingletonHolder {
        private static Cafe cafe= new Cafe();

    }

    public static Cafe getInstance(){
        return SingletonHolder.cafe;
    }

    public Cafe() {

    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public String getCafeName() {
        return cafeName;
    }

    public void setCafeName(String cafeName) {
        this.cafeName = cafeName;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
