package com.m3das.biomech.design.implementdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "implement")
public class Implements {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String implement_type;
    private final String implement_qrcode;
    private final String date_of_survey;
    private final String used_on_machine;
    private final String used_on_machine_complete;
    private final String land_clearing;
    private final String pre_planting;
    private final String planting;
    private final String fertilizer_application;
    private final String pesticide_application;
    private final String irrigation_drainage;
    private final String cultivation;
    private final String ratooning;
    private final String harvest;
    private final String post_harvest;
    private final String hauling;

    private final String total_service_area_main;
    private final String average_operating_hours_main;
    private final String average_operating_days_main;
    private final String effective_area_accomplished_main;
    private final String time_used_during_operation_main;
    private final String field_capacity_main;

    private final String type_of_planter;
    private final String number_of_rows_planter;
    private final String distance_of_materials_planter;
    private final String total_service_area_planter;
    private final String average_operating_hours_planter;
    private final String average_operating_days_planter;
    private final String effective_area_accomplished_planter;
    private final String time_used_during_operation_planter;
    private final String field_capacity_planter;

    private final String total_service_area_fertilizer;
    private final String average_operating_hours_fertilizer;
    private final String average_operating_days_fertilizer;
    private final String effective_area_accomplished_fertilizer;
    private final String time_used_during_operation_fertilizer;
    private final String field_capacity_fertilizer;
    private  final String weight_fertilizer;
    private final String delivery_rate_fetilizer;

    private final String total_service_area_harvester;
    private final String average_operating_hours_harvester;
    private final String average_operating_days_harvester;
    private final String effective_area_accomplished_harvester;
    private final String time_used_during_operation_harvester;
    private final String field_capacity_harvester;
    private final String average_yield_harvester;

    private final String total_service_area_cane_grab_loader;
    private final String average_operating_hours_cane_grab_loader;
    private final String average_operating_days_cane_grab_loader;
    private final String loading_capacity_cane_grab_loader;
    private final String number_loads_cane_grab_loader;

    private final String total_service_area_ditcher;
    private final String average_operating_hours_ditcher;
    private final String average_operating_days_ditcher;
    private final String depth_cut_ditcher;

    private final String year_acquired;
    private final String condition;
    private final String location;
    private final String province;
    private final String city;
    private final String barangay;
    private final String image_base64;
    private final String latitude;
    private final String longitude;
    private final String accuracy;

    public Implements(String implement_type, String implement_qrcode, String date_of_survey, String used_on_machine, String used_on_machine_complete, String land_clearing, String pre_planting, String planting, String fertilizer_application, String pesticide_application, String irrigation_drainage, String cultivation, String ratooning, String harvest, String post_harvest, String hauling, String total_service_area_main, String average_operating_hours_main, String average_operating_days_main, String effective_area_accomplished_main, String time_used_during_operation_main, String field_capacity_main, String type_of_planter, String number_of_rows_planter, String distance_of_materials_planter, String total_service_area_planter, String average_operating_hours_planter, String average_operating_days_planter, String effective_area_accomplished_planter, String time_used_during_operation_planter, String field_capacity_planter, String total_service_area_fertilizer, String average_operating_hours_fertilizer, String average_operating_days_fertilizer, String effective_area_accomplished_fertilizer, String time_used_during_operation_fertilizer, String field_capacity_fertilizer, String weight_fertilizer, String delivery_rate_fetilizer, String total_service_area_harvester, String average_operating_hours_harvester, String average_operating_days_harvester, String effective_area_accomplished_harvester, String time_used_during_operation_harvester, String field_capacity_harvester, String average_yield_harvester, String total_service_area_cane_grab_loader, String average_operating_hours_cane_grab_loader, String average_operating_days_cane_grab_loader, String loading_capacity_cane_grab_loader, String number_loads_cane_grab_loader, String total_service_area_ditcher, String average_operating_hours_ditcher, String average_operating_days_ditcher, String depth_cut_ditcher, String year_acquired, String condition, String location, String province, String city, String barangay, String image_base64, String latitude, String longitude, String accuracy) {
        this.implement_type = implement_type;
        this.implement_qrcode = implement_qrcode;
        this.date_of_survey = date_of_survey;
        this.used_on_machine = used_on_machine;
        this.used_on_machine_complete = used_on_machine_complete;
        this.land_clearing = land_clearing;
        this.pre_planting = pre_planting;
        this.planting = planting;
        this.fertilizer_application = fertilizer_application;
        this.pesticide_application = pesticide_application;
        this.irrigation_drainage = irrigation_drainage;
        this.cultivation = cultivation;
        this.ratooning = ratooning;
        this.harvest = harvest;
        this.post_harvest = post_harvest;
        this.hauling = hauling;
        this.total_service_area_main = total_service_area_main;
        this.average_operating_hours_main = average_operating_hours_main;
        this.average_operating_days_main = average_operating_days_main;
        this.effective_area_accomplished_main = effective_area_accomplished_main;
        this.time_used_during_operation_main = time_used_during_operation_main;
        this.field_capacity_main = field_capacity_main;
        this.type_of_planter = type_of_planter;
        this.number_of_rows_planter = number_of_rows_planter;
        this.distance_of_materials_planter = distance_of_materials_planter;
        this.total_service_area_planter = total_service_area_planter;
        this.average_operating_hours_planter = average_operating_hours_planter;
        this.average_operating_days_planter = average_operating_days_planter;
        this.effective_area_accomplished_planter = effective_area_accomplished_planter;
        this.time_used_during_operation_planter = time_used_during_operation_planter;
        this.field_capacity_planter = field_capacity_planter;
        this.total_service_area_fertilizer = total_service_area_fertilizer;
        this.average_operating_hours_fertilizer = average_operating_hours_fertilizer;
        this.average_operating_days_fertilizer = average_operating_days_fertilizer;
        this.effective_area_accomplished_fertilizer = effective_area_accomplished_fertilizer;
        this.time_used_during_operation_fertilizer = time_used_during_operation_fertilizer;
        this.field_capacity_fertilizer = field_capacity_fertilizer;
        this.weight_fertilizer = weight_fertilizer;
        this.delivery_rate_fetilizer = delivery_rate_fetilizer;
        this.total_service_area_harvester = total_service_area_harvester;
        this.average_operating_hours_harvester = average_operating_hours_harvester;
        this.average_operating_days_harvester = average_operating_days_harvester;
        this.effective_area_accomplished_harvester = effective_area_accomplished_harvester;
        this.time_used_during_operation_harvester = time_used_during_operation_harvester;
        this.field_capacity_harvester = field_capacity_harvester;
        this.average_yield_harvester = average_yield_harvester;
        this.total_service_area_cane_grab_loader = total_service_area_cane_grab_loader;
        this.average_operating_hours_cane_grab_loader = average_operating_hours_cane_grab_loader;
        this.average_operating_days_cane_grab_loader = average_operating_days_cane_grab_loader;
        this.loading_capacity_cane_grab_loader = loading_capacity_cane_grab_loader;
        this.number_loads_cane_grab_loader = number_loads_cane_grab_loader;
        this.total_service_area_ditcher = total_service_area_ditcher;
        this.average_operating_hours_ditcher = average_operating_hours_ditcher;
        this.average_operating_days_ditcher = average_operating_days_ditcher;
        this.depth_cut_ditcher = depth_cut_ditcher;
        this.year_acquired = year_acquired;
        this.condition = condition;
        this.location = location;
        this.province = province;
        this.city = city;
        this.barangay = barangay;
        this.image_base64 = image_base64;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getImplement_type() {
        return implement_type;
    }

    public String getImplement_qrcode() {
        return implement_qrcode;
    }

    public String getDate_of_survey() {
        return date_of_survey;
    }

    public String getUsed_on_machine() {
        return used_on_machine;
    }

    public String getUsed_on_machine_complete() {
        return used_on_machine_complete;
    }

    public String getLand_clearing() {
        return land_clearing;
    }

    public String getPre_planting() {
        return pre_planting;
    }

    public String getPlanting() {
        return planting;
    }

    public String getFertilizer_application() {
        return fertilizer_application;
    }

    public String getPesticide_application() {
        return pesticide_application;
    }

    public String getIrrigation_drainage() {
        return irrigation_drainage;
    }

    public String getCultivation() {
        return cultivation;
    }

    public String getRatooning() {
        return ratooning;
    }

    public String getHarvest() {
        return harvest;
    }

    public String getPost_harvest() {
        return post_harvest;
    }

    public String getHauling() {
        return hauling;
    }

    public String getTotal_service_area_main() {
        return total_service_area_main;
    }

    public String getAverage_operating_hours_main() {
        return average_operating_hours_main;
    }

    public String getAverage_operating_days_main() {
        return average_operating_days_main;
    }

    public String getEffective_area_accomplished_main() {
        return effective_area_accomplished_main;
    }

    public String getTime_used_during_operation_main() {
        return time_used_during_operation_main;
    }

    public String getField_capacity_main() {
        return field_capacity_main;
    }

    public String getType_of_planter() {
        return type_of_planter;
    }

    public String getNumber_of_rows_planter() {
        return number_of_rows_planter;
    }

    public String getDistance_of_materials_planter() {
        return distance_of_materials_planter;
    }

    public String getTotal_service_area_planter() {
        return total_service_area_planter;
    }

    public String getAverage_operating_hours_planter() {
        return average_operating_hours_planter;
    }

    public String getAverage_operating_days_planter() {
        return average_operating_days_planter;
    }

    public String getEffective_area_accomplished_planter() {
        return effective_area_accomplished_planter;
    }

    public String getTime_used_during_operation_planter() {
        return time_used_during_operation_planter;
    }

    public String getField_capacity_planter() {
        return field_capacity_planter;
    }

    public String getTotal_service_area_fertilizer() {
        return total_service_area_fertilizer;
    }

    public String getAverage_operating_hours_fertilizer() {
        return average_operating_hours_fertilizer;
    }

    public String getAverage_operating_days_fertilizer() {
        return average_operating_days_fertilizer;
    }

    public String getEffective_area_accomplished_fertilizer() {
        return effective_area_accomplished_fertilizer;
    }

    public String getTime_used_during_operation_fertilizer() {
        return time_used_during_operation_fertilizer;
    }

    public String getField_capacity_fertilizer() {
        return field_capacity_fertilizer;
    }

    public String getWeight_fertilizer() {
        return weight_fertilizer;
    }

    public String getDelivery_rate_fetilizer() {
        return delivery_rate_fetilizer;
    }

    public String getTotal_service_area_harvester() {
        return total_service_area_harvester;
    }

    public String getAverage_operating_hours_harvester() {
        return average_operating_hours_harvester;
    }

    public String getAverage_operating_days_harvester() {
        return average_operating_days_harvester;
    }

    public String getEffective_area_accomplished_harvester() {
        return effective_area_accomplished_harvester;
    }

    public String getTime_used_during_operation_harvester() {
        return time_used_during_operation_harvester;
    }

    public String getField_capacity_harvester() {
        return field_capacity_harvester;
    }

    public String getAverage_yield_harvester() {
        return average_yield_harvester;
    }

    public String getTotal_service_area_cane_grab_loader() {
        return total_service_area_cane_grab_loader;
    }

    public String getAverage_operating_hours_cane_grab_loader() {
        return average_operating_hours_cane_grab_loader;
    }

    public String getAverage_operating_days_cane_grab_loader() {
        return average_operating_days_cane_grab_loader;
    }

    public String getLoading_capacity_cane_grab_loader() {
        return loading_capacity_cane_grab_loader;
    }

    public String getNumber_loads_cane_grab_loader() {
        return number_loads_cane_grab_loader;
    }

    public String getTotal_service_area_ditcher() {
        return total_service_area_ditcher;
    }

    public String getAverage_operating_hours_ditcher() {
        return average_operating_hours_ditcher;
    }

    public String getAverage_operating_days_ditcher() {
        return average_operating_days_ditcher;
    }

    public String getDepth_cut_ditcher() {
        return depth_cut_ditcher;
    }

    public String getYear_acquired() {
        return year_acquired;
    }

    public String getCondition() {
        return condition;
    }

    public String getLocation() {
        return location;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getBarangay() {
        return barangay;
    }

    public String getImage_base64() {
        return image_base64;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAccuracy() {
        return accuracy;
    }

    //    public Implements(String implement_type, String implement_qrcode, String date_of_survey, String used_on_machine, String used_on_machine_complete, String land_clearing, String pre_planting,
//                      String planting, String fertilizer_application, String pesticide_application, String irrigation_drainage, String cultivation, String ratooning, String harvest,
//                      String post_harvest, String hauling, String total_service_area_main, String average_operating_hours_main, String average_operating_days_main, String effective_area_accomplished_main,
//                      String time_used_during_operation_main, String field_capacity_main, String type_of_planter, String number_of_rows_planter, String distance_of_materials_planter,
//                      String total_service_area_planter, String average_operating_hours_planter, String average_operating_days_planter, String effective_area_accomplished_planter,
//                      String time_used_during_operation_planter, String field_capacity_planter, String total_service_area_fertilizer, String average_operating_hours_fertilizer,
//                      String average_operating_days_fertilizer, String effective_area_accomplished_fertilizer, String time_used_during_operation_fertilizer, String field_capacity_fertilizer,
//                      String weight_fertilizer, String delivery_rate_fetilizer, String total_service_area_harvester, String average_operating_hours_harvester, String average_operating_days_harvester,
//                      String effective_area_accomplished_harvester, String time_used_during_operation_harvester, String field_capacity_harvester, String average_yield_harvester, String total_service_area_cane_grab_loader,
//                      String average_operating_hours_cane_grab_loader, String average_operating_days_cane_grab_loader, String loading_capacity_cane_grab_loader,
//                      String number_loads_cane_grab_loader, String total_service_area_ditcher, String average_operating_hours_ditcher, String average_operating_days_ditcher,
//                      String depth_cut_ditcher, String year_acquired, String condition, String location, String province, String city, String barangay, String image_base64, String latitude, String longitude, String accuracy) {
//        this.implement_type = implement_type;
//        this.implement_qrcode = implement_qrcode;
//        this.date_of_survey = date_of_survey;
//        this.used_on_machine = used_on_machine;
//        this.used_on_machine_complete = used_on_machine_complete;
//        this.land_clearing = land_clearing;
//        this.pre_planting = pre_planting;
//        this.planting = planting;
//        this.fertilizer_application = fertilizer_application;
//        this.pesticide_application = pesticide_application;
//        this.irrigation_drainage = irrigation_drainage;
//        this.cultivation = cultivation;
//        this.ratooning = ratooning;
//        this.harvest = harvest;
//        this.post_harvest = post_harvest;
//        this.hauling = hauling;
//        this.total_service_area_main = total_service_area_main;
//        this.average_operating_hours_main = average_operating_hours_main;
//        this.average_operating_days_main = average_operating_days_main;
//        this.effective_area_accomplished_main = effective_area_accomplished_main;
//        this.time_used_during_operation_main = time_used_during_operation_main;
//        this.field_capacity_main = field_capacity_main;
//        this.type_of_planter = type_of_planter;
//        this.number_of_rows_planter = number_of_rows_planter;
//        this.distance_of_materials_planter = distance_of_materials_planter;
//        this.total_service_area_planter = total_service_area_planter;
//        this.average_operating_hours_planter = average_operating_hours_planter;
//        this.average_operating_days_planter = average_operating_days_planter;
//        this.effective_area_accomplished_planter = effective_area_accomplished_planter;
//        this.time_used_during_operation_planter = time_used_during_operation_planter;
//        this.field_capacity_planter = field_capacity_planter;
//        this.total_service_area_fertilizer = total_service_area_fertilizer;
//        this.average_operating_hours_fertilizer = average_operating_hours_fertilizer;
//        this.average_operating_days_fertilizer = average_operating_days_fertilizer;
//        this.effective_area_accomplished_fertilizer = effective_area_accomplished_fertilizer;
//        this.time_used_during_operation_fertilizer = time_used_during_operation_fertilizer;
//        this.field_capacity_fertilizer = field_capacity_fertilizer;
//        this.weight_fertilizer = weight_fertilizer;
//        this.delivery_rate_fetilizer = delivery_rate_fetilizer;
//        this.total_service_area_harvester = total_service_area_harvester;
//        this.average_operating_hours_harvester = average_operating_hours_harvester;
//        this.average_operating_days_harvester = average_operating_days_harvester;
//        this.effective_area_accomplished_harvester = effective_area_accomplished_harvester;
//        this.time_used_during_operation_harvester = time_used_during_operation_harvester;
//        this.field_capacity_harvester = field_capacity_harvester;
//        this.average_yield_harvester = average_yield_harvester;
//        this.total_service_area_cane_grab_loader = total_service_area_cane_grab_loader;
//        this.average_operating_hours_cane_grab_loader = average_operating_hours_cane_grab_loader;
//        this.average_operating_days_cane_grab_loader = average_operating_days_cane_grab_loader;
//        this.loading_capacity_cane_grab_loader = loading_capacity_cane_grab_loader;
//        this.number_loads_cane_grab_loader = number_loads_cane_grab_loader;
//        this.total_service_area_ditcher = total_service_area_ditcher;
//        this.average_operating_hours_ditcher = average_operating_hours_ditcher;
//        this.average_operating_days_ditcher = average_operating_days_ditcher;
//        this.depth_cut_ditcher = depth_cut_ditcher;
//        this.year_acquired = year_acquired;
//        this.condition = condition;
//        this.location = location;
//        this.province = province;
//        this.city = city;
//        this.barangay = barangay;
//        this.image_base64 = image_base64;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.accuracy = accuracy;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getImplement_type() {
//        return implement_type;
//    }
//
//    public String getImplement_qrcode() {
//        return implement_qrcode;
//    }
//
//    public String getDate_of_survey() {
//        return date_of_survey;
//    }
//
//    public String getUsed_on_machine() {
//        return used_on_machine;
//    }
//
//    public String getUsed_on_machine_complete() {
//        return used_on_machine_complete;
//    }
//
//    public String getLand_clearing() {
//        return land_clearing;
//    }
//
//    public String getPre_planting() {
//        return pre_planting;
//    }
//
//    public String getPlanting() {
//        return planting;
//    }
//
//    public String getFertilizer_application() {
//        return fertilizer_application;
//    }
//
//    public String getPesticide_application() {
//        return pesticide_application;
//    }
//
//    public String getIrrigation_drainage() {
//        return irrigation_drainage;
//    }
//
//    public String getCultivation() {
//        return cultivation;
//    }
//
//    public String getRatooning() {
//        return ratooning;
//    }
//
//    public String getHarvest() {
//        return harvest;
//    }
//
//    public String getPost_harvest() {
//        return post_harvest;
//    }
//
//    public String getHauling() {
//        return hauling;
//    }
//
//    public String getTotal_service_area_main() {
//        return total_service_area_main;
//    }
//
//    public String getAverage_operating_hours_main() {
//        return average_operating_hours_main;
//    }
//
//    public String getAverage_operating_days_main() {
//        return average_operating_days_main;
//    }
//
//    public String getEffective_area_accomplished_main() {
//        return effective_area_accomplished_main;
//    }
//
//    public String getTime_used_during_operation_main() {
//        return time_used_during_operation_main;
//    }
//
//    public String getField_capacity_main() {
//        return field_capacity_main;
//    }
//
//    public String getType_of_planter() {
//        return type_of_planter;
//    }
//
//    public String getNumber_of_rows_planter() {
//        return number_of_rows_planter;
//    }
//
//    public String getDistance_of_materials_planter() {
//        return distance_of_materials_planter;
//    }
//
//    public String getTotal_service_area_planter() {
//        return total_service_area_planter;
//    }
//
//    public String getAverage_operating_hours_planter() {
//        return average_operating_hours_planter;
//    }
//
//    public String getAverage_operating_days_planter() {
//        return average_operating_days_planter;
//    }
//
//    public String getEffective_area_accomplished_planter() {
//        return effective_area_accomplished_planter;
//    }
//
//    public String getTime_used_during_operation_planter() {
//        return time_used_during_operation_planter;
//    }
//
//    public String getField_capacity_planter() {
//        return field_capacity_planter;
//    }
//
//    public String getTotal_service_area_fertilizer() {
//        return total_service_area_fertilizer;
//    }
//
//    public String getAverage_operating_hours_fertilizer() {
//        return average_operating_hours_fertilizer;
//    }
//
//    public String getAverage_operating_days_fertilizer() {
//        return average_operating_days_fertilizer;
//    }
//
//    public String getEffective_area_accomplished_fertilizer() {
//        return effective_area_accomplished_fertilizer;
//    }
//
//    public String getTime_used_during_operation_fertilizer() {
//        return time_used_during_operation_fertilizer;
//    }
//
//    public String getField_capacity_fertilizer() {
//        return field_capacity_fertilizer;
//    }
//
//    public String getWeight_fertilizer() {
//        return weight_fertilizer;
//    }
//
//    public String getDelivery_rate_fetilizer() {
//        return delivery_rate_fetilizer;
//    }
//
//    public String getTotal_service_area_harvester() {
//        return total_service_area_harvester;
//    }
//
//    public String getAverage_operating_hours_harvester() {
//        return average_operating_hours_harvester;
//    }
//
//    public String getAverage_operating_days_harvester() {
//        return average_operating_days_harvester;
//    }
//
//    public String getEffective_area_accomplished_harvester() {
//        return effective_area_accomplished_harvester;
//    }
//
//    public String getTime_used_during_operation_harvester() {
//        return time_used_during_operation_harvester;
//    }
//
//    public String getField_capacity_harvester() {
//        return field_capacity_harvester;
//    }
//
//    public String getAverage_yield_harvester() {
//        return average_yield_harvester;
//    }
//
//    public String getTotal_service_area_cane_grab_loader() {
//        return total_service_area_cane_grab_loader;
//    }
//
//    public String getAverage_operating_hours_cane_grab_loader() {
//        return average_operating_hours_cane_grab_loader;
//    }
//
//    public String getAverage_operating_days_cane_grab_loader() {
//        return average_operating_days_cane_grab_loader;
//    }
//
//    public String getLoading_capacity_cane_grab_loader() {
//        return loading_capacity_cane_grab_loader;
//    }
//
//    public String getNumber_loads_cane_grab_loader() {
//        return number_loads_cane_grab_loader;
//    }
//
//    public String getTotal_service_area_ditcher() {
//        return total_service_area_ditcher;
//    }
//
//    public String getAverage_operating_hours_ditcher() {
//        return average_operating_hours_ditcher;
//    }
//
//    public String getAverage_operating_days_ditcher() {
//        return average_operating_days_ditcher;
//    }
//
//    public String getDepth_cut_ditcher() {
//        return depth_cut_ditcher;
//    }
//
//    public String getYear_acquired() {
//        return year_acquired;
//    }
//
//    public String getCondition() {
//        return condition;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public String getProvince() {
//        return province;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public String getBarangay() {
//        return barangay;
//    }
//
//    public String getImage_base64() {
//        return image_base64;
//    }
//
//    public String getLatitude() {
//        return latitude;
//    }
//
//    public String getLongitude() {
//        return longitude;
//    }
//
//    public String getAccuracy() {
//        return accuracy;
//    }
}
