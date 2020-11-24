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

            @Field("impTSAMainPost") String implementTSAMain,
            @Field("impAOHMainPost") String implementAveOpHoursMain,
            @Field("impAODMainPost") String implementAveOpDaysMain,
            @Field("impEFFAAMainPost") String implementEFFAAMain,
            @Field("impTUDOpMaintPost") String implementTUDOpMain,
            @Field("impFieldCapMainPost") String implementFieldCapMain,

            @Field("impTypePlantPost") String implementTypePlant,
            @Field("impNumRowsPlantPost") String implementNumRowsPlant,
            @Field("impDistMatPlantPost") String implementDistMatPlant,
            @Field("impTSAPlantPost") String implementTSAPlant,
            @Field("impAOHPlantPost") String implementAveOpHoursPlant,
            @Field("impAODPlantPost") String implementAveOpDaysPlant,
            @Field("impEFFAAPlantPost") String implementEFFAAPlant,
            @Field("impTUDOpPlantPost") String implementTUDOpPlant,
            @Field("impFieldCapPlantPost") String implementFieldCapPlant,

            @Field("impTSAFertPost") String implementTSAFert,
            @Field("impAOHFertPost") String implementAveOpHoursFert,
            @Field("impAODFertPost") String implementAveOpDaysFert,
            @Field("impEFFAAFertPost") String implementEFFAAFert,
            @Field("impTUDOpFertPost") String implementTUDOpFert,
            @Field("impFieldCapFertPost") String implementFieldCapFert,
            @Field("impWeightFertPost") String implementWeightFert,
            @Field("impDelRateFertPost") String implementDelRateFert,

            @Field("impTSAHarvestPost") String implementTSAHarvest,
            @Field("impAOHHarvestPost") String implementAveOpHoursHarvest,
            @Field("impAODHarvestPost") String implementAveOpDaysHarvest,
            @Field("impEFFAAHarvestPost") String implementEFFAAHarvest,
            @Field("impTUDOpHarvestPost") String implementTUDOpHarvest,
            @Field("impFieldCapHarvestPost") String implementFieldCapHarvest,
            @Field("impAveYieldHarvestPost") String implementAveYieldHarvest,

            @Field("impTSAGrabPost") String implementTSAGrab,
            @Field("impAOHGrabPost") String implementAveOpHoursGrab,
            @Field("impAODGrabPost") String implementAveOpDaysGrab,
            @Field("impEFFAAGrabPost") String implementEFFAAGrab,
            @Field("impLoadCapGrabPost") String implementLoadCapGrab,
            @Field("impNumLoadsGrabPost") String implementNumLoadsGrab,

            @Field("impTSADitchPost") String implementTSADitch,
            @Field("impAOHDitchPost") String implementAveOpHoursDitch,
            @Field("impAODDitchPost") String implementAveOpDaysDitch,
            @Field("impDepthCutDitchPost") String implementDepthCutDitch,

            @Field("impYearAcqPost") String implementYearAcq,
            @Field("impConditionPost") String implementCondition,
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