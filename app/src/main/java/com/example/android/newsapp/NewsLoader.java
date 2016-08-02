package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by katsiarynamashokha on 7/28/16.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private static final String LOG_TAG = NewsLoader.class.getName();
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v(LOG_TAG, "onStartLoading is called");
    }

    @Override
    public List<News> loadInBackground() {
        Log.v(LOG_TAG, "onLoadInBackground is called");
        if (mUrl == null) {
            return null;
        }
        List<News> news = QueryUtils.fetchNewsData(mUrl);
        Log.v(LOG_TAG, "Loader performed the network request");
        return news;
    }
}
