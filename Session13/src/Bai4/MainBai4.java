package Bai4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
public class MainBai4 {
    public static void main(String[] args) {
        Connection conn = null;
        Map<Integer, BenhNhanDTO> mapBenhNhan = new LinkedHashMap<>();

        try {
            conn = DataConnectB4.openConnect();
            if (conn == null) return;
            conn.setAutoCommit(false);

            String sql = "SELECT b.id_patients, b.name_patients, b.status_patients, " +
                    "i.id_invoices, i.price, i.status_invoice " +
                    "FROM PATIENTS b " +
                    "LEFT JOIN INVOICES i ON b.id_patients = i.id_patients " +
                    "ORDER BY b.id_patients DESC";

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int idBN = rs.getInt("id_patients");
                    BenhNhanDTO dto = mapBenhNhan.get(idBN);

                    if (dto == null) {
                        dto = new BenhNhanDTO();
                        dto.setMaBN(idBN);
                        dto.setTenBN(rs.getString("name_patients"));
                        dto.setTrangThai(rs.getString("status_patients"));
                        dto.setDsDichVu(new ArrayList<>());
                        mapBenhNhan.put(idBN, dto);
                    }

                    int idDV = rs.getInt("id_invoices");
                    if (idDV != 0) {
                        DichVu dv = new DichVu();
                        dv.setMaDV(idDV);
                        dv.setGia(rs.getDouble("price"));
                        dv.setTrangThaiDV(rs.getString("status_invoice"));
                        dto.getDsDichVu().add(dv);
                    }
                }
            }

            conn.commit();
            renderDashboard(mapBenhNhan);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void renderDashboard(Map<Integer, BenhNhanDTO> map) {
        System.out.println("--- Danh sách bệnh nhân ---");
        for (BenhNhanDTO bn : map.values()) {
            System.out.println("Bệnh nhân: " + bn.getTenBN() + " [" + bn.getTrangThai() + "]");
            if (bn.getDsDichVu().isEmpty()) {
                System.out.println("Dịch vụ trống.");
            } else {
                for (DichVu dv : bn.getDsDichVu()) {
                    System.out.println("  - DV #" + dv.getMaDV() + ": " + dv.getGia());
                }
            }
        }
    }
}