/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package DoAn.GiaoDien;

import DoAn.Connect.KetNoi;
import DoAn.Dao.ChiTietDonHangDao;
import DoAn.Dao.DonHangDao;
import DoAn.Dao.HoaDonDao;
import DoAn.Dao.KhachHangDao;
import DoAn.Dao.NhanVienDao;
import static DoAn.Dao.NhanVienDao.getAllEmployees;
import static DoAn.Dao.NhanVienDao.getEmployeesByPosition;
import DoAn.Dao.SanPhamDao;
import DoAn.Model.ChiTietDonHangMD;
import DoAn.Model.DonHangMD;
import DoAn.Model.HoaDonMD;
import DoAn.Model.KhachHangMD;
import DoAn.Model.NhanVienMD;
import DoAn.Model.SanPhamMD;
import java.awt.CardLayout;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author PC
 */
public class QuanLyCoffee extends javax.swing.JFrame {

    /**
     * Creates new form QuanLyCoffee
     */
    
    private NhanVienDao nv;
    private KhachHangDao kh;
    private DonHangDao dh;
    int width = 140;
    int heigth = 800;    //chieu cao
    
    
    public QuanLyCoffee() {
        initComponents();
        setCustom();
      
       
        loadDataToTableNV();
        loadDataToTableKH();
        loadDataToTableDH();
        loadDataToTableSP();
        loadDataToTableCTHD();
        loadDataToTableHoaDon();

    }

    private void setCustom(){
        
        
        
        //Set carf
       pnlContainer.setLayout(new CardLayout());

        // Add the cards with the correct names
        pnlContainer.add(pnlHome, "pnlHome");
        pnlContainer.add(pnlHSanPham, "pnlHSanPham");
        pnlContainer.add(pnlHoaDon, "pnlHoaDon");
        pnlContainer.add(pnlLichSu, "pnlLichSu");
        pnlContainer.add(pnlKhachHang, "pnlKhachHang");
        pnlContainer.add(pnlNhanVien, "pnlNhanVien");
        
    }
    

    public void tinhTien(){
        int sanpham = tbOder.getRowCount();
        double tongtien = 0;
        for (int i=0; i<sanpham; i++){
            double tien = Double.valueOf(tbOder.getValueAt(i, 3).toString());
            tongtien += tien;
     }
        DecimalFormat df = new DecimalFormat("00.00");
        String d1 = df.format(tongtien);
        blbTongTien.setText(d1);
     }
 public void xuatHoaDon() {
        txtxuathoadon.setText("");

        // Lưu thông tin hóa đơn vào cơ sở dữ liệu và lấy mã hóa đơn và ngày tạo
        String maHoaDon = luuHoaDon();  // Lưu hóa đơn và lấy mã hóa đơn
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ngayTao = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(timestamp);

        // Thêm thông tin mã hóa đơn và ngày tạo vào hóa đơn
        txtxuathoadon.append("Mã hóa đơn: " + maHoaDon + "\n");
        txtxuathoadon.append("Ngày tạo: " + ngayTao + "\n\n");

        // Thêm thông tin cửa hàng vào hóa đơn
        txtxuathoadon.append("              Enndme The Coffee House \n");
        txtxuathoadon.append("              215 Trần Phú - TP Huế \n");

        // Thêm đường gạch ngang
        txtxuathoadon.append("----------------------------------------------------------------------------------------------\n");

        // Thêm tiêu đề của danh sách sản phẩm
        txtxuathoadon.append("MSP\tTên                                              SL        \tGiá \n");

        // Thêm đường gạch ngang
        txtxuathoadon.append("----------------------------------------------------------------------------------------------\n");

        // Duyệt qua từng hàng trong bảng và thêm thông tin sản phẩm vào hóa đơn
        DefaultTableModel dt = (DefaultTableModel) tbOder.getModel();
        StringBuilder chiTietHoaDon = new StringBuilder();

        for (int i = 0; i < tbOder.getRowCount(); i++) {
            String ma = dt.getValueAt(i, 0).toString();
            String ten = dt.getValueAt(i, 1).toString();
            String sl = dt.getValueAt(i, 2).toString();
            String gia = dt.getValueAt(i, 3).toString();

            // Thêm thông tin của từng sản phẩm vào hóa đơn
            txtxuathoadon.append(String.format("%-15s", ma));
            txtxuathoadon.append(String.format("%-40s", ten)); // Điều chỉnh độ rộng của cột Tên
            txtxuathoadon.append(String.format("%-5s", sl));   // Điều chỉnh độ rộng của cột Số lượng
            txtxuathoadon.append(String.format("%-10s", gia)); // Điều chỉnh độ rộng của cột Giá
            txtxuathoadon.append("\n");

            // Thêm thông tin chi tiết vào chuỗi để lưu vào cơ sở dữ liệu
            chiTietHoaDon.append(ma).append(", ")
                         .append(ten).append(", ")
                         .append(sl).append(", ")
                         .append(gia).append("\n");
        }

        // Thêm đường gạch ngang
        txtxuathoadon.append("----------------------------------------------------------------------------------------------\n");

        // Gọi phương thức tinhTien() để tính tổng tiền
        tinhTien();

        // Thêm phần tổng tiền từ biến đã tính
        String tongTien = blbTongTien.getText();
        txtxuathoadon.append("Tổng tiền: " + tongTien + " VND\n");

        // Thêm đường gạch ngang
        txtxuathoadon.append("----------------------------------------------------------------------------------------------\n");

        // Thêm thông tin về wifi và mật khẩu
        txtxuathoadon.append("        Wifi: EnndmeCoffeeHouse\n");
        txtxuathoadon.append("        Mật khẩu wifi: lanhuongkhong  \n");
    }

    public String luuHoaDon() {
        String maHoaDon = null;
        try {
            // Generate the next sequential invoice number
            String nextInvoiceId = InvoiceNumberManager.getNextInvoiceNumber();

            // Lấy tổng số lượng từ tbOder
            int tongSoLuong = 0;
            DefaultTableModel dt = (DefaultTableModel) tbOder.getModel();
            for (int i = 0; i < tbOder.getRowCount(); i++) {
                tongSoLuong += Integer.parseInt(dt.getValueAt(i, 2).toString());
            }

            // Lấy giá từ blbTongTien và loại bỏ dấu phẩy
            String giaTongStr = blbTongTien.getText().replace(",", "");
            BigDecimal giaTong = new BigDecimal(giaTongStr);

            // Lấy ngày hiện tại
            Date ngayHienTai = new Date();

            // Tạo một đối tượng HoaDonMD mới
            HoaDonMD hoaDon = new HoaDonMD();
            hoaDon.setMaHoaDon(nextInvoiceId); // Set the generated invoice ID
            hoaDon.setSoLuong(tongSoLuong);
            hoaDon.setGia(giaTong);
            hoaDon.setNgayTao(ngayHienTai);

            // Chèn hóa đơn vào cơ sở dữ liệu
            HoaDonDao hoaDonDao = new HoaDonDao();
            long insertedId = hoaDonDao.insertAndReturnId(hoaDon);

            // Set the returned maHoaDon to the generated invoice ID
            maHoaDon = nextInvoiceId;

        } catch (Exception ex) {
            ex.printStackTrace(); // Handle exceptions appropriately
        }

        return maHoaDon;
    }

       public void openMenu(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                for(int i = 0; i<width; i++)
                    pnlSlideMenu.setSize(i,heigth);
                
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(QuanLyCoffee.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
    public void closeMenu(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                for(int i = width; i>0; i--)
                    pnlSlideMenu.setSize(i,heigth);
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(QuanLyCoffee.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
    
    private void loadDataToTableNV() {
        try {
            // Lấy dữ liệu 
            ArrayList<NhanVienMD> nhanvienList = nv.getALL();
            
            // Tạo một đối tượng DefaultTableModel để quản lý dữ liệu cho bảng
            DefaultTableModel model = new DefaultTableModel();
            
            // Đặt tên cho các cột
            model.addColumn("Mã Nhân Viên");
            model.addColumn("Họ Tên");
            model.addColumn("Điện Thoại");
            model.addColumn("Vị Trí");
            
            // Thêm dữ liệu từ nhanvienList vào bảng
            for (NhanVienMD nhanvien : nhanvienList) {
                model.addRow(new Object[]{nhanvien.getMaNhanVien(), nhanvien.getHoTen(), nhanvien.getDienThoai(), nhanvien.getViTri()});
            }
            
            // Đặt model cho bảng
            tbNhanVien.setModel(model); // Assuming tblNhanVien is the name of your JTable
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void loadDataToTableKH() {
        try {
            // Get customer data
            KhachHangDao khDao = new KhachHangDao();
            ArrayList<KhachHangMD> khachHangList = khDao.getALL();
            
            // Create a DefaultTableModel to manage data for the table
            DefaultTableModel model = new DefaultTableModel();
            
            // Set column names
            model.addColumn("Mã Khách Hàng");
            model.addColumn("Họ Tên");
            model.addColumn("Điện Thoại");
            model.addColumn("Địa Chỉ");
            
            // Add data from khachHangList to the table
            for (KhachHangMD khachHang : khachHangList) {
                model.addRow(new Object[]{khachHang.getMaKhachHang(), khachHang.getHoTen(), khachHang.getDienThoai(), khachHang.getDiaChi()});
            }
            
            // Set the model for the table
            tbKhachHang.setModel(model); // Assuming tbKhachHang is the name of your JTable
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
   
    private void loadDataToTableDH() {
    try {
        // Get order data
        DonHangDao dhDao = new DonHangDao();
        ArrayList<DonHangMD> donHangList = dhDao.getAll();
        
        // Create a DefaultTableModel to manage data for the table
        DefaultTableModel model = new DefaultTableModel();
        
        // Set column names
        model.addColumn("Mã Đơn Hàng");
        model.addColumn("Mã Khách Hàng");
        model.addColumn("Ngày Đặt Hàng");
        model.addColumn("Tổng Tiền");
        model.addColumn("Trạng Thái");
        
        // Add data from donHangList to the table
        for (DonHangMD donHang : donHangList) {
            model.addRow(new Object[]{donHang.getMaDonHang(), donHang.getMaKhachHang(), donHang.getNgayDatHang(), donHang.getTongTien(), donHang.getTrangThai()});
        }
        
        // Set the model for the table
        tbDonHang.setModel(model); // Assuming tbDonHang is the name of your JTable
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    }
    private void loadDataToTableSP() {
        try {
            // Get product data
            SanPhamDao spDao = new SanPhamDao();
            ArrayList<SanPhamMD> sanPhamList = spDao.getAll();

            // Create a DefaultTableModel to manage data for the table
            DefaultTableModel model = new DefaultTableModel();

            // Set column names
            model.addColumn("Mã Sản Phẩm");
            model.addColumn("Tên Sản Phẩm");
            model.addColumn("Loại Sản Phẩm");
            model.addColumn("Giá");

            // Add data from sanPhamList to the table
            for (SanPhamMD sanPham : sanPhamList) {
                model.addRow(new Object[]{sanPham.getMaSanPham(), sanPham.getTenSanPham(), sanPham.getLoaiSanPham(), sanPham.getGia()});
            }

            // Set the model for the table
            tbSanPham.setModel(model); // Assuming tbSanPham is the name of your JTable
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void loadDataToTableCTHD() {
            try {
                // Get order details data
                ChiTietDonHangDao cthdDao = new ChiTietDonHangDao();
                ArrayList<ChiTietDonHangMD> chiTietDonHangList = cthdDao.getAll();

                // Create a DefaultTableModel to manage data for the table
                DefaultTableModel model = new DefaultTableModel();

                // Set column names
                model.addColumn("Mã Chi Tiết Đơn Hàng");
                model.addColumn("Mã Đơn Hàng");
                model.addColumn("Mã Sản Phẩm");
                model.addColumn("Số Lượng");
                model.addColumn("Đơn Giá");

                // Add data from chiTietDonHangList to the table
                for (ChiTietDonHangMD cthd : chiTietDonHangList) {
                    model.addRow(new Object[]{cthd.getMaChiTietDonHang(), cthd.getMaDonHang(), 
                                               cthd.getMaSanPham(), cthd.getSoLuong(), cthd.getDonGia()});
                }

                // Set the model for the table
                tbChiTietDonHang.setModel(model); // Assuming tbChiTietDonHang is the name of your JTable
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
     private void loadDataToTableHoaDon() {
        try {
            // Get HoaDon data
            HoaDonDao hoaDonDao = new HoaDonDao();
            ArrayList<HoaDonMD> hoaDonList = hoaDonDao.getAll();

            // Create a DefaultTableModel to manage data for the table
            DefaultTableModel model = new DefaultTableModel();

            // Set column names
            model.addColumn("Mã Hóa Đơn");
            model.addColumn("Số Lượng");
            model.addColumn("Giá");
            model.addColumn("Ngày Tạo");

            // Add data from hoaDonList to the table
            for (HoaDonMD hoaDon : hoaDonList) {
                model.addRow(new Object[]{
                    hoaDon.getMaHoaDon(),
                    hoaDon.getSoLuong(),
                    hoaDon.getGia(),
                    hoaDon.getNgayTao()
                });
            }

            // Set the model for the table
            tbHoaDon.setModel(model); // Assuming tbHoaDon is the name of your JTable
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    

    // Other methods and components


    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnMain = new javax.swing.JPanel();
        pnlContainer = new javax.swing.JPanel();
        pnlHome = new javax.swing.JPanel();
        banner = new javax.swing.JLabel();
        pnlHSanPham = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtTim = new javax.swing.JTextField();
        btnTim = new javax.swing.JButton();
        cboSanPham = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        pnListSanPham = new javax.swing.JScrollPane();
        pnlSanPham = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        btnMua3 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        btnMua4 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        btnMua1 = new javax.swing.JButton();
        btnMua2 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        btnMua5 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        btnMua6 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        btnMua = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbSanPham = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbOder = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtxuathoadon = new javax.swing.JTextArea();
        jLabel67 = new javax.swing.JLabel();
        blbTongTien = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        btnXuatHoaDon = new javax.swing.JButton();
        pnlHoaDon = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbDonHang = new javax.swing.JTable();
        jLabel58 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbChiTietDonHang = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        pnlLichSu = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbHoaDon = new javax.swing.JTable();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tbThongKe = new javax.swing.JTable();
        txtNgayKetThuc = new javax.swing.JTextField();
        txtNgayBatDau = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        btnThongKe = new javax.swing.JButton();
        pnlKhachHang = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbKhachHang = new javax.swing.JTable();
        jLabel62 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        txtMaKH = new javax.swing.JTextField();
        txtHoTenKH = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        txtDiaChiKH = new javax.swing.JTextField();
        txtDTKH = new javax.swing.JTextField();
        btnThemKH = new javax.swing.JButton();
        btnSuaKH = new javax.swing.JButton();
        btnXoaKH = new javax.swing.JButton();
        pnlNhanVien = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        txtTenNV = new javax.swing.JTextField();
        txtChucVu = new javax.swing.JTextField();
        btnThemNV = new javax.swing.JButton();
        btnSuaNV = new javax.swing.JButton();
        btnXoaNV = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel59 = new javax.swing.JLabel();
        txtDienThoai = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        btnClearNV = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbNhanVien = new javax.swing.JTable();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel51 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel50 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        txtTimNV = new javax.swing.JTextField();
        btnTimNV = new javax.swing.JButton();
        pnlNavbar = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        pnlSlideMenu = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        btnHome = new javax.swing.JButton();
        btnSanPham = new javax.swing.JButton();
        btnHoaDon = new javax.swing.JButton();
        btnLichSu = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnKhachHang = new javax.swing.JButton();
        btnNhanVien = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Enndme Coffe");
        setPreferredSize(new java.awt.Dimension(1200, 800));

        pnMain.setPreferredSize(new java.awt.Dimension(1200, 800));

        pnlContainer.setMaximumSize(new java.awt.Dimension(1200, 800));
        pnlContainer.setLayout(new java.awt.CardLayout());

        pnlHome.setBackground(new java.awt.Color(204, 204, 204));
        pnlHome.setPreferredSize(new java.awt.Dimension(1200, 800));
        pnlHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlHomeMouseClicked(evt);
            }
        });

        banner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/banner.jpg"))); // NOI18N

        javax.swing.GroupLayout pnlHomeLayout = new javax.swing.GroupLayout(pnlHome);
        pnlHome.setLayout(pnlHomeLayout);
        pnlHomeLayout.setHorizontalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(banner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1223, Short.MAX_VALUE)
        );
        pnlHomeLayout.setVerticalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(banner, javax.swing.GroupLayout.PREFERRED_SIZE, 862, Short.MAX_VALUE)
        );

        pnlContainer.add(pnlHome, "card2");

        pnlHSanPham.setBackground(new java.awt.Color(255, 255, 255));
        pnlHSanPham.setPreferredSize(new java.awt.Dimension(1200, 800));

        jLabel5.setText("Tên Sản Phẩm");

        btnTim.setText("Tim");
        btnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimActionPerformed(evt);
            }
        });

        cboSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất Cả", "Thức Uống", "Đồ Ăn" }));
        cboSanPham.setToolTipText("");
        cboSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSanPhamActionPerformed(evt);
            }
        });

        jLabel6.setText("Loại sản Phẩm");

        pnListSanPham.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        pnlSanPham.setBackground(new java.awt.Color(255, 245, 238));
        pnlSanPham.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel4.setBackground(new java.awt.Color(255, 204, 204));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Đường Đen Sữa Đá.png"))); // NOI18N

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Đường Đen Sữa Đá");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setText("45.000 VND");

        btnMua3.setBackground(new java.awt.Color(255, 204, 204));
        btnMua3.setText("Mua");
        btnMua3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMua3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMua3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 18, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel23))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(btnMua3))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 204, 204));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cà Phê Sữa Đá.png"))); // NOI18N

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("Cà Phê Sữa Đá");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setText("29.000 VND");

        btnMua4.setBackground(new java.awt.Color(255, 204, 204));
        btnMua4.setText("Mua");
        btnMua4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMua4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel25))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnMua4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel10))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(jLabel25)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(btnMua4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 204, 204));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Trà Xanh Espresso Marble.png"))); // NOI18N

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("Trà Xanh Espresso Marble");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("49.000 VND");

        btnMua1.setBackground(new java.awt.Color(255, 204, 204));
        btnMua1.setText("Mua");

        btnMua2.setBackground(new java.awt.Color(255, 204, 204));
        btnMua2.setText("Mua");
        btnMua2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMua2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(btnMua2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel8)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(61, 61, 61)
                    .addComponent(btnMua1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGap(61, 61, 61)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnMua2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(96, 96, 96)
                    .addComponent(btnMua1)
                    .addContainerGap(96, Short.MAX_VALUE)))
        );

        jPanel8.setBackground(new java.awt.Color(255, 204, 204));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Bạc Sỉu.png"))); // NOI18N

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel27.setText("Bạc Sỉu");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel28.setText("29.000 VND");

        btnMua5.setBackground(new java.awt.Color(255, 204, 204));
        btnMua5.setText("Mua");
        btnMua5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMua5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel28)
                        .addGap(18, 18, 18)
                        .addComponent(btnMua5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jLabel27))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel11)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(jLabel27)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(btnMua5))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 204, 204));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/CloudTea Oolong Berry.png"))); // NOI18N

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel29.setText("CloudTea Oolong Berry");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel30.setText("69.000 VND");

        btnMua6.setBackground(new java.awt.Color(255, 204, 204));
        btnMua6.setText("Mua");
        btnMua6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMua6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(21, 21, 21))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMua6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel12)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(jLabel29)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnMua6)
                    .addComponent(jLabel30))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 204, 204));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/CloudTea Trà Xanh Tây Bắc.png"))); // NOI18N

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel31.setText("CloudTea Trà Xanh Tây Bắc");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel32.setText("69.000 VND");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel31))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel32)))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel32)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(255, 204, 204));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Hi-Tea Vải.png"))); // NOI18N

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel33.setText("Hi-Tea Vải");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setText("49.000 VND");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabel33))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel34))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel14)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel34)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(255, 204, 204));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Hi-Tea Đào.png"))); // NOI18N

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel35.setText("Hi-Tea Đào");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel36.setText("49.000 VND");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel15))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel36))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel35)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(255, 204, 204));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Phin Sữa Tươi Bánh Flan.png"))); // NOI18N

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Phin Sữa Tươi Bánh Flan");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("49.000 VND");

        btnMua.setBackground(new java.awt.Color(255, 204, 204));
        btnMua.setText("Mua");
        btnMua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMuaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnMua, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                            .addComponent(jLabel19)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel4)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(btnMua))
                .addGap(7, 7, 7))
        );

        jPanel14.setBackground(new java.awt.Color(255, 204, 204));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Mochi Kem Việt Quất.png"))); // NOI18N

        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel39.setText("Mochi Kem Việt Quất");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel40.setText("19.000 VND");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel39)
                            .addComponent(jLabel17))
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(57, 57, 57))))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addComponent(jLabel39)
                .addGap(18, 18, 18)
                .addComponent(jLabel40)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.setBackground(new java.awt.Color(255, 204, 204));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Mochi Kem Chocolate.png"))); // NOI18N

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel41.setText("Mochi Kem Chocolate");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel42.setText("19.000 VND");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel41)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jLabel42)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addComponent(jLabel41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel42)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBackground(new java.awt.Color(255, 204, 204));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Mochi Kem Chocolate.png"))); // NOI18N

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel37.setText("Mochi Kem Phúc Bồn Tử");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel38.setText("19.000 VND");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel16))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel37))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel38)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel38)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel18.setBackground(new java.awt.Color(255, 204, 204));

        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Mousse Tiramisu.png"))); // NOI18N

        jLabel47.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel47.setText("Mousse Tiramisu");

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel48.setText("35.000 VND");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel47)
                            .addComponent(jLabel46))
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel48)
                        .addGap(57, 57, 57))))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel46)
                .addGap(18, 18, 18)
                .addComponent(jLabel47)
                .addGap(18, 18, 18)
                .addComponent(jLabel48)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jPanel20.setBackground(new java.awt.Color(255, 204, 204));

        jLabel52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Mousse Gấu Chocolate.png"))); // NOI18N

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel53.setText("Mousse Gấu Chocolate");

        jLabel54.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel54.setText("39.000 VND");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel53)
                    .addComponent(jLabel52))
                .addGap(13, 13, 13))
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel54)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel52)
                .addGap(18, 18, 18)
                .addComponent(jLabel53)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel54)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setBackground(new java.awt.Color(255, 204, 204));

        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Mochi Kem Phúc Bồn Tử.png"))); // NOI18N

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel44.setText("Mochi Kem Phúc Bồn Tử");

        jLabel45.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel45.setText("19.000 VND");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel43))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel44))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel45)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel43)
                .addGap(18, 18, 18)
                .addComponent(jLabel44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel45))
        );

        javax.swing.GroupLayout pnlSanPhamLayout = new javax.swing.GroupLayout(pnlSanPham);
        pnlSanPham.setLayout(pnlSanPhamLayout);
        pnlSanPhamLayout.setHorizontalGroup(
            pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSanPhamLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSanPhamLayout.createSequentialGroup()
                        .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(20, 20, 20)
                        .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlSanPhamLayout.createSequentialGroup()
                                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(20, 20, 20)
                                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlSanPhamLayout.createSequentialGroup()
                                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(20, 20, 20)
                                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(0, 20, Short.MAX_VALUE))
        );
        pnlSanPhamLayout.setVerticalGroup(
            pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSanPhamLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(23, 23, 23)
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnListSanPham.setViewportView(pnlSanPham);

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tbSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã Sản Phẩm", "Tên Sản Phẩm", "Loại", "Giá (VND)"
            }
        ));
        tbSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbSanPhamMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbSanPham);

        tbOder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Giá"
            }
        ));
        jScrollPane4.setViewportView(tbOder);

        jButton1.setBackground(new java.awt.Color(255, 204, 204));
        jButton1.setText("Tạo Mới");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtxuathoadon.setColumns(20);
        txtxuathoadon.setRows(5);
        jScrollPane7.setViewportView(txtxuathoadon);

        jLabel67.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel67.setText("Tổng Tiền:");

        blbTongTien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        blbTongTien.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        blbTongTien.setText("0");

        jLabel68.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel68.setText("VND");

        btnXuatHoaDon.setText("Xuất Hóa Đơn");
        btnXuatHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(89, 89, 89)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(btnXuatHoaDon))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel67))
                                .addGap(22, 22, 22))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(blbTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addComponent(jScrollPane7)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel3)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel67)
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel68)
                            .addComponent(blbTongTien))
                        .addGap(86, 86, 86)
                        .addComponent(btnXuatHoaDon))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlHSanPhamLayout = new javax.swing.GroupLayout(pnlHSanPham);
        pnlHSanPham.setLayout(pnlHSanPhamLayout);
        pnlHSanPhamLayout.setHorizontalGroup(
            pnlHSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHSanPhamLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlHSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnListSanPham, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHSanPhamLayout.createSequentialGroup()
                        .addGroup(pnlHSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(pnlHSanPhamLayout.createSequentialGroup()
                                .addComponent(txtTim, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTim, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlHSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(86, 86, 86)))
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlHSanPhamLayout.setVerticalGroup(
            pnlHSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHSanPhamLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnlHSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlHSanPhamLayout.createSequentialGroup()
                        .addGroup(pnlHSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlHSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTim, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTim))
                        .addGap(18, 18, 18)
                        .addComponent(pnListSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 666, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(116, Short.MAX_VALUE))
        );

        pnlContainer.add(pnlHSanPham, "card3");

        pnlHoaDon.setBackground(new java.awt.Color(255, 255, 255));
        pnlHoaDon.setPreferredSize(new java.awt.Dimension(1200, 800));

        jPanel19.setBackground(new java.awt.Color(255, 204, 204));
        jPanel19.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 435, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 444, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(255, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tbDonHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã Đơn Hàng", "Mã Khách Hàng", "Ngày Đặt Hàng", "Tổng Tiền ", "Trạng Thái"
            }
        ));
        jScrollPane3.setViewportView(tbDonHang);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
        );

        jLabel58.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel58.setText("Đặt Hàng ");

        jLabel60.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel60.setText("Chi Tiết Đặt Hàng");

        jLabel61.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel61.setText("Xuất Hóa Đơn");

        jScrollPane5.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());

        tbChiTietDonHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã CTDH", "Mã Đơn Hàng", "Mã Sản Phẩm", "Số Lượng", "Đơn Giá"
            }
        ));
        jScrollPane5.setViewportView(tbChiTietDonHang);

        jLabel2.setText("Trạng Thái");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất Cả" }));

        javax.swing.GroupLayout pnlHoaDonLayout = new javax.swing.GroupLayout(pnlHoaDon);
        pnlHoaDon.setLayout(pnlHoaDonLayout);
        pnlHoaDonLayout.setHorizontalGroup(
            pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHoaDonLayout.createSequentialGroup()
                .addContainerGap(141, Short.MAX_VALUE)
                .addGroup(pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHoaDonLayout.createSequentialGroup()
                        .addGroup(pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5)
                            .addGroup(pnlHoaDonLayout.createSequentialGroup()
                                .addGroup(pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(pnlHoaDonLayout.createSequentialGroup()
                        .addComponent(jLabel58)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(33, 33, 33)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)))
                .addGroup(pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );
        pnlHoaDonLayout.setVerticalGroup(
            pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHoaDonLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel58)
                    .addGroup(pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel61)
                        .addComponent(jLabel2)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHoaDonLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel60)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(pnlHoaDonLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(347, Short.MAX_VALUE))
        );

        pnlContainer.add(pnlHoaDon, "card4");

        pnlLichSu.setBackground(new java.awt.Color(255, 255, 255));
        pnlLichSu.setPreferredSize(new java.awt.Dimension(1200, 800));

        jPanel21.setBackground(new java.awt.Color(255, 204, 204));

        tbHoaDon.setBackground(new java.awt.Color(255, 204, 204));
        tbHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Số Hóa Đơn", "Só Lượng", "Giá", "Ngày Tạo"
            }
        ));
        jScrollPane8.setViewportView(tbHoaDon);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel69.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel69.setText("Hóa Đơn");

        jLabel70.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel70.setText("Thống Kê");

        tbThongKe.setBackground(new java.awt.Color(255, 204, 204));
        tbThongKe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Số Lượng ", "Tổng Tiền", "Ngày Bắt Đầu", "Ngày Kết Thúc"
            }
        ));
        jScrollPane9.setViewportView(tbThongKe);

        txtNgayBatDau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayBatDauActionPerformed(evt);
            }
        });

        jLabel71.setText("Ngày Bắt Đầu:");

        jLabel72.setText("Ngày Kết Thúc:");

        btnThongKe.setBackground(new java.awt.Color(255, 204, 204));
        btnThongKe.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnThongKe.setText("Thống Kê");
        btnThongKe.setBorder(null);
        btnThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongKeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlLichSuLayout = new javax.swing.GroupLayout(pnlLichSu);
        pnlLichSu.setLayout(pnlLichSuLayout);
        pnlLichSuLayout.setHorizontalGroup(
            pnlLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLichSuLayout.createSequentialGroup()
                .addGroup(pnlLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLichSuLayout.createSequentialGroup()
                        .addContainerGap(192, Short.MAX_VALUE)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlLichSuLayout.createSequentialGroup()
                        .addGroup(pnlLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlLichSuLayout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addGroup(pnlLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLichSuLayout.createSequentialGroup()
                                        .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(169, 169, 169))
                                    .addGroup(pnlLichSuLayout.createSequentialGroup()
                                        .addGap(32, 32, 32)
                                        .addComponent(btnThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(89, 89, 89))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLichSuLayout.createSequentialGroup()
                                        .addGroup(pnlLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(pnlLichSuLayout.createSequentialGroup()
                                                .addComponent(jLabel72)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlLichSuLayout.createSequentialGroup()
                                                .addComponent(jLabel71)
                                                .addGap(21, 21, 21)
                                                .addComponent(txtNgayBatDau)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlLichSuLayout.createSequentialGroup()
                                .addGap(159, 159, 159)
                                .addComponent(jLabel69)))
                        .addGap(64, 64, 64)))
                .addContainerGap(208, Short.MAX_VALUE))
        );
        pnlLichSuLayout.setVerticalGroup(
            pnlLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLichSuLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel69)
                .addGap(18, 18, 18)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLichSuLayout.createSequentialGroup()
                        .addComponent(jLabel70)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel71))
                        .addGap(81, 81, 81)
                        .addGroup(pnlLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel72))
                        .addGap(32, 32, 32)
                        .addComponent(btnThongKe))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(106, Short.MAX_VALUE))
        );

        pnlContainer.add(pnlLichSu, "card5");

        pnlKhachHang.setBackground(new java.awt.Color(255, 255, 255));
        pnlKhachHang.setPreferredSize(new java.awt.Dimension(1200, 800));

        tbKhachHang.setBackground(new java.awt.Color(255, 204, 204));
        tbKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã Khách Hàng", "Họ Tên", "Điện Thoại", "Địa Chỉ"
            }
        ));
        tbKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbKhachHangMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tbKhachHang);

        jLabel62.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel62.setText("Khách Hàng");

        jPanel23.setBackground(new java.awt.Color(255, 204, 204));

        jLabel63.setText("Mã Khách Hàng");

        jLabel64.setText("Họ Tên");

        jLabel65.setText("Điện Thoại");

        jLabel66.setText("Địa Chỉ");

        btnThemKH.setText("Thêm");
        btnThemKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemKHActionPerformed(evt);
            }
        });

        btnSuaKH.setText("Sữa");
        btnSuaKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaKHActionPerformed(evt);
            }
        });

        btnXoaKH.setText("Xóa");
        btnXoaKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnThemKH)
                    .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                            .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel65)))
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtHoTenKH)
                            .addComponent(txtMaKH)
                            .addComponent(txtDiaChiKH)
                            .addComponent(txtDTKH, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(btnSuaKH)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                        .addComponent(btnXoaKH)
                        .addGap(56, 56, 56))))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(txtHoTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel65)
                    .addComponent(txtDTKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(txtDiaChiKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemKH)
                    .addComponent(btnSuaKH)
                    .addComponent(btnXoaKH))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlKhachHangLayout = new javax.swing.GroupLayout(pnlKhachHang);
        pnlKhachHang.setLayout(pnlKhachHangLayout);
        pnlKhachHangLayout.setHorizontalGroup(
            pnlKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKhachHangLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(pnlKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel62)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(90, Short.MAX_VALUE))
        );
        pnlKhachHangLayout.setVerticalGroup(
            pnlKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKhachHangLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel62)
                .addGap(18, 18, 18)
                .addGroup(pnlKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(468, Short.MAX_VALUE))
        );

        pnlContainer.add(pnlKhachHang, "card6");

        pnlNhanVien.setBackground(new java.awt.Color(255, 255, 255));
        pnlNhanVien.setPreferredSize(new java.awt.Dimension(1200, 800));

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel55.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel55.setText("Mã Nhân Viên:");

        jLabel56.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel56.setText("Tên Nhân Viên:");

        jLabel57.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel57.setText("Chức Vụ:");

        txtMaNV.setBackground(new java.awt.Color(255, 204, 204));
        txtMaNV.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtMaNV.setBorder(null);

        txtTenNV.setBackground(new java.awt.Color(255, 204, 204));
        txtTenNV.setBorder(null);

        txtChucVu.setBackground(new java.awt.Color(255, 204, 204));
        txtChucVu.setBorder(null);

        btnThemNV.setBackground(new java.awt.Color(255, 204, 204));
        btnThemNV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemNV.setText("Thêm");
        btnThemNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNVActionPerformed(evt);
            }
        });

        btnSuaNV.setBackground(new java.awt.Color(255, 204, 204));
        btnSuaNV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuaNV.setText("Sửa");
        btnSuaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaNVActionPerformed(evt);
            }
        });

        btnXoaNV.setBackground(new java.awt.Color(255, 204, 204));
        btnXoaNV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoaNV.setText("Xóa");
        btnXoaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNVActionPerformed(evt);
            }
        });

        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));

        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));

        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));

        jLabel59.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel59.setText("Điện Thoại");

        txtDienThoai.setBackground(new java.awt.Color(255, 204, 204));
        txtDienThoai.setBorder(null);

        jSeparator6.setForeground(new java.awt.Color(0, 0, 0));

        btnClearNV.setBackground(new java.awt.Color(255, 204, 204));
        btnClearNV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnClearNV.setText("Clear");
        btnClearNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearNVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(btnThemNV)
                .addGap(28, 28, 28)
                .addComponent(btnSuaNV)
                .addGap(18, 18, 18)
                .addComponent(btnXoaNV)
                .addGap(18, 18, 18)
                .addComponent(btnClearNV)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator4)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtMaNV, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                            .addComponent(txtTenNV)))
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDienThoai, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jSeparator5)
                                .addComponent(txtChucVu, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                                .addComponent(jSeparator6)))))
                .addContainerGap(7, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(txtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(txtDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(txtChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemNV)
                    .addComponent(btnSuaNV)
                    .addComponent(btnClearNV)
                    .addComponent(btnXoaNV))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tbNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã Nhân Viên", "Tên Nhân Viên", "Điện Thoại", "Chức Vụ"
            }
        ));
        tbNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbNhanVienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbNhanVien);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất Cả" }));

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel51.setText("Trạng Thái");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất Cả", "Quản Lý", "Nhân Viên" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel50.setText("Vai Trò");

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel49.setText("Tên Nhân Viên");

        txtTimNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimNVActionPerformed(evt);
            }
        });

        btnTimNV.setText("Tìm");
        btnTimNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimNVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(15, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel49)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtTimNV, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnTimNV, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel50))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(jLabel50)
                    .addComponent(jLabel51))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimNV)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 59, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlNhanVienLayout = new javax.swing.GroupLayout(pnlNhanVien);
        pnlNhanVien.setLayout(pnlNhanVienLayout);
        pnlNhanVienLayout.setHorizontalGroup(
            pnlNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNhanVienLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );
        pnlNhanVienLayout.setVerticalGroup(
            pnlNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNhanVienLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(pnlNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(205, Short.MAX_VALUE))
        );

        pnlContainer.add(pnlNhanVien, "card7");

        pnlNavbar.setBackground(new java.awt.Color(255, 204, 204));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Enndme The Coffee House");

        jButton8.setBackground(new java.awt.Color(255, 204, 204));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/menu.png"))); // NOI18N
        jButton8.setBorder(null);
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton8MouseClicked(evt);
            }
        });
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlNavbarLayout = new javax.swing.GroupLayout(pnlNavbar);
        pnlNavbar.setLayout(pnlNavbarLayout);
        pnlNavbarLayout.setHorizontalGroup(
            pnlNavbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNavbarLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 397, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(296, 296, 296))
        );
        pnlNavbarLayout.setVerticalGroup(
            pnlNavbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNavbarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pnlNavbarLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlSlideMenu.setBackground(new java.awt.Color(255, 204, 204));
        pnlSlideMenu.setMinimumSize(new java.awt.Dimension(140, 800));
        pnlSlideMenu.setPreferredSize(new java.awt.Dimension(140, 800));
        pnlSlideMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logo.png"))); // NOI18N
        pnlSlideMenu.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, 100));

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        pnlSlideMenu.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 167, 140, -1));

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        pnlSlideMenu.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 650, 140, 30));

        btnHome.setBackground(new java.awt.Color(255, 204, 204));
        btnHome.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/home.png"))); // NOI18N
        btnHome.setText("Home");
        btnHome.setBorder(null);
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
        });
        pnlSlideMenu.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 140, -1));

        btnSanPham.setBackground(new java.awt.Color(255, 204, 204));
        btnSanPham.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/sanpham.png"))); // NOI18N
        btnSanPham.setText("Sản Phẩm");
        btnSanPham.setBorder(null);
        btnSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSanPhamMouseClicked(evt);
            }
        });
        pnlSlideMenu.add(btnSanPham, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 140, -1));

        btnHoaDon.setBackground(new java.awt.Color(255, 204, 204));
        btnHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/thanhtoan.png"))); // NOI18N
        btnHoaDon.setText("Hóa Đơn");
        btnHoaDon.setBorder(null);
        btnHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHoaDonMouseClicked(evt);
            }
        });
        pnlSlideMenu.add(btnHoaDon, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 330, 140, -1));

        btnLichSu.setBackground(new java.awt.Color(255, 204, 204));
        btnLichSu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLichSu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/lichsu.png"))); // NOI18N
        btnLichSu.setText("Lịch Sử");
        btnLichSu.setBorder(null);
        btnLichSu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLichSuMouseClicked(evt);
            }
        });
        pnlSlideMenu.add(btnLichSu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, 140, -1));

        btnLogout.setBackground(new java.awt.Color(255, 204, 204));
        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout.png"))); // NOI18N
        btnLogout.setBorder(null);
        pnlSlideMenu.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 730, -1, -1));

        btnKhachHang.setBackground(new java.awt.Color(255, 204, 204));
        btnKhachHang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnKhachHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/khachhang.png"))); // NOI18N
        btnKhachHang.setText("Khách Hàng");
        btnKhachHang.setBorder(null);
        btnKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnKhachHangMouseClicked(evt);
            }
        });
        pnlSlideMenu.add(btnKhachHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 460, 140, -1));

        btnNhanVien.setBackground(new java.awt.Color(255, 204, 204));
        btnNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/user.png"))); // NOI18N
        btnNhanVien.setText("Nhân Viên");
        btnNhanVien.setBorder(null);
        btnNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNhanVienMouseClicked(evt);
            }
        });
        pnlSlideMenu.add(btnNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, 140, -1));

        jButton7.setBackground(new java.awt.Color(255, 204, 204));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/close.png"))); // NOI18N
        jButton7.setBorder(null);
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton7MouseClicked(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        pnlSlideMenu.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        javax.swing.GroupLayout pnMainLayout = new javax.swing.GroupLayout(pnMain);
        pnMain.setLayout(pnMainLayout);
        pnMainLayout.setHorizontalGroup(
            pnMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnMainLayout.createSequentialGroup()
                .addComponent(pnlSlideMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(pnlNavbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnMainLayout.setVerticalGroup(
            pnMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlSlideMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnMainLayout.createSequentialGroup()
                .addComponent(pnlNavbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 862, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1223, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 938, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pnlHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlHomeMouseClicked
        
    }//GEN-LAST:event_pnlHomeMouseClicked

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSanPhamMouseClicked
        CardLayout cardLayout = (CardLayout) pnlContainer.getLayout();
        cardLayout.show(pnlContainer, "pnlHSanPham");
        
    }//GEN-LAST:event_btnSanPhamMouseClicked

    private void btnHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHoaDonMouseClicked
         CardLayout cardLayout = (CardLayout) pnlContainer.getLayout();
         cardLayout.show(pnlContainer, "pnlHoaDon");
    }//GEN-LAST:event_btnHoaDonMouseClicked

    private void btnLichSuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLichSuMouseClicked
         CardLayout cardLayout = (CardLayout) pnlContainer.getLayout();
         cardLayout.show(pnlContainer, "pnlLichSu");
    }//GEN-LAST:event_btnLichSuMouseClicked

    private void btnKhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnKhachHangMouseClicked
        CardLayout cardLayout = (CardLayout) pnlContainer.getLayout();
        cardLayout.show(pnlContainer, "pnlKhachHang");
    }//GEN-LAST:event_btnKhachHangMouseClicked

    private void btnNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNhanVienMouseClicked
        CardLayout cardLayout = (CardLayout) pnlContainer.getLayout();
        cardLayout.show(pnlContainer, "pnlNhanVien");
       
    }//GEN-LAST:event_btnNhanVienMouseClicked

    private void jButton7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseClicked
       closeMenu();
    }//GEN-LAST:event_jButton7MouseClicked

    private void jButton8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseClicked
       openMenu();
    }//GEN-LAST:event_jButton8MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        closeMenu();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        openMenu();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void tbNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbNhanVienMouseClicked
         // Get the selected row index
    int selectedRow = tbNhanVien.getSelectedRow();
    
    // Check if a row is selected
    if (selectedRow >= 0) {
        // Retrieve data from the selected row
        String maNV = tbNhanVien.getValueAt(selectedRow, 0).toString();
        String tenNV = tbNhanVien.getValueAt(selectedRow, 1).toString();
        String dienThoai = tbNhanVien.getValueAt(selectedRow, 2).toString();
        String viTri = tbNhanVien.getValueAt(selectedRow, 3).toString();
        
        // Populate text fields with retrieved data
        txtMaNV.setText(maNV);
        txtTenNV.setText(tenNV);
        txtDienThoai.setText(dienThoai);
        txtChucVu.setText(viTri);
    }
    }//GEN-LAST:event_tbNhanVienMouseClicked

    private void btnThemNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNVActionPerformed
         try {
        // Lấy dữ liệu từ các trường văn bản
        String maNV = txtMaNV.getText();
        String tenNV = txtTenNV.getText();
        String dienThoai = txtDienThoai.getText();
        String viTri = txtChucVu.getText();
        
        // Tạo một đối tượng NhanVienMD mới
        NhanVienMD nhanVienMoi = new NhanVienMD(maNV, tenNV, dienThoai, viTri);
        
        // Gọi phương thức trong NhanVienDao để chèn nhân viên mới vào cơ sở dữ liệu
        boolean thanhCong = NhanVienDao.insert(nhanVienMoi);
        
        if (thanhCong) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công.");
            
            // Làm mới bảng để hiển thị danh sách nhân viên đã được cập nhật
            loadDataToTableNV();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại.");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    }//GEN-LAST:event_btnThemNVActionPerformed

    private void btnSuaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaNVActionPerformed
        try {
        // Lấy thông tin mới từ các trường văn bản
        String maNV = txtMaNV.getText();
        String tenNV = txtTenNV.getText();
        String dienThoai = txtDienThoai.getText();
        String viTri = txtChucVu.getText();
        
        // Tạo một đối tượng NhanVienMD mới với thông tin đã cập nhật
        NhanVienMD nhanVienUpdated = new NhanVienMD(maNV, tenNV, dienThoai, viTri);
        
        // Gọi phương thức update từ lớp NhanVienDao để cập nhật thông tin của nhân viên trong cơ sở dữ liệu
        boolean thanhCong = NhanVienDao.update(nhanVienUpdated);
        
        if (thanhCong) {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin nhân viên thành công.");
            
            // Làm mới bảng để hiển thị danh sách nhân viên đã được cập nhật
            loadDataToTableNV();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin nhân viên thất bại.");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    }//GEN-LAST:event_btnSuaNVActionPerformed

    private void btnXoaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNVActionPerformed
        try {
        // Lấy mã nhân viên từ trường văn bản hoặc danh sách
        String maNV = txtMaNV.getText(); // hoặc từ bảng danh sách nhân viên
        
        // Gọi phương thức delete từ lớp NhanVienDao để xóa nhân viên khỏi cơ sở dữ liệu
        boolean thanhCong = NhanVienDao.delete(maNV);
        
        if (thanhCong) {
            JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công.");
            
            // Làm mới bảng để hiển thị danh sách nhân viên đã được cập nhật
            loadDataToTableNV();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa nhân viên thất bại.");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    }//GEN-LAST:event_btnXoaNVActionPerformed

    private void btnTimNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimNVActionPerformed
       ArrayList<NhanVienMD> ketQua = null;
        String hoTen = txtTimNV.getText();

        try {
            ketQua = NhanVienDao.searchByName(hoTen);

            // Hiển thị kết quả trên giao diện người dùng
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Mã Nhân Viên");
            model.addColumn("Họ Tên");
            model.addColumn("Điện Thoại");
            model.addColumn("Vị Trí");

            for (NhanVienMD nv : ketQua) {
                model.addRow(new Object[]{nv.getMaNhanVien(), nv.getHoTen(), nv.getDienThoai(), nv.getViTri()});
            }

            // Hiển thị model trên bảng
            tbNhanVien.setModel(model);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
    }//GEN-LAST:event_btnTimNVActionPerformed

    private void btnMua2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMua2ActionPerformed
        String Ma = "SP002";
        String Ten = "Trà Xanh Espresso Marble";
        int Gia = 49000;
        String SoLuong = JOptionPane.showInputDialog(this, "Nhấn để thêm sản phẩm","1");
        Double tSoLuong = Double.valueOf(SoLuong);
        Double tGia = Gia * tSoLuong;
        System.out.print(tGia);
        DefaultTableModel dt = (DefaultTableModel) tbOder.getModel();
            Vector v = new Vector();
            v.add(Ma);
            v.add(Ten);
            v.add(SoLuong);
            v.add(tGia);
            dt.addRow(v);
             tinhTien();
    }//GEN-LAST:event_btnMua2ActionPerformed

    private void btnMuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMuaActionPerformed
        String Ma = "SP001";
        String Ten = "Phin Sữa Tươi Bánh Flan";
        int Gia = 49000;
        String SoLuong = JOptionPane.showInputDialog(this, "Nhấn để thêm sản phẩm","1");
        Double tSoLuong = Double.valueOf(SoLuong);
        Double tGia = Gia * tSoLuong;
        System.out.print(tGia);
        DefaultTableModel dt = (DefaultTableModel) tbOder.getModel();
            Vector v = new Vector();
            v.add(Ma);
            v.add(Ten);
            v.add(SoLuong);
            v.add(tGia);
            dt.addRow(v);
             tinhTien();
    }//GEN-LAST:event_btnMuaActionPerformed

    private void btnMua3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMua3ActionPerformed
        String Ma = "SP003";
        String Ten = "Đường Đen Sữa Đá";
        int Gia = 45000;
        String SoLuong = JOptionPane.showInputDialog(this, "Nhấn để thêm sản phẩm","1");
        Double tSoLuong = Double.valueOf(SoLuong);
        Double tGia = Gia * tSoLuong;
        System.out.print(tGia);
        DefaultTableModel dt = (DefaultTableModel) tbOder.getModel();
            Vector v = new Vector();
            v.add(Ma);
            v.add(Ten);
            v.add(SoLuong);
            v.add(tGia);
            dt.addRow(v);
             tinhTien();
    }//GEN-LAST:event_btnMua3ActionPerformed

    private void btnMua4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMua4ActionPerformed
        String Ma = "SP004";
        String Ten = "Cà Phê Sữa Đá"; 
        int Gia = 29000;
        String SoLuong = JOptionPane.showInputDialog(this, "Nhấn để thêm sản phẩm","1");
        Double tSoLuong = Double.valueOf(SoLuong);
        Double tGia = Gia * tSoLuong;
        System.out.print(tGia);
        DefaultTableModel dt = (DefaultTableModel) tbOder.getModel();
            Vector v = new Vector();
            v.add(Ma);
            v.add(Ten);
            v.add(SoLuong);
            v.add(tGia);
            dt.addRow(v);
             tinhTien();
    }//GEN-LAST:event_btnMua4ActionPerformed

    private void btnMua5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMua5ActionPerformed
        String Ma = "SP005";
        String Ten = "Bạc Sỉu";
        int Gia = 29000;
        String SoLuong = JOptionPane.showInputDialog(this, "Nhấn để thêm sản phẩm","1");
        Double tSoLuong = Double.valueOf(SoLuong);
        Double tGia = Gia * tSoLuong;
        System.out.print(tGia);
        DefaultTableModel dt = (DefaultTableModel) tbOder.getModel();
            Vector v = new Vector();
            v.add(Ma);
            v.add(Ten);
            v.add(SoLuong);
            v.add(tGia);
            dt.addRow(v);
             tinhTien();
    }//GEN-LAST:event_btnMua5ActionPerformed

    private void btnMua6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMua6ActionPerformed
        String Ma = "SP006";
        String Ten = "CloudTea Oolong Berry";
        int Gia = 69000;
        String SoLuong = JOptionPane.showInputDialog(this, "Nhấn để thêm sản phẩm","1");
        Double tSoLuong = Double.valueOf(SoLuong);
        Double tGia = Gia * tSoLuong;
        System.out.print(tGia);
        DefaultTableModel dt = (DefaultTableModel) tbOder.getModel();
            Vector v = new Vector();
            v.add(Ma);
            v.add(Ten);
            v.add(SoLuong);
            v.add(tGia);
            dt.addRow(v);
            tinhTien();
    }//GEN-LAST:event_btnMua6ActionPerformed

    private void tbSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbSanPhamMouseClicked
        // Lấy chỉ số của hàng được chọn
    int selectedRow = tbSanPham.getSelectedRow();
    // Kiểm tra xem hàng được chọn có hợp lệ không
    if(selectedRow != -1) {
        // Lấy thông tin sản phẩm từ hàng được chọn
        String ma = tbSanPham.getValueAt(selectedRow, 0).toString();
        String ten = tbSanPham.getValueAt(selectedRow, 1).toString();
        String giaStr = tbSanPham.getValueAt(selectedRow, 3).toString();
        double gia = Double.parseDouble(giaStr);
        
        // Hiển thị hộp thoại để nhập số lượng
        String soLuong = JOptionPane.showInputDialog(this, "Nhấn để thêm sản phẩm", "1");
        
        // Kiểm tra nếu người dùng không nhập số lượng
        if(soLuong != null && !soLuong.isEmpty()) {
            try {
                // Chuyển đổi số lượng nhập vào kiểu số
                double tSoLuong = Double.parseDouble(soLuong);
                
                // Tính tổng giá
                double tGia = gia * tSoLuong;
                
                // Thêm thông tin sản phẩm vào giỏ hàng
                DefaultTableModel dt = (DefaultTableModel) tbOder.getModel();
                Vector v = new Vector();
                v.add(ma);
                v.add(ten);
                v.add(soLuong);
                v.add(tGia);
                dt.addRow(v);
            } catch(NumberFormatException ex) {
                // Xử lý khi người dùng nhập không đúng định dạng số
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng là một số.");
            }
        }
        tinhTien();
    }
    }//GEN-LAST:event_tbSanPhamMouseClicked

    private void btnXuatHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatHoaDonActionPerformed
        xuatHoaDon();
        luuHoaDon();
    }//GEN-LAST:event_btnXuatHoaDonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        DefaultTableModel model = (DefaultTableModel) tbOder.getModel();
        model.setRowCount(0);
        txtxuathoadon.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtTimNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimNVActionPerformed

    private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimActionPerformed
        try {
        // Lấy văn bản nhập vào từ trường txtTim
        String searchText = txtTim.getText().trim();

        if (searchText.isEmpty()) {
            // Nếu văn bản tìm kiếm trống, tải tất cả sản phẩm
            loadDataToTableSP();
        } else {
            // Gọi phương thức searchByName từ SanPhamDao để tìm sản phẩm
            ArrayList<SanPhamMD> searchResults = SanPhamDao.searchByName(searchText);

            // Cập nhật bảng tbSanPham với kết quả tìm kiếm
            DefaultTableModel model = (DefaultTableModel) tbSanPham.getModel();
            model.setRowCount(0); // Xóa các hàng hiện có

            for (SanPhamMD product : searchResults) {
                Object[] row = {product.getMaSanPham(), product.getTenSanPham(), product.getLoaiSanPham(), product.getGia()};
                model.addRow(row);
            }

            tbSanPham.setModel(model);
        }
    } catch (Exception ex) {
        ex.printStackTrace(); // Xử lý ngoại lệ một cách phù hợp
    }
                   
    }//GEN-LAST:event_btnTimActionPerformed

    private void cboSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSanPhamActionPerformed
                                          
    try {
        // Lấy loại sản phẩm được chọn từ combobox
        String selectedType = (String) cboSanPham.getSelectedItem();

        // Kiểm tra nếu loại sản phẩm được chọn là null hoặc trống
        if (selectedType == "Tất Cả" || selectedType.isEmpty()) {
            // Nếu không có loại sản phẩm được chọn, hiển thị tất cả sản phẩm
             loadDataToTableSP();
        } else {
            // Gọi phương thức để lọc sản phẩm theo loại từ cơ sở dữ liệu
            ArrayList<SanPhamMD> filteredProducts = SanPhamDao.getProductsByType(selectedType);

            // Cập nhật bảng hiển thị với danh sách sản phẩm đã lọc
            DefaultTableModel model = (DefaultTableModel) tbSanPham.getModel();
            model.setRowCount(0); // Xóa các hàng hiện có

            for (SanPhamMD product : filteredProducts) {
                Object[] row = {product.getMaSanPham(), product.getTenSanPham(), product.getLoaiSanPham(), product.getGia()};
                model.addRow(row);
            }

            tbSanPham.setModel(model);
        }
    } catch (Exception ex) {
        ex.printStackTrace(); // Xử lý ngoại lệ một cách phù hợp
    }

    }//GEN-LAST:event_cboSanPhamActionPerformed

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeActionPerformed
          try {
        String ngayBatDau = txtNgayBatDau.getText();
        String ngayKetThuc = txtNgayKetThuc.getText();

        HoaDonDao hoaDonDao = new HoaDonDao();
        int tongSoLuong = hoaDonDao.getTongSoLuongTrongKhoangThoiGian(ngayBatDau, ngayKetThuc);
        float tongGiaTien = hoaDonDao.getTongGiaTienTrongKhoangThoiGian(ngayBatDau, ngayKetThuc);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Tổng Số Lượng");
        model.addColumn("Tổng Giá Tiền");
        model.addColumn("Ngày Bắt Đầu");
        model.addColumn("Ngày Kết Thúc");
        model.addRow(new Object[]{tongSoLuong, tongGiaTien, ngayBatDau, ngayKetThuc});

        tbThongKe.setModel(model);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    
    }//GEN-LAST:event_btnThongKeActionPerformed

    private void btnThemKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemKHActionPerformed
        try {
        // Lấy dữ liệu nhập từ các trường trên giao diện
        String maKhachHang = txtMaKH.getText();
        String hoTen = txtHoTenKH.getText();
        String dienThoai = txtDTKH.getText();
        String diaChi = txtDiaChiKH.getText();
        
        // Tạo một đối tượng KhachHangMD mới với dữ liệu nhập
        KhachHangMD khachHangMoi = new KhachHangMD();
        khachHangMoi.setMaKhachHang(maKhachHang);
        khachHangMoi.setHoTen(hoTen);
        khachHangMoi.setDienThoai(dienThoai);
        khachHangMoi.setDiaChi(diaChi);
        
        // Gọi phương thức insert từ lớp KhachHangDao để thêm khách hàng mới vào cơ sở dữ liệu
        boolean thanhCong = KhachHangDao.insert(khachHangMoi);
        
        // Hiển thị thông báo thành công nếu việc thêm thành công
        if (thanhCong) {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công.");
            loadDataToTableKH();
            
            // Tuỳ chọn: Xóa nội dung trường nhập hoặc thực hiện các hành động cần thiết khác
            xoaNoiDungTruongNhap();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi thêm khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace(); // Ghi log lỗi để debug
    }
    }//GEN-LAST:event_btnThemKHActionPerformed

    private void btnSuaKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaKHActionPerformed
       try {
        // Lấy mã khách hàng cần sửa từ trường nhập trên giao diện
        String maKhachHang = txtMaKH.getText();
        
        // Kiểm tra xem mã khách hàng có tồn tại hay không
        KhachHangMD khachHang = KhachHangDao.getById(maKhachHang);
        if (khachHang == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng có mã " + maKhachHang, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return; // Kết thúc phương thức nếu không tìm thấy khách hàng
        }
        
        // Lấy thông tin cập nhật từ các trường nhập trên giao diện
        String hoTen = txtHoTenKH.getText();
        String dienThoai = txtDTKH.getText();
        String diaChi = txtDiaChiKH.getText();
        
        // Cập nhật thông tin của khách hàng
        khachHang.setHoTen(hoTen);
        khachHang.setDienThoai(dienThoai);
        khachHang.setDiaChi(diaChi);
        
        // Gọi phương thức update từ lớp KhachHangDao để cập nhật thông tin vào cơ sở dữ liệu
        boolean thanhCong = KhachHangDao.update(khachHang);
        
        // Hiển thị thông báo thành công nếu việc cập nhật thành công
        if (thanhCong) {
            JOptionPane.showMessageDialog(this, "Thông tin khách hàng đã được cập nhật thành công.");
            loadDataToTableKH();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin khách hàng thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi cập nhật thông tin khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace(); // Ghi log lỗi để debug
    }
    }//GEN-LAST:event_btnSuaKHActionPerformed

    private void btnXoaKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaKHActionPerformed
        try {
        // Lấy mã khách hàng cần xóa từ trường nhập trên giao diện
        String maKhachHang = txtMaKH.getText();
        
        // Kiểm tra xem mã khách hàng có tồn tại hay không
        KhachHangMD khachHang = KhachHangDao.getById(maKhachHang);
        if (khachHang == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng có mã " + maKhachHang, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return; // Kết thúc phương thức nếu không tìm thấy khách hàng
        }
        
        // Hiển thị hộp thoại xác nhận trước khi xóa khách hàng
        int luaChon = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khách hàng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        // Nếu người dùng chọn "Yes" (Có) trong hộp thoại xác nhận
        if (luaChon == JOptionPane.YES_OPTION) {
            // Gọi phương thức delete từ lớp KhachHangDao để xóa khách hàng khỏi cơ sở dữ liệu
            boolean thanhCong = KhachHangDao.delete(maKhachHang);
            
            // Hiển thị thông báo thành công nếu việc xóa thành công
            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Khách hàng đã được xóa thành công.");
                loadDataToTableKH();
                
                // Tuỳ chọn: Xóa nội dung trường nhập hoặc thực hiện các hành động cần thiết khác
                xoaNoiDungTruongNhap();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi xóa khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace(); // Ghi log lỗi để debug
    }
    }//GEN-LAST:event_btnXoaKHActionPerformed

    private void tbKhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbKhachHangMouseClicked
         int rowIndex = tbKhachHang.getSelectedRow();
    
        // Kiểm tra xem có hàng được chọn không
        if (rowIndex >= 0) {
            // Lấy thông tin của khách hàng từ hàng được chọn
            String maKhachHang = tbKhachHang.getValueAt(rowIndex, 0).toString();
            String hoTen = tbKhachHang.getValueAt(rowIndex, 1).toString();
            String dienThoai = tbKhachHang.getValueAt(rowIndex, 2).toString();
            String diaChi = tbKhachHang.getValueAt(rowIndex, 3).toString();

            // Hiển thị thông tin của khách hàng lên các ô nhập liệu
            txtMaKH.setText(maKhachHang);
            txtHoTenKH.setText(hoTen);
            txtDTKH.setText(dienThoai);
            txtDiaChiKH.setText(diaChi);
        }
    }//GEN-LAST:event_tbKhachHangMouseClicked

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
       String selectedPosition = (String) jComboBox1.getSelectedItem(); // Lấy vị trí được chọn từ JComboBox
        try {
            ArrayList<NhanVienMD> ketQua;

            // Gọi phương thức getEmployeesByPosition để lấy danh sách nhân viên dựa trên vị trí
            if ("Tất Cả".equals(selectedPosition)) {
                // Tải tất cả nhân viên nếu "Tất cả" được chọn
                ketQua = getAllEmployees();
            } else {
                ketQua = getEmployeesByPosition(selectedPosition);
            }

            // Xóa dữ liệu hiện tại trong bảng
            DefaultTableModel model = (DefaultTableModel) tbNhanVien.getModel();
            model.setRowCount(0);

            // Hiển thị thông tin nhân viên trong bảng
            for (NhanVienMD nv : ketQua) {
                // Thêm hàng mới vào bảng với thông tin của nhân viên
                model.addRow(new Object[]{nv.getMaNhanVien(), nv.getHoTen(), nv.getDienThoai(), nv.getViTri()});
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Xử lý ngoại lệ
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void txtNgayBatDauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayBatDauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayBatDauActionPerformed

    private void btnClearNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearNVActionPerformed
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtDienThoai.setText("");
        txtChucVu.setText("");
    }//GEN-LAST:event_btnClearNVActionPerformed
    private void xoaNoiDungTruongNhap() {
    // Xóa nội dung trường nhập sau khi thêm thành công
    txtMaKH.setText("");
    txtHoTenKH.setText("");
    txtDTKH.setText("");
    txtDiaChiKH.setText("");
}
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QuanLyCoffee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyCoffee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyCoffee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyCoffee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLyCoffee().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel banner;
    private javax.swing.JLabel blbTongTien;
    private javax.swing.JButton btnClearNV;
    private javax.swing.JButton btnHoaDon;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnKhachHang;
    private javax.swing.JButton btnLichSu;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMua;
    private javax.swing.JButton btnMua1;
    private javax.swing.JButton btnMua2;
    private javax.swing.JButton btnMua3;
    private javax.swing.JButton btnMua4;
    private javax.swing.JButton btnMua5;
    private javax.swing.JButton btnMua6;
    private javax.swing.JButton btnNhanVien;
    private javax.swing.JButton btnSanPham;
    private javax.swing.JButton btnSuaKH;
    private javax.swing.JButton btnSuaNV;
    private javax.swing.JButton btnThemKH;
    private javax.swing.JButton btnThemNV;
    private javax.swing.JButton btnThongKe;
    private javax.swing.JButton btnTim;
    private javax.swing.JButton btnTimNV;
    private javax.swing.JButton btnXoaKH;
    private javax.swing.JButton btnXoaNV;
    private javax.swing.JButton btnXuatHoaDon;
    private javax.swing.JComboBox<String> cboSanPham;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JScrollPane pnListSanPham;
    private javax.swing.JPanel pnMain;
    private javax.swing.JPanel pnlContainer;
    private javax.swing.JPanel pnlHSanPham;
    private javax.swing.JPanel pnlHoaDon;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlKhachHang;
    private javax.swing.JPanel pnlLichSu;
    private javax.swing.JPanel pnlNavbar;
    private javax.swing.JPanel pnlNhanVien;
    private javax.swing.JPanel pnlSanPham;
    private javax.swing.JPanel pnlSlideMenu;
    private javax.swing.JTable tbChiTietDonHang;
    private javax.swing.JTable tbDonHang;
    private javax.swing.JTable tbHoaDon;
    private javax.swing.JTable tbKhachHang;
    private javax.swing.JTable tbNhanVien;
    private javax.swing.JTable tbOder;
    private javax.swing.JTable tbSanPham;
    private javax.swing.JTable tbThongKe;
    private javax.swing.JTextField txtChucVu;
    private javax.swing.JTextField txtDTKH;
    private javax.swing.JTextField txtDiaChiKH;
    private javax.swing.JTextField txtDienThoai;
    private javax.swing.JTextField txtHoTenKH;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtNgayBatDau;
    private javax.swing.JTextField txtNgayKetThuc;
    private javax.swing.JTextField txtTenNV;
    private javax.swing.JTextField txtTim;
    private javax.swing.JTextField txtTimNV;
    private javax.swing.JTextArea txtxuathoadon;
    // End of variables declaration//GEN-END:variables
}
