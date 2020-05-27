package com.example.instaimage.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.InetAddresses;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instaimage.Activities.OnePictureInfo;
import com.example.instaimage.Activities.ShowAndSearchPictures;
import com.example.instaimage.Activities.uploadTheTakenPicture;
import com.example.instaimage.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static androidx.core.content.ContextCompat.getExternalFilesDirs;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Top_Camera extends Fragment {

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;
    Button capturePictureButton,GalleryPicturesButton;
    ImageView squareCamera;
    String pathToFile;
    StorageReference storageReference;


    public Fragment_Top_Camera() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fragment__top__camera, container, false);
            initiallize(v);
        storageReference= FirebaseStorage.getInstance().getReference();

            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{
                        Manifest.permission.CAMERA
                  }
                , 100);
            }

            GalleryPicturesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallery,105);
                }
            });

            capturePictureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                            // dispatchTakePictureIntent();
                    askCameraPermissions();

                }
            });


        return v;
    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.CAMERA},101);

        }
        else{
           // openCamera();
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if(requestCode==101){
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //openCamera();
                    dispatchTakePictureIntent();
                }

                else{
                    Toast.makeText(getContext(),"Camera permission is Required to use camera.",Toast.LENGTH_SHORT).show();
                }
            }
    }

    private void openCamera() {

        Intent camera =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,102);
    }

        @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if(requestCode==102){
                Log.i("hello","first"+"if");

                if(resultCode== RESULT_OK){
                    File f=new File(currentPhotoPath);
                    //squareCamera.setImageURI(Uri.fromFile(f));

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    getContext().sendBroadcast(mediaScanIntent);

                    Toast.makeText(getContext(),"Wait for upload"+"\n"+"the Image",Toast.LENGTH_SHORT).show();
                    showProgressDialog();
                    uploadImageToFireBase(f.getName(),contentUri);

                    Log.i("Info ","Image"+" Path: "+currentPhotoPath+" type"+contentUri.toString());
                }


            }


            if(requestCode==105){
                Log.i("hello","first"+"if");
                if(resultCode== RESULT_OK){
                    Uri contentUri=data.getData();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "."+getFileExt(contentUri);
                    Log.i("Info ","Image"+"Name: "+imageFileName+" type"+contentUri.toString());
                    //squareCamera.setImageURI(contentUri);
                    Toast.makeText(getContext(),"Wait for upload"+"\n"+"  the Image",Toast.LENGTH_SHORT).show();
                    showProgressDialog();
                    uploadImageToFireBase(imageFileName,contentUri);


                }
            }

    }

    private void uploadImageToFireBase(String name, Uri contentUri) {
            final StorageReference image=storageReference.child("images/"+name);
            image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into((squareCamera));
                        Log.i("The","Url"+uri.toString());
                        Intent intent = new Intent(getContext(), uploadTheTakenPicture.class);
                        intent.putExtra("URL_OF_PICTURE", uri.toString());
                        startActivity(intent);

                    }
                });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"Upload Failled",Toast.LENGTH_SHORT).show();
                }
            });
    }



    private String getFileExt(Uri contentUri) {
        ContentResolver c=getContext().getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    public void initiallize(View v){
        squareCamera=v.findViewById(R.id.squareCamera);
        GalleryPicturesButton=v.findViewById(R.id.GalleryPicturesButton);
        capturePictureButton=v.findViewById(R.id.capturePictureButton);

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        //File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);;
        File storageDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }




    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 102);
            }
        }
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Loading ...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }


    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}




