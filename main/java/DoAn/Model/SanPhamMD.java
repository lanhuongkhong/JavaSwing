/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoAn.Model;

import java.math.BigDecimal;

/**
 *
 * @author PC
 */
public class SanPhamMD {
    private String MaSanPham;
    private String TenSanPham;
    private String LoaiSanPham;
    private BigDecimal Gia;
    
    public SanPhamMD(String maSanPham, String tenSanPham, String loaiSanPham, BigDecimal gia){
        this.MaSanPham = maSanPham;
        this.TenSanPham = tenSanPham;
        this.LoaiSanPham = loaiSanPham;
        this.Gia = gia;
    }
    public SanPhamMD(){
        
    }
    /**
     * @return the MaSanPham
     */
    public String getMaSanPham() {
        return MaSanPham;
    }

    /**
     * @param MaSanPham the MaSanPham to set
     */
    public void setMaSanPham(String MaSanPham) {
        this.MaSanPham = MaSanPham;
    }

    /**
     * @return the TenSanPham
     */
    public String getTenSanPham() {
        return TenSanPham;
    }

    /**
     * @param TenSanPham the TenSanPham to set
     */
    public void setTenSanPham(String TenSanPham) {
        this.TenSanPham = TenSanPham;
    }

    /**
     * @return the LoaiSanPham
     */
    public String getLoaiSanPham() {
        return LoaiSanPham;
    }

    /**
     * @param LoaiSanPham the LoaiSanPham to set
     */
    public void setLoaiSanPham(String LoaiSanPham) {
        this.LoaiSanPham = LoaiSanPham;
    }

    /**
     * @return the Gia
     */
    public BigDecimal getGia() {
        return Gia;
    }

    /**
     * @param Gia the Gia to set
     */
    public void setGia(BigDecimal Gia) {
        this.Gia = Gia;
    }

    public String getSoLuong() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
   
    
}
