package Controller.Admin;

import Util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.DiaDiem;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerAddDD implements Initializable {
    @FXML
    private JFXTextField edtTen;

    @FXML
    private JFXTextField edtDiaChi;

    @FXML
    private JFXTextField edtSucChua;

    @FXML
    private JFXButton btnAddorUpdate;

    @FXML
    private JFXButton btnCancel;

    private DiaDiem diaDiem=new DiaDiem();

    private boolean changed=false;

    @FXML
    void ActionCancel(ActionEvent event) {
        btnCancel.getScene().getWindow().hide();
    }

    @FXML
    void ActionAddorUpdate(ActionEvent event) {
        readData();
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();

        session.saveOrUpdate(diaDiem);

        transaction.commit();
        session.close();
        changed=true;
        btnCancel.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        edtSucChua.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    edtSucChua.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
    protected void sendDataDD(DiaDiem dd){
        diaDiem=dd;
        edtTen.setText(dd.getTen());
        edtDiaChi.setText(dd.getDiaChi());
        edtSucChua.setText(dd.getSucChua()+"");
        btnAddorUpdate.setText("Update");
    }
    private void readData(){
        diaDiem.setTen(edtTen.getText().trim());
        diaDiem.setDiaChi(edtDiaChi.getText().trim());
        diaDiem.setSucChua(Integer.parseInt(edtSucChua.getText().trim()));
    }
    public boolean isChanged(){
        return changed;
    }
}
