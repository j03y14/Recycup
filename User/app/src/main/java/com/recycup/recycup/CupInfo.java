package com.recycup.recycup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CupInfo {

    @SerializedName("cafeId")
    @Expose
    int cafeId;
    @SerializedName("cafeName")
    @Expose
    String cafeName;

    @SerializedName("cupMeterial")
    @Expose
    String cupMeterial;
    @SerializedName("cafeLogo")
    @Expose
    String cafeLogo;

    public CupInfo(int cafeId, String cafeName,  String cupMeterial, String cafeLogo) {
        this.cafeId = cafeId;
        this.cafeName = cafeName;

        this.cupMeterial = cupMeterial;
        this.cafeLogo = cafeLogo;
    }

    public int getCafeId() {
        return cafeId;
    }

    public void setCafeId(int cafeId) {
        this.cafeId = cafeId;
    }

    public String getCafeName() {
        return cafeName;
    }

    public void setCafeName(String cafeName) {
        this.cafeName = cafeName;
    }


    public String getCupMeterial() {
        return cupMeterial;
    }

    public void setCupMeterial(String cupMeterial) {
        this.cupMeterial = cupMeterial;
    }

    public String getCafeLogo() {
        return cafeLogo;
    }

    public void setCafeLogo(String cafeLogo) {
        this.cafeLogo = cafeLogo;
    }
}
