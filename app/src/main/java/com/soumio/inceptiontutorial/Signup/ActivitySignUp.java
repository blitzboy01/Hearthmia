package com.soumio.inceptiontutorial.Signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soumio.inceptiontutorial.Analyze.ChooseModel;
import com.soumio.inceptiontutorial.Login.ActivityLogin;
import com.soumio.inceptiontutorial.R;

public class ActivitySignUp extends AppCompatActivity {
    EditText emailId, password, name, birthdate, phonenumber;
    Button signupbutton;
    FirebaseAuth mFirebaseAuth;
    String showName;
    ImageView signupbackimage;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();

        emailId = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
        signupbutton = findViewById(R.id.sign_up_button);
        name = findViewById(R.id.sign_up_name);
        birthdate = findViewById(R.id.sign_up_birthdate);
        phonenumber = findViewById(R.id.sign_up_phone_number);
        signupbackimage = findViewById(R.id.sign_up_back_button);

        reff = FirebaseDatabase.getInstance().getReference().child("Member");

        signupbutton.setOnClickListener(view -> {
            String email = emailId.getText().toString();
            String pwd = password.getText().toString();
            String nombre = name.getText().toString();
            String bday = birthdate.getText().toString();
            String cpnum = phonenumber.getText().toString();

            if (email.isEmpty()) {
                emailId.setError("Please Enter email id");
                emailId.requestFocus();
            } else if (pwd.isEmpty()) {
                password.setError("Please Enter your password");
                password.requestFocus();
            } else if (nombre.isEmpty()) {
                name.setError("Please Enter your name");
                name.requestFocus();
            } else if (bday.isEmpty()) {
                birthdate.setError("Please Enter your birthday");
                birthdate.requestFocus();
            } else if (cpnum.isEmpty()) {
                phonenumber.setError("Please Enter your phone number");
                phonenumber.requestFocus();
            } else if (email.isEmpty() && pwd.isEmpty()) {
                Toast.makeText(ActivitySignUp.this, "Field are empty!", Toast.LENGTH_SHORT).show();
            } else if (!(email.isEmpty() && pwd.isEmpty())) {
                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(ActivitySignUp.this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(ActivitySignUp.this, "Sign Up Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(this, ChooseModel.class));
                    }
                });
            }


        });
        signupbackimage.setOnClickListener(view -> {

            Intent i = new Intent(this, ActivityLogin.class);
            startActivity(i);
            finish();
        });
    }


}
