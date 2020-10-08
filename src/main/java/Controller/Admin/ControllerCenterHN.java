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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.DiaDiem;
import model.HoiNghi;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerCenterHN implements Initializable {
    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private TableView<HoiNghi> tbvHoiNghi;

    @FXML
    private TableColumn<HoiNghi, String> colName;

    @FXML
    private TableColumn<HoiNghi,String> colDesc;

    @FXML
    private TableColumn<HoiNghi,LocalDateTime> colStartTime;

    @FXML
    private TableColumn<HoiNghi,LocalDateTime> colEndTime;

    @FXML
    private TableColumn<HoiNghi,String> colAddress;

    @FXML
    private TableColumn<HoiNghi,String> colCount;

    @FXML
    private TableColumn<HoiNghi, Void> colAction;


    private ObservableList<HoiNghi> observableListHH=FXCollections.observableArrayList();;

    @FXML
    void ShowDetail(ActionEvent event) throws IOException {

        showDetailHN(null);
    }

    @FXML
    void RefreshAction(ActionEvent event) {
        observableListHH.clear();
        LoadTableHoiNghi();
        //tbvHoiNghi.refresh();
    }

    private void intiColsHoiNghi() {
        colName.setCellValueFactory(new PropertyValueFactory<>("ten"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("moTa"));

        colStartTime.setCellValueFactory(new PropertyValueFactory<>("thoiGianBD"));
        colStartTime.setCellFactory(getcustomDate());

        colEndTime.setCellValueFactory(new PropertyValueFactory<>("thoiGianKT"));
        colEndTime.setCellFactory(getcustomDate());

        colCount.setCellFactory(new Callback<TableColumn<HoiNghi, String>, TableCell<HoiNghi, String>>() {
            @Override
            public TableCell<HoiNghi, String> call(TableColumn<HoiNghi, String> param) {
                return new TableCell<HoiNghi, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setText(null);
                        } else {
                            HoiNghi hoiNghi = this.getTableView().getItems().get(this.getIndex());
                            if (hoiNghi == null)
                                return;
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            Transaction transaction = session.beginTransaction();

                            Query query = session.createQuery("Select count(*) From DSThamGiaHN Where IDHOINGHI=:id and TRANGTHAI=1")
                                    .setParameter("id", hoiNghi.getIdHoiNghi());

                            long sl = (long) query.getSingleResult();
                            transaction.commit();
                            session.close();
                            this.setText(sl + " người");
                        }

                    }
                };
            }
        });
        colAddress.setCellFactory(new Callback<TableColumn<HoiNghi, String>, TableCell<HoiNghi, String>>() {
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
                            Session session=HibernateUtil.getSessionFactory().openSession();
                            Transaction transaction=session.beginTransaction();

                            HoiNghi hn=session.load(HoiNghi.class,hoiNghi.getIdHoiNghi());

                            transaction.commit();
                            session.close();
                            if(hoiNghi.getDiaDiem()!=null) {
                                this.setText(hoiNghi.getDiaDiem().getTen()+"_"+hoiNghi.getDiaDiem().getIdDiaDiem());
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
                    //private JFXButton btnDelete=new JFXButton("Xóa");

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
//                        btnDelete.setOnAction(event -> {
//                            // cần kiểm tra số luongj người đăng ký
//
//                            HoiNghi hoiNghi=(HoiNghi) this.getTableRow().getItem();
//                            if(hoiNghi.getThoiGianBD().isBefore(LocalDateTime.now())){
//                                System.out.println("Sự kiện đã diễn ra rồi");
//                                return;
//                            }
//
//                            Session session=HibernateUtil.getSessionFactory().openSession();
//                            Transaction transaction=session.beginTransaction();
//
//
//                            long count=(long)session.createQuery("Select count(*) From DSThamGiaHN Where IDHOINGHI=:idHN")
//                                    .setParameter("idHN",hoiNghi.getIdHoiNghi())
//                                    .getSingleResult();
//                            if(count>0){
//                                System.out.println("Không Thể xóa hội nghị");
//
//                            }else {
//                                session.remove(hoiNghi);
//                                observableListHH.remove(this.getIndex());
//                                tbvHoiNghi.refresh();
//                            }
//                            transaction.commit();
//                            session.close();
//                        });

                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        HBox hBox=new HBox(5);
                        if(empty){
                            setGraphic(null);
                            return;
                        }else {
                            //hBox.getChildren().addAll(btnDelete,btnDetail);
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
                            this.setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy\nHH:mm:ss")));
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        intiColsHoiNghi();
        LoadTableHoiNghi();
    }
    private void showDetailHN(HoiNghi hn) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/views/Admin/AddHoiNghi.fxml"));
        Parent root = fxmlLoader.load();

        ControllerAddHN controller = fxmlLoader.getController();
        if(hn!=null) {
            controller.sendData(hn,1);
        }
        Stage stage = new Stage();
        stage.setTitle("Thông tin hội nghị");
        stage.setScene(new Scene(root, 760, 670));
        stage.showAndWait();
        if(controller.isChanged()){
            RefreshTable();
        }
    }
    private void RefreshTable(){
        observableListHH.clear();
        tbvHoiNghi.getItems().clear();
        LoadTableHoiNghi();
        tbvHoiNghi.refresh();
    }

}
