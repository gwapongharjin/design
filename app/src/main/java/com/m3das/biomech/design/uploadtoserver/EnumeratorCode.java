package com.m3das.biomech.design.uploadtoserver;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface EnumeratorCode {

    @FormUrlEncoded
    @POST("enumerator_check.php")
    Call<ResponsePojo> checkEnumerator(
            @Field("enumCodePost") String enumCode
    );
}
