package com.example.flicon.database;

public class Function {
    private long id;
    private String type;
    private String number;
    private String message;

    public Function() {

    }
    public Function(String type) {
        this.type = type;
    }
    public Function(String type, String number) {
        this.type = type;
        this.number = number;
    }
    public Function(String type, String number, String message) {
        this.type = type;
        this.number = number;
        this.message = message;
    }
    public Function(long id, String  type, String number, String message){
        this.id = id;
        this.type = type;
        this.number = number;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Function{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", number='" + number + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
