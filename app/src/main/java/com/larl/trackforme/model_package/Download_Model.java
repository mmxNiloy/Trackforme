package com.larl.trackforme.model_package;

public class Download_Model {
    String contact , loc , name ,  salary , workday;

    public Download_Model() {
    }

    public Download_Model(String contact, String loc, String name, String salary, String workday) {
        this.contact = contact;
        this.loc = loc;
        this.name = name;
        this.salary = salary;
        this.workday = workday;
    }

    // Getter Setter

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getWorkday() {
        return workday;
    }

    public void setWorkday(String workday) {
        this.workday = workday;
    }


}
