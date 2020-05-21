package com.example.covid19.model;

public class user_profmodel {
    public String name, dob, gender,address,loc,city,pin;
    public user_profmodel(){

    }
    public user_profmodel(String name,String dob,String gender, String loc, String address, String city, String pin){
        this.name=name;
        this.dob=dob;
        this.gender=gender;
        this.loc=loc;
        this.address=address;
        this.city=city;
        this.pin=pin;

    }
    public String toString() {
        return "user_profile{ " + "name= ' " + name + '\'' + "dob= ' " + dob + '\'' +
                "gender= '" + gender + '\'' +
                "address='" + address + '\'' +
                "location='" + loc + '\'' +
                "city='" + city + '\'' +
                "pin= '" + city + '\'' +
                "}";

    }
}
