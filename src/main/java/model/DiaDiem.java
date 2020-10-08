package model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name ="dia_diem")
public class DiaDiem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IDDIADIEM")
    private Integer idDiaDiem;

    @Column(name="TEN")
    private String ten;

    @Column(name="DIACHI",length = 1024)
    private String diaChi;

    @Column(name="SUCCHUA")
    private Integer sucChua;

    @OneToMany(mappedBy = "diaDiem",cascade = CascadeType.ALL,orphanRemoval =true )
    private List<HoiNghi> hoiNghiList;

    public DiaDiem() {
    }

    public DiaDiem(String ten, String diaChi, Integer sucChua) {
        this.ten = ten;
        this.diaChi = diaChi;
        this.sucChua = sucChua;
    }

    public Integer getIdDiaDiem() {
        return idDiaDiem;
    }

    public void setIdDiaDiem(Integer idDiaDiem) {
        this.idDiaDiem = idDiaDiem;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Integer getSucChua() {
        return sucChua;
    }

    public void setSucChua(Integer sucChua) {
        this.sucChua = sucChua;
    }

    public List<HoiNghi> getHoiNghiList() {
        return hoiNghiList;
    }

    public void setHoiNghiList(List<HoiNghi> hoiNghiList) {
        this.hoiNghiList = hoiNghiList;
    }

    @Override
    public String toString() {
        return this.getTen()+"_ SC: "+this.getSucChua();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this){
            return true;
        }
        if(!(obj instanceof DiaDiem)){
            return false;
        }
        DiaDiem dd=(DiaDiem)obj;
        return dd.getIdDiaDiem()==this.getIdDiaDiem();
    }
}
