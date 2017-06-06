package com.paringer.newsapireader.model.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zhenya on 01.06.2017.
 */
public interface NewsFeed {

    public static class HotTopicsList {
        public static class Article {
            public String author;
            public String title;
            public String description;
            public String url;
            public String urlToImage;
            public String publishedAt;

        }

        public String status;
        public String source;
        public String sortBy;
        public List<Article> articles;
    }

    @GET(Sources.HOST2)
    Call<HotTopicsList> getNewsFeed(@Query("source") String source, @Query("sortBy") String sortBy, @Query("apiKey") String apiKey);
}
