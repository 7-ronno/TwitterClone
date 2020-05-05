package com.example.twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayList<String> mArrayList;
    private ArrayAdapter mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Home");

        mListView = findViewById(R.id.HomeListView);
        mArrayList = new ArrayList();
        mArrayAdapter = new ArrayAdapter(Home.this, android.
                R.layout.simple_list_item_checked, mArrayList);

        mListView.setOnItemClickListener(this);

        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            final ProgressDialog dialog = new ProgressDialog(Home.this);
            dialog.setMessage("Loading");
            dialog.show();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if (users.size() > 0 && e == null) {
                        for (ParseUser user : users) {
                            mArrayList.add(user.getUsername());
                        }

                    } else if (e != null) {
                        Toast.makeText(Home.this, e.getMessage()
                                , Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Home.this, "Nobody else", Toast.LENGTH_LONG).show();
                    }

                    mListView.setAdapter(mArrayAdapter);

                    for(String user:mArrayList){
                        if(ParseUser.getCurrentUser().getList("fanOf")!=null) {
                            if (ParseUser.getCurrentUser().getList("fanOf").
                                    contains(user)) {
                                mListView.setItemChecked(mArrayList.indexOf(user), true);
                            }
                        }
                    }

                    dialog.dismiss();
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.menuLogout){
            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Toast.makeText(Home.this,"Logged Out"
                                ,Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(Home.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
            );
        }
        else if (item.getItemId()==R.id.menuTweet){
            Intent intent=new Intent(Home.this,Tweet.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView=(CheckedTextView) view;

        if(checkedTextView.isChecked()){
            FancyToast.makeText(this,mArrayList.get(position)+
                    "is followed",Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
            ParseUser.getCurrentUser().add("fanOf",mArrayList.get(position));
        }else{
            FancyToast.makeText(this,mArrayList.get(position)+
                    "is unfollowed",Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(mArrayList.get(position));
            List newList=ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",newList);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                FancyToast.makeText(Home.this, "saved",Toast.LENGTH_LONG,
                        FancyToast.SUCCESS,true).show();
            }
        });

    }
}
