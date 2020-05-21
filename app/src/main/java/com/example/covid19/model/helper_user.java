package com.example.covid19.model;

public class helper_user
{
    public String full_name,mobile_no,Age;
    public helper_user(){

    }
    public helper_user(String full_name, String mobile_no, String age) {
        this.full_name = full_name;
        this.mobile_no = mobile_no;
        Age = age;

    }
    /*
        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }

        public String getEmail() {
            return Date_of_Birth;
        }

      //  public void setEmail(String email) {
        //    Date_of_Birth = dob;
        //}

        public String getCollege() {
            return Categories;
        }

       // public void setCollege(String college) {
        //    Categories = categories;
        //}
    */
    @Override
    public String toString() {
        return "helper_user{" +
                "full_name='" + full_name + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", Date_of_Birth='" + Age + '\'' +
                '}';
    }
}
