package com.example.authtestapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    FirebaseAuth mAuth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();
        Button btnLogIn = findViewById(R.id.btnLogIn);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogIn();
            }
        });

    }

    private void verifyEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()) {
            Toast.makeText(LogInActivity.this, "Logged In Successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            mAuth.signOut();
            Toast.makeText(LogInActivity.this, "Please Verify your account", Toast.LENGTH_LONG).show();
        }
    }

    private void LogIn() {
        final ProgressBar progBarLogIn = findViewById(R.id.progBarLogIn);
        progBarLogIn.setVisibility(View.VISIBLE);
        final EditText edlogInEmail = findViewById(R.id.edlogInEmail);
        final EditText edlogInPass = findViewById(R.id.edlogInpassword);
        String logInEmail = edlogInEmail.getText().toString();
        String logInPass = edlogInPass.getText().toString();
        if (!logInEmail.isEmpty() && !logInPass.isEmpty()) {
            mAuth.signInWithEmailAndPassword(logInEmail, logInPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progBarLogIn.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        verifyEmail();
                    } else {
                        Toast.makeText(LogInActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            progBarLogIn.setVisibility(View.GONE);
            Toast.makeText(LogInActivity.this, "Fill Blanks", Toast.LENGTH_LONG).show();
        }
    }
}
