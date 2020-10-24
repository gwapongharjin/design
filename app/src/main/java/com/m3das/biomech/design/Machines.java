package com.m3das.biomech.design;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "machines")
public class Machines {



    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String machine_qrcode;
//    private String date_of_survey;
//    private String profile_of_respondent;
//    private String name_of_respondent;
//    private String address;
//    private String age;
//    private String sex;
//    private String mobile_number;
//    private String telephone_number;
//    private String educational_attainment;
//    private String location_of_machine;
    private final String machine_type;
//    private String brand_of_machine;
//    private String model_of_machine;
//    private String rated_power;
//    private String service_area;
//    private String average_operating_hours;
//    private String average_operating_days;
//    private String type_of_ownership;
    private final String machine_latitude;
    private final String machine_longitude;
    private final String machine_image_base64;

    public Machines(String machine_qrcode, String machine_type, String machine_latitude, String machine_longitude, String machine_image_base64) {
        this.machine_qrcode = machine_qrcode;
        this.machine_type = machine_type;
        this.machine_latitude = machine_latitude;
        this.machine_longitude = machine_longitude;
        this.machine_image_base64 = machine_image_base64;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {return id;}

    public String getMachine_type() {
        return machine_type;
    }

    public String getMachine_qrcode() {
        return machine_qrcode;
    }

    public String getMachine_latitude() {
        return machine_latitude;
    }

    public String getMachine_longitude() {
        return machine_longitude;
    }

    public String getMachine_image_base64() {
        return machine_image_base64;
    }

}
