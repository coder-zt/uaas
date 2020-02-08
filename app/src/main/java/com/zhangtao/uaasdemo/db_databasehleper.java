package com.zhangtao.uaasdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class db_databasehleper extends SQLiteOpenHelper {

    /*
    */
    public db_databasehleper(@Nullable Context context){
        super(context, db_constants.DATABAS_NAME, null, db_constants.VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        //专业信息表
        String sql = "create table " + db_constants.TABLE_PROFESSION + "(_id integer primary key autoincrement, name varchar unique, teacher_id integer);";
        db.execSQL(sql);
        //课程信息表
        sql = "create table " + db_constants.TABLE_COURSE + "(_id integer primary key autoincrement, name varchar, credit varchar, time varchar, pro_id integer, teacher_id integer);";
        db.execSQL(sql);
        //学生信息表
        sql = "create table " + db_constants.TABLE_STUDENT + "(_id integer, student_id varchar, sex varchar, phone varchar, user_psd varcahr, pro_id integer);";
        db.execSQL(sql);
        //教师信息表
        sql = "create table " + db_constants.TABLE_TEACHER + "(_id integer primary key autoincrement, name varchar,work_index varchar, position varchar,user_psd varcahr, pro_id integer);";
        db.execSQL(sql);
        //选课信息表
        sql = "create table " + db_constants.TABLE_STU_COURSE + "(_id integer, stu_id integer, cou_id integer, grade integer);";
        db.execSQL(sql);
        sql = "CREATE unique INDEX unique_info on " + db_constants.TABLE_COURSE +" (name, pro_id);";
        db.execSQL(sql);
//        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //版本1-》版本2

    }
}
