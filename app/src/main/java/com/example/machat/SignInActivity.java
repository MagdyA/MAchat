package com.example.machat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.machat.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://ma-chat-b8892-default-rtdb.europe-west1.firebasedatabase.app/");

        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please wait \n,Validation in progress");

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.txtEmail.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty()) {
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                                        startActivity(intent);

                                    }
                                    else{
                                        Toast.makeText(SignInActivity.this, "Couldn't Sign in\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }



                                }
                            });
                }
                else{
                    Toast.makeText(SignInActivity.this, "Please fill out your credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
        }


    }
}