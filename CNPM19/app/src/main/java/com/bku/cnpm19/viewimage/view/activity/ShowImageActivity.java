package com.bku.cnpm19.viewimage.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.bku.cnpm19.R;


import com.bku.cnpm19.viewimage.model.Constant;
import com.bku.cnpm19.viewimage.model.Profile;
import com.bku.cnpm19.viewimage.view.binding.ViewBindingAdapter;
import com.bku.cnpm19.databinding.ActivityShowImageBinding;

public class ShowImageActivity extends AppCompatActivity {
    private ActivityShowImageBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_show_image);
        initViews();
    }

    private void initViews() {
        Intent intent = getIntent();
        if (intent == null) return;
        int positionImage = intent.getIntExtra(Constant.KEY_POSITION_IMAGE, -1);
        if (positionImage != -1) {
            Profile profile = (Profile) intent.getSerializableExtra(Constant.KEY_DATA_PROFILE);
            if (profile != null) {
                String urlImage = profile.getImageList().get(positionImage);
                ViewBindingAdapter.loadImage(mBinding.imageShow, urlImage);
            }
        }
    }
}
