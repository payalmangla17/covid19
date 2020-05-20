package com.example.covid19.model;

public class User
{
    public String categories,address,add_lat,add_long;

    public User(String categories, String address,String add_lat,String add_long) {
        this.categories = categories;
        this.address = address;
        this.add_lat=add_lat;
        this.add_long=add_long;


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
        return "User{" +
                "Categories='" + categories + '\'' +
                ", Address='" + address + '\'' +
                '}';
    }
}
