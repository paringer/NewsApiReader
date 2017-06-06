package com.paringer.newsapireader.model.retrofit;

/**
 * Created by Zhenya on 01.06.2017.
 */
public interface Sources {
    String SOURCES = "https://newsapi.org/v1/sources?language=en";
    String ARS_TECHNICA_FULL = "https://newsapi.org/v1/articles?source=ars-technica&sortBy=latest&apiKey=9fc8f76440c84d1ba6cfd9a4dcade040";
    String BBC_NEWS_FULL = "https://newsapi.org/v1/articles?source=bbc-news&sortBy=top&apiKey=9fc8f76440c84d1ba6cfd9a4dcade040";
    String BBC_SPORT_FULL = "https://newsapi.org/v1/articles?source=bbc-sport&sortBy=top&apiKey=9fc8f76440c84d1ba6cfd9a4dcade040";
    String TECH_RADAR_FULL = "https://newsapi.org/v1/articles?source=techradar&sortBy=top&apiKey=9fc8f76440c84d1ba6cfd9a4dcade040";
    String HOST0 = "https://newsapi.org/v1/articles/";
    String HOST = "https://newsapi.org/";
    String HOST2 = "v1/articles";
    String API_KEY = "9fc8f76440c84d1ba6cfd9a4dcade040";
    String SORT_BY = "top";
    String ARS_TECHNICA = "ars-technica";
    String BBC_NEWS = "bbc-news";
    String BBC_SPORT = "bbc-sport";
    String TECH_RADAR = "techradar";
    String [] SOURCES_ARR = {ARS_TECHNICA, BBC_NEWS, BBC_SPORT};
}
