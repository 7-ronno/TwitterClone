package com.example.twitterclone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tweet extends AppCompatActivity {

    private Button btnTweet,btnViewTweets;
    private ListView mListView;
    private EditText enTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        btnTweet=findViewById(R.id.btnTweet);
        enTweet=findViewById(R.id.enTweetMsg);
        mListView=findViewById(R.id.TweetListVIew);
        btnViewTweets=findViewById(R.id.btnViewTweets);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enTweet.getText()==null){
                    FancyToast.makeText(Tweet.this,"Enter a msg",
                            Toast.LENGTH_LONG,FancyToast.ERROR,true).show();
                }
                else {
                    ParseObject tweets=new ParseObject("tweets");
                    tweets.put("user", ParseUser.getCurrentUser().getUsername());
                    tweets.put("tweet",enTweet.getText().toString());
                    tweets.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                FancyToast.makeText(Tweet.this,"Tweeted",
                                        Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                            }
                            else {
                                FancyToast.makeText(Tweet.this,e.getMessage(),
                                        Toast.LENGTH_LONG,FancyToast.ERROR,true).show();
                            }
                        }
                    });
                }
            }
        });

        btnViewTweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<HashMap<String, String>> Tweetlist = new ArrayList<>();
                final SimpleAdapter simpleAdapter = new SimpleAdapter(Tweet.this, Tweetlist,
                        android.R.layout.simple_list_item_2, new String[]{"username", "tweet"
                }, new int[]{android.R.id.text1, android.R.id.text2});
                try {
                    ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>
                            ("tweets");
                    parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().
                            getList("fanOf"));
                    parseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects.size() > 0 && e == null) {
                                for (ParseObject object : objects) {
                                    HashMap<String, String> tweet = new HashMap<>();
                                    tweet.put("username", object.get("user").toString());
                                    tweet.put("tweet", object.get("tweet").toString());
                                    Tweetlist.add(tweet);
                                }
                            }
                            mListView.setAdapter(simpleAdapter);
                        }
                    });


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
