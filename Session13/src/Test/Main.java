package Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException{
        Connection conn = null;
        try {
            conn = DataConnect.openConnect();
            if (conn == null) return;
            conn.setAutoCommit(false);
            String sqlBuy = """
                    UPDATE accounts SET balance = balance - ? WHERE id = ?
                    """;

            String sqlSell = """
                    UPDATE accounts SET balance = balance + ? WHERE id = ?
                    """;

            String sqlTransfers = """
                    INSERT INTO transfers (type, amount, account_id)
                    VALUES (?, ?, ?)
                    """;

            int amountID = 1;
            int amountBuy = 100;
            int amountSell = 1000;
            PreparedStatement preparedBuy = conn.prepareStatement(sqlBuy);
            preparedBuy.setDouble(1, amountBuy);
            preparedBuy.setInt(2, amountID);

            PreparedStatement preparedSell = conn.prepareStatement(sqlSell);
            preparedSell.setDouble(1, amountSell);
            preparedSell.setInt(2, amountID);

            PreparedStatement preparedTransfers = conn.prepareStatement(sqlTransfers);
            preparedTransfers.setString(1, "BUY");
            preparedTransfers.setDouble(2, amountBuy);
            preparedTransfers.setDouble(3, amountID);

            preparedBuy.executeUpdate();
            preparedBuy.executeUpdate();

            preparedTransfers.executeUpdate();
            preparedTransfers.executeUpdate();

            preparedSell.executeUpdate();

            preparedTransfers.clearParameters();
            preparedTransfers.setString(1, "SELL");
            preparedTransfers.setDouble(2, amountBuy);
            preparedTransfers.setDouble(3, amountID);


            System.out.println("Giao dịch thành công!");
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
