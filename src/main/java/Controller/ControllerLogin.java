package Controller;

import Account.Security;
import Controller.User.ControllerCenterHome;
import Util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.sun.javaws.IconUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Person;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLogin implements Initializable {
    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField edtUsername;

    @FXML
    private JFXPasswordField edtPassword;

    @FXML
    private JFXButton bnLogin;

    @FXML
    private JFXButton btnSignup;

    @FXML
    private Label idMess;

    private Person account=null;

    @FXML
    void ActionSignUp(ActionEvent event) throws IOException {
        //btnSignup.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("/views/Signup.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Sign up");
        stage.setScene(new Scene(root, 600, 450));
        stage.show();
    }

    @FXML
    void Mouse(MouseEvent event) {
        idMess.setText("");
    }
    @FXML
    void ActionLogin(ActionEvent event) {
        if(edtUsername.getText().trim().isEmpty()||edtPassword.getText().trim().isEmpty()){
            return;
        }
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();

        try {
            Person user=(Person)session.createQuery("From Person where USERNAME=:username")
                    .setParameter("username",edtUsername.getText().trim())
                    .getSingleResult();
            transaction.commit();
            session.close();

            String mess="";
            if(user.getPassword().equals(Security.get_SHA_1_SecurePassword(edtPassword.getText().trim(),user.getSalt()))){

                if(user.isTrangThai()){
                    idMess.setText("Tài khoản đã bị cấm");
                    return;
                }
                account=user;
                btnSignup.getScene().getWindow().hide();
                idMess.setText("Login success !!");
            }else {
                idMess.setText("Tài khoản không tồn tại");
            }
        }catch (NoResultException ex){
            idMess.setText("Tài khoản không tồn tại");
        }

    }

    public void initialize(URL location, ResourceBundle resources) {
        edtPassword.setOnMouseClicked(event);
        edtUsername.setOnMouseClicked(event);
    }
    public Person getData(){
        return account;
    }

    EventHandler<MouseEvent> event=new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            idMess.setText("");
            idMess.setStyle("-fx-text-fill: red");
        }
    };
}
