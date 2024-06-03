/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoAn.Model;

/**
 *
 * @author PC
 */
public class NhanVienMD {
    private String MaNhanVien;
    private String HoTen;
    private String DienThoai;
    private String ViTri;
    
    public NhanVienMD(String maNhanVien, String hoTen, String dienThoai, String viTri){
        this.MaNhanVien = maNhanVien;
        this.HoTen = hoTen;
        this.DienThoai = dienThoai;
        this.ViTri = viTri;
    }
    public NhanVienMD(){
        
    }

    /**
     * @return the MaNhanVien
     */
    public String getMaNhanVien() {
        return MaNhanVien;
    }

    /**
     * @param MaNhanVien the MaNhanVien to set
     */
    public void setMaNhanVien(String MaNhanVien) {
        this.MaNhanVien = MaNhanVien;
    }

    /**
     * @return the HoTen
     */
    public String getHoTen() {
        return HoTen;
    }

    /**
     * @param HoTen the HoTen to set
     */
    public void setHoTen(String HoTen) {
        this.HoTen = HoTen;
    }

    /**
     * @return the DienThoai
     */
    public String getDienThoai() {
        return DienThoai;
    }

    /**
     * @param DienThoai the DienThoai to set
     */
    public void setDienThoai(String DienThoai) {
        this.DienThoai = DienThoai;
    }

    /**
     * @return the ViTri
     */
    public String getViTri() {
        return ViTri;
    }

    /**
     * @param ViTri the ViTri to set
     */
    public void setViTri(String ViTri) {
        this.ViTri = ViTri;
    }
    
    
    
   
}
