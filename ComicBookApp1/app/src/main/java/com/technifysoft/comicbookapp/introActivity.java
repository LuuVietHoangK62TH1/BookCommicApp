package com.technifysoft.comicbookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class introActivity extends AppCompatActivity {

    ImageView img,appName,logo;
    LottieAnimationView lottieAnimationView,lottieAnimationView1;

    Button buttonGetStart;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

//        PHẦN INTRO
//        tham chiếu
        img = (ImageView) findViewById(R.id.img);
        appName = (ImageView) findViewById(R.id.app_name);
//        logo = (ImageView) findViewById(R.id.logo);
        lottieAnimationView = findViewById(R.id.lottie);
        lottieAnimationView1 = findViewById(R.id.openbook);

        buttonGetStart = findViewById(R.id.btnGetStart);

        lottieAnimationView.animate().translationY(1600).setDuration(1000).setStartDelay(2500);
//        lottieAnimationView1.animate().translationY(1600).setDuration(1000).setStartDelay(4000);

//       setDuration - khoảng thời gian dịch chuyển / SetstartDelay - khoảng thời gian delay của object
        buttonGetStart.animate().translationY(-750).setDuration(1000).setStartDelay(2500);


        buttonGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swichToLogin_SignUp();
            }
        });
    }

    private void swichToLogin_SignUp() {
        Intent intent = new Intent(introActivity.this,Login_SignUp.class);
        startActivity(intent);
    }
}