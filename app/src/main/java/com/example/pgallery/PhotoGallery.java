package com.example.pgallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pgallery.Models.Example;
import com.example.pgallery.Models.Photo;
import com.example.pgallery.api.FlickrAPI;
import com.example.pgallery.api.ServiceAPI;
import com.example.pgallery.db.PhotosDB;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotoGallery extends AppCompatActivity implements PhotoAdapter.OnInsertListener {

    private PhotosDB db;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = PhotosDB.getDatabase(getApplicationContext());
        button = (Button) findViewById(R.id.button);

        final RecyclerView rv = findViewById(R.id.rv);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoAdapter_db pa = new PhotoAdapter_db(this, db.photoDao().LoadAll());
                rv.setAdapter(pa);
            }
        });


        rv.setLayoutManager(new GridLayoutManager(this, 3));
        Retrofit r = ServiceAPI.getRetrofit();
        r.create(FlickrAPI.class).getRecent().enqueue(new Callback<Example>()
        {

            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                response.body();
                Log.v("b", ((Example) response.body()).getPhotos().getTotal());
                PhotoAdapter pa = new PhotoAdapter(this, response.body().getPhotos().getPhoto());
                pa.setOnInsertListener(PhotoGallery.this);
                rv.setAdapter(pa);
            }
            @Override
            public void onFailure(Call<Example> call, Throwable t) {
            }
        });

    }

    @Override
    public void onInsert(Photo photo) {
        db.photoDao().insertPhoto(photo);
    }
}