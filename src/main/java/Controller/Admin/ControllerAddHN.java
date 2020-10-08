package Controller.Admin;

import Util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import model.DSThamGiaHN;
import model.DiaDiem;
import model.HoiNghi;
import model.Person;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sample.MyAlert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerAddHN implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView imHoiNghi;

    @FXML
    private JFXButton btnChooseFile;

    @FXML
    private JFXTextField edtTenHN;

    @FXML
    private ComboBox<DiaDiem> cbDiaDiem;

    @FXML
    private TextArea edMoTaChiTiet;

    @FXML
    private JFXDatePicker dateStart;

    @FXML
    private JFXDatePicker endDate;

    @FXML
    private JFXTimePicker timeStart;

    @FXML
    private JFXTimePicker endTime;

    @FXML
    private JFXTextField edtMota;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private TableView<Person> tbDSTG;

    @FXML
    private TableColumn<Person, String> colTen;

    @FXML
    private TableColumn<Person, String> colEmail;

    @FXML
    private TableColumn<Person, Void> colAction;

    @FXML
    private JFXTextField edtCap;

    @FXML
    private Label lb;

    private final int SENDDETAIL=1;
    private final int SENDADD=2;
    private HoiNghi hoiNghiDatasend;
    ObservableList<Person> people;
    private List<DiaDiem> listDd;
    private boolean changed=false;
    private FileChooser fileChooser = new FileChooser();
    private String imaPathCurr=null;

    @FXML
    void ActionUpdateOrCreate(ActionEvent event) {

        HoiNghi hn=readChanged();
        if(hn!=null) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();

            session.saveOrUpdate(hn);

            transaction.commit();
            session.close();
            changed=true;
            btnAdd.getScene().getWindow().hide();
        }
    }
    @FXML
    void ActionChooseFile(ActionEvent event) {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png","*.jpg"));
        File selectedFile=fileChooser.showOpenDialog(btnAdd.getScene().getWindow());
        if( selectedFile!=null) {
            imaPathCurr = selectedFile.toURI().toString();
            Image image = new Image(imaPathCurr);
            imHoiNghi.setImage(image);
        }
    }
    private List<DiaDiem> initCombobox(){
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();
        List<DiaDiem> listDd= session.createQuery("From DiaDiem").getResultList();
        ObservableList<DiaDiem> list= FXCollections.observableArrayList(listDd);
        cbDiaDiem.setItems(list);
        transaction.commit();
        session.close();
        return listDd;
    }
    private void intiCols(){
        colTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAction.setCellFactory(new Callback<TableColumn<Person, Void>, TableCell<Person, Void>>() {
            @Override
            public TableCell<Person, Void> call(TableColumn<Person, Void> param) {
                return new TableCell<Person,Void>(){
                    JFXButton btnCancel=new JFXButton("Cancel");
                    {
                        btnCancel.setUnderline(true);

                        btnCancel.setOnAction(event -> {

                            Session session =HibernateUtil.getSessionFactory().openSession();
                            Transaction transaction=session.beginTransaction();

                            session.createQuery("Update DSThamGiaHN set TRANGTHAI=-1 where IDPERSON =:idp and IDHOINGHI=:idhn")
                                    .setParameter("idp",((Person)this.getTableRow().getItem()).getIdPerson())
                                    .setParameter("idhn",hoiNghiDatasend.getIdHoiNghi())
                                    .executeUpdate();

                            transaction.commit();
                            session.close();

                            this.getTableView().getItems().remove(this.getIndex());
                        });
                    }
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setGraphic(null);
                            return;
                        }
                        setGraphic(btnCancel);
                    }
                };
            }
        });
    }
    private void loadTableDSTG(){
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();

        List<DSThamGiaHN> listDSTG=session.createQuery("From DSThamGiaHN Where IDHOINGHI=:id and TRANGTHAI=1")
                .setParameter("id",hoiNghiDatasend.getIdHoiNghi())
                .getResultList();

        people=FXCollections.observableArrayList();

        for (DSThamGiaHN it:listDSTG) {
            people.add(it.getUser());
        }

        transaction.commit();
        session.close();

        tbDSTG.setItems(people);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(1);
        intiCols();
        listDd=initCombobox();
        hideTableDSTG(true);
    }
    private HoiNghi readChanged(){

        HoiNghi hoiNghi=new HoiNghi();
        if(hoiNghiDatasend!=null){
            hoiNghi=hoiNghiDatasend;
        }

        if(edtTenHN.getText().trim().isEmpty()||edtCap.getText().trim().isEmpty()||edMoTaChiTiet.getText().trim().isEmpty()
                ||edtMota.getText().trim().isEmpty()){
            MyAlert myAlert=new MyAlert(MyAlert.INF,"Information Dialog","Nhập đầy đủ thông tin");
            myAlert.show();
            return null;
        }
        if(Integer.parseInt(edtCap.getText())>cbDiaDiem.getSelectionModel().getSelectedItem().getSucChua()){
            MyAlert myAlert=new MyAlert(MyAlert.ERROR,"Error Dialog","Số lượng người đăng ký phải nhỏ hơn sức chứa của địa điểm");
            myAlert.show();
            return null;
        }
        hoiNghi.setTen(edtTenHN.getText().trim());
        hoiNghi.setMoTa(edtMota.getText().trim());
        hoiNghi.setMoTaChiTiet(edMoTaChiTiet.getText().trim());
        hoiNghi.setSoLuongNguoiDangKy(Integer.parseInt(edtCap.getText()));
        hoiNghi.setDiaDiem(cbDiaDiem.getSelectionModel().getSelectedItem());


        LocalDateTime localDateTimeBD=LocalDateTime.of(dateStart.getValue(), timeStart.getValue());
        LocalDateTime localDateTimeKT=LocalDateTime.of(endDate.getValue(), endTime.getValue());

        if(localDateTimeBD.isAfter(localDateTimeKT)){
            MyAlert myAlert=new MyAlert(MyAlert.ERROR,"Error Dialog","Ngày không hợp lệ");
            myAlert.show();
            return null;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        CriteriaBuilder criteriaBuilder=session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQueryCount=criteriaBuilder.createQuery(Long.class);
        Root<HoiNghi> root= criteriaQueryCount.from(HoiNghi.class);

        Predicate bwBD=criteriaBuilder.between(root.get("thoiGianBD"),localDateTimeBD,localDateTimeKT);
        Predicate bwBD1=criteriaBuilder.between(criteriaBuilder.literal(localDateTimeBD),root.get("thoiGianBD"),root.get("thoiGianKT"));
        Predicate bwKT=criteriaBuilder.between(root.get("thoiGianKT"),localDateTimeBD,localDateTimeKT);
        Predicate bwKT1=criteriaBuilder.between(criteriaBuilder.literal(localDateTimeKT),root.get("thoiGianBD"),root.get("thoiGianKT"));
        Predicate difHH=criteriaBuilder.notEqual(root.get("idHoiNghi"),hoiNghi.getIdHoiNghi()==null?-1:hoiNghi.getIdHoiNghi());
        Predicate eqDD=criteriaBuilder.equal(root.get("diaDiem"),hoiNghi.getDiaDiem());

        criteriaQueryCount.select(criteriaBuilder.count(root)).where(criteriaBuilder.and(difHH,eqDD,criteriaBuilder.or(bwBD,bwKT,bwBD1,bwKT1)));

        int row=session.createQuery(criteriaQueryCount).getSingleResult().intValue();

        if (row > 0) {
            MyAlert myAlert=new MyAlert(MyAlert.ERROR,"Error Dialog","Hội nghị khác đang diễn ra");
            myAlert.show();
            return null;
        }
        transaction.commit();
        session.close();

        hoiNghi.setThoiGianBD(localDateTimeBD);
        hoiNghi.setThoiGianKT(localDateTimeKT);
        if(imaPathCurr!=null) {
            hoiNghi.setHinhAnh(imaPathCurr);
        }
        return hoiNghi;
    }
    public void sendData(HoiNghi hn,int tp){
        System.out.println(2);
        hoiNghiDatasend=hn;
        //type=tp;
        edtTenHN.setText(hoiNghiDatasend.getTen());
        dateStart.setValue(hn.getThoiGianBD().toLocalDate());
        timeStart.setValue(hn.getThoiGianBD().toLocalTime());

        endDate.setValue(hn.getThoiGianKT().toLocalDate());
        endTime.setValue(hn.getThoiGianKT().toLocalTime());

        edtCap.setText(hn.getSoLuongNguoiDangKy()+"");

        edMoTaChiTiet.setText(hoiNghiDatasend.getMoTaChiTiet());
        edtMota.setText(hoiNghiDatasend.getMoTa());
        if(hoiNghiDatasend.getHinhAnh()!=null){
            imHoiNghi.setImage(new Image(hoiNghiDatasend.getHinhAnh()));
        }

        intiCols();
        loadTableDSTG();
        hideTableDSTG(false);

        cbDiaDiem.getSelectionModel().select(listDd.indexOf(hoiNghiDatasend.getDiaDiem()));

        loadTableDSTG();
        btnAdd.setText("Cập nhật");
        if(hoiNghiDatasend.getThoiGianBD().isBefore(LocalDateTime.now())){
            btnAdd.setDisable(true);
        }
    }
    private void hideTableDSTG(boolean bl){
        tbDSTG.setOpacity(bl?0:1);
        lb.setOpacity(bl?0:1);
        anchorPane.setMaxHeight(bl?380:650);
    }
    public boolean isChanged(){
        return changed;
    }
}
