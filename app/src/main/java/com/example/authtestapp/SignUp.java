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

public class SignUp extends AppCompatActivity {
    FirebaseAuth mAuth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText edEmail = findViewById(R.id.edEmail);
        final EditText edpass = findViewById(R.id.edpassword);
        Button btnRegister = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressBar progBarRegister = findViewById(R.id.progBarRegister);
                progBarRegister.setVisibility(View.VISIBLE);
                String inputEmail = edEmail.getText().toString();
                String inputpass = edpass.getText().toString();
                if (!inputEmail.isEmpty() && !inputpass.isEmpty()) {
                    mAuth.createUserWithEmailAndPassword(inputEmail, inputpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progBarRegister.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                sendEmailverify();
                            } else {
                                Toast.makeText(SignUp.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    progBarRegister.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this, "Fill Blanks", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void sendEmailverify() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Successful...Go to your mail to Verify account", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUp.this, LogInActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignUp.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
