package org.example.models.viewModel;

import org.example.models.Teacher;

public class TeacherGroup extends Teacher {

    private int groupId;
    private String groupName;

    public TeacherGroup() {
    }

    public TeacherGroup(String fullName, String groupName) {
        super(fullName);
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "TeacherGroup{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                "} " + super.toString();
    }
}
