package com.technifysoft.comicbookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.technifysoft.comicbookapp.adapter.Adapter_Login_SignUp;
import com.google.android.material.tabs.TabLayout;

public class Login_SignUp extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;


    Button btnLogin;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        Adapter_Login_SignUp adapter_login_signUp = new Adapter_Login_SignUp(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter_login_signUp);
        tabLayout.setupWithViewPager(viewPager);

    }

}