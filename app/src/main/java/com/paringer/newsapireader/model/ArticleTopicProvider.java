package com.paringer.newsapireader.model;

import com.activeandroid.query.Select;
import com.paringer.newsapireader.model.dao.ArticleTopic;

import java.util.List;

/**
 * Created by Zhenya on 02.06.2017.
 */

public class ArticleTopicProvider {
    public static ArticleTopic getByUrl(String source, String url) {
        return new Select()
                .from(ArticleTopic.class)
//                .where("source = ? AND url = ?", source, url)
                .where("url = ?", url)
                .executeSingle();
    }
    public static List<ArticleTopic> getAll(String source) {
        return new Select()
                .from(ArticleTopic.class)
                .where("source = ?", source)
                .execute();
    }
    public static List<ArticleTopic> getAllFavorites(String source) {
        return new Select()
                .from(ArticleTopic.class)
                .where("source = ? AND favorite = ?", source, Boolean.TRUE)
                .execute();
    }
}
