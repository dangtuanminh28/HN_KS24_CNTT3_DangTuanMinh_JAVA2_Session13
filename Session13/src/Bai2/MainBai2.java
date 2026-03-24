package Bai2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MainBai2 {

    public static void main(String[] args) {
        Connection conn = null;
        int patientId = 1;
        int invoiceId = 101;
        double amount = 500000;
        try {
            conn = DataConnectB2.openConnect();
            if (conn == null) return;
            conn.setAutoCommit(false);

            String sqlUpdateWallet = "UPDATE Patient_Wallet SET balance = balance - ? WHERE patient_id = ?";
            try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdateWallet)) {
                ps1.setDouble(1, amount);
                ps1.setInt(2, patientId);
                ps1.executeUpdate();
            }

            String sqlUpdateInvoice = "UPDATE Invoices SET status_invoice = 'Đã thanh toán' WHERE id_invoices = ?";
            try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdateInvoice)) {
                ps2.setInt(1, invoiceId);
                ps2.executeUpdate();
            }

            conn.commit();
            System.out.println("Thanh toán thành công!");

        } catch (SQLException e) {
            System.err.println(e.getMessage());

            if (conn != null) {
                try {
                    System.out.println("Đang thực hiện Rollback dữ liệu...");
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
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