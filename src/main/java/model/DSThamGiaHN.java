package model;

import javax.persistence.*;

@Entity
@Table(name = "danh_sach_tham_gia")
public class DSThamGiaHN {

    //private DSThamGiaHNPK id;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDSTG;

    @ManyToOne
    @JoinColumn(name = "IDHOINGHI")
    private HoiNghi hoiNghi;

    @ManyToOne
    @JoinColumn(name = "IDPERSON")
    private Person user;

    @Column(name = "TRANGTHAI")
    private int trangThai;

    public DSThamGiaHN() {

    }

    public DSThamGiaHN(HoiNghi hoiNghi, Person user) {
        this.hoiNghi = hoiNghi;
        this.user = user;
    }

    public Integer getIdDSTG() {
        return idDSTG;
    }

    public void setIdDSTG(Integer idDSTG) {
        this.idDSTG = idDSTG;
    }

    public HoiNghi getHoiNghi() {
        return hoiNghi;
    }

    public void setHoiNghi(HoiNghi hoiNghi) {
        this.hoiNghi = hoiNghi;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

}
