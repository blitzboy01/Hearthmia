package com.soumio.inceptiontutorial.ui.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.soumio.inceptiontutorial.R;
import com.soumio.inceptiontutorial.ui.Analyze.ChooseModel;
import com.soumio.inceptiontutorial.ui.Signup.ActivitySignUp;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    //declare variables that will be used for backend programming
    private FirebaseAuth firebaseAuth;
    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private Button loginRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        //if a user is currently logged in or has previously not pressed the logout button, user will be directed to home page
       /* if (firebaseAuth.getCurrentUser() != null){
            Intent UserIsPresent = new Intent(this, MainActivity.class);
            startActivity(UserIsPresent);
        }*/

        //relate .xml Edit Text to LoginActivity.java
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        //relate .xml Buttons to LoginActivity.java
        loginButton = findViewById(R.id.login_button);
        loginRegisterButton = findViewById(R.id.login_register_button);
//        loginforgottextview = findViewById(R.id.LoginForgotTextView);
        //relate button to onClick function
        loginButton.setOnClickListener(this);
        loginRegisterButton.setOnClickListener(this);
//        loginforgottextview.setOnClickListener(this);

    }

    void loginUser() {
        //Retrieve string from edit text
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();
        //if email or password edit text is empty prompt a toast.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return; //return stops the function (loginuser) from continuing further
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return; //return stops the function (loginuser) from continuing further
        }

        //login user based on email and password enterd in loginemail and loginpassword edit text
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ActivityLogin.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityLogin.this, ChooseModel.class));
                        finish();
                    } else {
                        Toast.makeText(ActivityLogin.this, "Login failed please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                loginUser();
                break;

            case R.id.login_register_button:
                startActivity(new Intent(this, ActivitySignUp.class));
                finish();
                break;

            default:
                Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show();

        }
       /* if (v == loginforgottextview) {
            //close current activity and start MainActivity.class
            finish();
            startActivity(new Intent(this, ChooseModel.class));

        }*/
    }

}

