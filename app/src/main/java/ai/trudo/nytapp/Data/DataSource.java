package ai.trudo.nytapp.Data;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import static ai.trudo.nytapp.Utils.Utils.NYT_API_KEY;

public class DataSource {

    public void getTopArticles( DataLoadedCallback callback) {

        new ExecuteHttpRequest(callback).execute();

        //For testing purposes.
       /* final ArrayList<ArticleModel> models = new ArrayList<>();
        ArticleModel model1 = new ArticleModel("Article about California and stuff",
                "California is really hot. San Jose is great",
                "https://www.cars.com/cstatic-images/car-pictures/xl/USC60BMC111A021001.jpg",
                "https://github.com/square/picasso");


        ArticleModel model2 = new ArticleModel("Article about washington state",
                "Washington is great. I like Seattle and the people who live there",
                "https://amp.businessinsider.com/images/5aabc7bbc72ac12f008b4609-750-563.jpg",
                "https://www.businessinsider.com/cars-2018-geneva-motor-show-2018-3");
        models.add(model1);
        models.add(model2);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                callback.onFinished(models);
            }
        }, 5000);   //5 second to simulate network connection.*/

    }

    public interface DataLoadedCallback{
        void onFinished(ArrayList<ArticleModel> models);
    }


    public static class ExecuteHttpRequest extends AsyncTask<Void, Void, JSONObject>{
        String requestString = "https://api.nytimes.com/svc/topstories/v2/world.json?api-key="+NYT_API_KEY;
        DataLoadedCallback callback;
        ExecuteHttpRequest(DataLoadedCallback callback){
            this.callback = callback;
        }


        @Override
        protected JSONObject doInBackground(Void... voids) {

            try {
                URL url = new URL(requestString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader( conn.getInputStream()));

                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine())!=null){
                    response.append(inputLine);
                }
                in.close();

                return new JSONObject(response.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPreExecute();
            if(json == null){
                callback.onFinished(new ArrayList<ArticleModel>());
                return;
            }

            ArrayList<ArticleModel> result = new ArrayList<>();
            try {
                if(!json.has("results") || !json.has("status")|| !json.getString("status").equals("OK")){
                    //if no articles, or status not okay - return empty arrayList
                    callback.onFinished(result);
                    return;
                }

                JSONArray list = json.getJSONArray("results");
                for(int i=0; i<list.length(); i++){
                    JSONObject oneArticle = list.getJSONObject(i);
                    ArticleModel model = new ArticleModel();
                    model.setTitle(oneArticle.getString("title"));
                    model.setSubTitle(oneArticle.getString("abstract"));
                    model.setArticleUrl(oneArticle.getString("url"));
                    //get image
                    if(oneArticle.has("multimedia")
                            && oneArticle.getJSONArray("multimedia").length()>0){
                        model.setImageUrl(oneArticle.getJSONArray("multimedia")
                                .getJSONObject(oneArticle.getJSONArray("multimedia").length()-1)
                                .getString("url"));
                    }else{
                        //Set default image is no images provided
                        model.setImageUrl("https://sweetwaterbrew.com/wp-content/uploads/nyt-logo.png");
                    }
                    result.add(model);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            callback.onFinished(result);
        }
    }
}
