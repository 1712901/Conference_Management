package Controller.User;

import Util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DSThamGiaHN;
import model.HoiNghi;
import model.Person;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sample.MyAlert;

public class ControllerProfile implements Initializable {

    @FXML
    private BorderPane bodePane;

    @FXML
    private FlowPane flowPane;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private ImageView imAvata;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXTextField edtName;

    @FXML
    private JFXTextField edtEmail;

    @FXML
    private TextArea edtAddress;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnChangePw;

    @FXML
    private JFXButton btnLogout;

    private FileChooser fileChooser = new FileChooser();
    private String imaPathCurr=null;

    @FXML
    void EditAvatarImage(ActionEvent event) {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png","*.jpg"));
        File selectedFile=fileChooser.showOpenDialog(btnEdit.getScene().getWindow());
        if( selectedFile!=null) {
            imaPathCurr = selectedFile.toURI().toString();
            Image image = new Image(imaPathCurr);
            imAvata.setImage(image);
        }
    }

    @FXML
    void UpdateAction(ActionEvent event) {
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();

        session.update(ReadChangeUserInf());

        transaction.commit();
        session.close();
        MyAlert myAlert=new MyAlert(MyAlert.INF,"Information Dialog","Thay đổi thông tin thành công");
        myAlert.show();
    }
    @FXML
    void ActionChange(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/Users/ChangePassword.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Change Password");
        stage.setScene(new Scene(root, 570, 400));
        stage.show();
    }
    @FXML
    void ActionLogout(ActionEvent event) throws IOException {
        btnLogout.getScene().getWindow().hide();

        Account.User.setAccount(null);

        Parent root = FXMLLoader.load(getClass().getResource("/views/Users/Home.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Home");
        stage.setScene(new Scene(root, 1000, 580));
        stage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setProfile();

        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();

        Person user=session.get(Person.class,Account.User.getAcount().getIdPerson());

        Set<DSThamGiaHN> dsThamGiaHNSet=user.getDsThamGiaHNS();

        List<HoiNghi> list=new ArrayList<>();
        for (DSThamGiaHN ds:dsThamGiaHNSet) {
            list.add(ds.getHoiNghi());
        }
        Collections.sort(list, new Comparator<HoiNghi>() {
            @Override
            public int compare(HoiNghi o1, HoiNghi o2) {
                return o1.getThoiGianBD().isAfter(o2.getThoiGianBD())?1:-1;
            }
        });

        transaction.commit();
        session.close();

        flowPane.getChildren().removeAll();
        flowPane.setHgap(5f);
        flowPane.setVgap(5f);

        ControllerCardviewCancel controllerCarView;
        for (HoiNghi hn: list) {
            controllerCarView=new ControllerCardviewCancel(hn);
            controllerCarView.setOnMouseClicked(mouseHandler);
            flowPane.getChildren().add(controllerCarView);

        }

    }

    private void setProfile(){
        Person user=Account.User.getAcount();
        edtAddress.setText(user.getAddress());
        edtName.setText(user.getHoTen());
        edtEmail.setText(user.getEmail());
        edtAddress.setText(user.getAddress()==null?"":user.getAddress());
        if(user.getImagePath()!=null && !user.getImagePath().isEmpty()){
            imAvata.setImage(new Image(user.getImagePath()));
        }
    }
    private Person ReadChangeUserInf(){
        Person user=Account.User.getAcount();
        user.setHoTen(edtName.getText().trim());
        user.setEmail(edtEmail.getText().trim());
        user.setAddress(edtAddress.getText().trim());
        if(imaPathCurr!=null&& !imaPathCurr.isEmpty()){
            user.setImagePath(imaPathCurr);
        }
        return  user;
    }
    EventHandler mouseHandler=new EventHandler() {
        @Override
        public void handle(Event event) {
            ControllerCardviewCancel controllerCarView=(ControllerCardviewCancel) event.getSource();


            FXMLLoader loader=new FXMLLoader(getClass().getResource("/views/Users/ChiTietHoiNghi.fxml"));
            Parent root = null;
            try {
                root = loader.load();
                ControllerDetailHN controllerDetailHN=loader.getController();
                controllerDetailHN.sendData(controllerCarView.getHoiNghi());
                Stage stage = new Stage();
                stage.setTitle("Detail");
                stage.setScene(new Scene(root, 760, 670));

                stage.showAndWait();

                if(Account.User.getAcount()!=null&&Account.User.getAcount().getPhanQuyen()==1){
                    btnChangePw.getScene().getWindow().hide();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
