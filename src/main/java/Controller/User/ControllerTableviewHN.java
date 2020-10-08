package Controller.User;

import Controller.Admin.ControllerAddHN;
import Util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.Callback;
import model.HoiNghi;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerTableviewHN implements Initializable {


    @FXML
    private TableView<HoiNghi> tbvHoiNghi;

    @FXML
    private TableColumn<HoiNghi,String> colName;

    @FXML
    private TableColumn<HoiNghi, LocalDateTime> colTGBatDau;

    @FXML
    private TableColumn<HoiNghi,LocalDateTime> colTimeKT;

    @FXML
    private TableColumn<HoiNghi,String> colDiaDiem;

    @FXML
    private TableColumn<HoiNghi,Void> colAction;

    private ObservableList<HoiNghi> observableListHH= FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        intiCols();
        LoadTableHoiNghi();
    }
    private void intiCols(){
        colName.setCellValueFactory(new PropertyValueFactory<>("ten"));

        colTGBatDau.setCellValueFactory(new PropertyValueFactory<>("thoiGianBD"));
        colTGBatDau.setCellFactory(getcustomDate());

        colTimeKT.setCellValueFactory(new PropertyValueFactory<>("thoiGianKT"));
        colTimeKT.setCellFactory(getcustomDate());

        colDiaDiem.setCellFactory(new Callback<TableColumn<HoiNghi, String>, TableCell<HoiNghi, String>>() {
            @Override
            public TableCell<HoiNghi, String> call(TableColumn<HoiNghi, String> param) {
                return new TableCell<HoiNghi,String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty) {
                            HoiNghi hoiNghi=(HoiNghi) this.getTableRow().getItem();
                            if(hoiNghi==null){
                                return;
                            }
                            Session session= HibernateUtil.getSessionFactory().openSession();
                            Transaction transaction=session.beginTransaction();

                            HoiNghi hn=session.load(HoiNghi.class,hoiNghi.getIdHoiNghi());

                            transaction.commit();
                            session.close();
                            if(hoiNghi.getDiaDiem()!=null) {
                                this.setText(hoiNghi.getDiaDiem().getTen());
                            }else this.setText(null);
                        }

                    }
                };
            }
        });
        colAction.setCellFactory(new Callback<TableColumn<HoiNghi, Void>, TableCell<HoiNghi, Void>>() {
            @Override
            public TableCell<HoiNghi, Void> call(TableColumn<HoiNghi, Void> param) {
                return new TableCell<HoiNghi,Void>(){

                    private JFXButton btnDetail=new JFXButton("Chi tiết");
                    {
                        btnDetail.setUnderline(true);
                        //btnDelete.setUnderline(true);

                        btnDetail.setOnAction(e->{
                            try {
                                showDetailHN((HoiNghi) this.getTableRow().getItem());
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        HBox hBox=new HBox(5);
                        if(empty){
                            setGraphic(null);
                            return;
                        }else {
                            hBox.getChildren().addAll(btnDetail);
                            setGraphic(hBox);
                        }
                    }
                };
            }
        });
    }
    private Callback<TableColumn<HoiNghi, LocalDateTime>, TableCell<HoiNghi, LocalDateTime>> getcustomDate() {
        return new Callback<TableColumn<HoiNghi, LocalDateTime>, TableCell<HoiNghi, LocalDateTime>>() {
            @Override
            public TableCell<HoiNghi, LocalDateTime> call(TableColumn<HoiNghi, LocalDateTime> param) {
                return new TableCell<HoiNghi, LocalDateTime>() {
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            this.setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                        }
                    }
                };
            }
        };
    }
    private void LoadTableHoiNghi(){
        SessionFactory factory= HibernateUtil.getSessionFactory();
        Session session=factory.openSession();
        Transaction transaction=session.beginTransaction();

        List list= session.createQuery("From HoiNghi").getResultList();

        transaction.commit();
        session.close();

        observableListHH.addAll(list);
        tbvHoiNghi.setItems(observableListHH);
    }
    private void showDetailHN(HoiNghi hn) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/views/Users/ChiTietHoiNghi.fxml"));
        Parent root = fxmlLoader.load();

        ControllerDetailHN controller = fxmlLoader.getController();
        if(hn!=null) {
            controller.sendData(hn);
        }
        Stage stage = new Stage();
        stage.setTitle("Thông tin hội nghị");
        stage.setScene(new Scene(root, 760, 670));

        stage.showAndWait();

        if(Account.User.getAcount()!=null&&Account.User.getAcount().getPhanQuyen()==1){
            tbvHoiNghi.getScene().getWindow().hide();
        }
    }
}
