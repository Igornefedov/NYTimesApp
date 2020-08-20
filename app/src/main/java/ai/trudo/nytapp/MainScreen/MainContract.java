package ai.trudo.nytapp.MainScreen;

import android.content.Context;

import java.util.ArrayList;

import ai.trudo.nytapp.BasePresenter;
import ai.trudo.nytapp.BaseView;
import ai.trudo.nytapp.Data.ArticleModel;

class MainContract {
    //This interface describes the methods in the View
    interface  View extends BaseView{
        void initViews();
        void showLoading(boolean show); //if true = show loading item; else hide loading
        Context getActContext(); //this context is used for the API Calls
        void updateList(String searchQuery);//it updates the list with this search query
    }

    //This Interface describes all the method used in the presenter (Business logic)
    interface Presenter extends BasePresenter{
        /*
        Methods needed:
        1. ArrayList<Articles> GetAllArticles(); Initial call to the NYT API
        2. ArrayList<Articles> getBySearch(String str); //search downloaded items
        3.
         */
        void loadAllArticles();
        ArrayList<ArticleModel> getArticles(String searchQuery);//in String == "", return all items.
        void deleteArticle(int pos);
    }
}
