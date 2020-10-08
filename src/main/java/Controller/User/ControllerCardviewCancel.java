package Controller.User;

import Util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.DSThamGiaHN;
import model.HoiNghi;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ControllerCardviewCancel extends VBox {

    @FXML
    private Label lbDate;

    @FXML
    private Label lbName;

    @FXML
    private Label lbDesc;

    @FXML
    private JFXButton btnCancel;

    private HoiNghi hoiNghi;
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    @FXML
    void ActionCancel(ActionEvent event) {
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);

        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();

        if(hoiNghi.getThoiGianBD().isBefore(LocalDateTime.now())){
            alert.setContentText("Sự kiện đã diễn ra rồi không thể hủy");
            alert.showAndWait();
            return;
        }

        session.createQuery("Delete From DSThamGiaHN Where IDHOINGHI=:idhn and IDPERSON =:idP")
                                    .setParameter("idhn",hoiNghi.getIdHoiNghi())
                                    .setParameter("idP",Account.User.getAcount().getIdPerson()).executeUpdate();

        FlowPane flowPane=(FlowPane)(btnCancel.getScene()).lookup("#flowPane");
        flowPane.getChildren().remove(this);

        transaction.commit();
        session.close();

    }
    public ControllerCardviewCancel(HoiNghi hoiNghi) {
        this.hoiNghi=hoiNghi;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Users/CardviewCancel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            lbDesc.setText(hoiNghi.getMoTa());
            lbName.setText(hoiNghi.getTen());
            lbDate.setText(hoiNghi.getThoiGianBD().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public HoiNghi getHoiNghi(){
        return this.hoiNghi;
    }
}
