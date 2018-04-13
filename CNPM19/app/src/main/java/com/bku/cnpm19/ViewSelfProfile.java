package com.bku.cnpm19;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bku.cnpm19.account.LoginActivity;
import com.bku.cnpm19.info.ImageUploadInfo;
import com.bku.cnpm19.viewimageprofile.RecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Welcome on 3/3/2018.
 */

public class ViewSelfProfile extends AppCompatActivity {
    private Button popUpMenu;
    private TextView userName,emailName,description,website;
    private ImageView avatar;

    public static final String Database_Path = "All_User_Info_Database";

    DatabaseReference databaseReference;
    DatabaseReference databaseReference2nd;
   // FirebaseAuth Auth;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;

    // Creating List of ImageUploadInfo class.
    List<ImageUploadInfo> list = new ArrayList<>();


        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfprofile);

        //declare
        userName=(TextView)findViewById(R.id.display_name);
        avatar=(ImageView) findViewById(R.id.avatar_user);
        emailName=(TextView)findViewById(R.id.username) ;
        description=(TextView)findViewById(R.id.description);
        website=(TextView)findViewById(R.id.website);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);
        // Setting 3 column Recycler View
        recyclerView.setLayoutManager(new GridLayoutManager(ViewSelfProfile.this, 3));


//        Get userId to get fileChild
        mAuth= FirebaseAuth.getInstance();
        String userId=mAuth.getCurrentUser().getUid().toString();

        //
            authListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                }
            };
        //Store

        //get UserInfo From DataBase
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child(userId);

        databaseReference.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                emailName.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.child("avatarURL").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Glide.with(ViewSelfProfile.this).load(dataSnapshot.getValue(String.class)).into(avatar);
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

                }
            });

            databaseReference.child("website").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    website.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        databaseReference2nd = FirebaseDatabase.getInstance().getReference("All_Image_Uploads_Database").child(userId);

        // Adding Add Value Event Listener to databaseReference.
        databaseReference2nd.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot snapshot) {
                                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                            ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
                                                            list.add(imageUploadInfo);

                                                        }
                                                        //list.add(imageUploadInfo);
                                                        //dao nguoc thu tu theo ngay thang
                                                        Collections.reverse(list);
                                                        adapter = new RecyclerViewAdapter(getApplicationContext(), list);

                                                        // chia list ra lam 3
                                                        recyclerView.setLayoutManager(new GridLayoutManager(ViewSelfProfile.this, 3));

                                                        recyclerView.setAdapter(adapter);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
        });

        popUpMenu=(Button) findViewById(R.id.profileMenu);
            popUpMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMenu();
                }
            });
    }

    private void ShowMenu(){
        PopupMenu MenuPopUp =new PopupMenu(this,popUpMenu);
        MenuPopUp.getMenuInflater().inflate(R.menu.menu_popup,MenuPopUp.getMenu());
        MenuPopUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit_profile_button:
                    {
                        list=new ArrayList<>();
                        Intent intent = new Intent(ViewSelfProfile.this, EditProfileActivity.class);
                        startActivity(intent);

                        //startActivity(new Intent(getApplicationContext(),EditProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    }
                    case R.id.accountSettings:
                    {
                        //Intent intent = new Intent(MainScreenActivity.this, MainLoginActivity.class);
                        //finish();
                        // startActivity(new Intent(getApplicationContext(),MainLoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    }

                    case R.id.yourPhoto:
                    {
                        //Intent intent = new Intent(MainScreenActivity.this, MainActivity.class);
                        //finish();
                        //  startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    }

                    case R.id.sign_out:{
                       signOut();
                        break;

                    }

                }
                return false;
            }
        });
        MenuPopUp.show();
    }
    public void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }
}

