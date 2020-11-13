package com.m3das.biomech.design.profiledb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "respondent_profile")
public class Profile {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private final String resCode;
    private final String profile;
    private final String profile_specify;
    private final String owner_type;
    private final String name_respondent;
    private final String address;
    private final String age;
    private final String sex;
    private final String contact_number;
    private final String mobnum1;
    private final String mobnum2;
    private final String telnum1;
    private final String telnum2;
    private final String educational_attainment;

    public void setId(int id) {
        this.id = id;
    }

    public Profile(String resCode, String profile, String profile_specify, String owner_type, String name_respondent, String address, String age,
                   String sex, String contact_number, String mobnum1, String mobnum2, String telnum1, String telnum2, String educational_attainment) {
        this.resCode = resCode;
        this.profile = profile;
        this.profile_specify = profile_specify;
        this.owner_type = owner_type;
        this.name_respondent = name_respondent;
        this.address = address;
        this.age = age;
        this.sex = sex;
        this.contact_number = contact_number;
        this.mobnum1 = mobnum1;
        this.mobnum2 = mobnum2;
        this.telnum1 = telnum1;
        this.telnum2 = telnum2;
        this.educational_attainment = educational_attainment;
    }

    public int getId() {
        return id;
    }

    public String getResCode() {
        return resCode;
    }

    public String getProfile() {
        return profile;
    }

    public String getProfile_specify() {
        return profile_specify;
    }

    public String getOwner_type() {
        return owner_type;
    }

    public String getName_respondent() {
        return name_respondent;
    }

    public String getAddress() {
        return address;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getMobnum1() {
        return mobnum1;
    }

    public String getMobnum2() {
        return mobnum2;
    }

    public String getTelnum1() {
        return telnum1;
    }

    public String getTelnum2() {
        return telnum2;
    }

    public String getEducational_attainment() {
        return educational_attainment;
    }
}
