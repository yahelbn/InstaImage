package com.example.instaimage.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.instaimage.Adapters.CommentsAdapter;
import com.example.instaimage.Objects.Comment;
import com.example.instaimage.Objects.Image;
import com.example.instaimage.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OnePictureInfo extends AppCompatActivity {


    private ArrayList<Comment> mCommentsArrayList = new ArrayList<Comment>();

    //
    private long lastTouchTime = 0;
    private long currentTouchTime = 0;

    //
    private CommentsAdapter adapterComments;
    EditText EditTextComment;
    TextView nameOfPic;
    TextView infoTextOfImage,LocationOfPic;
    ImageView imageUploaded;
    ListView listCommentsOfImage;
    Button ButtonComment;
    LinearLayout linearLayoutActivity;
    private float oldTouchValue;


    //Database
    DatabaseReference ref;
    FirebaseDatabase database;
    private Image currentImage;
    private String nameOfPicFromPreviusIntent;
    private String userName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_picture_info);

        initialize();
        database = FirebaseDatabase.getInstance();
        ref=database.getReference("Data").child("Images");

        Intent intent = getIntent();
        nameOfPicFromPreviusIntent = intent.getStringExtra("EXTRA_MESSAGE");


        //userName = intent.getStringExtra("USER");
        Log.i("check","user "+userName);
        Log.i("check","pic "+nameOfPicFromPreviusIntent);



        getAllTheComments();
        //Adding the comment after press on add Comment
        ButtonComment.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            lastTouchTime = currentTouchTime;
            currentTouchTime = System.currentTimeMillis();

            if (currentTouchTime - lastTouchTime < 250) {
                Log.i("Double","Click");
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                ButtonComment.startAnimation(shake);

                lastTouchTime = 0;
                currentTouchTime = 0;
            }

            SharedPreferences pref =  getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            //SharedPreferences.Editor editor = pref.edit();
            userName=pref.getString("current_user", ""); // getting int num score in integer

           String comment=userName+": "+EditTextComment.getText().toString();
           Comment commentExample=new Comment(comment);
           mCommentsArrayList.add(commentExample);
           ref.child(nameOfPicFromPreviusIntent).child("Comments").setValue(mCommentsArrayList);

            Intent intent = new Intent(getApplicationContext(), OnePictureInfo.class);
            intent.putExtra("EXTRA_MESSAGE", nameOfPicFromPreviusIntent);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);

            finish();

        }
    });


        // Attach a listener to read the data at our posts reference
        ref.child(nameOfPicFromPreviusIntent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Image image = dataSnapshot.getValue(Image.class);
                //System.out.println(image);
                setCurrentImage(image);
                Log.i("From DB",""+getCurrentImage().name);
                setScreenByCurrentImage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



        linearLayoutActivity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())

                {
                    case MotionEvent.ACTION_DOWN:
                    {
                        oldTouchValue = event.getX();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        float currentX = event.getX();
                        if (oldTouchValue < currentX)
                        {
                            Log.i("hey","left");
                            Intent i=new Intent(OnePictureInfo.this,ShowAndSearchPictures.class);
                            startActivity(i);


                        }
                        if (oldTouchValue > currentX )
                        {
                            Log.i("hey","right");
                                          }
                        break;
                    }
                }
                return true;
            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(OnePictureInfo.this,ShowAndSearchPictures.class);
        startActivity(i);
    }

    //    @Override
//    protected void onResume() {
//        // TODO Auto-generated method stub
//        super.onResume();
//        Log.i("You","press"+"resume");
//        adapterComments = new CommentsAdapter(OnePictureInfo.this, mCommentsArrayList);
//        listCommentsOfImage.setAdapter(adapterComments);
//    }


    public void getAllTheComments(){
        //Getting the array of comments
        ref.child(nameOfPicFromPreviusIntent).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                mProductArrayList = new ArrayList<>();

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Comment comment = d.getValue(Comment.class);
                    mCommentsArrayList.add(comment);
                }

                adapterComments = new CommentsAdapter(OnePictureInfo.this, mCommentsArrayList);
                listCommentsOfImage.setAdapter(adapterComments);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ptt", "Failed to read value.", error.toException());
            }
        });

    }

    public void setCurrentImage(Image currentImage) {
        this.currentImage = currentImage;
    }

    public Image getCurrentImage() {
        return currentImage;
    }

    public void setScreenByCurrentImage(){
        nameOfPic.setText("Name of Picture:"+"\n"+getCurrentImage().name);
        if(getCurrentImage().lng!=null &&getCurrentImage().lat!=null){
            LocationOfPic.setText("The current location:"+"\n"+"Latitude :"+getCurrentImage().lat+"\n"
                    +"LongTitude:"+getCurrentImage().lng);
        }
        if(getCurrentImage().info!=null){
            infoTextOfImage.setText("information:"+"\n"+getCurrentImage().info);
        }
        Picasso.get().load(getCurrentImage().ImageURL).fit().into(imageUploaded);




    }

    public void initialize(){
        nameOfPic=findViewById(R.id.nameOfPic);
        imageUploaded=findViewById(R.id.imageUploaded);
        infoTextOfImage=findViewById(R.id.infoTextOfImage);
        listCommentsOfImage=findViewById(R.id.listCommentsOfImage);
        ButtonComment=findViewById(R.id.ButtonComment);
        EditTextComment=findViewById(R.id.EditTextComment);
        linearLayoutActivity=findViewById(R.id.linearLayoutActivity);
        LocationOfPic=findViewById(R.id.LocationOfPic);
    }







}
