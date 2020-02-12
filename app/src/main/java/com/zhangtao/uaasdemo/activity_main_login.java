package com.zhangtao.uaasdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class activity_main_login extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "activity_main_login";
    private Button btnLogin;
    private TextView tvResgister;
    private RadioButton rbAmdin,rbStudent,rbTeacher;
    private EditText edAccount,edPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        initView();
    }

    /**
     * 界面初始化操作
     */
    private void initView() {
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        rbAmdin = findViewById(R.id.rb_admin);
        rbTeacher = findViewById(R.id.rb_teacher);
        edAccount = findViewById(R.id.ed_Account);
        edPassword = findViewById(R.id.ed_password);
        tvResgister = findViewById(R.id.tv_register);
        tvResgister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_login:
                if(rbAmdin.isChecked()){
                    if(true){
//                    if(edAccount.getText().toString().equals(getString(R.string.user)) && edPassword.getText().toString().equals(getString(R.string.password))){
                        Intent intent = new Intent(activity_main_login.this,activity_sec_admin.class);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(this, "账号和密码有误，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(rbTeacher.isChecked()){
                    if(edPassword.getText().toString().equals("获取密码")){
                        Intent intent = new Intent(activity_main_login.this,activity_sec_admin.class);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(this, "账号和密码有误，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.tv_register:
                Intent intent = new Intent(activity_main_login.this,activity_sec_register.class);
                startActivity(intent);
                break;
        }
    }
}
