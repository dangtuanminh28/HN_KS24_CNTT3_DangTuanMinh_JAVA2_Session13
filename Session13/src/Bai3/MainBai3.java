package Bai3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainBai3 {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DataConnectB3.openConnect();
            if (conn == null) return;
            conn.setAutoCommit(false);

            int maBenhNhan = 1;
            double tienVienPhi = 500000;

            String sqlCheck = "SELECT balance FROM Patient_Wallet WHERE patient_id = ?";
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, maBenhNhan);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next()) {
                    double currentBalance = rs.getDouble("balance");
                    if (currentBalance < tienVienPhi) {
                        throw new SQLException("Thanh toán thất bại");
                    }
                } else {
                    throw new SQLException("Ko tìm thấy id: " + maBenhNhan);
                }
            }

            String sqlUpdateWallet = "UPDATE Patient_Wallet SET balance = balance - ? WHERE patient_id = ?";
            try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdateWallet)) {
                ps1.setDouble(1, tienVienPhi);
                ps1.setInt(2, maBenhNhan);
                int rows = ps1.executeUpdate();
                if (rows == 0) throw new SQLException("Id ko hợp lệ");
            }

            String sqlUpdateBed = "UPDATE BEDS SET status_bed = 'Trống', id_patients = NULL WHERE id_patients = ?";
            try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdateBed)) {
                ps2.setInt(1, maBenhNhan);
                int rows = ps2.executeUpdate();
                if (rows == 0) System.out.println("Ko tìm thấy giường cho bênh nhân");
            }

            String sqlUpdatePatient = "UPDATE PATIENTS SET status_patients = 'Đã xuất viện' WHERE id_patients = ?";
            try (PreparedStatement ps3 = conn.prepareStatement(sqlUpdatePatient)) {
                ps3.setInt(1, maBenhNhan);
                int rows = ps3.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("Id ko tồn tại");
                }
            }

            conn.commit();
            System.out.println("Bệnh nhân " + maBenhNhan + " đã xuất viện và thanh toán.");

        } catch (SQLException e) {
            System.err.println("Giao dịch thất bại! Đang rollback");
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Đã khôi phục trạng thái số dư ban đầu cho bệnh nhân.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}