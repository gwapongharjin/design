package com.m3das.biomech.design;

public class Variable {

    static String image_string_global = "";

    public static void setStringImage(String image){
        image_string_global = image;
    }

    public static String getStringImage(){
        return image_string_global;
    }
}
