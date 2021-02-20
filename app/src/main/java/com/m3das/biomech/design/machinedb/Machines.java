package com.m3das.biomech.design.machinedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "machines")
public class Machines {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String machine_type;
    private final String type_tubewells;
    private final String type_mill;
    private final String machine_qrcode;
    private final String date_of_survey;
    private final String machine_brand;
    private final String machine_brand_specify;
    private final String machine_model;
    private final String machine_model_specify;
    private final String rated_power;
    private final String service_area;
    private final String newly_planted_area;
    private final String ratooned_area;
    private final String ave_op_hours;
    private final String ave_op_days;
    private final String effective_area;
    private final String time_used;
    private final String capacity;
    private final String ave_yield;
    private final String num_loads;
    private final String rate;
    private final String ownership;
    private final String purch_grant_dono;
    private final String agency;
    private final String agency_specify;
    private final String name_owner;
    private final String year_acquired;
    private final String condition_acquired;
    private final String rental;
    private final String main_custom_rent;
    private final String main_custom_rent_unit;
    private final String main_custom_rent_unit_specify;
    private final String plow_custom_rent;
    private final String plow_custom_rent_unit;
    private final String plow_custom_rent_unit_specify;
    private final String harr_custom_rent;
    private final String harr_custom_rent_unit;
    private final String harr_custom_rent_unit_specify;
    private final String furr_custom_rent;
    private final String furr_custom_rent_unit;
    private final String furr_custom_rent_unit_specify;
    private final String other_unit_operation;
    private final String other_custom_rent;
    private final String other_custom_rent_unit;
    private final String other_custom_rent_unit_specify;
    private final String plow_ave_fuel;
    private final String harr_ave_fuel;
    private final String furr_ave_fuel;
    private final String availability;
    private final String rent_prov;
    private final String rent_mun;
    private final String rent_brgy;
    private final String condition;
    private final String problems;
    private final String specify_problems;
    private final String year_inoperable;
    private final String location;
    private final String prov;
    private final String mun;
    private final String brgy;
    private final String machine_latitude;
    private final String machine_longitude;
    private final String machine_image_base64;
    private final String accuracy;
    private final String resCode;
    private final String resName;

    public void setId(int id) {
        this.id = id;
    }

    public Machines(String machine_type, String type_tubewells, String type_mill, String machine_qrcode, String date_of_survey, String machine_brand, String machine_brand_specify,
                    String machine_model, String machine_model_specify, String rated_power, String service_area, String newly_planted_area, String ratooned_area, String ave_op_hours,
                    String ave_op_days, String effective_area, String time_used, String capacity, String ave_yield, String num_loads, String rate, String ownership, String purch_grant_dono, String agency, String agency_specify,
                    String name_owner, String year_acquired, String condition_acquired, String rental, String main_custom_rent, String main_custom_rent_unit, String main_custom_rent_unit_specify,
                    String plow_custom_rent, String plow_custom_rent_unit, String plow_custom_rent_unit_specify, String harr_custom_rent, String harr_custom_rent_unit,
                    String harr_custom_rent_unit_specify, String furr_custom_rent, String furr_custom_rent_unit, String furr_custom_rent_unit_specify, String other_unit_operation, String other_custom_rent,
                    String other_custom_rent_unit, String other_custom_rent_unit_specify, String plow_ave_fuel, String harr_ave_fuel, String furr_ave_fuel, String availability,
                    String rent_prov, String rent_mun, String rent_brgy, String condition, String problems, String specify_problems, String year_inoperable, String location, String prov, String mun, String brgy,
                    String machine_latitude, String machine_longitude, String machine_image_base64, String accuracy, String resCode, String resName) {
        this.machine_type = machine_type;
        this.type_tubewells = type_tubewells;
        this.type_mill = type_mill;
        this.machine_qrcode = machine_qrcode;
        this.date_of_survey = date_of_survey;
        this.machine_brand = machine_brand;
        this.machine_brand_specify = machine_brand_specify;
        this.machine_model = machine_model;
        this.machine_model_specify = machine_model_specify;
        this.rated_power = rated_power;
        this.service_area = service_area;
        this.newly_planted_area = newly_planted_area;
        this.ratooned_area = ratooned_area;
        this.ave_op_hours = ave_op_hours;
        this.ave_op_days = ave_op_days;
        this.effective_area = effective_area;
        this.time_used = time_used;
        this.capacity = capacity;
        this.ave_yield = ave_yield;
        this.num_loads = num_loads;
        this.rate = rate;
        this.ownership = ownership;
        this.purch_grant_dono = purch_grant_dono;
        this.agency = agency;
        this.agency_specify = agency_specify;
        this.name_owner = name_owner;
        this.year_acquired = year_acquired;
        this.condition_acquired = condition_acquired;
        this.rental = rental;
        this.main_custom_rent = main_custom_rent;
        this.main_custom_rent_unit = main_custom_rent_unit;
        this.main_custom_rent_unit_specify = main_custom_rent_unit_specify;
        this.plow_custom_rent = plow_custom_rent;
        this.plow_custom_rent_unit = plow_custom_rent_unit;
        this.plow_custom_rent_unit_specify = plow_custom_rent_unit_specify;
        this.harr_custom_rent = harr_custom_rent;
        this.harr_custom_rent_unit = harr_custom_rent_unit;
        this.harr_custom_rent_unit_specify = harr_custom_rent_unit_specify;
        this.furr_custom_rent = furr_custom_rent;
        this.furr_custom_rent_unit = furr_custom_rent_unit;
        this.furr_custom_rent_unit_specify = furr_custom_rent_unit_specify;
        this.other_unit_operation = other_unit_operation;
        this.other_custom_rent = other_custom_rent;
        this.other_custom_rent_unit = other_custom_rent_unit;
        this.other_custom_rent_unit_specify = other_custom_rent_unit_specify;
        this.plow_ave_fuel = plow_ave_fuel;
        this.harr_ave_fuel = harr_ave_fuel;
        this.furr_ave_fuel = furr_ave_fuel;
        this.availability = availability;
        this.rent_prov = rent_prov;
        this.rent_mun = rent_mun;
        this.rent_brgy = rent_brgy;
        this.condition = condition;
        this.problems = problems;
        this.specify_problems = specify_problems;
        this.year_inoperable = year_inoperable;
        this.location = location;
        this.prov = prov;
        this.mun = mun;
        this.brgy = brgy;
        this.machine_latitude = machine_latitude;
        this.machine_longitude = machine_longitude;
        this.machine_image_base64 = machine_image_base64;
        this.accuracy = accuracy;
        this.resCode = resCode;
        this.resName = resName;
    }

    public int getId() {
        return id;
    }

    public String getMachine_type() {
        return machine_type;
    }

    public String getType_tubewells() {
        return type_tubewells;
    }


    public String getType_mill() {
        return type_mill;
    }

    public String getMachine_qrcode() {
        return machine_qrcode;
    }

    public String getDate_of_survey() {
        return date_of_survey;
    }

    public String getMachine_brand() {
        return machine_brand;
    }

    public String getMachine_brand_specify() {
        return machine_brand_specify;
    }

    public String getMachine_model() {
        return machine_model;
    }

    public String getMachine_model_specify() {
        return machine_model_specify;
    }

    public String getRated_power() {
        return rated_power;
    }

    public String getService_area() {
        return service_area;
    }

    public String getNewly_planted_area() {
        return newly_planted_area;
    }

    public String getRatooned_area() {
        return ratooned_area;
    }

    public String getAve_op_hours() {
        return ave_op_hours;
    }

    public String getAve_op_days() {
        return ave_op_days;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getEffective_area() {
        return effective_area;
    }

    public String getTime_used() {
        return time_used;
    }

    public String getAve_yield() {
        return ave_yield;
    }

    public String getNum_loads() {
        return num_loads;
    }

    public String getRate() {
        return rate;
    }

    public String getOwnership() {
        return ownership;
    }

    public String getPurch_grant_dono() {
        return purch_grant_dono;
    }

    public String getAgency() {
        return agency;
    }

    public String getAgency_specify() {
        return agency_specify;
    }

    public String getName_owner() {
        return name_owner;
    }

    public String getYear_acquired() {
        return year_acquired;
    }

    public String getCondition_acquired() {
        return condition_acquired;
    }

    public String getRental() {
        return rental;
    }

    public String getMain_custom_rent() {
        return main_custom_rent;
    }

    public String getMain_custom_rent_unit() {
        return main_custom_rent_unit;
    }

    public String getMain_custom_rent_unit_specify() {
        return main_custom_rent_unit_specify;
    }

    public String getPlow_custom_rent() {
        return plow_custom_rent;
    }

    public String getPlow_custom_rent_unit() {
        return plow_custom_rent_unit;
    }

    public String getPlow_custom_rent_unit_specify() {
        return plow_custom_rent_unit_specify;
    }

    public String getHarr_custom_rent() {
        return harr_custom_rent;
    }

    public String getHarr_custom_rent_unit() {
        return harr_custom_rent_unit;
    }

    public String getHarr_custom_rent_unit_specify() {
        return harr_custom_rent_unit_specify;
    }

    public String getFurr_custom_rent() {
        return furr_custom_rent;
    }

    public String getFurr_custom_rent_unit() {
        return furr_custom_rent_unit;
    }

    public String getFurr_custom_rent_unit_specify() {
        return furr_custom_rent_unit_specify;
    }

    public String getOther_unit_operation() {
        return other_unit_operation;
    }

    public String getOther_custom_rent() {
        return other_custom_rent;
    }

    public String getOther_custom_rent_unit() {
        return other_custom_rent_unit;
    }

    public String getOther_custom_rent_unit_specify() {
        return other_custom_rent_unit_specify;
    }

    public String getPlow_ave_fuel() {
        return plow_ave_fuel;
    }

    public String getHarr_ave_fuel() {
        return harr_ave_fuel;
    }

    public String getFurr_ave_fuel() {
        return furr_ave_fuel;
    }

    public String getAvailability() {
        return availability;
    }

    public String getRent_prov() {
        return rent_prov;
    }

    public String getRent_mun() {
        return rent_mun;
    }

    public String getRent_brgy() {
        return rent_brgy;
    }

    public String getCondition() {
        return condition;
    }

    public String getProblems() {
        return problems;
    }

    public String getSpecify_problems() {
        return specify_problems;
    }

    public String getYear_inoperable() {
        return year_inoperable;
    }

    public String getLocation() {
        return location;
    }

    public String getProv() {
        return prov;
    }

    public String getMun() {
        return mun;
    }

    public String getBrgy() {
        return brgy;
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

    public String getAccuracy() {
        return accuracy;
    }

    public String getResCode() {
        return resCode;
    }

    public String getResName() {
        return resName;
    }

    //    public void setId(int id) {
//        this.id = id;
//    }
//
//    public Machines(String machine_type, String type_tubewells, String type_mill, String machine_qrcode, String date_of_survey, String machine_brand, String machine_brand_specify, String machine_model, String machine_model_specify, String rated_power, String service_area, String ave_op_hours, String ave_op_days, String capacity, String ave_yield, String num_loads, String rate, String ownership, String purch_grant_dono, String agency, String agency_specify, String name_owner, String year_acquired, String condition_acquired, String rental, String custom_rate, String custom_unit, String specify_custom_unit, String availability, String rent_prov, String rent_mun, String rent_brgy, String condition, String problems, String specify_problems, String location, String prov, String mun, String brgy, String machine_latitude, String machine_longitude, String machine_image_base64, String accuracy, String resCode, String resName) {
//        this.machine_type = machine_type;
//        this.type_tubewells = type_tubewells;
//        this.type_mill = type_mill;
//        this.machine_qrcode = machine_qrcode;
//        this.date_of_survey = date_of_survey;
//        this.machine_brand = machine_brand;
//        this.machine_brand_specify = machine_brand_specify;
//        this.machine_model = machine_model;
//        this.machine_model_specify = machine_model_specify;
//        this.rated_power = rated_power;
//        this.service_area = service_area;
//        this.ave_op_hours = ave_op_hours;
//        this.ave_op_days = ave_op_days;
//        this.capacity = capacity;
//        this.ave_yield = ave_yield;
//        this.num_loads = num_loads;
//        this.rate = rate;
//        this.ownership = ownership;
//        this.purch_grant_dono = purch_grant_dono;
//        this.agency = agency;
//        this.agency_specify = agency_specify;
//        this.name_owner = name_owner;
//        this.year_acquired = year_acquired;
//        this.condition_acquired = condition_acquired;
//        this.rental = rental;
//        this.custom_rate = custom_rate;
//        this.custom_unit = custom_unit;
//        this.specify_custom_unit = specify_custom_unit;
//        this.availability = availability;
//        this.rent_prov = rent_prov;
//        this.rent_mun = rent_mun;
//        this.rent_brgy = rent_brgy;
//        this.condition = condition;
//        this.problems = problems;
//        this.specify_problems = specify_problems;
//        this.location = location;
//        this.prov = prov;
//        this.mun = mun;
//        this.brgy = brgy;
//        this.machine_latitude = machine_latitude;
//        this.machine_longitude = machine_longitude;
//        this.machine_image_base64 = machine_image_base64;
//        this.accuracy = accuracy;
//        this.resCode = resCode;
//        this.resName = resName;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getMachine_type() {
//        return machine_type;
//    }
//
//    public String getType_tubewells() {
//        return type_tubewells;
//    }
//
//    public String getType_mill() {
//        return type_mill;
//    }
//
//    public String getMachine_qrcode() {
//        return machine_qrcode;
//    }
//
//    public String getDate_of_survey() {
//        return date_of_survey;
//    }
//
//    public String getMachine_brand() {
//        return machine_brand;
//    }
//
//    public String getMachine_brand_specify() {
//        return machine_brand_specify;
//    }
//
//    public String getMachine_model() {
//        return machine_model;
//    }
//
//    public String getMachine_model_specify() {
//        return machine_model_specify;
//    }
//
//    public String getRated_power() {
//        return rated_power;
//    }
//
//    public String getService_area() {
//        return service_area;
//    }
//
//    public String getAve_op_hours() {
//        return ave_op_hours;
//    }
//
//    public String getAve_op_days() {
//        return ave_op_days;
//    }
//
//    public String getCapacity() {
//        return capacity;
//    }
//
//    public String getAve_yield() {
//        return ave_yield;
//    }
//
//    public String getNum_loads() {
//        return num_loads;
//    }
//
//    public String getRate() {
//        return rate;
//    }
//
//    public String getOwnership() {
//        return ownership;
//    }
//
//    public String getPurch_grant_dono() {
//        return purch_grant_dono;
//    }
//
//    public String getAgency() {
//        return agency;
//    }
//
//    public String getAgency_specify() {
//        return agency_specify;
//    }
//
//    public String getName_owner() {
//        return name_owner;
//    }
//
//    public String getYear_acquired() {
//        return year_acquired;
//    }
//
//    public String getCondition_acquired() {
//        return condition_acquired;
//    }
//
//    public String getRental() {
//        return rental;
//    }
//
//    public String getCustom_rate() {
//        return custom_rate;
//    }
//
//    public String getCustom_unit() {
//        return custom_unit;
//    }
//
//    public String getSpecify_custom_unit() {
//        return specify_custom_unit;
//    }
//
//    public String getAvailability() {
//        return availability;
//    }
//
//    public String getRent_prov() {
//        return rent_prov;
//    }
//
//    public String getRent_mun() {
//        return rent_mun;
//    }
//
//    public String getRent_brgy() {
//        return rent_brgy;
//    }
//
//    public String getCondition() {
//        return condition;
//    }
//
//    public String getProblems() {
//        return problems;
//    }
//
//    public String getSpecify_problems() {
//        return specify_problems;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public String getProv() {
//        return prov;
//    }
//
//    public String getMun() {
//        return mun;
//    }
//
//    public String getBrgy() {
//        return brgy;
//    }
//
//    public String getMachine_latitude() {
//        return machine_latitude;
//    }
//
//    public String getMachine_longitude() {
//        return machine_longitude;
//    }
//
//    public String getMachine_image_base64() {
//        return machine_image_base64;
//    }
//
//    public String getAccuracy() {
//        return accuracy;
//    }
//
//    public String getResName() {
//        return resName;
//    }
//
//    public String getResCode() {
//        return resCode;
//    }
}
