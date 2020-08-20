package ai.trudo.nytapp.ArticleDetails;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ai.trudo.nytapp.Data.ArticleModel;
import ai.trudo.nytapp.R;

public class ArticleDetailsView extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details_view);

        ArticleModel model;

        try{
            model = new ArticleModel(getIntent().getStringExtra(ArticleModel.TITLE),
                    getIntent().getStringExtra(ArticleModel.SUBTITLE),
                    getIntent().getStringExtra(ArticleModel.IMAGE_URL),
                    getIntent().getStringExtra(ArticleModel.ARTICLE_URL));
        }catch (Exception e){
            e.printStackTrace();
            finish();
            return;
        }

        //initialize views;
        ((TextView)findViewById(R.id.title)).setText(model.getTitle());
        ((TextView)findViewById(R.id.subtitle)).setText(model.getSubTitle());
        Picasso.with(getApplicationContext())
                .load(model.getImageUrl())
                .into(((ImageView)findViewById(R.id.image)));

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ArticleModel finalModel = model;
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //share the link to this article with your friends
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = getString(R.string.link)+ finalModel.getArticleUrl();
                String subject = finalModel.getTitle();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
            }
        });
    }
}
