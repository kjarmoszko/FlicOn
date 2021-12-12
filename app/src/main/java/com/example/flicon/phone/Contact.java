package com.example.flicon.phone;

import java.text.Collator;

public class Contact implements Comparable<Contact> {
    private String name;
    private String phoneNumber;
    private long id;

    public Contact() {

    }

    public Contact(String name, String phoneNumber, long id) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int compareTo(Contact o) {
        if(getName() == null || o.getName() == null) {
            return 0;
        }
        else {
            Collator collator = Collator.getInstance();
            return collator.compare(getName().toLowerCase(), o.getName().toLowerCase());
        }
    }
}
