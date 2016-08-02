package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = NewsActivity.class.getName();

    private static final String SCHEME = "http";
    private static final String NEWS_BASE_URL = "content.guardianapis.com";
    private static final String PATH = "search";
    private static final String QUERY_PARAM = "q";
    private static final String DATE_PARAM = "from-date";
    private static final String API = "api-key";
    private static final String QUERY_PARAM_TAG = "show-tags";
    private static final int NEWS_LOADER_ID = 0;
    private static String GUARDIAN_REQUEST_URL = getStringUrl();
    SwipeRefreshLayout mSwipeRefreshLayout;
    private NewsAdapter mAdapter;
    private TextView emptyStateTextView;

    private static String getStringUrl() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(NEWS_BASE_URL)
                .appendPath(PATH)
                .appendQueryParameter(QUERY_PARAM, "sports")
                .appendQueryParameter(DATE_PARAM, todayDate())
                .appendQueryParameter(API, "test")
                .appendQueryParameter(QUERY_PARAM_TAG, "contributor");
        String myUrl = builder.build().toString();
        return myUrl;
    }

    private static String todayDate() {
        Calendar currentDate = Calendar.getInstance();
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate.getTime());
        return formattedDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        ListView newsListView = (ListView) findViewById(R.id.list);
        emptyStateTextView = (TextView) findViewById(R.id.empty_text);
        newsListView.setEmptyView(emptyStateTextView);


        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsListView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                startLoader();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
        startLoader();
    }

    private void startLoader() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

            Log.v(LOG_TAG, "initLoader has been called");
        } else {
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                startLoader();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "onCreateLoader is called");
        return new NewsLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);
        Log.v(LOG_TAG, "onLoadFinished has been called");
        emptyStateTextView.setText(R.string.no_news);
        mAdapter.clear();

        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.v(LOG_TAG, "onLoaderReset has been called");
        mAdapter.clear();
    }
}
