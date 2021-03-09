package com.m3das.biomech.design.uploadtoserver;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ImplementsApi {

    @FormUrlEncoded
    @POST("upload_implements.php")
    Call<ResponsePojo> uploadImplements(
            @Field("impTypePost") String implementType,
            @Field("impQRPost") String implementQR,
            @Field("impDateSurveyPost") String dateSurvey,
            @Field("impUsedOnMachPost") String implementUsedOnMachine,
            @Field("impUsedOnMachCompPost") String implementUsedOnMachineComplete,

            @Field("brandPost") String brand,
            @Field("modelPost") String model,

            @Field("impLandClearPost") String implementLandClearing,
            @Field("impPrePlantPost") String implementPrePlant,
            @Field("impPlantPost") String implementPlanting,
            @Field("impFertAppPost") String implementFertApp,
            @Field("impPestAppPost") String implementPestApp,
            @Field("impIrriDrainPost") String implementIrriDrain,
            @Field("impCultPost") String implementCult,
            @Field("impRatoonPost") String implementRatoon,
            @Field("impHarvestPost") String implementHarvest,
            @Field("impPostHarvestPost") String implementPostHarvest,
            @Field("impHaulPost") String implementHaul,

            @Field("impEFFAAMainPost") String implementEFFAAMain,
            @Field("impTUDOpMaintPost") String implementTUDOpMain,
            @Field("impFieldCapMainPost") String implementFieldCapMain,

            @Field("impTypePlantPost") String implementTypePlant,
            @Field("impNumRowsPlantPost") String implementNumRowsPlant,
            @Field("impDistMatPlantPost") String implementDistMatPlant,
            @Field("impEFFAAPlantPost") String implementEFFAAPlant,
            @Field("impTUDOpPlantPost") String implementTUDOpPlant,
            @Field("impFieldCapPlantPost") String implementFieldCapPlant,


            @Field("impEFFAAFertPost") String implementEFFAAFert,
            @Field("impTUDOpFertPost") String implementTUDOpFert,
            @Field("impFieldCapFertPost") String implementFieldCapFert,
            @Field("impEFFAAFertWeightPost") String implementEFFAAFertWeight,
            @Field("impWeightFertPost") String implementWeightFert,
            @Field("impDelRateFertPost") String implementDelRateFert,


            @Field("impEFFAAHarvestPost") String implementEFFAAHarvest,
            @Field("impTUDOpHarvestPost") String implementTUDOpHarvest,
            @Field("impFieldCapHarvestPost") String implementFieldCapHarvest,
            @Field("impAveYieldHarvestPost") String implementAveYieldHarvest,


            @Field("impEFFAAGrabPost") String implementEFFAAGrab,
            @Field("impTUDOpGrabPost") String implementTUDOpGrab,
            @Field("impLoadCapGrabPost") String implementLoadCapGrab,
            @Field("impFieldCapGrabPost") String implementFieldCapGrab,

            @Field("impDepthCutDitchPost") String implementDepthCutDitch,


            @Field("impOwnershipPost")String ownership,
            @Field("impPurchGrantDonoPost")String purchGrantDono,
            @Field("impAgencyPost")String agency,
            @Field("impAgencySpecifyPost")String agencySpecify,

            @Field("impYearAcqPost") String implementYearAcq,
            @Field("impConditionPost") String implementCondition,

            @Field("impModificationsPost")String modifications,
            @Field("impProblemsPost")String problems,
            @Field("impProblemsSpecifyPost")String problemsSpecify,
            @Field("impYearInoperablePost")String yearInoperable,

            @Field("impLocationPost") String implementLocation,
            @Field("impProvPost") String implementProvince,
            @Field("impMunPost") String implementMunicipality,
            @Field("impBrgyPost") String implementBrgy,
            @Field("impImageBase64Post") String implementImgBase64,
            @Field("impLatPost") String implementLatitude,
            @Field("impLongPost") String implementLongitude,
            @Field("impAccPost") String implementAccuracy,
            @Field("impEnumeratorCodePost")String enumCode

    );

}