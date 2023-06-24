package com.technifysoft.comicbookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class resetPassword extends AppCompatActivity {

    TextView textView;
    Button btnComfirm;
    EditText edt_old_pass,edt_new_pass,edt_comfirm_pass;

    //    Các event cần xử lý là : 1 kiểm tra trống, kiểm tra có khớp không , nếu khớp thì sử dụng fireBaseuser dùng để get về Current user rồi toast ra màn hình
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        textView = findViewById(R.id.RePass_back);
        btnComfirm = findViewById(R.id.btn_ComfirmPass);
        edt_old_pass = findViewById(R.id.edt_old_pass);
        edt_new_pass = findViewById(R.id.edt_new_pass);
        edt_comfirm_pass = findViewById(R.id.edt_comfirm_pass);

        back();
        btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RePassWord();
            }
        });
    }

    private void RePassWord() {
        btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_old_pass.getText().toString().trim().isEmpty() && edt_new_pass.getText().toString().trim().isEmpty() && edt_comfirm_pass.getText().toString().trim().isEmpty() ){
                    Toast.makeText(resetPassword.this,"Bạn phải nhập đầy đủ thông tin!",Toast.LENGTH_LONG).show();
                }else{
                    if(edt_new_pass.getText().toString().equals(edt_comfirm_pass.getText().toString())){
                        ComfirmPass1(edt_old_pass.getText().toString(),edt_new_pass.getText().toString());
                    }else{
                        Toast.makeText(resetPassword.this,"Mật khẩu không khớp!",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    private void ComfirmPass1(String passOld, String passNew) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), passOld);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(passNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(resetPassword.this, "Thay đổi mật khẩu thành công!",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(resetPassword.this, com.technifysoft.comicbookapp.Login_SignUp.class);
                                startActivity(intent);
//                                dùng finish luôn có nghĩa là không cần return lại nữa mà ở tại Login&signUp luôn
                                finish();
                            }
                            else{
                                Toast.makeText(resetPassword.this, "Thay đổi mật khẩu không thành công!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(resetPassword.this,"Thay đổi mật khẩu không thành công!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void ComfirmPass() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), edt_old_pass.getText().toString().trim());

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(edt_new_pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(resetPassword.this, "Thay đổi mật khẩu thành công!",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(resetPassword.this, com.technifysoft.comicbookapp.Login_SignUp.class);
                                startActivity(intent);
//                                dùng finish luôn có nghĩa là không cần return lại nữa mà ở tại Login&signUp luôn
                                finish();
                            }
                            else{
                                Toast.makeText(resetPassword.this, "Thay đổi mật khẩu khong thành công!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(resetPassword.this,"Thay đổi mật khẩu không thành công!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void back() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(resetPassword.this, com.technifysoft.comicbookapp.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}