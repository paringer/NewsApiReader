package com.paringer.newsapireader.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.paringer.newsapireader.R;
import com.paringer.newsapireader.model.ArticleTopicProvider;
import com.paringer.newsapireader.model.dao.ArticleTopic;
import com.paringer.newsapireader.model.retrofit.NewsFeed;
import com.paringer.newsapireader.model.retrofit.Sources;
import com.paringer.newsapireader.view.interfaces.AdapterClickListener;
import com.paringer.newsapireader.viewmodel.MyAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.paringer.newsapireader.view.NewsDetailFragment.ARG_TOPIC_LINK;

public class TopicsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener, Callback<NewsFeed.HotTopicsList> {

    private Unbinder unbinder;
    @BindView(R.id.fab)
    protected FloatingActionButton fab;
    @BindView(R.id.nav_view)
    protected NavigationView navigationView;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.articlesTopicsList)
    protected RecyclerView articlesTopicsList;

    private String currentSource = Sources.BBC_NEWS;
    private boolean favouritesOnly = false;
    private boolean initedSwipeDismiss = false;
    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standalone_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        unbinder = ButterKnife.bind(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        articlesTopicsList.setLayoutManager(new LinearLayoutManager(this));
        reloadArticleTopicList();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if(drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START);
            else
                drawer.openDrawer(GravityCompat.START);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        RecyclerView.Adapter oldAdapter = articlesTopicsList.getAdapter();
        if(oldAdapter instanceof MyAdapter) ((MyAdapter)oldAdapter).setOnClickListener(null);
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        super.onDestroy();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ars_technica) {
            currentSource = Sources.ARS_TECHNICA;
            onRefresh();
        } else if (id == R.id.nav_bbc_news) {
            currentSource = Sources.BBC_NEWS;
            onRefresh();
        } else if (id == R.id.nav_bbc_sport) {
            currentSource = Sources.BBC_SPORT;
            onRefresh();
        } else if (id == R.id.nav_tech_radar) {
            currentSource = Sources.TECH_RADAR;
            onRefresh();

        } else if (id == R.id.nav_favorites) {
            favouritesOnly = !favouritesOnly;
            item.setChecked(favouritesOnly);
            reloadArticleTopicList();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        refreshAsync();
    }

    protected void refreshAsync(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Sources.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<NewsFeed.HotTopicsList> hotTopicsListCall = retrofit
                .create(NewsFeed.class)
                .getNewsFeed(currentSource, Sources.SORT_BY, Sources.API_KEY);
        hotTopicsListCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<NewsFeed.HotTopicsList> call, Response<NewsFeed.HotTopicsList> response) {
        NewsFeed.HotTopicsList feed = response.body();
        if(feed.articles != null){
            for(NewsFeed.HotTopicsList.Article article : feed.articles){
                if(ArticleTopicProvider.getByUrl(currentSource, article.url) == null)
                    new ArticleTopic(article, feed.source, false, false).save();
            }
        }

        swipeRefreshLayout.setRefreshing(false);
        reloadArticleTopicList();
    }

    @Override
    public void onFailure(Call<NewsFeed.HotTopicsList> call, Throwable t) {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void reloadArticleTopicList() {
        RecyclerView.Adapter oldAdapter = articlesTopicsList.getAdapter();
        if(oldAdapter instanceof MyAdapter) ((MyAdapter)oldAdapter).setOnClickListener(null);
        MyAdapter adapter;
        if(favouritesOnly) {
            adapter = new MyAdapter(ArticleTopicProvider.getAllFavorites(currentSource), this);
            articlesTopicsList.setAdapter(adapter);
        }
        else {
            adapter = new MyAdapter(ArticleTopicProvider.getAll(currentSource), this);
            articlesTopicsList.setAdapter(adapter);
        }
        articlesTopicsList.invalidate();
        fab.invalidate();
        adapter.setOnClickListener(new AdapterClickListener<ArticleTopic>() {
            @Override
            public void onItemClick(int which, ArticleTopic data, MyAdapter.ViewHolder v) {
                if(data == null) return;
                NewsDetailFragment fragment = new NewsDetailFragment();
                Bundle args = new Bundle();
                args.putString(ARG_TOPIC_LINK, data.getUrl());
                fragment.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(NewsDetailFragment.class.getName())
                        .replace(R.id.news_detail_container, fragment, NewsDetailFragment.class.getName())
                        .commitAllowingStateLoss();
            }
        });
        adapter.setFavoritesOnly(favouritesOnly);
        if(!initedSwipeDismiss){adapter.initSwipeToDismiss(articlesTopicsList); initedSwipeDismiss = true;}
    }


}
