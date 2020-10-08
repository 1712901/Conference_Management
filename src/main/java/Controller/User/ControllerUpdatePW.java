package Controller.User;

import Account.Security;
import Util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import model.Person;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class ControllerUpdatePW implements Initializable {


    @FXML
    private JFXPasswordField edtRepeat;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXPasswordField edtNew;

    @FXML
    private JFXPasswordField edtCurr;


    @FXML
    private Label lbMess;

    @FXML
    void ActionCancel(ActionEvent event) {
        btnCancel.getScene().getWindow().hide();
    }

    @FXML
    void ActionUpdate(ActionEvent event) throws NoSuchAlgorithmException {
        if(Account.User.getAcount()==null){
            lbMess.setText("Bạn cần phải đăng nhập");
            return;
        }
        if(edtCurr.getText().isEmpty()||edtNew.getText().isEmpty()||edtRepeat.getText().isEmpty()){
            lbMess.setText("Điền đầu đủ thông tin");
            return;
        }
        if (!edtNew.getText().trim().equals(edtRepeat.getText().trim())){
            System.out.println("");
            return;
        }
        if(Account.User.getAcount().getPassword().equals(
                Security.get_SHA_1_SecurePassword(edtCurr.getText().trim(),Account.User.getAcount().getSalt()))){
            Person user=Account.User.getAcount();
            byte[] salt=Security.getSalt();
            user.setPassword(Security.get_SHA_1_SecurePassword(edtNew.getText().trim(),salt));
            user.setSalt(salt);
            Session session= HibernateUtil.getSessionFactory().openSession();
            Transaction transaction=session.beginTransaction();

            session.update(user);

            transaction.commit();
            session.close();
            lbMess.setText("Thay đổi mật khẩu thành công");
            lbMess.setStyle("-fx-text-fill: green");
        }
    }
    EventHandler<MouseEvent> event=new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            lbMess.setText("");
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        edtNew.setOnMouseClicked(event);
        edtRepeat.setOnMouseClicked(event);
        edtCurr.setOnMouseClicked(event);
    }
}
