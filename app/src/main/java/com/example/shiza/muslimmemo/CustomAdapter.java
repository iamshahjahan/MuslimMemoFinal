package com.example.shiza.muslimmemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Shiza on 05-08-2015.
 */
public class CustomAdapter extends BaseAdapter {

    Context context;
    Wrapper_custom wrapper_custom = new Wrapper_custom();
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context activity, MainActivity.Wrapper wrapper) {
        context = activity;

        Log.d("Custom", "Heading is here: " + wrapper.heading);
        wrapper_custom.heading.add(wrapper.heading);
        wrapper_custom.category.add(wrapper.category);
        wrapper_custom.author.add(wrapper.author);
        wrapper_custom.published.add(wrapper.published);
        wrapper_custom.headingSummary.add(wrapper.headingSummary);
        wrapper_custom.bitmaps.add(wrapper.bitmap);
        wrapper_custom.headingLinks.add(wrapper.headingLink);
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return wrapper_custom.heading.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        ImageView imageView;
        TextView heading;
        TextView content;
        TextView author;
        TextView published;
        TextView category;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("tag", "I am in getView.");

        View rowView;
        rowView = inflater.inflate(R.layout.single_row, null);

        Holder holder = new Holder();


        holder.heading = (TextView) rowView.findViewById(R.id.title);
        holder.content = (TextView) rowView.findViewById(R.id.rating);
        holder.category = (TextView) rowView.findViewById(R.id.genre);
        holder.published = (TextView) rowView.findViewById(R.id.releaseYear);
        holder.imageView = (ImageView) rowView.findViewById(R.id.thumbnail);
        holder.author = (TextView) rowView.findViewById(R.id.author);

        holder.heading.setText(wrapper_custom.heading.get(position));
        holder.content.setText(wrapper_custom.headingSummary.get(position));
        holder.category.setText(wrapper_custom.category.get(position));
        holder.published.setText(wrapper_custom.published.get(position));
        holder.imageView.setImageBitmap(wrapper_custom.bitmaps.get(position));
        holder.author.setText(wrapper_custom.author.get(position));

        return rowView;
    }

    public Bitmap getImage(String url,Context context) {
        try {
            Document doc = Jsoup.connect(url).get();
            // Using Elements to get the class data
            Element img = doc.select("header.site-header").first();
            // Locate the src attribute
            String temp = img.getElementsByAttribute("style")
                    .toString();
            // URL of image
            String imageStrg = temp
                    .substring(temp.indexOf("(") + 1, temp.indexOf(")"));
            // Download image from URL
            InputStream input = new java.net.URL(imageStrg).openStream();
            // Decode Bitmap

            Bitmap bit = BitmapFactory.decodeStream(input);
            return Bitmap.createScaledBitmap(bit, 100, 100, true);

        } catch (IOException e) {
            e.printStackTrace();
            return BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.bg);

        }

    }

    public void addItem(MainActivity.Wrapper w) {
        Log.d("addItem", "I am in additem.");

        wrapper_custom.heading.add(w.heading);
        wrapper_custom.category.add(w.category);
        wrapper_custom.author.add(w.author);
        wrapper_custom.published.add(w.published);
        wrapper_custom.headingSummary.add(w.headingSummary);
        wrapper_custom.bitmaps.add(w.bitmap);
        wrapper_custom.headingLinks.add(w.headingLink);
        notifyDataSetChanged();
    }

    public class Wrapper_custom {
        ArrayList<String> category = new ArrayList<>();
        ArrayList<String> heading = new ArrayList<>();
        ArrayList<String> headingLinks = new ArrayList<>();
        ArrayList<String> headingSummary = new ArrayList<>();
        ArrayList<String> author = new ArrayList<>();
        ArrayList<String> published = new ArrayList<>();
        ArrayList<Bitmap> bitmaps = new ArrayList<>();

    }
}
