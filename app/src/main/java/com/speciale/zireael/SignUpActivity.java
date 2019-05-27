package com.speciale.zireael;

import android.content.Intent;

import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignup;
    TextView btnLogin, btnForgotPass;
    EditText input_email, input_pass;
    RelativeLayout relativeLayout_signup;

    private FirebaseAuth auth;
    Snackbar snackbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //View
        btnSignup = findViewById(R.id.signup_btn_register);
        btnLogin = findViewById(R.id.signup_btn_login);
        btnForgotPass = findViewById(R.id.signup_btn_forgot_password);
        input_email = findViewById(R.id.signup_email);
        input_pass = findViewById(R.id.signup_password);
        relativeLayout_signup = findViewById(R.id.signup_layout);

        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);

        //Init Firebase
        auth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.signup_btn_login)
        {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        }
        else if (view.getId() == R.id.signup_btn_forgot_password)
        {
            startActivity(new Intent(SignUpActivity.this, ForgotPasswordActivity.class));
            finish();
        }
        else if (view.getId() == R.id.signup_btn_register)
        {
            //for close to virtual keyboard.
            InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            //
            signUpUser(input_email.getText().toString(), input_pass.getText().toString());
        }



    }

    private void signUpUser(String email, String password) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                        {
                            snackbar = Snackbar.make(relativeLayout_signup,
                                    "Error:" + task.getException(), Snackbar.LENGTH_LONG );
                            snackbar.show();
                        }
                        else
                        {
                            snackbar = Snackbar.make(relativeLayout_signup,
                                    "Register Success!", Snackbar.LENGTH_LONG );
                            snackbar.show();
                        }
                    }
                });


    }
}
