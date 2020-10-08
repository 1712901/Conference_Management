package Controller.User;

import Account.User;
import Controller.Admin.ControllerAdmin;
import Controller.ControllerLogin;
import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Person;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerCenterHome implements Initializable {
    @FXML
    private BorderPane boderpane;


    @FXML
    private JFXButton btnHoiNghi;

    @FXML
    private JFXButton btnAccount;

    @FXML
    private JFXButton btnLogout;


    private Person user=null;


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

    public void initialize(URL location, ResourceBundle resources) {
        btnHoiNghi.addEventHandler(MouseEvent.MOUSE_CLICKED,new MyEventHandler());
        btnAccount.addEventHandler(MouseEvent.MOUSE_CLICKED,new MyEventHandler());
        showButtonLogout(false);
    }

    private class MyEventHandler implements EventHandler<Event> {
        public void handle(Event event) {
            String id=((Control)event.getSource()).getId();
            String fileName="";
            if(id.equals(btnHoiNghi.getId())){

                fileName="CenterHoiNghi";

            }else if(id.equals(btnAccount.getId())){
                if(Account.User.getAcount()==null&&!showLoginScreen()){
                    return;
                }
                fileName="Profile";
            }
            setCenterBoderPane(fileName);
        }
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
            System.out.println(user.getHoTen()+" "+user.getIdPerson());
            //Login thành công hiện btn loagout
            showButtonLogout(true);
            User.check=true;
            btnAccount.setText("My Account");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    private void setCenterBoderPane(String fileName){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/BoderPaneCenterUser/"+fileName+".fxml"));
        Pane viewCenter = null;
        try {
            viewCenter = (Pane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            boderpane.setCenter(viewCenter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showAdminScreen(Person user) throws IOException {
        btnHoiNghi.getScene().getWindow().hide();
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/views/Admin/Admin.fxml"));
        Parent root = loader.load();
        ControllerAdmin controller=loader.getController();
        controller.senData(user);
        Stage stage = new Stage();
        stage.setTitle("Admin");
        stage.setScene(new Scene(root, 1003, 583));
        stage.show();
    }
    private void showButtonLogout(boolean b) {
        btnLogout.setDisable(!b);
        btnLogout.setOpacity(b?1:0);
    }

}