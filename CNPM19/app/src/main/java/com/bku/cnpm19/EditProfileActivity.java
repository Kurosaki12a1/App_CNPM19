package com.bku.cnpm19;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.cnpm19.info.ImageUploadInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import com.bku.cnpm19.info.UserInfo;
/**
 * Created by Welcome on 3/21/2018.
 */

public class EditProfileActivity extends AppCompatActivity {

        private EditText userName,displayName,website,phoneNumber,email,description;
        private TextView changePhoto;

        private DatabaseReference databaseReference;
        private DatabaseReference databaseReference2nd;
        private FirebaseAuth mAuth;
        private String imageUrl="";
        StorageReference storageReference;



        private ImageView backArrow,saveChanges,profilePhoto;
        public static final String Database_Path = "All_User_Info_Database";
        int Image_Request_Code = 7;

         // Creating URI.
         Uri FilePathUri;

        String Storage_Path = "Image Store";

        ProgressDialog progressDialog ;
        ProgressDialog progressDialog2nd ;
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_profile);

            userName=(EditText)findViewById(R.id.username);
            displayName=(EditText)findViewById(R.id.display_name);
            website=(EditText)findViewById(R.id.website);
            phoneNumber=(EditText)findViewById(R.id.phoneNumber);
            email=(EditText)findViewById(R.id.email);
            description=(EditText)findViewById(R.id.description);
            changePhoto=(TextView)findViewById(R.id.changeProfilePhoto);
            backArrow=(ImageView)findViewById(R.id.backArrow);
            saveChanges=(ImageView)findViewById(R.id.saveChanges);
            profilePhoto=(ImageView)findViewById(R.id.profile_photo);

            //Set disabled username and email
            userName.setEnabled(false);
            email.setEnabled(false);

            progressDialog = new ProgressDialog(EditProfileActivity.this);
            progressDialog2nd=new ProgressDialog(EditProfileActivity.this);
            //getUserID
            mAuth= FirebaseAuth.getInstance();
            String userId=mAuth.getCurrentUser().getUid().toString();

            //create code

            storageReference = FirebaseStorage.getInstance().getReference();


            //
            databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child(userId);
            databaseReference2nd = FirebaseDatabase.getInstance().getReference("All_Image_Uploads_Database").child(userId);
            databaseReference.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userName.setText(dataSnapshot.getValue(String.class));
                    email.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    displayName.setText(dataSnapshot.getValue(String.class));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference.child("description").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    description.setText(dataSnapshot.getValue(String.class));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    description.setText("");
                }
            });

            databaseReference.child("website").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    website.setText(dataSnapshot.getValue(String.class));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    website.setText("");
                }
            });

            databaseReference.child("phoneNumber").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    phoneNumber.setText(dataSnapshot.getValue(String.class));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    phoneNumber.setText("");
                }
            });


            changePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();

                    // Setting intent type as image to select image from phone storage.
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
                    //activity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });

            backArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


            saveChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UploadImageFileToFirebaseStorage();
                    int FinishTime = 10;
                    int countDownInterval = 1000;
                    CountDownTimer counterTimer = new CountDownTimer(FinishTime * 1000, countDownInterval) {
                        public void onFinish() {
                            finish();
                            //finish your activity here
                        }

                        public void onTick(long millisUntilFinished) {
                            progressDialog2nd.setTitle("Please wait our system update your profile");
                            progressDialog2nd.show();
                            Toast.makeText(getApplicationContext(), Long.toString(millisUntilFinished), Toast.LENGTH_SHORT).show();
                            //called every 1 sec coz countDownInterval = 1000 (1 sec)
                        }
                    };
                    counterTimer.start();
                   /* Intent intent = new Intent(EditProfileActivity.this, ViewSelfProfile.class);
                    startActivity(intent);*/

                }
            });
        }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                profilePhoto.setImageBitmap(bitmap);

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

        }

    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            //progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
           // progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            //       String TempImageName = ImageName.getText().toString().trim();

                            // Hiding the progressDialog after done uploading.
                            //   progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Upload Successfully ", Toast.LENGTH_LONG).show();

                            // Getting image upload ID.
                            String ImageUploadId = databaseReference2nd.push().getKey();


                            //get UserId
                            mAuth = FirebaseAuth.getInstance();

                            String userId = mAuth.getCurrentUser().getUid().toString();

                            //get ImageURL
                            imageUrl = taskSnapshot.getDownloadUrl().toString();
                            // @SuppressWarnings("VisibleForTests")
                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(taskSnapshot.getDownloadUrl().toString(), 0, "", userId);

                            // Getting image upload ID.
                            //String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            databaseReference2nd.child(ImageUploadId).setValue(imageUploadInfo);


                            //update database user
                            UserInfo userInfo = new UserInfo(email.getText().toString(), displayName.getText().toString(), imageUrl.toString(),
                                    description.getText().toString(), website.getText().toString(), phoneNumber.getText().toString());
                            databaseReference.setValue(userInfo);

                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(EditProfileActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            // Setting progressDialog Title.
                            progressDialog.setTitle("Please wait , we are saving what you edit "+ progress+"%");
                            if(progress>=100) {
                                Intent intent = new Intent(EditProfileActivity.this, ViewSelfProfile.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
        else {

            Toast.makeText(EditProfileActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

}
