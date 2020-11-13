package com.m3das.biomech.design.uploadtoserver;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {


    private static final String BASE_URL = "http://192.168.1.4/m3dastest/";
    private static RetroClient myClient;
    private Retrofit retrofit;

    private RetroClient(){

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();



    }

    public static synchronized RetroClient getInstance(){

        if (myClient == null){
            myClient = new RetroClient();

        }
        return myClient;
    }

//    public Api getMachinesApi(){
//        return retrofit.create(MachinesApi.class);
//    }

    public MachinesApi getMachinesApi(){return  retrofit.create(MachinesApi.class);}
}
