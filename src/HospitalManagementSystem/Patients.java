package HospitalManagementSystem;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patients {
    private Connection connection;
    private Scanner scanner;

    //creating constructor
    public Patients(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatients() {

            System.out.print("Enter patient name: ");
            String patient_name = scanner.next();
            System.out.print("Enter age: ");
            int age = scanner.nextInt();
            System.out.print("Enter patient gender: ");
            String gender = scanner.next();
            System.out.print("Enter patient address: ");
            String address = scanner.next();
        try {
            String query = "INSERT INTO patients(patient_name, age , gender, address) values (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,patient_name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            preparedStatement.setString(4,address);

            int affectedRow = preparedStatement.executeUpdate();



            if(affectedRow > 0){
                System.out.println("Patient data successfully.");

            }else{
                System.out.println("Failed to add patient.");
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }


    public void viewPatient(){
        String query = "select * from patients";
        try{
            PreparedStatement preparedViewStatement = connection.prepareStatement(query);
            ResultSet result = preparedViewStatement.executeQuery();

            System.out.println("Total patients");
            System.out.println("Id      Name        Age     Gender      Address");
            while(result.next()){
                int id = result.getInt("id");
                String patient_name = result.getString("patient_name");
                int age = result.getInt("age");
                String gender = result.getString("gender");
                String address = result.getString("address");
                System.out.println(id+"     "+patient_name+ "       "+age+ "        "+gender + "        "+address);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public boolean viewPatientById(int id){

        try{
            String query = "SELECT * FROM patients WHERE id = ?";
            PreparedStatement preparedStatementToView = connection.prepareStatement(query);
            preparedStatementToView.setInt(1,id);

            ResultSet resultSet = preparedStatementToView.executeQuery();

            if(resultSet.next()){
                return true;
            }else {
                return  false;
            }


        }catch(SQLException e){
            System.out.println(e.getMessage());

        }
        return  false;
    }


}
