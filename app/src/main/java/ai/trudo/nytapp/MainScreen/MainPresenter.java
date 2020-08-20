package ai.trudo.nytapp.MainScreen;

import java.util.ArrayList;
import ai.trudo.nytapp.Data.ArticleModel;
import ai.trudo.nytapp.Data.DataSource;
import ai.trudo.nytapp.Utils.Utils;

public class MainPresenter implements MainContract.Presenter{

    private MainContract.View view;
    private DataSource dataSource;
    private ArrayList<ArticleModel> topArticles;  // this is the original list of articles

    private String oldSearchWord = "";
    private ArrayList<ArticleModel> searchByKey = new ArrayList<>();


    MainPresenter(MainContract.View view){
        this.view = view;
        dataSource = new DataSource();
    }

    @Override
    public void start() {
        view.initViews();
    }

    /*
    This method only gets called one when the activity is initialized
     */
    @Override
    public void loadAllArticles() {
        if(topArticles == null){
            topArticles = new ArrayList<>();
            view.showLoading(true);
            dataSource.getTopArticles(new DataSource.DataLoadedCallback() {
                @Override
                public void onFinished(ArrayList<ArticleModel> models) {
                    if(view == null || view.getActContext() == null){
                        return;
                    }
                    topArticles = models;
                    searchByKey = new ArrayList<>(models);
                    view.showLoading(false);
                    view.updateList("");
                }
            });
        }
    }

    @Override
    public void deleteArticle(int pos) {
        this.topArticles.remove(pos);
        this.searchByKey.remove(pos);
        this.view.updateList(oldSearchWord); //update the view.
    }

    @Override
    public ArrayList<ArticleModel> getArticles(String searchQuery) {

        if(topArticles == null){
            loadAllArticles();
            return null;
        }

        //This searches all articled for this specific search word.
        //if title or subtitle contain that word, it'll be added
        if(!searchQuery.toLowerCase().equals(oldSearchWord.toLowerCase())){
            //this is a different keyword, get a different ArrayList
            oldSearchWord = searchQuery;
            searchByKey.clear();
            //look over the original array and add only items that contain the keyword
            for(ArticleModel model: topArticles){
                if(model.getTitle().toLowerCase().contains(searchQuery.toLowerCase())
                        || model.getSubTitle().toLowerCase().contains(searchQuery.toLowerCase())){
                    searchByKey.add(model);
                }
            }
        }
        return searchByKey;
    }
}
