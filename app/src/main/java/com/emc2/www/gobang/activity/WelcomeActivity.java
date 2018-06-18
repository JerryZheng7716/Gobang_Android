package com.emc2.www.gobang.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.emc2.www.gobang.R;

import java.util.Date;

/**
 * Created by 74011 on 2018/4/29.
 */

@SuppressLint("Registered")
public class WelcomeActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_activity);
        imageView = findViewById(R.id.logo);
        imageView.setImageResource(R.drawable.logo);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        Date dt = new Date();
        Long time = dt.getTime();
        animationDrawable.start();

        Animation animationWait = AnimationUtils.loadAnimation(this, R.anim.wait);
        imageView.startAnimation(animationWait);
        animationWait.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                alpha();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void alpha() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent2 = new Intent();
                intent2.setComponent(new ComponentName(WelcomeActivity.this, MainActivity.class));
                startActivity(intent2);
                WelcomeActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
