package com.zhangtao.uaasdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class activity_sec_admin extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout llTableNames;
    private TextView tvTableName,tvSelector,tvTableContext, tvInputTime, tvInputProfession;
    private PopupWindow pwTableNames;
    private Button btnShowTables,btnInsertProfession, btnAddNweCourse;
    private EditText etInputProfession,etInputCourseName, etInputCredit;
    private View view;
    private final String TAG = "activity_sec_admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_admin);
        initView();
    }

    /**
     * 页面控件的初始化操作
     */
    private void initView() {
        btnShowTables = findViewById(R.id.btn_showdata);
        btnShowTables.setOnClickListener(this);
        tvSelector = findViewById(R.id.tv_selector);
        tvSelector.setOnClickListener(this);
        tvTableContext = findViewById(R.id.tv_table_context);
        btnInsertProfession = findViewById(R.id.btn_insert_profession);
        btnInsertProfession.setOnClickListener(this);
        etInputProfession = findViewById(R.id.et_input_profession);
        tvInputTime = findViewById(R.id.tv_input_time);
        tvInputTime.setOnClickListener(this);
        tvInputProfession = findViewById(R.id.tv_input_pro_id);
        tvInputProfession.setOnClickListener(this);
        btnAddNweCourse = findViewById(R.id.btn_insert_course);;
        btnAddNweCourse.setOnClickListener(this);
        etInputCourseName = findViewById(R.id.et_input_course);
        etInputCredit = findViewById(R.id.et_input_credit);
    }

    /**
     * 创建弹出单选框的View
     * @param tableNames
     * @param v
     * @return
     */
    private View createPopList(final List<String> tableNames, View v){

        llTableNames = new LinearLayout(this);
        llTableNames.setOrientation(LinearLayout.VERTICAL);
        for(int i=0;i< tableNames.size();i++){
            // 设置每个item的样式
            tvTableName = new TextView(this);
            tvTableName.setText(tableNames.get(i));
            tvTableName.setTextSize(12);
            tvTableName.setGravity(Gravity.CENTER);
            tvTableName.setId(v.getId());//设置Item的ID
            final int position = i;
            tvTableName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(v.getId()){
                        case R.id.tv_selector:
                            tvSelector.setText(tableNames.get(position));
                            break;
                        case R.id.tv_input_time:
                            tvInputTime.setText(tableNames.get(position));
                            break;
                        case R.id.tv_input_pro_id:
                            tvInputProfession.setText(tableNames.get(position));
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
            view  = new View(this);
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

    /**
     * 展示数据表中所有的数据
     * 将数据以文本的形式呈现再TextView中
     * @param dao
     */
    private void showTableContext(db_dao dao){
        HashMap args = new HashMap();
        args.put(app_constants.MODE, app_constants.MODE_QUERY_ALL_INFO);
        HashMap hmResult = dao.qurey(tvSelector.getText().toString(),args);
        String[] ColumnNames = (String[])hmResult.get("ColumnNames");
        String result = sArraytoString(ColumnNames);
        hmResult.remove("ColumnNames");

        for(Object s :hmResult.keySet()){
            String[] o =  (String[])hmResult.get(s);
            result += "\n" + sArraytoString(o);
        }
        tvTableContext.setText(result);
    }

    @Override
    public void onClick(View v) {
        db_dao dao = new db_dao(this);
        List<String> result;
        switch (v.getId()){
            case R.id.btn_showdata://显示数据表中的所有信息
                if(tvSelector.getText().toString().equals("选择数据表"))
                    return;
                showTableContext(dao);
                break;
            case R.id.tv_selector://展示所有数据表
                result = dao.qurey_all_table_name();
                showData(result, v);
                break;
            case R.id.btn_insert_profession://新增新专业
                addNewProfession(dao);
                break;
            case R.id.tv_input_time://选择课程时间
                result = new ArrayList<String>();
                result.add("2020-1");
                result.add("2020-2");
                result.add("2021-1");
                result.add("2021-2");
                showData(result, v);
                break;
            case R.id.tv_input_pro_id:
                result = getAllProfessionName(dao);
                showData(result, v);
                break;
            case R.id.btn_insert_course:
                addNewCourse(dao);
            default:
                Toast.makeText(this,Integer.toString(v.getId()), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 将新专业信息写入到数据库中
     * @param dao
     */
    private void addNewCourse(db_dao dao){
        String name = etInputCourseName.getText().toString();
        String credit = etInputCredit.getText().toString();
        String time = tvInputTime.getText().toString();
        String pro_name = tvInputProfession.getText().toString();
        if(name.isEmpty() || credit.isEmpty() || time.isEmpty()||pro_name.isEmpty()){
            Toast.makeText(this, "课程信息填写不完整！", Toast.LENGTH_SHORT).show();
            return;
        }
        //根据专业名称查询专业ID
        HashMap args = new HashMap();
        args.put(app_constants.MODE, app_constants.MODE_QUERY_PROFESSION_NAME_TO_ID);
        args.put("pro_name",pro_name);
        int pro_id = (int)dao.qurey(db_constants.TABLE_PROFESSION,args).get(app_constants.RE_INT);
        data_object.course data = new data_object.course(name, credit, time, pro_id);
        if (dao.insert(db_constants.TABLE_COURSE, data)) {
            Toast.makeText(this,"数据写入成功！",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"数据写入失败！",Toast.LENGTH_SHORT).show();
        }

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

    private String sArraytoString(String[] s){
        String line = "";
        for(String item:s){
            line += item + " | ";
        }
        return line;
    }

    /**
     *     向数据中插入专业
     */
    private void addNewProfession(db_dao dao){
        String proName = etInputProfession.getText().toString();
        if( !proName.equals("")){
            data_object.profession proAdd = new data_object.profession(proName);
            dao.insert(db_constants.TABLE_PROFESSION, proAdd);
        }else{
            Toast.makeText(this, "专业名称为空!", Toast.LENGTH_SHORT).show();
        }

    }
}
