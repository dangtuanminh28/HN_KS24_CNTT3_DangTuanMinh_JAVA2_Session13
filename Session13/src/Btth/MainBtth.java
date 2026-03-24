package Btth;

import Connect.DataConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainBtth {
    public static void main(String[] args) throws SQLException{
        Connection conn = null;
        try {
            conn = DataConnect.openConnect();
            if (conn == null) return;
            conn.setAutoCommit(false);

            int idPa = 1;
            double priceFee = 150;

            String sqlInvoice = "INSERT INTO INVOICES (price, date, id_patients) VALUES (?, CURRENT_DATE, ?)";
            PreparedStatement ps1 = conn.prepareStatement(sqlInvoice);
            ps1.setDouble(1, priceFee );
            ps1.setInt(2, idPa);
            ps1.executeUpdate();

            String sqlPatient = "UPDATE PATIENTS SET status_patients = 'Đã xuất viện' WHERE id_patients = ?";
            PreparedStatement ps2 = conn.prepareStatement(sqlPatient);
            ps2.setInt(1, idPa);
            ps2.executeUpdate();

            String sqlBed = "UPDATE BEDS SET id_patients = NULL, status_bed = 'Trống' WHERE id_patients = ?";
            PreparedStatement ps3 = conn.prepareStatement(sqlBed);
            ps3.setInt(1, idPa);
            ps3.executeUpdate();

            System.out.println("Giao dịch thành công!");
            ResultSet rs = conn.createStatement().executeQuery("SELECT id_invoices, price, date, id_patients FROM INVOICES");
            while (rs.next()) {
                System.out.println("Mã bệnh nhân: " + rs.getInt("id_invoices") + " | Số tiền: " + rs.getDouble("price"));
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
