package DoAn.Model;

import java.math.BigDecimal;
import java.util.Date;

public class HoaDonMD {
    private String maHoaDon;
    private int soLuong;
    private BigDecimal gia;
    private Date ngayTao;

    // Constructor
    public HoaDonMD(String maHoaDon, int soLuong, BigDecimal gia, Date ngayTao) {
        this.maHoaDon = maHoaDon;
        this.soLuong = soLuong;
        this.gia = gia;
        this.ngayTao = ngayTao;
    }

    // Default constructor
    public HoaDonMD() {}

    // Getter and Setter for maHoaDon
    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    // Getter and Setter for soLuong
    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    // Getter and Setter for gia
    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
    }

    // Getter and Setter for ngayTao
    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }
}
