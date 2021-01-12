package com.example.pgallery;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pgallery.Models.Example;
import com.example.pgallery.Models.Photo;
import com.squareup.picasso.Picasso;
import java.util.List;
import retrofit2.Callback;

import static android.widget.Toast.LENGTH_SHORT;

public class PhotoAdapter extends RecyclerView.Adapter <PhotoAdapter.ViewHolder> {
    private final Callback<Example> photoGallery;
    private final List<Photo> values;
    private OnInsertListener onInsertListener;

    PhotoAdapter(Callback<Example> parent, List<Photo> items) {
        photoGallery = parent;
        values = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String s;
        s = "https://farm" + Integer.toString(values.get(position).getFarm()) + ".staticflickr.com/" +
                values.get(position).getServer() + "/" + values.get(position).getId() +
                "_" + values.get(position).getSecret() + "_q.jpg";
        Picasso.get().load(s).into(holder.image);
        holder.itemView.setTag(values.get(position));
        holder.title.setText(values.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public interface OnInsertListener {
        void onInsert(Photo photo);
    }

    public void setOnInsertListener(OnInsertListener onInsertListener) {
        this.onInsertListener = onInsertListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView title;

        ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_photo);
            image = view.findViewById(R.id.iv);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("HORSE", String.valueOf(values.get(ViewHolder.this.getAdapterPosition()) == null));
                    onInsertListener.onInsert(values.get(ViewHolder.this.getAdapterPosition()));
                    Toast toast = Toast.makeText(view.getContext(), "Added to BD", LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }
}