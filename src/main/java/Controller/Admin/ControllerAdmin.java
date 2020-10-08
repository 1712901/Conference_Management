package Controller.Admin;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Person;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerAdmin implements Initializable {

    @FXML
    private BorderPane boderPane;

    @FXML
    private JFXButton btnHoiNghi;

    @FXML
    private JFXButton btnDiaDiem;

    @FXML
    private JFXButton btnYeuCau;

    @FXML
    private JFXButton btnUsers;

    @FXML
    private JFXButton btnLogout;

    @FXML
    private Label lbName;

    private Person admin;

    @FXML
    void ActionLogout(ActionEvent event) throws IOException {
        btnLogout.getScene().getWindow().hide();

        Account.User.setAccount(null);

        Parent root = FXMLLoader.load(getClass().getResource("/views/Users/Home.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Change Password");
        stage.setScene(new Scene(root, 1000, 580));
        stage.show();
    }


    public void initialize(URL location, ResourceBundle resources) {


        btnHoiNghi.addEventHandler(MouseEvent.MOUSE_CLICKED,new MyEventHandler());
        btnDiaDiem.addEventHandler(MouseEvent.MOUSE_CLICKED,new MyEventHandler());
        btnYeuCau.addEventHandler(MouseEvent.MOUSE_CLICKED,new MyEventHandler());
        btnUsers.addEventHandler(MouseEvent.MOUSE_CLICKED,new MyEventHandler());
        btnLogout.addEventHandler(MouseEvent.MOUSE_CLICKED,new MyEventHandler());
    }

    private class MyEventHandler implements  EventHandler<Event>{

        public void handle(Event event) {


            String id=((Control)event.getSource()).getId();
            if(id.equals(btnLogout.getId())){
                return;
            }

            String fileName="";
            if(id.equals(btnHoiNghi.getId())){
                System.out.println("HN");
                fileName="CenterHoiNghi";
            }else if(id.equals(btnDiaDiem.getId())){
                System.out.println("DD");
                fileName="CenterDiaDiem";
            }else if(id.equals(btnUsers.getId())){
                System.out.println("Users");
                fileName="CenterUsers";
            }else if(id.equals(btnYeuCau.getId())){
                System.out.println("Yeucau");
                fileName="CenterRequest";
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/BoderPaneCenter/"+fileName+".fxml"));
            Pane viewCenter = null;
            try {
                viewCenter = (Pane) fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                boderPane.setCenter(viewCenter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void senData(Person admin){
        this.admin=admin;
        lbName.setText(admin.getHoTen());
    }
}
