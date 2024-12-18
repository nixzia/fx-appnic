package com.nicole.controller;

import com.nicole.DatabaseConnection;
import com.nicole.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainController {
    @FXML
    private TextField first_name;
    @FXML
    private TextField middle_name;
    @FXML
    private TextField last_name;
    @FXML
    private TextField address;
    @FXML
    private TextField phone_number;
    @FXML
    private TextField email;
    @FXML
    private RadioButton male;
    @FXML
    private RadioButton female;
    @FXML
    private TableView<Student> table;
    @FXML
    private TableColumn<Student, String> colFN;
    @FXML
    private TableColumn<Student, String> colMN;
    @FXML
    private TableColumn<Student, String> colLN;
    @FXML
    private TableColumn<Student, String> colAddress;
    @FXML
    private TableColumn<Student, String> colPN;
    @FXML
    private TableColumn<Student, String> colEmail;
    @FXML
    private TableColumn<Student, String> colGen;
    public ToggleGroup gender;

    private DatabaseConnection connection;

    private boolean isEditing = false;
    private int studentId = 0;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    public void initialize() throws SQLException {

        connection = new DatabaseConnection();

        colFN.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colMN.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        colLN.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPN.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGen.setCellValueFactory(new PropertyValueFactory<>("gender"));
        loadStudents();
    }

    public void loadStudents() throws SQLException{
        studentList.clear();
        String sql = "SELECT * FROM student_info";
        Statement stmt = connection.getConnection().createStatement();
        ResultSet result = stmt.executeQuery(sql);

        while(result.next()){
            Student student = new Student(result.getInt("student_id"),
                    result.getString("first_name"),
                    result.getString("middle_name"),
                    result.getString("last_name"),
                    result.getString("address"),
                    result.getString("phone_number"),
                    result.getString("email"),
                    result.getString("gender"));
            studentList.add(student);
        }
        table.setItems(studentList);

    }

    @FXML
    private void save() throws SQLException {
        if(!isEditing) {
            String sql = "INSERT INTO student_info(first_name, middle_name, last_name, address, phone_number, email, gender) VALUES (?, ?, ?,?, ?, ?, ?)";
            PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
            stmt.setString(1, first_name.getText());
            stmt.setString(2, middle_name.getText());
            stmt.setString(3, last_name.getText());
            stmt.setString(4, address.getText());
            stmt.setString(5, phone_number.getText());
            stmt.setString(6, email.getText());

            if (male.isSelected()) {
                stmt.setString(7, "Male");
            } else if (female.isSelected()) {
                stmt.setString(7, "Female");
            }

            if (stmt.executeUpdate() == 1) {
                first_name.clear();
                middle_name.clear();
                last_name.clear();
                address.clear();
                phone_number.clear();
                email.clear();
                loadStudents();
            }
        }else{
            String sql = "UPDATE student_info SET first_name = ?, middle_name = ?, last_name = ?, address = ?, phone_number = ?, email = ?, gender = ? WHERE student_id = ?";
            try{
                PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
                stmt.setString(1,first_name.getText());
                stmt.setString(2,middle_name.getText());
                stmt.setString(3,last_name.getText());
                stmt.setString(4,address.getText());
                stmt.setString(5,phone_number.getText());
                stmt.setString(6,email.getText());
                if(male.isSelected()){
                    stmt.setString(7,"Male");
                }
                else if (female.isSelected()){
                    stmt.setString(7, "Female");
                }else{
                    stmt.setString(7, "");
                }
                stmt.setInt(8,studentId);

                if (stmt.executeUpdate() == 1) {
                    first_name.clear();
                    middle_name.clear();
                    last_name.clear();
                    address.clear();
                    phone_number.clear();
                    email.clear();
                    loadStudents();
                    isEditing = false;
                    studentId = 0;
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void delete(){
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        if(selectedStudent != null){
            String sql = "DELETE from student_info WHERE student_id = ?";
            try{
                PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
                stmt.setInt(1, selectedStudent.getId());
                stmt.executeUpdate();

                studentList.remove(selectedStudent);
            }catch(SQLException e){
                e.printStackTrace();
            }

        }
    }
    @FXML
    private void edit(){
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        if(selectedStudent != null){
            first_name.setText(selectedStudent.getFirstName());
            middle_name.setText(selectedStudent.getMiddleName());
            last_name.setText(selectedStudent.getLastName());
            address.setText(selectedStudent.getAddress());
            phone_number.setText(selectedStudent.getPhoneNumber());
            email.setText(selectedStudent.getEmail());

            isEditing = true;
            studentId = selectedStudent.getId();
        }
    }
}


