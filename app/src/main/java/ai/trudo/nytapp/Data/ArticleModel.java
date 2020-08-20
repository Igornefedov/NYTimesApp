package ai.trudo.nytapp.Data;


import android.support.annotation.NonNull;

public class ArticleModel {
    public static String IMAGE_URL = "image_url";
    public static String ARTICLE_URL = "article_url";
    public static String TITLE = "title";
    public static String SUBTITLE = "subtitle";

   // private int id;
    private String title;
    private String subTitle; //summary of the article in a few sentences. called 'abstract' in API
    private String imageUrl;
    private String articleUrl;

    public ArticleModel(@NonNull String title, @NonNull String subTitle, @NonNull String imageUrl, @NonNull String articleUrl) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageUrl = imageUrl;
        this.articleUrl = articleUrl;
    }

    ArticleModel(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }
}
