package com.example.machat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.machat.Models.Users;
import com.example.machat.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    private String dbURL;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://ma-chat-b8892-default-rtdb.europe-west1.firebasedatabase.app/");

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.txtUsername.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty() && !binding.txtEmail.getText().toString().isEmpty()){
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        Users user = new Users(binding.txtUsername.getText().toString(),binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString());
                                        String id = task.getResult().getUser().getUid();

                                        database.getReference().child("Users").child(id).setValue(user);
                                        Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Please fill out your credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });


        binding.txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}