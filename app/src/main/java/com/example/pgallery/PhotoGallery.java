package com.example.pgallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;

import com.example.pgallery.Models.Example;
import com.example.pgallery.api.FlickrAPI;
import com.example.pgallery.api.ServiceAPI;
import com.example.pgallery.db.PhotosDB;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotoGallery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PhotosDB db = Room.databaseBuilder(getApplicationContext(),
                PhotosDB.class, "photos-database").build();

        final RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        Retrofit r = ServiceAPI.getRetrofit();
        r.create(FlickrAPI.class).getRecent().enqueue(new Callback<Example>()
        {

            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                response.body();
                Log.v("b",((Example) response.body()).getPhotos().getTotal());
                rv.setAdapter(new PhotoAdapter(this, response.body().getPhotos().getPhoto()));
            }
            @Override
            public void onFailure(Call<Example> call, Throwable t) {
            }
        });

    }
}