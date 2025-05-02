package Controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import Model.Database;
import Model.Operation; // Ensure Operation model is correctly defined

public class CreateOperation {

    private Operation o;
    private Database database;

    public CreateOperation(Operation o, Database database) {
        this.o = o;
        this.database = database;
    }

    // Ensure this method throws SQLException
    public boolean isCreated() throws SQLException {
        // Verify table name 'operations' and column names match your database exactly
        String insertSQL = "INSERT INTO `operations`(`Doctor`, `Patient`, `DateTime`, `Paid`, `Diagnosis`) VALUES (?, ?, ?, ?, ?)";
        boolean created = false;

        // Check if database connection is available
        if (database == null || database.getConnection() == null) {
            throw new SQLException("Database connection is not available.");
        }
        // Check if Operation object itself is null
        if (o == null) {
            throw new SQLException("Operation object provided is null.");
        }

        // Use try-with-resources for PreparedStatement
        try (PreparedStatement pstmt = database.getConnection().prepareStatement(insertSQL)) {

            // Check if Doctor or Patient objects/IDs are null before accessing
            if (o.getDoctor() == null) throw new SQLException("Doctor object is null in Operation.");
            if (o.getPatient() == null) throw new SQLException("Patient object is null in Operation.");

            pstmt.setInt(1, o.getDoctor().getID());        // Doctor ID
            pstmt.setInt(2, o.getPatient().getID());        // Patient ID
            pstmt.setString(3, o.getDateTime());        // DateTime string
            pstmt.setBoolean(4, o.isPaid());            // Paid status (should be false)
            pstmt.setString(5, o.getDiagnosis());       // Diagnosis

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                created = true;
            }
        } catch (SQLException e) {
            // Provide more specific error context if possible
            System.err.println("SQL Error in CreateOperation for DoctorID=" + (o.getDoctor() != null ? o.getDoctor().getID() : "null") +
                               ", PatientID=" + (o.getPatient() != null ? o.getPatient().getID() : "null") +
                               ": " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the exception to be handled by the View
        }
        return created;
    }
}