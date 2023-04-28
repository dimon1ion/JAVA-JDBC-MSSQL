package org.example.models;

public class Teacher {

    private int id;
    private String fullName;

    public Teacher() {
    }

    public Teacher(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
