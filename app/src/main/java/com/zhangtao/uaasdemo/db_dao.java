package com.zhangtao.uaasdemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//为程序提供操作数据的操作
public class db_dao {
    final String TAG = "DB_Dao";
    private final db_databasehleper mhleper;
    db_dao(Context context){
        mhleper = new db_databasehleper(context);
    }

    /**
     * 插入操作
     * @param tableName
     * @param data
     * @return
     */
    public boolean insert(String tableName, Object data) {
        SQLiteDatabase db = mhleper.getWritableDatabase();
        return insert_table(db, tableName, data);
    }

    /**
     *查询操作
     * @param tableName 需要查询操作的表格
     * @param args 查询相关参数 mode 查询具体操作 args 所需要的参数
     * @return
     */
    public HashMap qurey(String tableName, HashMap args){
        SQLiteDatabase db = mhleper.getWritableDatabase();
        //查询数据表的所有信息
        if(args.get(app_constants.MODE).equals(app_constants.MODE_QUERY_ALL_INFO)){
            return qurey_table_detail_info(tableName, db);
        }
        switch(tableName){
            //专业数据表的操作
            case db_constants.TABLE_PROFESSION:
                //查询所有专业名称
                if(args.get(app_constants.MODE).equals(app_constants.MODE_QUERY_ALL_PROFESSION_NAMES)){
                    return qurey_table_profession_all_name(db, tableName);
                }
                //根据专业名称查询专业ID
                else if(args.get(app_constants.MODE).equals(app_constants.MODE_QUERY_PROFESSION_NAME_TO_ID)){
                    return qurey_table_profession_name_to_id(db, tableName, args);
                }
                break;
                //课程表的操作
            case db_constants.TABLE_COURSE:
                //根据专业名称查询专业课程
                if(args.get(app_constants.MODE).equals(app_constants.MODE_QUERY_PROFESSION_NAME_TO_COURSE)){
                    Log.d(TAG, args.get("pro_name").toString());
                    //获取专业ID
                    args.put(app_constants.MODE, app_constants.MODE_QUERY_PROFESSION_NAME_TO_ID);
                    int pro_id = (int)qurey(db_constants.TABLE_PROFESSION, args).get(app_constants.RE_INT);
                    //根据专业ID获取专业课程
                    return qurey_table_profession_name_to_course(db, pro_id);
                }
                //根据课程名称查询课程ID
                else if(args.get(app_constants.MODE).equals(app_constants.MODE_QUERY_COURSE_NAME_TO_ID)){
                    return qurey_table_course_name_to_id(db, tableName, args);
                }


        }
        return null;
    }

    /**
     * 根据专业ID查询专业课程
     * @param db
     * @param pro_id
     * @return key: RESList
     */
    private HashMap qurey_table_profession_name_to_course(SQLiteDatabase db, int pro_id) {
        String sql = "select name from " + db_constants.TABLE_COURSE + " where pro_id = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(pro_id)});
        List<String> result = new ArrayList<String>();
        while(cursor.moveToNext()){
            String name  = cursor.getString(cursor.getColumnIndex("name"));
            result.add(name);
        }
        HashMap reList = new HashMap();
        reList.put(app_constants.RE_LIST, result);
        return reList;
    }
    /**
     * 根据课程名称查询课程ID
     * @param db
     * @param tableName
     * @param args
     * @return RE_INT
     */
    private HashMap qurey_table_course_name_to_id(SQLiteDatabase db, String tableName, HashMap args) {
        String sql = "select _id from " + tableName + " where name = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{(String)args.get("course_name")});
        int _id = -1;
        HashMap result = new HashMap();
        while(cursor.moveToNext()){
            _id  = cursor.getInt(cursor.getColumnIndex("_id"));
        }
        cursor.close();
        if(_id == -1){
            return null;
        }
        result.put(app_constants.RE_INT, _id);
        return result;
    }


    /**
     * 根据专业名称查询专业ID
     * @param db
     * @param tableName
     * @param args
     * @return RE_INT
     */
    private HashMap qurey_table_profession_name_to_id(SQLiteDatabase db, String tableName, HashMap args) {
        String sql = "select _id from " + tableName + " where name = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{(String)args.get("pro_name")});
       int _id = -1;
        HashMap result = new HashMap();
        while(cursor.moveToNext()){
            _id  = cursor.getInt(cursor.getColumnIndex("_id"));
        }
        cursor.close();
        if(_id == -1){
         return null;
        }
        result.put(app_constants.RE_INT, _id);
        return result;
    }

    /**
     * 返回专业数据表中的所有专业名称
     *
     * @param db
     * @param tableName
     * @return key: RESList
     */
    private HashMap qurey_table_profession_all_name(SQLiteDatabase db, String tableName) {
        String sql = "select name from " + tableName + ";";
        Cursor cursor = db.rawQuery(sql, null);
        List <String>RESList = new ArrayList<String>();
        HashMap result = new HashMap();
        while(cursor.moveToNext()){
           String name = cursor.getString(cursor.getColumnIndex("name"));
           RESList.add(name);
        }
        cursor.close();
        result.put(app_constants.RE_LIST, RESList);
        return result;
    }

    //更新
    public boolean update(String tableName, String[] args){
        SQLiteDatabase db = mhleper.getWritableDatabase();
        switch(tableName){
            //专业数据表的操作
            case db_constants.TABLE_PROFESSION:
                return update_table_profession(db, db_constants.TABLE_PROFESSION, args);
            default:
                return false;
        }
    }

    //删除
    public boolean delete(String tableName,String[] args){
        SQLiteDatabase db = mhleper.getWritableDatabase();
        switch (tableName){
            case db_constants.TABLE_PROFESSION:
                return delete_table_profession(db, tableName, args);

        }
        return  false;
    }

    /**
     * 查找所有数据表的名称
     */
    public List qurey_all_table_name(){
        List<String> listTableNames= new ArrayList<String>();
        SQLiteDatabase db = mhleper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while(cursor.moveToNext()){
            //遍历出表名
            String name = cursor.getString(0);
           listTableNames.add(name);
        }
        return listTableNames;
    }
    //根据专业id删除专业
    private boolean delete_table_profession(SQLiteDatabase db, String tableName, String[] args){
        String sql = "delete from " + tableName + " where _id = ?;";
        db.execSQL(sql, args);
        return  true;
    }

    //更新专业主任
    private boolean update_table_profession(SQLiteDatabase db,String tableName, String[] args){
        String sql = "updata " + tableName + " set teach_id = ? where _id = ?";
        db.execSQL(sql, args);
        return true;
    }

    //查询所有专业
    private HashMap qurey_table_detail_info(String tableName , SQLiteDatabase db){
        String sql = "select * from " + tableName + ";";
        Cursor cursor = db.rawQuery(sql, null);
        HashMap result = new HashMap();
        //获取列名
        String[] ColumnNames = cursor.getColumnNames();
        result.put("ColumnNames",ColumnNames);
        String[] columnContext = new String[ColumnNames.length];
        int countLine = 0;
        while(cursor.moveToNext()){
            for(int i=0;i<ColumnNames.length;i++){
                int index = cursor.getColumnIndex(ColumnNames[i]);
                columnContext[i] = cursor.getString(index);
            }
            result.put(Integer.toString(countLine),columnContext.clone());
            countLine ++;
        }
        cursor.close();
       return result;
    }

    //查询专业主任名称
    private String[] qurey_table_profession_teacher(SQLiteDatabase db,String[] args){
        String sql = "select pro_id from " + db_constants.TABLE_PROFESSION + " where _id = ?;";
        Cursor cursor = db.rawQuery(sql, args);
        String[] result = null;
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex("pro_id");
            int pro_id =cursor.getInt(index);
        }
        cursor.close();
        // 待补充===================================================================================
//        result = 根据pro_id查询教师名称;
        return result;
    }

    /**
     * 将数据添加到数据表中
     * @param db
     * @param tableName
     * @param data
     * @return
     */
    private boolean insert_table(SQLiteDatabase db,String tableName,Object data){
        String sql="";
        data_object.tabledata tabledata= (data_object.tabledata)data;
        switch (tableName){
            case db_constants.TABLE_PROFESSION:
                sql= "insert into " + tableName  + " (name, teacher_id)values(?,?);";
                break;
            case db_constants.TABLE_COURSE:
                sql= "insert into " + tableName  + "(name, credit, time, pro_id, teacher_id) values(?,?,?,?,?)";
                break;
            case db_constants.TABLE_TEACHER:
                sql= "insert into " + tableName  + "(name, position,work_index, user_psd ,pro_id, course_id) values(?,?,?,?,?,?)";
                break;
        }
        try{
            db.execSQL(sql, tabledata.toStringArray());
            return true;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 提供外部调用函数
     * @param name
     * @return
     */
    public int get_maxid(String name){
           return getMaxId(name);
    }

    /**
     * 获取数据表ID的最大值以便设置新插入数据的ID值
     * @param tableName
     * @return
     */
    private int getMaxId(String tableName){
        int maxId = -1;
        SQLiteDatabase db = mhleper.getWritableDatabase();
        String sql = null;
        int count = 0;
        sql = "select count(*) from " + tableName + ";";
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex("count(*)");
            count =cursor.getInt(index);
        }
        cursor.close();
        if(count == 0){
            maxId = 0;
        }else{
            sql = "select max(_id) from " + tableName + ";";
            cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()){
                int index = cursor.getColumnIndex("max(_id)");
                maxId =cursor.getInt(index);
            }
        }
        return maxId;
    }

}
