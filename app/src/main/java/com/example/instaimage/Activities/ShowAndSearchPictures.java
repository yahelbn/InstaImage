package com.example.instaimage.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.instaimage.Adapters.MyAdapter;
import com.example.instaimage.Objects.Image;
import com.example.instaimage.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowAndSearchPictures extends AppCompatActivity {

    private LinearLayout llContainer;
    private EditText etSearch;
    private ListView lvProducts;


    private ArrayList<Image> mProductArrayList = new ArrayList<Image>();
    private MyAdapter adapter1;

     SearchView mySearchView;
     Button addPictureButton;

     //Database
     DatabaseReference ref;
     FirebaseDatabase database;

    public String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_and_search_pictures);

        Intent intent = getIntent();

        setUserName(intent.getStringExtra("USER_NAME"));
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Log.i("user","name"+userName);
        editor.putString("current_user", userName);
        editor.commit();

        database = FirebaseDatabase.getInstance();
        ref=database.getReference("Data").child("Images");

        Image imagepica=new Image("Picasso","http://i.imgur.com/DvpvklR.png");
        Image imageSurf=new Image("Surf","https://a.cdn-hotels.com/gdcs/production107/d1108/048025aa-b66a-4dc2-b8b4-136a3d0ced33.jpg");
        Image imageChita=new Image("Chita","https://cdn.mos.cms.futurecdn.net/Z6j2a3pPdyBTQ5iicDe9kn-320-80.jpg");
        Image imageZebra=new Image("Zebra","https://www.peta.org/wp-content/uploads/2017/03/iStock-157381164_freder-300x190.jpg");

        ref.child(imagepica.getName()).setValue(imagepica);
        ref.child(imageSurf.getName()).setValue(imageSurf);
        ref.child(imageChita.getName()).setValue(imageChita);
        ref.child(imageZebra.getName()).setValue(imageZebra);



        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                mProductArrayList = new ArrayList<>();

                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Image image = d.getValue(Image.class);
                    mProductArrayList.add(image);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ptt", "Failed to read value.", error.toException());
            }
        });
//        mProductArrayList.add(new Image("a", 100));
//        mProductArrayList.add(new Image("b", 200));
//        mProductArrayList.add(new Image("c", 300));
//        mProductArrayList.add(new Image("d", 400));


        initialize();

        //Handle Search
        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                Log.i("search",""+newText);
                adapter1.getFilter().filter(newText);

                return false;
            }
        });


        //Handle the Click on item on ListView

        lvProducts.setClickable(true);
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = lvProducts.getItemAtPosition(position);
                String nameOfPic=o.toString();
                Log.i("",""+nameOfPic);

                Intent intent = new Intent(ShowAndSearchPictures.this, OnePictureInfo.class);
                intent.putExtra("EXTRA_MESSAGE", nameOfPic);
                //Log.i("show","pic"+nameOfPic);
                //intent.putExtra("USER", getUserName().toString());
                Log.i("show","user"+getUserName());


                startActivity(intent);
                finish();
            }
        });


        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAndSearchPictures.this, addPictureActivity.class);
//                intent.putExtra("EXTRA_MESSAGE", nameOfPic);
                startActivity(intent);
            }
        });



    }

    private void initialize() {
        mySearchView=findViewById(R.id.search_view);
        addPictureButton=findViewById(R.id.addPictureButton);
        lvProducts = (ListView)findViewById(R.id.lvProducts);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        adapter1 = new MyAdapter(ShowAndSearchPictures.this, mProductArrayList);
        lvProducts.setAdapter(adapter1);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(ShowAndSearchPictures.this,MainActivity.class);
        startActivity(i);
    }


}
