package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by katsiarynamashokha on 7/28/16.
 */
public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);

            holder = new ViewHolder();
            holder.topicTextView = (TextView) convertView.findViewById(R.id.topic);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.title);
            holder.authorTextView = (TextView) convertView.findViewById(R.id.author_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        News currentNews = getItem(position);
        holder.topicTextView.setText(currentNews.getTopic());
        holder.titleTextView.setText(currentNews.getTitle());
        holder.authorTextView.setText(currentNews.getFullName());


        return convertView;
    }

    static class ViewHolder {
        private TextView topicTextView;
        private TextView titleTextView;
        private TextView authorTextView;
    }
}

