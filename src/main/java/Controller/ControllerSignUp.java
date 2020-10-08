package Controller;

import Account.Security;
import Util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Person;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ResourceBundle;

public class ControllerSignUp implements Initializable{

    @FXML
    private JFXButton btnSignup;

    @FXML
    private JFXTextField edtName;

    @FXML
    private JFXTextField edtUserName;

    @FXML
    private JFXTextField edtEmail;

    @FXML
    private JFXPasswordField edtPassword;

    @FXML
    private JFXPasswordField edtConfirm;

    @FXML
    private Label lbMess;


    @FXML
    private JFXButton btnCancel;

    @FXML
    void ActionCancel(ActionEvent event) {

        btnCancel.getScene().getWindow().hide();
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
//            Stage stage = new Stage();
//            stage.setTitle("Login");
//            stage.setScene(new Scene(root, 600, 450));
//            stage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    @FXML
    void SignupAction(ActionEvent event) throws NoSuchAlgorithmException {
        if(edtName.getText().trim().isEmpty()||edtEmail.getText().trim().isEmpty()
                ||edtPassword.getText().trim().isEmpty()||edtConfirm.getText().trim().isEmpty()
                || edtUserName.getText().trim().isEmpty()){
            lbMess.setText("Yêu cầu nhập đủ thông tin");
            return;
        }
        if(!edtPassword.getText().trim().equals(edtConfirm.getText().trim())){
            lbMess.setText("Mật khẩu không khớp");
            return;
        }
        byte[] salt=Security.getSalt();
        Person user=new Person(edtName.getText().trim(),edtUserName.getText().trim(),
                Security.get_SHA_1_SecurePassword(edtPassword.getText().trim(),salt),edtEmail.getText().trim());
        user.setSalt(salt);

        Session session= HibernateUtil.getSessionFactory().openSession();

        Transaction transaction=session.beginTransaction();

        long count= (long)session.createQuery("Select count(*) From Person where USERNAME=:username")//CAST(USERNAME as binary)=CAST(:username as binary)
                .setParameter("username",user.getUserName())
                .getSingleResult();
        if(count>0){
            lbMess.setText("Tài khoản đã tồn tại");
        }else {
            session.save(user);
            lbMess.setText("Đăng ký tài khoản thành công");
            lbMess.setStyle("-fx-text-fill: green");
        }
        transaction.commit();
        session.close();
    }
    public void initialize(URL location, ResourceBundle resources) {
        edtConfirm.setOnMouseClicked(event);
        edtEmail.setOnMouseClicked(event);
        edtName.setOnMouseClicked(event);
        edtPassword.setOnMouseClicked(event);
        edtPassword.setOnMouseClicked(event);
    }
    EventHandler<MouseEvent> event=new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            lbMess.setText("");
            lbMess.setStyle("-fx-text-fill: red");
        }
    };
}
