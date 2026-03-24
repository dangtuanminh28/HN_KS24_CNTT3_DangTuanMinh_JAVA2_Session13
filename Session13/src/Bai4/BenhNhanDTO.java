package Bai4;

import java.util.List;

public class BenhNhanDTO {
    private int maBN;
    private String tenBN;
    private String trangThai;
    private List<DichVu> dsDichVu; // Quan hệ 1-N

    // Getter và Setter
    public int getMaBN() { return maBN; }
    public void setMaBN(int maBN) { this.maBN = maBN; }

    public String getTenBN() { return tenBN; }
    public void setTenBN(String tenBN) { this.tenBN = tenBN; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public List<DichVu> getDsDichVu() { return dsDichVu; }
    public void setDsDichVu(List<DichVu> dsDichVu) { this.dsDichVu = dsDichVu; }
}
