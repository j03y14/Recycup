package com.recycup.recycup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CupInfo {

    @SerializedName("headName")
    @Expose
    String headName;

    @SerializedName("cupMeterial")
    @Expose
    String cupMeterial;
    @SerializedName("cafeLogo")
    @Expose
    String cafeLogo;

    public CupInfo(String headName,  String cupMeterial, String cafeLogo) {
        this.headName = headName;

        this.cupMeterial = cupMeterial;
        this.cafeLogo = cafeLogo;
    }



    public String getHeadName() {
        return headName;
    }

    public void getHeadName(String headName) {
        this.headName = headName;
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
