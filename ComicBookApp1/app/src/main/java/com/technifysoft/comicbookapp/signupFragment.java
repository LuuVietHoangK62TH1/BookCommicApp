package com.technifysoft.comicbookapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static signupFragment newInstance(String param1, String param2) {
        signupFragment fragment = new signupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

//    Các bước xử lý đăng ký tài khoản Email
//    B1: Khai báo FireBase auth dùng để tạo tài khoản thông qua hàm CreatACC
//    B2: Dùng hàm Fetchprovider để kiểm tra Email có tồn tại không, nếu có thì thông báo ACC đã tồn tại , nếu chưa thì chúng ta tiến hành tạo
//    mới tài khoản


    FirebaseAuth mAuth;
    Button btnSignUp;
    EditText edtName,edtMail,edtPassEmail,edtComfirmPassMail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        btnSignUp = (Button) view.findViewById(R.id.SuBtn);
        edtName = (EditText) view.findViewById(R.id.SuName);
        edtMail = (EditText) view.findViewById(R.id.SuEmail);
        edtPassEmail = (EditText) view.findViewById(R.id.SuPass);
        edtComfirmPassMail = (EditText) view.findViewById(R.id.SuComfirmPass);

        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignUpAcc();
            }
        });

        return view;
    }

    private void SignUpAcc() {
        if(edtName.getText().toString().trim().isEmpty()){
            edtName.setError("Không được để trống tên!");
        }else if(edtMail.getText().toString().trim().isEmpty()){
            edtMail.setError("Không được để trống email!");
        }else if(edtPassEmail.getText().toString().trim().isEmpty()){
            edtPassEmail.setError("Không được để trống mật khẩu!");
        }else if(edtComfirmPassMail.getText().toString().trim().isEmpty()){
            edtComfirmPassMail.setError("Không được để trống mật khẩu");
        }else if(edtPassEmail.getText().toString().trim().length()< 6 || edtPassEmail.getText().toString().trim().length()>9){
            edtPassEmail.setError("Mật khẩu phải lớn hơn 6 kí tự!");
        }else if(!edtComfirmPassMail.getText().toString().trim().equals(edtPassEmail.getText().toString().trim())){
            edtComfirmPassMail.setError("Mật khẩu không khớp!");
        }else{
//            kiểm tra Acc trên Firebase đã tồn tại chưa
            String email = edtMail.getText().toString().trim();
            String pass = edtPassEmail.getText().toString().trim();
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    if(task.isComplete()){
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(),"Đăng ký thành công",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getContext(), com.technifysoft.comicbookapp.Login_SignUp.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getContext(),"Email đã được đăng ký",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}