package Controller.User;

import Controller.Admin.ControllerAdmin;
import Controller.ControllerLogin;
import Util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.DSThamGiaHN;
import model.DiaDiem;
import model.HoiNghi;
import model.Person;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerDetailHN implements Initializable {
    @FXML
    private Label lbTenHN;

    @FXML
    private ImageView imHoiNghi;

    @FXML
    private Label lbTGBD;

    @FXML
    private Label lbTGKT;

    @FXML
    private Label lbDiachi;

    @FXML
    private Label lbMota;

    @FXML
    private Label lbMotaChiTiet;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private TableView<Person> tbvUsers;

    @FXML
    private TableColumn<Person, String> colName;

    @FXML
    private TableColumn<Person, String> colEmail;



    private HoiNghi hoiNghi;

    private Person user=null;
    ObservableList<Person> people=FXCollections.observableArrayList();
    private List<DiaDiem> listDd;

    @FXML
    void AcctionDK(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);

        if (Account.User.getAcount()==null){
            showLoginScreen();
            return;
        }
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();


        try {
            DSThamGiaHN dsThamGiaHN=(DSThamGiaHN) session.createQuery("From DSThamGiaHN Where IDHOINGHI=:idHN and IDPERSON=:idPer")
                    .setParameter("idHN",hoiNghi.getIdHoiNghi())
                    .setParameter("idPer",Account.User.getAcount().getIdPerson())
                    .getSingleResult();

            if(dsThamGiaHN.getTrangThai()==1){
                alert.setContentText("Bạn đã đăng ký hội nghị này rồi");
            }else {
                alert.setContentText("Bạn đã đăng ký hội nghị và đang chờ được duyệt");
            }
            alert.showAndWait();
        }catch (Exception ex){

            Person user=session.get(Person.class,Account.User.getAcount().getIdPerson());
            HoiNghi hn=session.get(HoiNghi.class,hoiNghi.getIdHoiNghi());

            long count=(long)session.createQuery("Select count(*) from DSThamGiaHN where IDHOINGHI=:idHN and TRANGTHAI !=-1")
                                    .setParameter("idHN",hn.getIdHoiNghi()).getSingleResult();

            if(count>=hn.getSoLuongNguoiDangKy()){
                alert.setContentText("Hội nghị đã đủ người đăng ký");
                alert.showAndWait();
                return;
            }
            DSThamGiaHN ds=new DSThamGiaHN(hn,user);
            session.save(ds);
            alert.setContentText("Đăng ký tham gia hội nghị thành công và chờ được duyệt");
            alert.showAndWait();
        }

        transaction.commit();
        session.close();

    }
    public void sendData(HoiNghi hn){
        hoiNghi=hn;
        lbTenHN.setText(hoiNghi.getTen());
        lbDiachi.setText(hoiNghi.getDiaDiem().getTen()+"_"+hoiNghi.getDiaDiem().getDiaChi());

        lbTGBD.setText(hoiNghi.getThoiGianBD().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        lbTGKT.setText(hoiNghi.getThoiGianKT().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));


        lbMota.setText(hoiNghi.getMoTa());
        lbMotaChiTiet.setText(hoiNghi.getMoTaChiTiet());

        if(hoiNghi.getHinhAnh()!=null){
            imHoiNghi.setImage(new Image(hoiNghi.getHinhAnh()));
        }

        if(hoiNghi.getThoiGianBD().isBefore(LocalDateTime.now())){
            btnAdd.setDisable(true);
        }

        loadTableDSTG();
    }
    private boolean showLoginScreen(){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/views/Login.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Sign up");
            stage.setScene(new Scene(root, 680, 430));
            stage.showAndWait();
            // lấy user khi login
            ControllerLogin controller = loader.getController();
            user=controller.getData();

            if(user==null){
                return false;
            }
            Account.User.setAccount(user);
            if(Account.User.getAcount().getPhanQuyen()==1){
                showAdminScreen(user);
                return false;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    private void showAdminScreen(Person user) throws IOException {
        btnAdd.getScene().getWindow().hide();
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/views/Admin/Admin.fxml"));
        Parent root = loader.load();
        ControllerAdmin controller=loader.getController();
        controller.senData(user);
        Stage stage = new Stage();
        stage.setTitle("Admin");
        stage.setScene(new Scene(root, 1003, 583));
        stage.show();
    }
    private void intiCols() {
        colName.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
    private void loadTableDSTG(){
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();

        HoiNghi hn=session.get(HoiNghi.class,hoiNghi.getIdHoiNghi());
        List<DSThamGiaHN> listDSTG=session.createQuery("From DSThamGiaHN Where IDHOINGHI=:id and TRANGTHAI=1")
                .setParameter("id",hn.getIdHoiNghi())
                .getResultList();

        for (DSThamGiaHN it:listDSTG) {
            people.add(it.getUser());
        }

        transaction.commit();
        session.close();

        tbvUsers.setItems(people);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        intiCols();
    }
}
