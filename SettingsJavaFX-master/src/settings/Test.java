package settings;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class Test implements Initializable {

    boolean isActive = false;

    @FXML
    Button btn;

    @FXML
    VBox vb;
    @FXML
    Label p1;
    @FXML
    Label p2;
    @FXML
    Label p3;
    int pAct = 1;
    @FXML
    Pane con;

    KeyFrame kf;

    @FXML
    public void exit(){
        System.exit(0);
    }

    private void removeAct(){
        p1.getStyleClass().remove("active");
        p2.getStyleClass().remove("active");
        p3.getStyleClass().remove("active");
    }
    @FXML
    public void lclick(MouseEvent ae){
        try {

            if(ae.getSource() == p1) {
                if(pAct != 1){
                    con.getChildren().clear();
                    con.getChildren().addAll((AnchorPane)FXMLLoader.load(getClass().getResource("page1.fxml")));
                    removeAct();
                    p1.getStyleClass().add("active");
                    pAct = 1;
                }
            }else if(ae.getSource() == p2){
                if(pAct != 2) {
                    con.getChildren().clear();
                    con.getChildren().addAll((AnchorPane) FXMLLoader.load(getClass().getResource("page2.fxml")));
                    removeAct();
                    p2.getStyleClass().add("active");
                    pAct = 2;
                }
            }else if(ae.getSource() == p3){
                if(pAct != 3) {
                    con.getChildren().clear();
                    con.getChildren().addAll((AnchorPane) FXMLLoader.load(getClass().getResource("page3.fxml")));
                    removeAct();
                    p3.getStyleClass().add("active");
                    pAct = 3;
                }
            }
            click(new ActionEvent());
        }catch (Exception e){
            System.out.println("not click");
        }

    }
    @FXML
    public void click(ActionEvent ae){
        KeyValue xAct = new KeyValue(vb.prefWidthProperty(),150);
        KeyValue xNotAct = new KeyValue(vb.prefWidthProperty(),0);
        if(isActive){
            kf = new KeyFrame(Duration.millis(200),xAct,xNotAct);
            isActive = false;
        }else {
            kf = new KeyFrame(Duration.millis(200),xNotAct,xAct);
            isActive = true;
        }
        Timeline t = new Timeline();
        t.getKeyFrames().add(kf);
        t.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            con.getChildren().addAll((AnchorPane)FXMLLoader.load(getClass().getResource("page1.fxml")));
            p1.getStyleClass().add("active");
        }catch (Exception e){
            System.out.println("erroro");
        }
    }
}
