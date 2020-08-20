package ai.trudo.nytapp.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ai.trudo.nytapp.ArticleDetails.ArticleDetailsView;
import ai.trudo.nytapp.Data.ArticleModel;
import ai.trudo.nytapp.R;
import ai.trudo.nytapp.Utils.Utils;

public class MainActivity extends AppCompatActivity implements MainContract.View, View.OnClickListener {

    private MainContract.Presenter presenter;
    private RecyclerView recyclerView;
    private String searchPhrase = "";
    ArticleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!Utils.isNetworkAvailable(getApplicationContext())){
            Toast.makeText(getApplicationContext(),
                    getString(R.string.connect_to_internet),
                    Toast.LENGTH_LONG).show();
            return;
        }

        setPresenter(this);
    }

    @Override
    public void setPresenter(Object Presenter) {
        presenter = new MainPresenter(this);
        presenter.start();
    }

    @Override
    public void initViews() {
        //initialize the basic views and then call the presenter to load the data
        //1. Set up search button listener
        findViewById(R.id.searchButton).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);

        //2. Set up the RecycledList View
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ArticleListAdapter();
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //3. Set up input text listener
        //When use enters some text and preses enter, this listener will be called.
        findViewById(R.id.searchField).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchPhrase = ((EditText) findViewById(R.id.searchField)).getText().toString().trim();
                    if(!searchPhrase.trim().equals("")){
                        updateList(searchPhrase);
                    }
                    return false;
                }
                return false;
            }
        });


        presenter.loadAllArticles();
    }

    @Override
    public void showLoading(boolean show) {
        if(show){
            //hide list and show loading
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            //show list and hide loading
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public Context getActContext() {
        return this.getApplicationContext();
    }

    @Override
    public void updateList(String searchQuery) {
        if(presenter.getArticles(searchQuery).size()<1){
            findViewById(R.id.noResultsTextView).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.noResultsTextView).setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        int id = v.getId();
        switch (id){
            case R.id.searchButton:
                //start search thing
                findViewById(R.id.actionToolbar).setVisibility(View.GONE); //hide the action bar
                findViewById(R.id.searchToolBar).setVisibility(View.VISIBLE); //Show the search bar
                //show keyboard.
                findViewById(R.id.searchField).requestFocus();
                imm.showSoftInput(findViewById(R.id.searchField), InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.cancel:
                //cancels the search, hides the search field
                ((EditText) findViewById(R.id.searchField)).setText(""); //clear the user input
                imm.hideSoftInputFromWindow(findViewById(R.id.searchField).getWindowToken(), 0);
                findViewById(R.id.actionToolbar).setVisibility(View.VISIBLE); //show the action bar
                findViewById(R.id.searchToolBar).setVisibility(View.GONE); //hide the search bar
                searchPhrase = "";
                ((EditText) findViewById(R.id.searchField)).setText("");
                updateList(""); //reset to default
                break;
            case R.id.clear:
                //clear the text field
                ((EditText) findViewById(R.id.searchField)).setText(""); //clear the user input.
                searchPhrase = "";
                updateList("");
                break;
        }
    }


    public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {

        private LayoutInflater inflater;

        ArticleListAdapter(){
            inflater = LayoutInflater.from(getBaseContext());
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            View view;
            ImageView image;
            TextView title;
            TextView subtitle;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView;
                image = view.findViewById(R.id.image);
                title = view.findViewById(R.id.title);
                subtitle = view.findViewById(R.id.subtitle);
            }
        }


        @NonNull
        @Override
        public ArticleListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.article_list_view, viewGroup, false);
            return new ArticleListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int pos) {
            holder.title.setText(presenter.getArticles(searchPhrase).get(pos).getTitle());
            holder.subtitle.setText(presenter.getArticles(searchPhrase).get(pos).getSubTitle());
            Picasso.with(getApplicationContext())
                    .load(presenter.getArticles(searchPhrase).get(pos).getImageUrl())
                    .resize(800, 800)
                    .centerCrop()
                    .into(holder.image);

            final int tempPos = pos;
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open another activity to display details
                    Intent intent = new Intent(MainActivity.this, ArticleDetailsView.class);
                    //this passes the whole ArticleModel object to another activity.
                    intent.putExtra(ArticleModel.IMAGE_URL, presenter.getArticles(searchPhrase).get(tempPos).getImageUrl());
                    intent.putExtra(ArticleModel.ARTICLE_URL, presenter.getArticles(searchPhrase).get(tempPos).getArticleUrl());
                    intent.putExtra(ArticleModel.TITLE, presenter.getArticles(searchPhrase).get(tempPos).getTitle());
                    intent.putExtra(ArticleModel.SUBTITLE, presenter.getArticles(searchPhrase).get(tempPos).getSubTitle());
                    startActivity(intent);
                }
            });

            //This is a new feature: Create a new branch
            //Delete an article on a long click
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    presenter.deleteArticle(pos);
                    return false;
                }
            });


            //Animates the way each item appears on the screen
            ScaleAnimation anim = new ScaleAnimation(
                    0.0f, 1.0f, 0.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(450);
            holder.view.startAnimation(anim);
        }

        @Override
        public int getItemCount() {
            return presenter.getArticles(searchPhrase).size();
        }

    }


}
