package com.bku.cnpm19.post;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bku.cnpm19.BottomNavigationViewHelper;
import com.bku.cnpm19.databinding.ActivityViewimageBinding;
import com.bku.cnpm19.info.ImageUploadInfo;
import com.bku.cnpm19.info.PostInfo;
import com.bku.cnpm19.info.UserInfo;
import com.bku.cnpm19.likedpost.LikedThisPostFragment;
import com.bku.cnpm19.MainScreenActivity;
import com.bku.cnpm19.R;
import com.bku.cnpm19.viewimage.CreateData;
import com.bku.cnpm19.viewimage.listener.OnClickShowImageListener;
import com.bku.cnpm19.viewimage.model.Constant;
import com.bku.cnpm19.viewimage.model.Profile;
import com.bku.cnpm19.viewimage.view.activity.ShowImageActivity;
import com.bku.cnpm19.viewimage.view.activity.ViewImageFragment;
import com.bku.cnpm19.viewimage.view.adapter.ProfileAdapter;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Welcome on 3/22/2018.
 */

public class ViewPostActivity extends AppCompatActivity implements OnClickShowImageListener {

    private static final String TAG = "View Post Activity";
    private static final int ACTIVITY_NUM = 1;
    public static final String Database_Path="All_User_Info_Database";
    public static final String Post_Path = "All_Post_Info_Database";
    public static final String Image_Post="Image_Of_Post_Database";
    public static final String Liked_Post="All_Liked_Database";
    public static final String All_Image="All_Image_Uploads_Database";
    private TextView   mUsername, mTimestamp, mLikes,mComments;
    private EditText mCaption;
    private ImageView mPostImage, mBackArrow, mEllipses, mHeartRed, mHeartWhite, mProfileImage, mComment;
    public String Image_Of_Post="";
    private ProfileAdapter mProfileAdapter;
    private List<Profile> mProfileList;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2nd;
    DatabaseReference databaseReference3rd;
    DatabaseReference databaseReference4th;
    DatabaseReference databaseReference5th;
    private BottomNavigationViewEx bottomNavigationView;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.


    List<ImageUploadInfo> list = new ArrayList<>();
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpost);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        //Khai bao

        bottomNavigationView = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        mBackArrow = (ImageView) findViewById(R.id.backArrow);
        mCaption = (EditText) findViewById(R.id.image_caption);
        mUsername = (TextView) findViewById(R.id.username);
        mTimestamp = (TextView) findViewById(R.id.image_time_posted);
        mEllipses = (ImageView) findViewById(R.id.ivEllipses);
        mHeartRed = (ImageView) findViewById(R.id.image_heart_red);
        mHeartWhite = (ImageView) findViewById(R.id.image_heart);
        mProfileImage = (ImageView) findViewById(R.id.profile_photo);
        mLikes = (TextView) findViewById(R.id.image_likes);
        mComment = (ImageView) findViewById(R.id.speech_bubble);
        mComments = (TextView) findViewById(R.id.image_comments_link);
      recyclerView = (RecyclerView) findViewById(R.id.recycler_view_profile);

        setupFirebaseAuth();

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);



        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewPostActivity.this));

        mAuth=FirebaseAuth.getInstance();
        final String userId=mAuth.getCurrentUser().getUid().toString();

        Bundle extras=getIntent().getExtras();
        final String postId=extras.getString("postID");

        final String[] imageUrl = {""};
        mCaption.setText(extras.getString("Caption"));

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child(userId);

        databaseReference.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsername.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("avatarURL").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageUrl[0] =dataSnapshot.getValue(String.class);
                Glide.with(ViewPostActivity.this).load(dataSnapshot.getValue(String.class)).into(mProfileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference2nd=FirebaseDatabase.getInstance().getReference(Image_Post).child(postId);

        databaseReference2nd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<String> arrayImageURL=new ArrayList<String>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
                   // list.add(imageUploadInfo);
                    arrayImageURL.add(imageUploadInfo.getImageURL());

                }
          //      mBinding = DataBindingUtil.setContentView(ViewPostActivity.this, R.layout.activity_viewimage);
                initData(userId,postId,1,arrayImageURL);
                initViews();
              /*  Bundle bundle=new Bundle();
                bundle.putString("USERID",userId);
                bundle.putString("POSTID",postId);
                bundle.putString("LIKED",mLikes.getText().toString());
                bundle.putStringArrayList("arrayImageURL",arrayImageURL);

                ViewImageFragment viewImage=new ViewImageFragment();
                viewImage.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().add(R.id.recycler_view_profile,viewImage).commit();*/
              /*  FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragmentHorizon,new ViewImageFragment());
                fragmentTransaction.commit();*/
               /* adapter = new RecyclerViewAdapter(getApplicationContext(), list);

                recyclerView.setAdapter(adapter);*/
           //     new ViewImageActivity(postId,userId,Long.parseLong(mLikes.getText().toString()),arrayImageURL);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference3rd = FirebaseDatabase.getInstance().getReference(Post_Path).child(postId);

        databaseReference3rd.child("liked").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLikes.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference4th=FirebaseDatabase.getInstance().getReference(Liked_Post).child(postId);

        databaseReference4th.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userId)){
                    mHeartRed.setVisibility(View.VISIBLE);
                    mHeartWhite.setVisibility(View.INVISIBLE);

                }
                else
                {
                    mHeartRed.setVisibility(View.INVISIBLE);
                    mHeartWhite.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mHeartWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  final String Liked = "";
                databaseReference3rd.child("liked").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String Liked=dataSnapshot.getValue(String.class);
                        int numLiked=Integer.parseInt(Liked);
                        Liked=String.valueOf(numLiked+1);
                        PostInfo postInfo=new PostInfo(postId,userId,Liked,mCaption.getText().toString());
                        //if set value same child will it consider update children?
                        databaseReference3rd.setValue(postInfo);
                        UserInfo userInfo=new UserInfo("",mUsername.getText().toString(),imageUrl[0],"","","");
                        databaseReference4th.child(userId).setValue(userInfo);


                        //Reload activity without delay
                        Intent intent = getIntent();
                        overridePendingTransition(0, 0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mHeartRed.setVisibility(View.VISIBLE);
                mHeartWhite.setVisibility(View.INVISIBLE);

            }
        });

        mHeartRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference3rd.child("liked").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String Liked=dataSnapshot.getValue(String.class);
                        int numLiked=Integer.parseInt(Liked);
                        Liked=String.valueOf(numLiked-1);
                        PostInfo postInfo=new PostInfo(postId,userId,Liked,mCaption.getText().toString());
                        //if set value same child will it consider update children?
                        databaseReference3rd.setValue(postInfo);

                        databaseReference4th.child(userId).removeValue();

                        ReLoadActivity();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mHeartRed.setVisibility(View.INVISIBLE);
                mHeartWhite.setVisibility(View.VISIBLE);

            }
        });

        //go to mainscreen
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainScreenActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        mLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewPostActivity.this,LikedThisPostFragment.class));
            }
        });


        databaseReference5th=FirebaseDatabase.getInstance().getReference(All_Image).child(userId);

        mEllipses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildName();

                ShowMenu();
            }
        });

        setupBottomNavigationView();



    }

    private void ReLoadActivity(){
        //Reload activity without delay
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
    private void ShowMenu(){
        PopupMenu popupMenu =new PopupMenu(this,mEllipses);
        popupMenu.getMenuInflater().inflate(R.menu.menupost,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit_post:
                    {
                        mCaption.setEnabled(true);
                        mCaption.requestFocus();
                        mCaption.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                if(actionId== EditorInfo.IME_ACTION_DONE){
                                    mCaption.setEnabled(false);
                                    return true;
                                }
                                return false;
                            }
                        });
                        // startActivity(new Intent(getApplicationContext(),ViewSelfProfile.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    }
                    case R.id.delete_post:
                    {
                        databaseReference2nd.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String [] keySplit=Image_Of_Post.split("#");
                                int size=keySplit.length;
                                int i=0;
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    if(ds.getKey()==keySplit[i]){
                                        databaseReference2nd.child(ds.getKey()).removeValue();
                                        i=i+1;
                                    }

                                    if(i==size){
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        databaseReference3rd.removeValue();
                        databaseReference4th.removeValue();


                        startActivity(new Intent(getApplicationContext(),MainScreenActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    }



                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void getChildName(){
        databaseReference2nd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    if(i==0){
                        Image_Of_Post=ds.getKey();
                    }
                    else Image_Of_Post = Image_Of_Post + "#" + ds.getKey();
                    i=i+1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation( ViewPostActivity.this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
      

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }

    private void initData(String postId,String userId,long liked,ArrayList<String>ImageURL) {
        mProfileList = CreateData.createProfile(userId,postId,liked,ImageURL);
    }


    private void initViews() {
        mProfileAdapter = new ProfileAdapter(this, mProfileList, this);
        recyclerView.setAdapter(mProfileAdapter);
   //     mBinding.recyclerViewProfile.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClickShowImage(Profile profile, int positionImage) {
       /* Intent intent = new Intent(this, ShowImageActivity.class);
        intent.putExtra(Constant.KEY_POSITION_IMAGE, positionImage);
        intent.putExtra(Constant.KEY_DATA_PROFILE, profile);
        startActivity(intent);*/
    }
}
