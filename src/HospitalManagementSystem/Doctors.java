package HospitalManagementSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Doctors {
    private Connection connection;

    private boolean isAvailable;

    public Doctors(Connection connection){
        this.connection = connection;


    }

    public void updateDoctorAvailabality(boolean status,int id){
        String updateQuery = "UPDATE doctors set is_available = ? where id = ? ";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setBoolean(1,status);
            preparedStatement.setInt(2,id);
            int rowAffect = preparedStatement.executeUpdate();
            if(rowAffect>0){
                System.out.println("Doctor availabality is updated. ");
            }else{
                System.out.println("Doctor is still on next appointments.");
            }



        }catch(SQLException e){
            System.out.println(e.getMessage());

        }
    }

    public void viewDoctor(){
        String doctorQuery = "SELECT * FROM doctors";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(doctorQuery);

            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("List of doctors:");
            System.out.println("Id      Name        Specialization      IsAvailable.");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String doctor_name = resultSet.getString("doctor_name");
                String specialization = resultSet.getString("specialization");
                boolean isAvailable = resultSet.getBoolean("is_available");
                String availableStatus = isAvailable ? "Available" : "Not Available";
                System.out.println(id+ "         " + doctor_name + "        " + specialization + "       " + availableStatus);

            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }


    public boolean viewDoctorById(int id){

        try{
            String query = "SELECT * FROM doctors WHERE id = ? and is_available = true";
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
