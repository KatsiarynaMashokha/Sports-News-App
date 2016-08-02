package com.example.android.newsapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by katsiarynamashokha on 7/28/16.
 */
public class News implements Parcelable {
    public static final Parcelable.Creator<News> CREATOR
            = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
    private String topic;
    private String title;
    private String authorName;
    private String url;

    public News(String topic, String title, String authorName, String url) {
        this.topic = topic;
        this.title = title;
        this.authorName = authorName;
        this.url = url;
    }

    private News(Parcel in) {
        topic = in.readString();
        title = in.readString();
        authorName = in.readString();
        url = in.readString();
    }

    public String getTopic() {
        return topic;
    }

    public String getTitle() {
        return title;
    }

    public String getFullName() {
        return authorName;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(topic);
        dest.writeString(title);
        dest.writeString(authorName);
        dest.writeString(url);

    }
}

