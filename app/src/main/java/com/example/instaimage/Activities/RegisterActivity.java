package com.example.instaimage.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instaimage.Objects.User;
import com.example.instaimage.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference ref;
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    EditText EditTextChooseUserName,EditTextChoosePassword1,EditTextChoosePassword2;
    Button signUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signUpButton=findViewById(R.id.signUpButton);
        EditTextChooseUserName=findViewById(R.id.EditTextChooseUserName);
        EditTextChoosePassword1= findViewById(R.id.EditTextChoosePassword1);
        EditTextChoosePassword2= findViewById(R.id.EditTextChoosePassword2);
        ref=database.getReference("Data").child("Users");


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=EditTextChooseUserName.getText().toString();
                String password1=EditTextChoosePassword1.getText().toString();
                String password2=EditTextChoosePassword2.getText().toString();
                if(password1.equals(password2)){
                    User usertry=new User(userName,password1);
                    ref.child(usertry.getUserName()).setValue(usertry);
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Password not the same"+"\n"+"try again.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
