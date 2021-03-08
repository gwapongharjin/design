package com.m3das.biomech.design;

import java.util.ArrayList;

public class Variable {

    static int cropSelected = 0;

    static String image_string_global = "";

    static String implement_list_global = "";

    static ArrayList<String> global_list_of_rescode = new ArrayList<>();

    static String machine_list_global = "";

    static String dummyImg = "";

    public static void setStringImage(String image) {
        image_string_global = image;
    }

    public static String getStringImage() {
        return image_string_global;
    }

    public static void setImpList(String str) {
        implement_list_global = str;
    }

    public static String getImpList() {
        return implement_list_global;
    }

    public static void setMachList(String str) {
        machine_list_global = str;
    }

    public static String getMachList() {
        return machine_list_global;
    }

    public static void setCrop(int crop) {
        cropSelected = crop;
    }

    public static int getCrop() {
        return cropSelected;
    }

    public static void setListResCode(ArrayList<String> profiles) {
        global_list_of_rescode = profiles;
    }

    public static ArrayList<String> getListResCode() {
        return global_list_of_rescode;
    }

    public static String getDummyImg() {
        return dummyImg;
    }

    public static void setDummyImg(String str) {
        dummyImg = str;
    }


}
