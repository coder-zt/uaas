package com.zhangtao.uaasdemo;

/**
 * 数据表的对象用于数据保存
 */
class data_object {

    /**
     * 数据表的父类
     */
    static class tabledata{
        private int _id;

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String[] toStringArray(){
            return null;
        }
    }

    /**
     * 专业数据表
     */
    static class profession extends tabledata{
        int _id;
        String name;
        int pro_id;

        profession(int id, String name, int pro_id){
            this._id = id;
            this.name = name;
            this.pro_id = pro_id;
        }

        profession(String name){
            this.name = name;
            this._id = -1;
            this.pro_id = -1;
        }

        public int get_id() {
            return _id;
        }

        public void set_id(int id){
            this._id = id;
        }
        public String getName(){
            return this.name;
        }

        public int getPro_id(){
            return  pro_id;
        }

        public void setPro_id(int pro_id){
            this.pro_id = pro_id;
        }

        @Override
        public String[] toStringArray() {
            return new String[]{ this.name, Integer.toString(pro_id)};
        }
    }

    /**
     * 课程数据表
     */
    static class course extends tabledata{
        int _id;
        String name;
        String credit;
        String time;
        int teacher_id;
        int pro_id;

        public course(String name, String credit, String time, int pro_id) {
            this.name = name;
            this.credit = credit;
            this.time = time;
            this.pro_id = pro_id;
            this._id = -1;
            this.teacher_id = -1;
        }

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getTeacher_id() {
            return teacher_id;
        }

        public void setTeacher_id(int teacher_id) {
            this.teacher_id = teacher_id;
        }

        public int getPro_id() {
            return pro_id;
        }

        public void setPro_id(int pro_id) {
            this.pro_id = pro_id;
        }

        @Override
        public String[] toStringArray() {
            return new String[]{this.name, this.credit, this.time, Integer.toString(pro_id),Integer.toString(teacher_id)};
        }
    }

    /**
     * 教师数据表
     */
    static class teacher extends tabledata{

        int _id;
        String name;
        int pro_id;
        int course_id;
        String position;
        String user_psd;
        String work_index;

        public teacher(String name,String work_index,String position,  String user_psd, int pro_id, int course_id) {
            this.name = name;
            this.pro_id = pro_id;
            this.position = position;
            this.user_psd = user_psd;
            this.work_index = work_index;
            this.course_id = course_id;
        }

        @Override
        public int get_id() {
            return _id;
        }

        @Override
        public void set_id(int _id) {
            this._id = _id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public int getPro_id() {
            return pro_id;
        }

        public void setPro_id(int pro_id) {
            this.pro_id = pro_id;
        }

        public String getUser_psd() {
            return user_psd;
        }

        public void setUser_psd(String user_psd) {
            this.user_psd = user_psd;
        }

        public String getWork_index() {
            return work_index;
        }

        public void setWork_index(String work_index) {
            this.work_index = work_index;
        }

        public int getCourse_id() {
            return course_id;
        }

        public void setCourse_id(int course_id) {
            this.course_id = course_id;
        }


        @Override
        public String[] toStringArray() {
            return new String[]{this.name, this.position, this.work_index, this.user_psd, Integer.toString(this.pro_id), Integer.toString(this.course_id)};
        }
    }
}
