package com.example.instaimage.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.instaimage.Objects.Image;
import com.example.instaimage.Objects.User;
import com.example.instaimage.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Button logInButton;

    private ArrayList<User>  usersArrayList= new ArrayList<User>();

    DatabaseReference ref;
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    EditText userNameId,passwordEditText;
    private TextView toSignUptext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ref=database.getReference("Data").child("Users");

        User usertry=new User("yahel","31232");
        ref.child(usertry.getUserName()).setValue(usertry);

        passwordEditText=findViewById(R.id.password);
        userNameId=findViewById(R.id.userNameId);
        toSignUptext=findViewById(R.id.toSignUptext);

        logInButton=findViewById(R.id.loginButton);

        toSignUptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String username=userNameId.getText().toString();
            String password=passwordEditText.getText().toString();
                for(int i=0;i<usersArrayList.size();i++) {
                    if(usersArrayList.get(i).userName.equals(username)&&usersArrayList.get(i).password.equals(password)){
                        Intent intent = new Intent(MainActivity.this,ShowAndSearchPictures.class);
                        intent.putExtra("USER_NAME", username);
                        startActivity(intent);
                    }
                }


            }
        });



        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                mProductArrayList = new ArrayList<>();

                for (DataSnapshot d: dataSnapshot.getChildren()){
                    User user = d.getValue(User.class);
                    usersArrayList.add(user);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ptt", "Failed to read value.", error.toException());
            }
        });




    }
}
