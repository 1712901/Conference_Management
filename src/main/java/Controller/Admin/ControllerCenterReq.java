package Controller.Admin;

import Util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.DSThamGiaHN;
import model.DiaDiem;
import model.HoiNghi;
import model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerCenterReq implements Initializable {
    @FXML
    private JFXButton btnRefresh;

    @FXML
    private TableView<DSThamGiaHN> tbvReq;

    @FXML
    private TableColumn<DSThamGiaHN,String> colNameHN;

    @FXML
    private TableColumn<DSThamGiaHN,String> colDD;

    @FXML
    private TableColumn<DSThamGiaHN, String> colStartTime;

    @FXML
    private TableColumn<DSThamGiaHN,String> colNameUser;

    @FXML
    private TableColumn<DSThamGiaHN,String> colEmail;

    @FXML
    private TableColumn<DSThamGiaHN, Void> colAction;

    private final int ACCEPT=1;
    private final int DELETE=-1;
    private final int WAITING=0;
    private ObservableList<DSThamGiaHN> observableListRq= FXCollections.observableArrayList();

    @FXML
    void ActionRefresh(ActionEvent event) {
        RefreshTbaleReq();
    }


    private void intiColReq(){
        colNameHN.setCellFactory(new Callback<TableColumn<DSThamGiaHN, String>, TableCell<DSThamGiaHN, String>>() {
            @Override
            public TableCell<DSThamGiaHN, String> call(TableColumn<DSThamGiaHN, String> param) {
                return new TableCell<DSThamGiaHN,String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        DSThamGiaHN dsThamGiaHN=(DSThamGiaHN) this.getTableRow().getItem();

                        if(!empty&&dsThamGiaHN!=null){


                            HoiNghi hoiNghi=dsThamGiaHN.getHoiNghi();
                            if(hoiNghi!=null){
                                this.setText(hoiNghi.getTen());
                            }
                        }
                    }
                };
            }
        });
        colDD.setCellFactory(new Callback<TableColumn<DSThamGiaHN, String>, TableCell<DSThamGiaHN, String>>() {
            @Override
            public TableCell<DSThamGiaHN, String> call(TableColumn<DSThamGiaHN, String> param) {
                return new TableCell<DSThamGiaHN,String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        DSThamGiaHN dsThamGiaHN=(DSThamGiaHN) this.getTableRow().getItem();
                        if(!empty && dsThamGiaHN!=null){

                            DiaDiem diaDiem=dsThamGiaHN.getHoiNghi().getDiaDiem();
                            if(diaDiem!=null) {
                                this.setText(diaDiem.getTen());
                            }
                        }
                    }
                };
            }
        });
        colStartTime.setCellFactory(new Callback<TableColumn<DSThamGiaHN, String>, TableCell<DSThamGiaHN, String>>() {
            @Override
            public TableCell<DSThamGiaHN, String> call(TableColumn<DSThamGiaHN, String> param) {
                return new TableCell<DSThamGiaHN,String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        DSThamGiaHN dsThamGiaHN=(DSThamGiaHN) this.getTableRow().getItem();
                        if(!empty && dsThamGiaHN!=null){

                            HoiNghi hoiNghi=dsThamGiaHN.getHoiNghi();
                            if(hoiNghi!=null) {
                                this.setText(hoiNghi.getThoiGianBD().format(DateTimeFormatter.ofPattern("dd-MM-yyyy \n HH:mm:ss")));
                            }
                        }
                    }
                };
            }
        });
        colNameUser.setCellFactory(new Callback<TableColumn<DSThamGiaHN, String>, TableCell<DSThamGiaHN, String>>() {
            @Override
            public TableCell<DSThamGiaHN, String> call(TableColumn<DSThamGiaHN, String> param) {
                return new TableCell<DSThamGiaHN,String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        DSThamGiaHN dsThamGiaHN=(DSThamGiaHN) this.getTableRow().getItem();
                        if(!empty && dsThamGiaHN!=null){

                            Person user=dsThamGiaHN.getUser();
                            if(user!=null) {
                                this.setText(user.getHoTen());
                            }
                        }
                    }
                };
            }
        });
        colEmail.setCellFactory(new Callback<TableColumn<DSThamGiaHN, String>, TableCell<DSThamGiaHN, String>>() {
            @Override
            public TableCell<DSThamGiaHN, String> call(TableColumn<DSThamGiaHN, String> param) {
                return new TableCell<DSThamGiaHN,String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        DSThamGiaHN dsThamGiaHN=(DSThamGiaHN) this.getTableRow().getItem();
                        if(!empty &&dsThamGiaHN!=null){

                            Person user=dsThamGiaHN.getUser();
                            if(user!=null) {
                                this.setText(user.getEmail());
                            }
                        }
                    }
                };
            }
        });
        colAction.setCellFactory(new Callback<TableColumn<DSThamGiaHN, Void>, TableCell<DSThamGiaHN, Void>>() {
            @Override
            public TableCell<DSThamGiaHN, Void> call(TableColumn<DSThamGiaHN, Void> param) {
                return new TableCell<DSThamGiaHN, Void>(){
                    JFXButton btnAccept=new JFXButton("Chấp nhận");
                    JFXButton btnDelete=new JFXButton("Xóa");
                    {
                        btnAccept.setUnderline(true);
                        btnDelete.setUnderline(true);
                        btnDelete.setOnAction(event -> {

                            DSThamGiaHN dsThamGiaHN=this.getTableView().getItems().get(this.getIndex());
                            dsThamGiaHN.setTrangThai(DELETE);
                            updateRequest(dsThamGiaHN);

                            observableListRq.remove(this.getIndex());
                            tbvReq.refresh();
                            System.out.println("Delete");
                        });
                        btnAccept.setOnAction(event -> {

                            DSThamGiaHN dsThamGiaHN=this.getTableView().getItems().get(this.getIndex());
                            dsThamGiaHN.setTrangThai(ACCEPT);
                            updateRequest(dsThamGiaHN);

                            observableListRq.remove(this.getIndex());
                            tbvReq.refresh();
                            System.out.println("Accept");
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {

                        super.updateItem(item, empty);
                        if(empty){
                            setGraphic(null);
                            return;
                        }
                        HBox hBox=new HBox(5);
                        hBox.getChildren().addAll(btnAccept,btnDelete);
                        setGraphic(hBox);
                    }
                };
            }
        });
    }

    private void RefreshTbaleReq() {

        observableListRq.clear();
        LoadDataTableReq();
    }

    private void updateRequest(DSThamGiaHN dsThamGiaHN){
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();

        session.update(dsThamGiaHN);

        transaction.commit();
        session.close();
    }
    private void LoadDataTableReq(){
        SessionFactory factory= HibernateUtil.getSessionFactory();
        Session session=factory.openSession();
        Transaction transaction=session.beginTransaction();

        List list= session.createQuery("From DSThamGiaHN Where TRANGTHAI="+WAITING).getResultList();

        transaction.commit();
        session.close();

        observableListRq.addAll(list);
        tbvReq.setItems(observableListRq);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        intiColReq();
        LoadDataTableReq();
    }
}
