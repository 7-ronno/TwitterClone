package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity {

    private EditText username,password;
    private Button signup,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseInstallation.getCurrentInstallation().saveInBackground();


        setTitle("Login");

        final ParseUser parseUser=new ParseUser();
        username=findViewById(R.id.enLoginUsername);
        password=findViewById(R.id.enLoginPassword);

        login=findViewById(R.id.btnLoginLogin);
        signup=findViewById(R.id.btnLoginSIgnUp);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseUser.logInInBackground(username.getText().toString(), password.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user != null && e == null) {
                                    FancyToast.makeText(LoginActivity.this, "Succefully Logged In",
                                            FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                    gotoHome();
                                }else if (e!=null){
                                    FancyToast.makeText(LoginActivity.this, e.getMessage(),
                                            FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                                }
                            }
                        }
                );

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void gotoHome() {
        Intent intent=new Intent(LoginActivity.this,Home.class);
        startActivity(intent);
        finish();
    }
}
