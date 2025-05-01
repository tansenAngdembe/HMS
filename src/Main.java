import HospitalManagementSystem.Doctors;
import HospitalManagementSystem.Patients;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/hospital_management_system";
    private static final String username = "root";
    private static final String password = "tansen@123";


    public static void bookAppointment(Patients patients, Doctors doctors, Connection connection, Scanner scanner) {
        System.out.print("Enter patient id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter doctor id: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter appointment date (yyyy-mm-dd): ");
        String dateStr = scanner.next();
        Timestamp appointmentDate = Timestamp.valueOf(dateStr.replace("T"," " )+ ":00");

        if (patients.viewPatientById(patientId) && doctors.viewDoctorById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentDate,connection)) {
                String appointmentQuery = "INSERT INTO appointments(patient_id,doctor_id,appointment_date) VALUES (?,?,?)";
                try {
                    PreparedStatement appointmentStatement = connection.prepareStatement(appointmentQuery);
                    appointmentStatement.setInt(1, patientId);
                    appointmentStatement.setInt(2, doctorId);
                    appointmentStatement.setTimestamp(3, appointmentDate);

                    int rowAffected = appointmentStatement.executeUpdate();
                    if (rowAffected > 0) {
                        doctors.updateDoctorAvailabality(false,doctorId);
                        System.out.println("Appointment booked.");
                    } else {
                        System.out.println("Appointment not booked.");
                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            } else {
                System.out.println("Doctor not available on this date.");
            }

        } else {
            System.out.println("Doctor is not available.");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, Timestamp appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setTimestamp(2, appointmentDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println("Database connected successfully");

        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patients patients = new Patients(connection, scanner);
            Doctors doctors = new Doctors(connection);


            System.out.println("Wellcome to Hospital Management System.");
            while (true) {
                System.out.println("1. Doctors.");
                System.out.println("2. View Patients.");
                System.out.println("3. Admit Patients.");
                System.out.println("4. Book Appointment.");
                System.out.println("5. Exit!");
                System.out.print("Enter you choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        // view doctor
                        System.out.println("1. View Doctors.");
                        System.out.println("2. Set availability: ");
                        System.out.println("Choose options:-");
                        int choiceDoctor = scanner.nextInt();
                        if(choiceDoctor == 1){
                            doctors.viewDoctor();
                        } else if (choiceDoctor == 2) {
                            System.out.print("Enter doctor id to update availability:");
                            int doctorId = scanner.nextInt();
                            doctors.updateDoctorAvailabality(true,doctorId);

                        }else {
                            System.out.println("Choose correct options: ");
                        }

                        break;
                    case 2:
                        // view patients
                        patients.viewPatient();
                        break;
                    case 3:
                        // Admit patient
                        patients.addPatients();
                        break;
                    case 4:
                        // Book Appointment
                        bookAppointment(patients,doctors,connection,scanner);
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Enter a valid choice!!!");

                }

            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}