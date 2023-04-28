package org.example;

import org.example.models.SchoolGroup;
import org.example.models.Student;
import org.example.models.Teacher;
import org.example.models.viewModel.GroupTeacher;
import org.example.models.viewModel.TeacherGroup;
import org.example.services.JdbcService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            JdbcService jdbcService = new JdbcService();
            Scanner scanner = new Scanner(System.in);
            Scanner lineScanner = new Scanner(System.in);
            int menu;
            while (true){
                System.out.print("1.Show teachers" +
                        "\n2.Show groups" +
                        "\n3.Exit" +
                        "\nEnter num => ");
                try {
                    menu = scanner.nextInt();
                } catch (Exception e) {
                    menu = 0;
                    scanner = new Scanner(System.in);
                }
                switch (menu){
                    case 1:
                        while(true) {
                            System.out.println("\nTeachers:");
                            List<TeacherGroup> teacherGroupList = jdbcService.getTeacherGroups();
                            if (teacherGroupList.isEmpty()){
                                System.out.println("Empty");
                            }
                            else {
                                for (TeacherGroup teacherGroup : teacherGroupList) {
                                    System.out.println(teacherGroup.getFullName() + "| Group:"
                                            + (teacherGroup.getGroupName() == null
                                            ? "none" : teacherGroup.getGroupName()));
                                }
                            }
                            System.out.print("\n1.Add a teacher" +
                                    "\n2.Add teacher to group" +
                                    "\n3.Delete teacher" +
                                    "\n4.Delete teacher from group" +
                                    "\n5.<--Back" +
                                    "\nEnter num => ");
                            try {
                                menu = scanner.nextInt();
                            } catch (Exception e) {
                                menu = 0;
                                scanner = new Scanner(System.in);
                            }
                            System.out.println("");
                            switch (menu) {
                                case 1:
                                    System.out.print("Enter full name => ");
                                    String fullName = lineScanner.nextLine();
                                    jdbcService.createTeacher(new Teacher(fullName));
                                    continue;
                                case 2:
                                    if (!teacherGroupList.isEmpty()){
                                        System.out.println("Choose teacher:");
                                        int teacherGroupIndex = chooseTeacherGroup(teacherGroupList);

                                        if (0 <= teacherGroupIndex && teacherGroupIndex < teacherGroupList.size()){
                                            List<GroupTeacher> groupTeacherList = jdbcService.getGroupTeachers();
                                            if (!groupTeacherList.isEmpty()){
                                                System.out.println("Choose group:");
                                                int groupTeacherIndex = chooseGroupTeacher(groupTeacherList);

                                                if (0 <= groupTeacherIndex && groupTeacherIndex < groupTeacherList.size()){
                                                    jdbcService.setTeacherIdGroup(groupTeacherList.get(
                                                            groupTeacherIndex).getId(),
                                                            teacherGroupList.get(teacherGroupIndex).getId());
                                                } else {
                                                    System.out.println("<--Back");
                                                }
                                            }
                                            else {
                                                System.out.println("Empty, add at least one group to continue");
                                            }
                                        } else {
                                            System.out.println("<--Back");
                                        }
                                    }
                                    else {
                                        System.out.println("Empty, add at least one teacher to continue");
                                    }
                                    continue;
                                case 3:
                                    if (!teacherGroupList.isEmpty()) {
                                        System.out.println("Choose teacher:");
                                        int teacherGroupIndex = chooseTeacherGroup(teacherGroupList);
                                        if (0 <= teacherGroupIndex && teacherGroupIndex < teacherGroupList.size()){
                                            jdbcService.deleteTeacher(teacherGroupList.get(teacherGroupIndex).getId());
                                        }
                                        else {
                                            System.out.println("<--Back");
                                        }
                                    } else {
                                        System.out.println("No teachers to delete");
                                    }

                                    continue;
                                case 4:
                                    List<GroupTeacher> busyGroupTeachers = jdbcService.getBusyGroupTeachers();
                                    if (!busyGroupTeachers.isEmpty()) {
                                        System.out.println("Choose group:");
                                        int groupTeacherIndex = chooseGroupTeacher(busyGroupTeachers);
                                        if (0 <= groupTeacherIndex && groupTeacherIndex < teacherGroupList.size()){
                                            jdbcService.deleteTeacherFromSchoolGroup(busyGroupTeachers
                                                    .get(groupTeacherIndex)
                                                    .getId());
                                        }
                                        else {
                                            System.out.println("<--Back");
                                        }
                                    } else {
                                        System.out.println("No teachers to delete from group");
                                    }
                                    continue;
                                default:
                                    break;
                            }
                            break;
                        }
                        continue;
                    case 2:
                        while (true) {
                            System.out.println("\nGroups:");
                            List<GroupTeacher> groupTeacherList = jdbcService.getGroupTeachers();
                            if (!groupTeacherList.isEmpty()){
                                printGroupTeacher(groupTeacherList);
                            } else {
                                System.out.println("Empty\n");
                            }
                            System.out.print("\n1.Create group" +
                                    "\n2.Show students in group" +
                                    "\n3.Delete group" +
                                    "\n4.<--Back" +
                                    "\nEnter num => ");
                            try {
                                menu = scanner.nextInt();
                            } catch (Exception e) {
                                menu = 0;
                                scanner = new Scanner(System.in);
                            }
                            System.out.println();
                            switch (menu){
                                case 1:
                                    System.out.print("Enter group name => ");
                                    String groupName = lineScanner.nextLine();
                                    jdbcService.createSchoolGroup(new SchoolGroup(groupName));
                                    continue;
                                case 2:
                                    if (!groupTeacherList.isEmpty()) {
                                        System.out.println("Choose group:");
                                        int groupTeacherIndex = chooseGroupTeacher(groupTeacherList);
                                        if (0 <= groupTeacherIndex && groupTeacherIndex < groupTeacherList.size()) {
                                            GroupTeacher groupTeacher = groupTeacherList.get(groupTeacherIndex);
                                            while (true){
                                                System.out.println("\nGroup name: " + groupTeacher.getName() +
                                                        "\nTeacher: " + (groupTeacher.getTeacherFullName() == null
                                                        ? "none"
                                                        : groupTeacher.getTeacherFullName()) +
                                                        "\nStudents:");
                                                List<Student> students = jdbcService.getStudentsByGroupId(groupTeacher.getId());
                                                printStudents(students);

                                                System.out.print("\n1.Add student" +
                                                        "\n2.Delete Student" +
                                                        "\n3.<--Back" +
                                                        "\nEnter num => ");
                                                try {
                                                    menu = scanner.nextInt();
                                                } catch (Exception e) {
                                                    menu = 0;
                                                    scanner = new Scanner(System.in);
                                                }
                                                System.out.println();
                                                switch (menu){
                                                    case 1:
                                                        System.out.print("Enter student full name => ");
                                                        String fullName = lineScanner.nextLine();
                                                        jdbcService.createStudent(new Student(fullName, groupTeacher.getId()));
                                                        continue;
                                                    case 2:
                                                        if (students.isEmpty()){
                                                            System.out.println("Add student to delete");
                                                            continue;
                                                        }
                                                        System.out.println("Choose student" +
                                                                "\nGroup name: " + groupTeacher.getName() +
                                                                "\nTeacher: " + (groupTeacher.getTeacherFullName() == null
                                                                ? "none"
                                                                : groupTeacher.getTeacherFullName()) +
                                                                "\nStudents:");
                                                        int selectedStudentIndex = chooseStudent(students);
                                                        if (0 <= selectedStudentIndex && selectedStudentIndex < students.size()){
                                                            jdbcService.deleteStudent(students.get(selectedStudentIndex).getId());
                                                        } else {
                                                            System.out.println("<--Back\n");
                                                        }
                                                        continue;
                                                    default:
                                                        break;
                                                }
                                                break;
                                            }
                                        } else {
                                            System.out.println("<--Back\n");
                                        }
                                    } else{
                                        System.out.println("Empty, add group to show\n");
                                    }
                                    continue;
                                case 3:
                                    if (!groupTeacherList.isEmpty()){
                                        System.out.println("Choose group:");
                                        int groupTeacherIndex = chooseGroupTeacher(groupTeacherList);
                                        if (0 <= groupTeacherIndex && groupTeacherIndex < groupTeacherList.size()){
                                            jdbcService.deleteSchoolGroup(groupTeacherList.get(groupTeacherIndex).getId());
                                        } else {
                                            System.out.println("<--Back\n");
                                        }
                                    } else{
                                        System.out.println("Empty, add group to delete\n");
                                    }
                                    continue;
                                default:
                                    break;
                            }
                            break;
                        }
                        continue;
                    case 3:
                        break;
                    default:
                        System.out.println("Unknown command\n\n");
                        continue;
                }
                break;
            }


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int chooseStudent(List<Student> students, boolean withoutIndex){
        Scanner scanner = new Scanner(System.in);
        Student student;
        for (int i = 0; i < students.size(); i++) {
            student = students.get(i);
            System.out.println((withoutIndex ? "" : i+1  + ".") + student.getFullName());
        }
        //noinspection DuplicatedCode
        if (!withoutIndex){
            System.out.print("\nEnter number or something to back => ");
            int selectedIndex = -1;
            try {
                selectedIndex = scanner.nextInt() - 1;
            } catch (Exception e) {}
            System.out.println();
            return selectedIndex;
        }

        return -1;
    }

    public static void printStudents(List<Student> students){
        chooseStudent(students, true);
    }

    public static int chooseStudent(List<Student> students){
        return chooseStudent(students, false);
    }

    public static int chooseTeacherGroup(List<TeacherGroup> teacherGroupList, boolean withoutIndex){
        Scanner scanner = new Scanner(System.in);
        TeacherGroup teacherGroup;
        for (int i = 0; i < teacherGroupList.size(); i++) {
            teacherGroup = teacherGroupList.get(i);
            System.out.println((withoutIndex ? "" : i+1 + ".") + teacherGroup.getFullName() + "| Group:"
                    + (teacherGroup.getGroupName() == null
                    ? "none" : teacherGroup.getGroupName()));
        }
        //noinspection DuplicatedCode
        if (!withoutIndex){
            System.out.print("\nEnter number or something to back => ");
            int selectedIndex = -1;
            try {
                selectedIndex = scanner.nextInt() - 1;
            } catch (Exception e) {}
            System.out.println();
            return selectedIndex;
        }

        return -1;
    }

    public static void printTeacherGroup(List<TeacherGroup> teacherGroupList){
        chooseTeacherGroup(teacherGroupList, true);
    }

    public static int chooseTeacherGroup(List<TeacherGroup> teacherGroupList){
        return chooseTeacherGroup(teacherGroupList, false);
    }

    public static int chooseGroupTeacher(List<GroupTeacher> groupTeacherList, boolean withoutIndex){
        Scanner scanner = new Scanner(System.in);
        GroupTeacher groupTeacher;
        for (int i = 0; i < groupTeacherList.size(); i++) {
            groupTeacher = groupTeacherList.get(i);
            System.out.println((withoutIndex ? "" : i+1 + ".") + groupTeacher.getName() + "| Teacher:"
                    + (groupTeacher.getTeacherFullName() == null
                    ? "none" : groupTeacher.getTeacherFullName()));
        }
        //noinspection DuplicatedCode
        if (!withoutIndex){
            System.out.print("\nEnter number or something to back => ");
            int selectedIndex = -1;
            try {
                selectedIndex = scanner.nextInt() - 1;
            } catch (Exception e) {}
            System.out.println();
            return selectedIndex;
        }
        return -1;
    }

    public static void printGroupTeacher(List<GroupTeacher> groupTeacherList){
        chooseGroupTeacher(groupTeacherList, true);
    }

    public static int chooseGroupTeacher(List<GroupTeacher> groupTeacherList){
        return chooseGroupTeacher(groupTeacherList, false);
    }
}