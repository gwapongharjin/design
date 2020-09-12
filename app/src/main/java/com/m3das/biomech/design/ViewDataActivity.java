package com.m3das.biomech.design;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_data_activity);

        TextView tvResult = findViewById(R.id.tvResultVD);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/test/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    tvResult.setText("Code: "+ response.code());
                    return;
                }
                List<Post> posts = response.body();

                for (Post post:posts) {
                    String content = "";
                    content += "QRCODE: "+ post.getQRCODE() + "\n";
                    content += "Age: "+ post.getAge() + "\n";
                    content += "Barangay: "+ post.getBrgy() + "\n";
                    content += "First Name: "+ post.getFirstName() + "\n\n";

                    tvResult.append(content);
                }


            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                tvResult.setText(t.getMessage());
            }
        });

    }
}