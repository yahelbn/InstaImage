package com.example.instaimage.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.instaimage.Objects.Image;
import com.example.instaimage.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class uploadTheTakenPicture extends AppCompatActivity {

    String UrlOfCurrentImage;
    EditText editTextNameOfCurrentPic,editTextInfoOfCurrentPic;
    ImageView ImageCurrentPic;
    Button uploadCurrentImageButton;


    //Database
    DatabaseReference ref;
    FirebaseDatabase database;

    //Location
    FusedLocationProviderClient client;
    LatLng latLng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_the_taken_picture);


        //initiallize FusedLocation
        client= LocationServices.getFusedLocationProviderClient(this);

        //Check permissions
        if(ActivityCompat.checkSelfPermission(uploadTheTakenPicture.this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }
        else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

        Intent intent = getIntent();
        UrlOfCurrentImage = intent.getStringExtra("URL_OF_PICTURE");

        initialize();
        Picasso.get().load(UrlOfCurrentImage).into((ImageCurrentPic));


        uploadCurrentImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =editTextNameOfCurrentPic.getText().toString();
                String info =editTextInfoOfCurrentPic.getText().toString();
                uploadTheCurrentImageToFireBase(name,info);

                Intent intent = new Intent(v.getContext(),ShowAndSearchPictures.class);
                startActivity(intent);

            }
        });

    }

    private void uploadTheCurrentImageToFireBase(String name,String info) {
        Log.i("current ","latLng: "+latLng);
        database = FirebaseDatabase.getInstance();
        ref=database.getReference("Data").child("Images");
        Image currentImage=new Image(name,UrlOfCurrentImage,info,latLng.latitude,latLng.longitude);
        ref.child(currentImage.getName()).setValue(currentImage);
    }


    private void initialize() {
        editTextInfoOfCurrentPic=findViewById(R.id.editTextInfoOfCurrentPic);
        editTextNameOfCurrentPic=findViewById(R.id.editTextNameOfCurrentPic);
        ImageCurrentPic=findViewById(R.id.ImageCurrentPic);
        uploadCurrentImageButton=findViewById(R.id.uploadCurrentImageButton);

    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    private void getCurrentLocation() {
        Task<Location> task=client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if(location!=null){
                    LatLng latLngCurrent=new LatLng(location.getLatitude(),location.getLongitude());
                    Log.i("this is","the: "+latLngCurrent);
                 setLatLng(latLngCurrent);
                }
            }
        });

    }
}
