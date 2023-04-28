package org.example.services;

import org.example.models.*;
import org.example.models.viewModel.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcService {

    // TODO: Change values
    private static final String url = "jdbc:sqlserver://localhost:2233;database=JavaTest";
    private static final String user = "admin";
    private static final String pass = "admin";

    {
        System.out.println("Connecting and checking the database...");
        createStudentTable();
        createTeacherTable();
        createSchoolGroupTable();
        System.out.println("Done.\n");
    }

    public JdbcService() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    }

    public boolean existTable(String name) throws SQLException {

        Connection connection = DriverManager.getConnection(url, user, pass);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT 1 " +
                "FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_TYPE='BASE TABLE' " +
                "AND TABLE_NAME='" + name + "'");
        return resultSet.next();
    }

    public void createStudentTable() {
        try {
            if (existTable("Student")) {
                return;
            }
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE Student(" +
                    "id INT PRIMARY KEY IDENTITY (1,1), " +
                    "fullName NVARCHAR(50) NOT NULL, " +
                    "groupId INT NOT NULL )");

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void createTeacherTable() {
        try {
            if (existTable("Teacher")) {
                return;
            }
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE Teacher(" +
                    "id INT PRIMARY KEY IDENTITY (1,1), " +
                    "fullName NVARCHAR(50) NOT NULL )");

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void createSchoolGroupTable() {
        try {
            if (existTable("SchoolGroup")) {
                return;
            }
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE SchoolGroup(" +
                    "id INT PRIMARY KEY IDENTITY (1,1), " +
                    "name NVARCHAR(50) NOT NULL, " +
                    "teacherId INT)");

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<TeacherGroup> getTeacherGroups(){
        List<TeacherGroup> list = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Teacher.id as id, " +
                    "Teacher.fullName, SchoolGroup.id as groupId, name FROM Teacher " +
                    "LEFT JOIN SchoolGroup " +
                    "ON Teacher.id = SchoolGroup.teacherId");
            while (resultSet.next()){
                TeacherGroup teacherGroup = new TeacherGroup();
                teacherGroup.setId(resultSet.getInt("id"));
                teacherGroup.setFullName(resultSet.getString("fullName"));
                teacherGroup.setGroupId(resultSet.getInt("groupId"));
                teacherGroup.setGroupName(resultSet.getString("name"));
                list.add(teacherGroup);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private List<GroupTeacher> getGroupTeachers(String sqlQuery){
        List<GroupTeacher> list = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()){
                GroupTeacher groupTeacher = new GroupTeacher();
                groupTeacher.setId(resultSet.getInt("id"));
                groupTeacher.setName(resultSet.getString("name"));
                groupTeacher.setTeacherId(resultSet.getInt("teacherId"));
                groupTeacher.setTeacherFullName(resultSet.getString("fullName"));
                list.add(groupTeacher);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public List<GroupTeacher> getGroupTeachers(){
        return this.getGroupTeachers("SELECT SchoolGroup.id as id, " +
                "name, Teacher.id as teacherId, fullName FROM SchoolGroup " +
                "LEFT JOIN Teacher " +
                "ON SchoolGroup.teacherId = Teacher.id");
    }

    public List<GroupTeacher> getBusyGroupTeachers(){
        return this.getGroupTeachers("SELECT SchoolGroup.id as id, " +
                "name, Teacher.id as teacherId, fullName FROM SchoolGroup " +
                "LEFT JOIN Teacher " +
                "ON SchoolGroup.teacherId = Teacher.id " +
                "WHERE SchoolGroup.teacherId IS NOT NULL");
    }

    public List<Student> getStudentsByGroupId(int groupId){
        List<Student> list = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, fullName, groupId " +
                    "FROM Student " +
                    "WHERE groupId = " + groupId);
            while (resultSet.next()){
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setFullName(resultSet.getString("fullName"));
                student.setGroupId(resultSet.getInt("groupId"));
                list.add(student);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void setTeacherIdGroup(int groupId, int teacherId){
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();

            statement.execute("UPDATE SchoolGroup " +
                    "SET teacherId = " + (teacherId < 0 ? "NULL" : teacherId) + " " +
                    "WHERE id = " + groupId);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTeacher(Teacher teacher){
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Teacher(fullName) " +
                    "VALUES (?)");
            preparedStatement.setString(1, teacher.getFullName());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createStudent(Student student){
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Student(fullName, groupId) " +
                    "VALUES (?, ?)");
            preparedStatement.setString(1, student.getFullName());
            preparedStatement.setInt(2, student.getGroupId());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createSchoolGroup(SchoolGroup group){
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO SchoolGroup(name) " +
                    "VALUES (?)");
            preparedStatement.setString(1, group.getName());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTeacher(int id){
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            statement.execute("UPDATE SchoolGroup " +
                    "SET teacherId = NULL "+
                    "WHERE teacherId = "+ id);
            statement.execute("DELETE FROM Teacher WHERE id = " + id + "");

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTeacherFromSchoolGroup(int groupId){
        this.setTeacherIdGroup(groupId, -1);
    }

    public void deleteSchoolGroup(int id){
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM Student WHERE groupId = " + id + "");
            statement.execute("DELETE FROM SchoolGroup WHERE id = " + id + "");

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteStudent(int id){
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM Student WHERE id = " + id + "");

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
