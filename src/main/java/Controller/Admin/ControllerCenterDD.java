package Controller.Admin;

import Util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.DiaDiem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sample.MyAlert;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerCenterDD implements Initializable {

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private TableView<DiaDiem> tbvDiaDiem;

    @FXML
    private TableColumn<DiaDiem,String> colName;

    @FXML
    private TableColumn<DiaDiem,String> colAddress;

    @FXML
    private TableColumn<DiaDiem,Integer> colCapa;

    @FXML
    private TableColumn<DiaDiem,Void> colAction;


    private ObservableList<DiaDiem> observableListDD= FXCollections.observableArrayList();

    @FXML
    void ShowDetail(ActionEvent event) throws IOException {

        showDiaDiemScreen(null);

    }

    @FXML
    void ActionRefresh(ActionEvent event) {
        RefreshTable();
    }

    public void initialize(URL location, ResourceBundle resources) {
        intiColsDiaDiem();
        LoadDataTableDD();
    }

    private void intiColsDiaDiem(){
        colName.setCellValueFactory(new PropertyValueFactory<>("ten"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colCapa.setCellValueFactory(new PropertyValueFactory<>("sucChua"));

        colAction.setCellFactory(param -> new TableCell<DiaDiem,Void>(){
            private JFXButton btnDelete=new JFXButton("delete");
            private JFXButton btnEdit=new JFXButton("Edit");
            {
                btnDelete.setUnderline(true);
                btnEdit.setUnderline(true);

                btnEdit.setOnAction(e->{
                    DiaDiem diaDiem=(DiaDiem) this.getTableRow().getItem();
                    try {
                        showDiaDiemScreen(diaDiem);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                btnDelete.setOnAction(e->{
                    DiaDiem diaDiem=(DiaDiem) this.getTableRow().getItem();
                    //remove Diadiem
                    Session session=HibernateUtil.getSessionFactory().openSession();
                    Transaction transaction=session.beginTransaction();

                    DiaDiem dd=session.load(DiaDiem.class,diaDiem.getIdDiaDiem());

                    System.out.println(dd.getHoiNghiList().size());

                    if(dd.getHoiNghiList().size()==0){

                        session.remove(dd);
                        this.getTableView().getItems().remove(this.getIndex());
                    }else {
                        MyAlert myAlert=new MyAlert(MyAlert.ERROR,"Error Dialog","Không thể xóa địa điểm: "+dd.getTen());
                        myAlert.show();
                    }
                    transaction.commit();
                    session.close();

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if(empty){
                    setGraphic(null);
                    return;
                }

                HBox pane=new HBox(5);
                pane.getChildren().addAll(btnEdit,btnDelete);
                setGraphic(pane);
            }

        });

    }
    private void showDiaDiemScreen(DiaDiem data) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/views/Admin/AddDiaDiem.fxml"));
        Parent root = fxmlLoader.load();
        ControllerAddDD controller=fxmlLoader.getController();
        if(data!=null){
            controller.sendDataDD(data);
        }
        Stage stage = new Stage();
        stage.setTitle("Thông tin Địa Điểm");
        stage.setScene(new Scene(root, 600, 400));
        stage.showAndWait();
        if(controller.isChanged()){
            RefreshTable();
            System.out.println("changed");
        }
    }
    private void LoadDataTableDD(){
        SessionFactory factory= HibernateUtil.getSessionFactory();
        Session session=factory.openSession();
        Transaction transaction=session.beginTransaction();

        List list= session.createQuery("From DiaDiem").getResultList();

        transaction.commit();
        session.close();

        observableListDD.addAll(list);
        tbvDiaDiem.setItems(observableListDD);
    }
    private void RefreshTable(){
        observableListDD.clear();
        LoadDataTableDD();
    }
}
