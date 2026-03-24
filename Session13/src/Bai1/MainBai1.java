package Bai1;

import Test.DataConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MainBai1 {

    public static void main(String[] args) {
        int medicineId = 1;
        int patientId = 1;

        Connection conn = null;

        try {
            conn = DataConnect.openConnect();
            if (conn != null) {
                conn.setAutoCommit(false);

                String sqlUpdate = "UPDATE Medicine_Inventory SET quantity = quantity - 1 WHERE medicine_id = ?";
                try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdate)) {
                    ps1.setInt(1, medicineId);
                    ps1.executeUpdate();
                }

                String sqlInsert = "INSERT INTO Prescription_History (patient_id, medicine_id, date) VALUES (?, ?, ?)";
                try (PreparedStatement ps2 = conn.prepareStatement(sqlInsert)) {
                    ps2.setInt(1, patientId);
                    ps2.setInt(2, medicineId);
                    ps2.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    ps2.executeUpdate();
                }
                conn.commit();
                System.out.println("Cấp phát thuốc thành công.");
            }

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}