package Controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.HoiNghi;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ControllerCardView extends VBox {

    @FXML
    private Label lbDate;

    @FXML
    private Label lbName;

    @FXML
    private Label lbDesc;

    private HoiNghi hoiNghi;

    public ControllerCardView(HoiNghi hoiNghi) {
        this.hoiNghi=hoiNghi;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Users/CardviewHN.fxml"));
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
    public String getName(){
        return lbName.getText();
    }
    public HoiNghi getHoiNghi(){
        return this.hoiNghi;
    }
}
