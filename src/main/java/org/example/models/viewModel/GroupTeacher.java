package org.example.models.viewModel;

import org.example.models.SchoolGroup;

public class GroupTeacher extends SchoolGroup {

    private int teacherId;
    private String teacherFullName;

    public GroupTeacher() {
    }

    public GroupTeacher(String name, String teacherFullName) {
        super(name);
        this.teacherFullName = teacherFullName;
    }

    @Override
    public int getTeacherId() {
        return teacherId;
    }

    @Override
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherFullName() {
        return teacherFullName;
    }

    public void setTeacherFullName(String teacherFullName) {
        this.teacherFullName = teacherFullName;
    }

    @Override
    public String toString() {
        return "GroupTeacher{" +
                "teacherId=" + teacherId +
                ", teacherFullName='" + teacherFullName + '\'' +
                "} " + super.toString();
    }
}
