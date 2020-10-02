package com.m3das.biomech.design;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "machines")
public class Machines {



    @PrimaryKey(autoGenerate = true)
    private int id;
    private String machine_type;
    private String machine_qrcode;
    private String machine_latitude;
    private String machine_longitude;
    private String machine_image_base64;

    public Machines(String machine_type, String machine_qrcode, String machine_latitude, String machine_longitude, String machine_image_base64) {
        this.machine_type = machine_type;
        this.machine_qrcode = machine_qrcode;
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
