package model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="hoi_nghi")
public class HoiNghi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IDHOINGHI")
    private Integer idHoiNghi;

    @Column(name="TEN")
    private String ten;

    @Column(name="MOTA")
    private String moTa;

    @Column(name="THOIGIANBD",columnDefinition="DATETIME")
    private LocalDateTime thoiGianBD;

    @Column(name="THOIGIANKT",columnDefinition="DATETIME")
    private LocalDateTime thoiGianKT;


    @Column(name = "MOTACHITIET",length = 1024)
    private String moTaChiTiet;

    @Column(name = "HINHANH")
    private String hinhAnh;

    @Column(name = "SOLUONG")
    private int soLuongNguoiDangKy;

    @ManyToOne(targetEntity = DiaDiem.class)
    @JoinColumn(name = "IDDIADIEM")
    private DiaDiem diaDiem;


    @OneToMany(mappedBy = "hoiNghi",cascade = CascadeType.ALL)
    private Set<DSThamGiaHN> dsThamGiaHNS=new HashSet<>();

    public HoiNghi() {
    }

    public HoiNghi(String ten, String moTa, String moTaChiTiet, String hinhAnh, LocalDateTime TGBD, LocalDateTime TGKT) {
        this.ten = ten;
        this.moTa = moTa;
        this.thoiGianBD=TGBD;
        this.thoiGianKT=TGKT;
        this.moTaChiTiet=moTaChiTiet;
        this.hinhAnh=hinhAnh;
    }

    public Integer getIdHoiNghi() {
        return idHoiNghi;
    }

    public void setIdHoiNghi(Integer idHoiNghi) {
        this.idHoiNghi = idHoiNghi;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public LocalDateTime getThoiGianBD() {
        return thoiGianBD;
    }

    public void setThoiGianBD(LocalDateTime thoiGianBD) {
        this.thoiGianBD = thoiGianBD;
    }

    public LocalDateTime getThoiGianKT() {
        return thoiGianKT;
    }

    public void setThoiGianKT(LocalDateTime thoiGianKT) {
        this.thoiGianKT = thoiGianKT;
    }

    public void addThamGiaHN(DSThamGiaHN ds){
        this.dsThamGiaHNS.add(ds);
    }


    public DiaDiem getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(DiaDiem diaDiem) {
        this.diaDiem = diaDiem;
    }

    public Set<DSThamGiaHN> getDsThamGiaHNS() {
        return dsThamGiaHNS;
    }

    public void setDsThamGiaHNS(Set<DSThamGiaHN> dsThamGiaHNS) {
        this.dsThamGiaHNS = dsThamGiaHNS;
    }

    public String getMoTaChiTiet() {
        return moTaChiTiet;
    }

    public void setMoTaChiTiet(String moTaChiTiet) {
        this.moTaChiTiet = moTaChiTiet;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getSoLuongNguoiDangKy() {
        return soLuongNguoiDangKy;
    }

    public void setSoLuongNguoiDangKy(int soLuongNguoiDangKy) {
        this.soLuongNguoiDangKy = soLuongNguoiDangKy;
    }
}
