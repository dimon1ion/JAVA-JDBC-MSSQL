package org.example.models;

public class Group {

    private int id;
    private String name;
    private int teacherId;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public Group(String name, int teacherId) {
        this.name = name;
        this.teacherId = teacherId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacherId=" + teacherId +
                '}';
    }
}
