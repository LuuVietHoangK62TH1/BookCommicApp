package com.technifysoft.comicbookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.technifysoft.comicbookapp.adapter.Adapter_Main;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    TextView Navi_drawer_tv;
    FirebaseAuth firebaseAuth;

    ViewPager2 viewPager2;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_tool_bar);

        navigationView = findViewById(R.id.navigation_View);
        drawerLayout = findViewById(R.id.main_layout);


//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Event_toolBar();
        Navi_ShowInfUser();
        Event_NavigationDrawer();
        Event_ViewPagerBottomNavigationView();
    }

    //    event for NavigationDrawer
    private void Event_NavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Navi_drawer_home:
                        drawerLayout.close();
                        return true;
                    case R.id.Navi_drawer_logout:
                        Toast.makeText(MainActivity.this, "Đăng xuất thành công!", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                    case R.id.Navi_drawer_ChangePass:
                        Intent intent1 = new Intent(MainActivity.this, resetPassword.class);
                        startActivity(intent1);
                }
                return true;
            }
        });
    }

    //    event for Show Inf User
    private void Navi_ShowInfUser() {
        //     Use FireBaseUser to get Email or pass if u want and assign to View Inf of User
        Navi_drawer_tv = navigationView.getHeaderView(0).findViewById(R.id.Navi_drawer_tv1);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String email = user.getEmail();
        Navi_drawer_tv.setText(email);
    }

    //    event for popup Navigation drawer

    private void Event_toolBar() {
        toolbar = (Toolbar) findViewById(R.id.main_tool_bar);
//        setSuport khi được sử dụng sẽ giúp activity hiểu được yêu cầu
        setSupportActionBar(toolbar);
//        pop up out main_activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    // event for Navigation bottom
    private void Event_ViewPagerBottomNavigationView() {
        viewPager2 = findViewById(R.id.ViewPagerMain);
        bottomNavigationView = findViewById(R.id.NavigationBottomMain);

        bottomNavigationView.setOnNavigationItemSelectedListener(ReselectedItem);

        Adapter_Main adapter_main = new Adapter_Main(this);
        viewPager2.setAdapter(adapter_main);

//        hàm này dùng để link fragment với các bottom tương ứng
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.trang_chu).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.top_truyen).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.the_loai).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.tim_kiem).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.ca_nhan).setChecked(true);
                        break;
                }
            }
        });

    }

//    Khai báo ra một hàm dùng để reselected nghĩa là sẽ dùng hàm setOnNavigationItemSelectedListener và truyền vào nó hàm
//    Reselected . Hàm này nó sẽ có chức năng là sẽ trả ngược lại khi click vào bottom thì sẽ trả ngược lại cái
//    thằng fragment tương ứng của nó.

    private BottomNavigationView.OnNavigationItemSelectedListener ReselectedItem
            = item -> {
        switch (item.getItemId()) {
            case R.id.trang_chu:
                viewPager2.setCurrentItem(0);
                getSupportActionBar().setTitle("Trang Chủ");
                break;
            case R.id.top_truyen:
                viewPager2.setCurrentItem(1);
                getSupportActionBar().setTitle("Top Truyện");
                break;
            case R.id.the_loai:
                viewPager2.setCurrentItem(2);
                getSupportActionBar().setTitle("Thể Loại");
                break;
            case R.id.tim_kiem:
                viewPager2.setCurrentItem(3);
                getSupportActionBar().setTitle("Tìm Kiếm");
                break;
            case R.id.ca_nhan:
                viewPager2.setCurrentItem(4);
                getSupportActionBar().setTitle("Tài Khoản");
                break;
        }
        return false;
    };

}