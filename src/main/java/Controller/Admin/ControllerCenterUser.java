package Controller.Admin;

import Util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.DiaDiem;
import model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerCenterUser implements Initializable {
    @FXML
    private JFXButton btnRefresh;

    @FXML
    private TableView<Person> tbvUsers;

    @FXML
    private TableColumn<Person,String> colName;

    @FXML
    private TableColumn<Person,String> colUsername;

    @FXML
    private TableColumn<Person,String> colPassword;

    @FXML
    private TableColumn<Person,String> colEmail;

    @FXML
    private TableColumn<Person,String> colAddress;

    @FXML
    private TableColumn<Person, Void> colAction;

    private ObservableList<Person> observableListUser= FXCollections.observableArrayList();

    @FXML
    void ActionRefresh(ActionEvent event) {
        observableListUser.clear();
        LoadDataTableUsers();
    }


    private void intiColsUser(){
        colName.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colAction.setCellFactory(param ->new TableCell<Person,Void>(){
            private final JFXButton btnBlock=new JFXButton();
            {
                btnBlock.setUnderline(true);

                // Xử lý việc cấm, mở cho 1 user
                btnBlock.setOnAction(event -> {
                    Person pers=getTableView().getItems().get(getIndex()); // get person tại hàng hiện tại
                    pers.setTrangThai(!pers.isTrangThai());
                    try {
                        SessionFactory factory= HibernateUtil.getSessionFactory();
                        Session session=factory.openSession();
                        Transaction transaction=session.beginTransaction();

                        // Update datebase
                        session.update(pers);

                        transaction.commit();
                        session.close();
                        // Cập nhật UI
                        setStateUser(pers.isTrangThai(),btnBlock);
                    }catch (Exception e){
                        //Thông báo lỗi
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){
                    setGraphic(null);
                    return;
                }

                Person person=(Person) this.getTableRow().getItem();
                if(person!=null) {
                    // Cập nhật UI
                    setStateUser(person.isTrangThai(), btnBlock);
                    // add button vào cell
                    setGraphic(btnBlock);
                }
            }
        });
    }
    private void LoadDataTableUsers(){
        SessionFactory factory= HibernateUtil.getSessionFactory();
        Session session=factory.openSession();
        Transaction transaction=session.beginTransaction();

        List list= session.createQuery("From Person where PHANQUYEN=0").getResultList();

        transaction.commit();
        session.close();

        observableListUser.addAll(list);
        tbvUsers.setItems(observableListUser);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        intiColsUser();
        LoadDataTableUsers();
    }
    private void setStateUser(boolean b, Button btn){
        if(!b){
            btn.setText("Block");
            btn.setStyle("-fx-text-fill: #a5ff5c;");
        }else {
            btn.setText("Blocked");
            btn.setStyle("-fx-text-fill: #f26f55;");
        }

    }
}
