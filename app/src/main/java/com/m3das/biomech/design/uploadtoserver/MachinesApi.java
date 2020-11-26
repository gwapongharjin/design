package com.m3das.biomech.design.uploadtoserver;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MachinesApi {
    @FormUrlEncoded
    @POST("upload_machines.php")
    Call<ResponsePojo> uploadMachines(
            @Field("machineTypePost") String machineType,
            @Field("typeTubewellsPost") String typeTubewells,
            @Field("typeMillPost") String typeMill,
            @Field("machineQRCodePost") String machineQRCode,
            @Field("dateSurveyPost")String dateSurvey,
            @Field("machineBrandPost")String machineBrand,
            @Field("machineBrandSpecifyPost")String machineBrandSpecify,
            @Field("machineModelPost")String machineModel,
            @Field("machineModelSpecifyPost")String machineModelSpecify,
            @Field("ratedPowerPost")String ratedPower,
            @Field("serviceAreaPost")String serviceArea,
            @Field("averageOperatingHoursPost")String aveOpHours,
            @Field("averageOperatingDaysPost")String aveOpDays,
            @Field("capacityPost")String capacity,
            @Field("averageYieldPost")String averageYield,
            @Field("numberOfLoadsPost")String numberLoads,
            @Field("ratePost")String rate,
            @Field("ownershipPost")String ownership,
            @Field("purchGrantDonoPost")String purchGrantDono,
            @Field("agencyPost")String agency,
            @Field("agencySpecifyPost")String agencySpecify,
            @Field("nameOfOwnerOrganizationPost")String nameOwnerOraganization,
            @Field("yearAcquiredPost")String yearAcquired,
            @Field("conditionAcquiredPost")String conditionAcquired,
            @Field("rentalPost")String rental,
            @Field("customRatePost")String customRate,
            @Field("customUnitPost")String customUnit,
            @Field("customUnitSpecifyPost")String customUnitSpecify,
            @Field("availabilityPost")String availability,
            @Field("rentProvincePost")String rentProv,
            @Field("rentMunicipalityPost")String rentMun,
            @Field("rentBarangayPost")String rentBrgy,
            @Field("conditionPost")String condition,
            @Field("problemsPost")String problems,
            @Field("problemsSpecifyPost")String problemsSpecify,
            @Field("locationPost")String location,
            @Field("provincePost")String prov,
            @Field("municipalityPost")String mun,
            @Field("barangayPost")String brgy,
            @Field("latitudePost")String latitude,
            @Field("longitudePost")String longitude,
            @Field("imageBase64Post")String imagebase64,
            @Field("accuracyPost")String accuracy,
            @Field("respondentCodePost")String resCode,
            @Field("respondentNamePost")String resName,
            @Field("enumeratorCodePost")String enumCode
    );

}
