package com.example.twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email,username,password;
    private Button login,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("SignUp");

        email=findViewById(R.id.enSignupEmail);
        username=findViewById(R.id.enSIgnUpUsername);
        password=findViewById(R.id.enSignupPassword);

        login=findViewById(R.id.btnSignupLogin);
        signup=findViewById(R.id.btnSignupSignup);


        signup.setOnClickListener(SignUpActivity.this);
        login.setOnClickListener(SignUpActivity.this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnSignupLogin:
                Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

                break;

            case  R.id.btnSignupSignup:
                ParseUser parseUser=new ParseUser();
                parseUser.setUsername(username.getText().toString());
                parseUser.setPassword(password.getText().toString());
                parseUser.setEmail(email.getText().toString());
                final ProgressDialog progress=new ProgressDialog(this);
                progress.setMessage("Signing Up"+ username.getText().toString());
                progress.show();

                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Toast.makeText(SignUpActivity.this,"Signed up",Toast.LENGTH_LONG)
                                    .show();
                            gotoHome();
                            finish();
                        }else {
                            Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_LONG)
                                    .show();
                        }
                        progress.dismiss();
                    }
                });


                break;
        }
    }

    private void gotoHome() {
        Intent intent=new Intent(SignUpActivity.this,Home.class);
        startActivity(intent);
    }
}
