package com.paringer.newsapireader.model.dao;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.paringer.newsapireader.model.retrofit.NewsFeed;

/**
 * Created by Zhenya on 01.06.2017.
 */
@Table(name = "Article", id = "id")
public class ArticleTopic extends Model {
    @Column(name = "author", index = true)
    String author;
    @Column(name = "title", index = true)
    String title;
    @Column(name = "description")
    String description;
    @Column(name = "url", index = true)
    String url;
    @Column(name = "urlToImage")
    String urlToImage;
    @Column(name = "publishedAt", index = true)
    String publishedAt;
    @Column(name = "source", index = true)
    String source;
    @Column(name = "favorite", index = true)
    int favorite = 0;
    @Column(name = "disabled", index = true)
    int disabled = 0;
//    @Column(name = "status", index = true)
//    String status;
//    @Column(name = "sortBy")
//    String sortBy;


    public ArticleTopic() {}

    public ArticleTopic(NewsFeed.HotTopicsList.Article article, String source, Boolean favorite, Boolean disabled) {
        this.author = article.author;
        this.title = article.title;
        this.description = article.description;
        this.url = article.url;
        this.urlToImage = article.urlToImage;
        this.publishedAt = article.publishedAt;
        this.source = source;
        this.favorite = favorite ? 1 : 0;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean getFavorite() {
        return favorite != 0;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite ? 1 : 0;
    }

    public boolean getDisabled() {
        return disabled != 0;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled ? 1 : 0;
    }
}
