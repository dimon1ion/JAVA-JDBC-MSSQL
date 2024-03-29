package org.example.models;

public class SchoolGroup {

    private int id;
    private String name;
    private int teacherId;

    public SchoolGroup() {
    }

    public SchoolGroup(String name) {
        this.name = name;
    }

    public SchoolGroup(String name, int teacherId) {
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
        return "SchoolGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacherId=" + teacherId +
                '}';
    }
}
