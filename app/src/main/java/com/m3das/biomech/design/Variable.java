package com.m3das.biomech.design;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.m3das.biomech.design.viewmodels.MachineListViewModel;

import java.util.ArrayList;
import java.util.List;

public class Variable {

    static String image_string_global = "";

    static String implement_list_global = "";

    static ArrayList<String>global_list_of_rescode = new ArrayList<>();

    static String machine_list_global = "";

    public static void setStringImage(String image) {
        image_string_global = image;
    }

    public static String getStringImage() {
        return image_string_global;
    }

    public static void setImpList(String str){ implement_list_global = str;}

    public static String getImpList(){return implement_list_global;}

    public static void setMachList(String str){machine_list_global = str;}

    public static String getMachList(){return machine_list_global;}

    public static void setListResCode( ArrayList<String> profiles){
        global_list_of_rescode = profiles;
    }
    public static ArrayList<String> getListResCode(){
        return global_list_of_rescode;
    }


}
