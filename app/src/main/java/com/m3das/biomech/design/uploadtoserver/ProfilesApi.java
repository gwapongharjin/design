package com.m3das.biomech.design.uploadtoserver;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ProfilesApi {

    @FormUrlEncoded
    @POST("upload_profiles.php")
    Call<ResponsePojo> uploadProfiles(
            @Field("profResCodePost") String resCode,
            @Field("profProfilePost") String profile,
            @Field("profProfileSPost") String profileSpecify,
            @Field("profOwnerTypePost") String ownerType,
            @Field("profNameRespondentPost") String nameRespondent,
            @Field("profAddressPost") String address,
            @Field("profAgePost") String age,
            @Field("profSexPost") String sex,
            @Field("profContactNumberPost") String contactNumber,
            @Field("profMobNum1Post") String mobNum1,
            @Field("profMobNum2Post") String mobNum2,
            @Field("profTelNum1Post") String telNum1,
            @Field("profTelNum2Post") String telNum2,
            @Field("profEducationalAttainmentPost") String educAttain
            );
}
