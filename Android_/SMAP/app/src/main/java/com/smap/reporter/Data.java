package com.smap.reporter;

/**
 * Created by user on 01/01/2019.
 */

public class Data {
    private  String O_3;
    private  String SO_2;
    private  String NO_2;
    private  String CO;
    private  String PM_2_5;
    private  String PM_10;


    public Data(String O_3, String SO_2, String NO_2, String CO, String PM_2_5, String PM_10) {
        this.O_3 = O_3;
        this.SO_2 = SO_2;
        this.NO_2 = NO_2;
        this.CO = CO;
        this.PM_2_5 = PM_2_5;
        this.PM_10 = PM_10;
    }

    // Get Functions
    public String getO_3() {
        return this.O_3;
    }
    public String getSO_2() {
        return this.SO_2;
    }
    public String getCO() {
        return this.CO;
    }
    public String getNO_2() {
        return this.NO_2;
    }
    public String getPM_2_5() {
        return this.PM_2_5;
    }
    public String getPM_10() {
        return this.PM_10;
    }

    // set functions
    public void setO_3(String O_3) {
        this.O_3 = O_3;
    }
    public void setSO_2(String SO_2) {
        this.SO_2 = SO_2;
    }
    public void setNO_2(String NO_2) {
        this.NO_2 = NO_2;
    }
    public void setCO(String CO) {
        this.CO = CO;
    }
    public void setPM_2_5(String PM_2_5) {
        this.PM_2_5 = PM_2_5;
    }
    public void setPM_10(String PM_10) {
        this.PM_10 = PM_10;
    }

    @Override
    public String toString(){
        return "O_3: " + this.O_3 + ", SO_2: "+ this.SO_2 + ", NO_2: " + this.NO_2 + ", CO: " +
                this.CO + ", PM_2_5: " + this.PM_2_5 + ", PM_10: " + this.PM_10;
    }
}
