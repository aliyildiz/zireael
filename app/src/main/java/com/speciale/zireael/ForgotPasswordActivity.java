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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText input_email;
    private Button btnResetPass;
    private TextView btnBack;
    private RelativeLayout relativeLayout_forgot;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        //View
        input_email = findViewById(R.id.forgot_email);
        btnResetPass = findViewById(R.id.forgot_btn_reset);
        btnBack = findViewById(R.id.forgot_btn_back);
        relativeLayout_forgot = findViewById(R.id.forgot_layout);

        btnBack.setOnClickListener(this);
        btnResetPass.setOnClickListener(this);

        //Init Firebase
        auth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.forgot_btn_back)
        {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        }
        else if (view.getId() == R.id.forgot_btn_reset)
        {
            //for close to virtual keyboard.
            InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            //
            resetPassword(input_email.getText().toString());
        }

    }

    private void resetPassword(final String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Snackbar snackbar = Snackbar.make(relativeLayout_forgot,
                                    "We have sent password to email:"+ email,
                                    Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        else
                        {
                            Snackbar snackbar = Snackbar.make(relativeLayout_forgot,
                                    "Failed to send password", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
    }
}
