package com.patelheggere.listfeed.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.patelheggere.listfeed.R;




import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;

import static android.app.Activity.RESULT_OK;


public class UploadFragment extends Fragment {

    private static final int SELECT_PHOTO = 100;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    ImageView imageView;


    public UploadFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //accessing the firebase storage
        storage = FirebaseStorage.getInstance();
        //creates a storage reference
        storageRef = storage.getReference();

        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
           // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ConstraintLayout constraintLayout = (ConstraintLayout)inflater.inflate(R.layout.fragment_upload, container, false);
        Button btnSelect = (Button) constraintLayout.findViewById(R.id.button2);
        Button btnUpload = (Button) constraintLayout.findViewById(R.id.button3);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        return constraintLayout;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getActivity(),"Image selected, click on upload button",Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                }
        }
    }

    public void selectImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public void uploadImage() {
       // new MainActivity().requestPermission();
        //create reference to images folder and assing a name to the file that will be uploaded
        imageRef = storageRef.child("images/"+selectedImage.getLastPathSegment());
        //creating and showing progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMax(100);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        progressDialog.setCancelable(false);
        //starting upload
        uploadTask = imageRef.putFile(selectedImage);
        // Observe state change events such as progress, pause, and resume
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //sets and increments value of progressbar
                progressDialog.incrementProgressBy((int) progress);
            }
        });
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getActivity(),"Error in uploading!",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(getActivity(),"Upload successful",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                System.out.println("download:"+downloadUrl.toString());
                //showing the uploaded image in ImageView using the download url
                //Picasso.with(getActivity()).load(downloadUrl).into(imageView);
            }
        });
    }



}
