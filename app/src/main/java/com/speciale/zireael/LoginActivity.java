package com.speciale.zireael;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    EditText input_email = null , input_password = null;
    TextView btnSignup, btnForgotPass;

    RelativeLayout relativeLayout;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //View
        btnLogin = findViewById(R.id.login_btn_login);
        input_email = findViewById(R.id.login_email);
        input_password = findViewById(R.id.login_password);
        btnSignup = findViewById(R.id.login_btn_signup);
        btnForgotPass = findViewById(R.id.login_btn_forgot_password);
        relativeLayout = findViewById(R.id.login_layout);

        btnSignup.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Check already session, if ok -> dashboard
        if (auth.getCurrentUser() != null){

            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        }

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.login_btn_forgot_password)
        {
            startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            finish();
        }

        else if (view.getId() == R.id.login_btn_signup)
        {
            startActivity(new Intent(LoginActivity.this, SignUp.class));
            finish();
        }

        else if (view.getId() == R.id.login_btn_login)
        {
            //for close to virtual keyboard.
            InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            //
            System.out.println("email:"+input_email.getText().toString()+input_password.getText().toString());
            if (TextUtils.isEmpty(input_email.getText().toString())){
                Snackbar snackbar = Snackbar.make(relativeLayout,
                        "Please enter a valid e-mail.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else if (TextUtils.isEmpty(input_password.getText().toString())){
                Snackbar snackbar = Snackbar.make(relativeLayout,
                        "Please enter a valid password.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else {
                loginUser(input_email.getText().toString(), input_password.getText().toString());
            }

        }


    }

    private void loginUser(String email, final String password) {

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                        {
                            if (password.length() < 6)
                            {
                                Snackbar snackbar = Snackbar.make(relativeLayout,
                                        "Password length must be over 6",
                                        Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                            else {
                                Snackbar snackbar = Snackbar.make(relativeLayout,
                                        "Please check your login information and try again.",
                                        Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                        else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });

    }
}
