package com.m3das.biomech.design;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("select.php")
    Call<List<Post>> getPosts();
}
