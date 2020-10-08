package sample;

import Util.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

public class Main extends Application {

    private static SessionFactory factory;
    private static Thread t;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/views/Users/Home.fxml"));
        primaryStage.setTitle("Home");
        primaryStage.setScene(new Scene(root, 1000, 580));
        t.join();
        primaryStage.show();
    }

    public static void main(String[] args) {
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                HibernateUtil.getSessionFactory();
            }
        });
        t.start();
        launch(args);


    }
}
