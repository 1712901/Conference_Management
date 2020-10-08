package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IDPERSON")
    private Integer idPerson;
    @Column(name = "HOTEN")
    private String hoTen;
    @Column(name = "USERNAME")
    private String userName;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = " EMAIL")
    private String email;
    @Column(name = "PHANQUYEN")
    private int phanQuyen;
    @Column(name="TRANGTHAI")
    private boolean trangThai;
    @Column(name="IMAGEPATH")
    private String imagePath;
    @Column(name="ADDRESS")
    private String address;

    @Column(name = "SALT")
    private byte[] salt;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private Set<DSThamGiaHN> dsThamGiaHNS=new HashSet<>();

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name ="danh_sach_tham_gia",
//    joinColumns = {@JoinColumn(name="IDPERSON")},
//    inverseJoinColumns = {@JoinColumn(name="IDHOINGHI")})
//    List<HoiNghi> danhSachHoiNghi=new ArrayList<HoiNghi>();


    public Person() {
    }

    public Person(String hoTen, String userName, String password, String email) {
        this.hoTen = hoTen;
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public Integer getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Integer idPerson) {
        this.idPerson = idPerson;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhanQuyen() {
        return phanQuyen;
    }

    public void setPhanQuyen(int phanQuyen) {
        this.phanQuyen = phanQuyen;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }



    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public void addThamGiaHN(DSThamGiaHN ds){
        this.dsThamGiaHNS.add(ds);
    }

    public byte[] getSalt() {
        return salt;
    }

    public Set<DSThamGiaHN> getDsThamGiaHNS() {
        return dsThamGiaHNS;
    }

    public void setDsThamGiaHNS(Set<DSThamGiaHN> dsThamGiaHNS) {
        this.dsThamGiaHNS = dsThamGiaHNS;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

}
