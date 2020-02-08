package com.zhangtao.uaasdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class activity_sec_register extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private static final String TAG = "activity_sec_register";
    private RadioButton rbTeacher,rbStudent;
    private LinearLayout llTeaher;
    private Button btnRegister;
    private EditText etInputname;
    private TextView tvProfession,tvPosition;
    private PopupWindow pwTableNames;
    private EditText etInputPsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_register);
        initView();

    }

    private void initView() {
        rbTeacher = findViewById(R.id.rb_teacher);
        rbStudent = findViewById(R.id.rb_student);
        rbStudent.setOnCheckedChangeListener(this);
        rbTeacher.setOnCheckedChangeListener(this);
        llTeaher = findViewById(R.id.ll_teacher);
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        etInputname = findViewById(R.id.et_input_name);
        tvPosition = findViewById(R.id.tv_position);
        tvPosition.setOnClickListener(this);
        tvProfession = findViewById(R.id.tv_input_profession);
        tvProfession.setOnClickListener(this);
        etInputPsd = findViewById(R.id.et_input_psd);
     }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(rbTeacher.isChecked()){
            llTeaher.setVisibility(View.VISIBLE);
        }
        if(rbStudent.isChecked()){
            llTeaher.setVisibility(View.GONE);
        }
    }

    /**
     * 创建弹出单选框的View
     * @param tableNames
     * @param v
     * @return
     */
    private View createPopList(final List<String> tableNames, View v){

        LinearLayout llTableNames = new LinearLayout(this);
        llTableNames.setOrientation(LinearLayout.VERTICAL);
        for(int i=0;i< tableNames.size();i++){
            // 设置每个item的样式
            TextView tvTableName = new TextView(this);
            tvTableName.setText(tableNames.get(i));
            tvTableName.setTextSize(12);
            tvTableName.setGravity(Gravity.CENTER);
            tvTableName.setId(v.getId());//设置Item的ID
            final int position = i;
            tvTableName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(v.getId()){
                        case R.id.tv_position:
                            tvPosition.setText(tableNames.get(position));
                            break;
                        case R.id.tv_input_profession:
                            tvProfession.setText(tableNames.get(position));
                            break;
                    }
                    if(pwTableNames.isShowing()){
                        pwTableNames.dismiss();
                    }


                }
            });
            tvTableName.setBackground(getDrawable(R.drawable.table_name_show_background));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
            llTableNames.addView(tvTableName,lp);
            //设置水平线
            View view  = new View(this);
            view.setBackgroundColor(Color.rgb(255, 255, 255));
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
            llTableNames.addView(view, lp);
        }
        return llTableNames;
    }

    /**
     * PopupWindow的相关设置
     * @param tableNames
     * @param v
     */
    public void showData(List<String> tableNames, View v){
        View view = createPopList(tableNames, v);
        pwTableNames = new PopupWindow(view,v.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        pwTableNames.setOutsideTouchable(true);
        pwTableNames.setFocusable(true);
        pwTableNames.setAttachedInDecor(true);
        pwTableNames.showAsDropDown(v);
    }

    @Override
    public void onClick(View v) {
        db_dao dao = new db_dao(this);
        switch(v.getId()){
            case R.id.btn_register:
                if (rbTeacher.isChecked() && rbTeacher.isChecked()) {
                    if (etInputname.getText().toString().isEmpty() || etInputPsd.getText().toString().isEmpty() || tvProfession.getText().toString().equals("选择专业") || tvPosition.getText().toString().equals("选择职务")) {
                        Toast.makeText(this, "信息填写不全！请检查", Toast.LENGTH_SHORT).show();
                    }else if(etInputPsd.getText().toString().length() != 6){
                        Toast.makeText(this, "密码只能是6位数字", Toast.LENGTH_SHORT).show();
                    }else{
                        addTeacherdata(new String[]{etInputname.getText().toString(), tvPosition.getText().toString(), tvProfession.getText().toString(), etInputPsd.getText().toString()});
                    }
                }else{
                    Toast.makeText(this, "请选择身份", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_position:
                List result = new ArrayList<String>();
                result.add("讲师");
                result.add("副教授");
                result.add("教授");
                showData(result, v);
                break;
            case R.id.tv_input_profession:
                result = getAllProfessionName(dao);
                showData(result, v);
                break;
        }
    }

    private void addTeacherdata(String[] strings) {
        for(String s:strings)
            Log.d(TAG,s);
    }

    /**
     * 获取所有专业的名称
     * @param dao
     * @return
     */
    private List<String> getAllProfessionName(db_dao dao) {
        HashMap args = new HashMap();
        args.put(app_constants.MODE,app_constants.MODE_QUERY_ALL_PROFESSION_NAMES );
        HashMap qurey = dao.qurey(db_constants.TABLE_PROFESSION, args);
        return  (List<String>)qurey.get(app_constants.RE_LIST);
    }
}
