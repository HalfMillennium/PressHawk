package com.digitalnode.presshawk;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.digitalnode.presshawk.R;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HeadlineAdapter extends RecyclerView.Adapter<HeadlineAdapter.StoryViewHolder> {
    private Story[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title, source, date, description;
        public ImageView thumb;

        public StoryViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            source = v.findViewById(R.id.source);
            date = v.findViewById(R.id.date);
            description = v.findViewById(R.id.description);
            thumb = v.findViewById(R.id.thumbnail);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HeadlineAdapter(Story[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HeadlineAdapter.StoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.today_headline_layout, parent, false);

        StoryViewHolder vh = new StoryViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(StoryViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(mDataset[position].getTitle());
        holder.description.setText(mDataset[position].getDescription());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd.yyyy");

        Date date1 = new Date();
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(mDataset[position].getPubAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.date.setText(" - " + simpleDateFormat.format(date1));
        holder.source.setText(mDataset[position].getSource());

        Glide.with(holder.thumb.getContext())
                .load(mDataset[position].getImageUrl())
                .centerCrop()
                .into(holder.thumb);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "reddit_thumbnail");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
