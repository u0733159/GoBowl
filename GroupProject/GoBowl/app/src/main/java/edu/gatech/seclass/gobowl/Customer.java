package edu.gatech.seclass.gobowl;

import java.util.ArrayList;

/**
 * Created by R on 7/4/2016.
 */
public class Customer {
    private String first_name;
    private String last_name;
    private String email;
    private String id;
    private Boolean isVip;
    private double yearSpending;
    private ArrayList<Score> historicScores;
    Customer(String name, String email){
        name=name;
        email=email;
        isVip=false;
        yearSpending=0;
    }

    Customer(Customer otherCustomer){
        if(otherCustomer != this){
            this.first_name = otherCustomer.getFirstName() ;
            this.last_name = otherCustomer.getLastName();
            this.email = otherCustomer.getEmail();
            this.isVip = otherCustomer.getVip();
            this.id = otherCustomer.getId();
            this.yearSpending = otherCustomer.getYearSpending();
        }
    }

    Customer(String id, String first_name, String last_name, String email, boolean isVip){
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.isVip = isVip;
    }
    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public Boolean getVip() {
        return isVip;
    }

    public String getFullName(){
        return (first_name + " " + last_name);
        }
    public double getYearSpending() {
        return yearSpending;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getNameAndId(){
        return (getFullName() + getId());
    }
    public void setVip(Boolean vip) {
        isVip = vip;
    }

    public void setYearSpending(double yearSpending) {
        this.yearSpending = yearSpending;
    }

    void setID(String random_id){
        id = random_id;
    }

    String printInfo(){
        String info= "FirstName:" + getFirstName() + "\n"
                   + "LastName:" + getLastName() + "\n"
                +"Id: " + getId() + "\n"
                +"email: "+getEmail();
        return info;
    }

    public String getInfoSingleLine(){
        String info= getFullName()  +" "+ getEmail() + " " + getId();
        return info;
    }
    public Customer(){}
}
