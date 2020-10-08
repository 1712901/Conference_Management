package sample;

import javafx.scene.control.Alert;

public class MyAlert {
    public static int ERROR=-1;
    public static int INF=0;
    private String title;
    private String content;
    private Alert alert=null;

    public MyAlert(int Standard, String title, String content) {

        if(Standard==ERROR){
            alert=new Alert(Alert.AlertType.ERROR);
        }else {
            alert=new Alert(Alert.AlertType.INFORMATION);
        }
        if(alert!=null){
            alert.setHeaderText(null);
            alert.setTitle(title);
            alert.setContentText(content);
        }
    }
    public void show(){
        alert.showAndWait();
    }
}
