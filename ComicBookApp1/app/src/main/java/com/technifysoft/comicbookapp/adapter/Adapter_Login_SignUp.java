package com.technifysoft.comicbookapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.technifysoft.comicbookapp.loginFragment;
import com.technifysoft.comicbookapp.signupFragment;

public class Adapter_Login_SignUp extends FragmentStatePagerAdapter {

    public Adapter_Login_SignUp(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    //   getitem sẽ lấy và hiển thị ra vị trí của fragment trên tabLayout
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new loginFragment();
            case 1:
                return new signupFragment();
            default:
                return new loginFragment();
        }
    }

    //    Trả về số cột hiển thị ra tablayout
    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Login";
                break;
            case 1:
                title = "Sign Up";
                break;
        }
        return title;
    }

}
