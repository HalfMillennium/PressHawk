package com.digitalnode.presshawk;

import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Story> allStories;
    private ProgressBar progressBar;
    private TextView gatheringText;

    private final String NEWS_API_KEY = "7b9a5818d54749d4b91fcb7639bd3278";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        gatheringText = findViewById(R.id.gathering_text);

        allStories = new ArrayList<>();

        YoYo.with(Techniques.Bounce)
                .duration(700)
                .playOn(findViewById(R.id.todays_headlines));

        new GetStories().execute();

        List<String> categories = new ArrayList<String>();
        categories.add("General");
        categories.add("Entertainment");
        categories.add("Business");
        categories.add("Health");
        categories.add("Science");
        categories.add("Sports");
        categories.add("Technology");

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    public void getHeadlines(String cat)
    {
        NewsApiClient newsApiClient = new NewsApiClient(NEWS_API_KEY);

        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .category(cat)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        for(Article a : response.getArticles())
                        {
                            try {
                                allStories.add(new AddNewStory().execute(a).get());
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        Log.d("Articles", allStories.get(0).getSource());

                        recyclerView = (RecyclerView) findViewById(R.id.headline_rv);

                        // use this setting to improve performance if you know that changes
                        // in content do not change the layout size of the RecyclerView
                        recyclerView.setHasFixedSize(true);

                        // use a linear layout manager
                        layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);

                        // specify an adapter (see also next example)

                        Story[] stories = new Story[allStories.size()];
                        for(int i = 0; i < stories.length; i++)
                        {
                            stories[i] = allStories.get(i);
                        }

                        progressBar.setVisibility(View.GONE);
                        gatheringText.setVisibility(View.GONE);

                        mAdapter = new HeadlineAdapter(stories);
                        recyclerView.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();
                        recyclerView.scheduleLayoutAnimation();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private class AddNewStory extends AsyncTask<Article, Void, Story> {
        @Override
        protected Story doInBackground(Article... params) {
            Article a = params[0];
            //int b = 1 / 0;
            String url = a.getUrl();
            try {
                Document doc = Jsoup.connect(url).get();
                Story newStory = new Story(a.getSource().getName(), a.getPublishedAt(), a.getAuthor(), a.getDescription(), a.getUrl(), a.getUrlToImage(), a.getTitle(), doc.body().text());

                return newStory;
            } catch (IOException e) {
                Log.d("Error", e.getMessage());
            }

            return null;
        }
    }

    private class GetStories extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            getHeadlines("general");
            return null;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        getHeadlines(item.toLowerCase());
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
