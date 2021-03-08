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
            @Field("dateSurveyPost") String dateSurvey,
            @Field("machineBrandPost") String machineBrand,
            @Field("machineBrandSpecifyPost") String machineBrandSpecify,
            @Field("machineModelPost") String machineModel,
            @Field("machineModelSpecifyPost") String machineModelSpecify,

            @Field("ratedPowerPost") String ratedPower,
            @Field("serviceAreaPost") String serviceArea,
            @Field("newlyPlantedAreaPost") String newlyPlantedArea,
            @Field("ratoonedAreaPost") String ratoonedArea,
            @Field("averageOperatingHoursPost") String aveOpHours,
            @Field("averageOperatingDaysPost") String aveOpDays,
            @Field("effectiveAreaPost") String effArea,
            @Field("timeUsedPost") String timeUsed,
            @Field("capacityPost") String capacity,
            @Field("averageYieldPost") String averageYield,
//            @Field("numberOfLoadsPost") String numberLoads,
            @Field("ratePost") String rate,
            @Field("waterPumpUnitPost") String waterpumpUnit,

            @Field("ownershipPost") String ownership,
            @Field("purchGrantDonoPost") String purchGrantDono,
            @Field("agencyPost") String agency,
            @Field("agencySpecifyPost") String agencySpecify,
            @Field("nameOfOwnerOrganizationPost") String nameOwnerOraganization,
            @Field("yearAcquiredPost") String yearAcquired,
            @Field("conditionAcquiredPost") String conditionAcquired,
            @Field("rentalPost") String rental,

            @Field("mainRentPost") String mainRent,
            @Field("mainRentUnitPost") String mainRentUnit,
            @Field("mainRentUnitSpecifyPost") String mainRentUnitSpecify,
            @Field("mainAveFuelPost") String mainAveFuel,

            @Field("plowRentPost") String plowRent,
            @Field("plowRentUnitPost") String plowRentUnit,
            @Field("plowRentUnitSpecifyPost") String plowRentUnitSpecify,

            @Field("harrRentPost") String harrRent,
            @Field("harrRentUnitPost") String harrRentUnit,
            @Field("harrRentUnitSpecifyPost") String harrRentUnitSpecify,

            @Field("furrRentPost") String furrRent,
            @Field("furrRentUnitPost") String furrRentUnit,
            @Field("furrRentUnitSpecifyPost") String furrRentUnitSpecify,

            @Field("otherRentOperationPost") String otherRentOperation,
            @Field("otherRentPost") String otherRent,
            @Field("otherRentUnitPost") String otherRentUnit,
            @Field("otherRentUnitSpecifyPost") String otherRentUnitSpecify,

            @Field("plowAveFuelPost") String plowAveFuel,
            @Field("harrAveFuelPost") String harrAveFuel,
            @Field("furrAveFuelPost") String furrAveFuel,

            @Field("availabilityPost") String availability,
            @Field("rentProvincePost") String rentProv,
            @Field("rentMunicipalityPost") String rentMun,
            @Field("rentBarangayPost") String rentBrgy,
            @Field("conditionPost") String condition,
            @Field("problemsPost") String problems,
            @Field("problemsSpecifyPost") String problemsSpecify,

            @Field("yearInoperablePost") String yearInoperable,

            @Field("locationPost") String location,
            @Field("provincePost") String prov,
            @Field("municipalityPost") String mun,
            @Field("barangayPost") String brgy,
            @Field("latitudePost") String latitude,
            @Field("longitudePost") String longitude,
            @Field("imageBase64Post") String imagebase64,
            @Field("accuracyPost") String accuracy,
            @Field("respondentCodePost") String resCode,
            @Field("respondentNamePost") String resName,
            @Field("enumeratorCodePost") String enumCode
    );

}
