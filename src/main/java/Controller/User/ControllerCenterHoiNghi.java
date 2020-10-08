package Controller.User;

import Account.User;
import Util.HoiNghiUtil;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.HoiNghi;

import javax.swing.text.TabableView;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerCenterHoiNghi implements Initializable {
    @FXML
    private JFXButton btnCardview;

    @FXML
    private JFXButton btnListview;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private AnchorPane anchorPane;

    private int TYPE=1;

    @FXML
    void ShowCardView(ActionEvent event) {

        showCarview();
    }

    @FXML
    void ShowListview(ActionEvent event) throws IOException {
        showTableview();
    }

    @FXML
    void ActionRefresh(ActionEvent event) throws IOException {
        if(TYPE==1){
            showCarview();
        }else {
            showTableview();
        }
    }

    EventHandler mouseHandler=new EventHandler() {
        @Override
        public void handle(Event event) {
            ControllerCardView controllerCarView=(ControllerCardView) event.getSource();


            FXMLLoader loader=new FXMLLoader(getClass().getResource("/views/Users/ChiTietHoiNghi.fxml"));
            Parent root = null;
            try {
                root = loader.load();
                ControllerDetailHN controllerDetailHN=loader.getController();
                controllerDetailHN.sendData(controllerCarView.getHoiNghi());
                Stage stage = new Stage();
                stage.setTitle("Thông Tin Hội Nghị");
                stage.setScene(new Scene(root, 760, 670));

                stage.showAndWait();

                if(Account.User.getAcount()!=null&&Account.User.getAcount().getPhanQuyen()==1){
                    btnCardview.getScene().getWindow().hide();
                }

                if(Account.User.getAcount()!=null && !User.check) {

                    User.check=true;

                    System.out.println("fdfd");
                    JFXButton btnAcc = (JFXButton) (btnRefresh.getScene()).lookup("#btnAccount");
                    btnAcc.setText("My Account");

                    JFXButton btnLogout=(JFXButton) (btnRefresh.getScene()).lookup("#btnLogout");
                    btnLogout.setDisable(false);
                    btnLogout.setOpacity(1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(controllerCarView.getName());
        }
    };
    private void setAnchorPane(Node node){
        AnchorPane.setBottomAnchor(node,0.0);
        AnchorPane.setTopAnchor(node,0.0);
        AnchorPane.setRightAnchor(node,0.0);
        AnchorPane.setLeftAnchor(node,0.0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showCarview();
    }
    private void showCarview(){

        TYPE=1;

        FlowPane flowPane=new FlowPane();
        flowPane.setHgap(5f);
        flowPane.setVgap(5f);

        List<HoiNghi> list= HoiNghiUtil.getData();
        ControllerCardView controllerCarView;
        for (HoiNghi hn: list) {
            controllerCarView=new ControllerCardView(hn);
            controllerCarView.setOnMouseClicked(mouseHandler);
            flowPane.getChildren().add(controllerCarView);
        }
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(flowPane);
        setAnchorPane(flowPane);
    }
    private void showTableview() throws IOException {
        TYPE=2;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Users/TableViewHN.fxml"));
        Pane viewCenter = (Pane) fxmlLoader.load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(viewCenter);
        setAnchorPane(viewCenter);
    }
}
