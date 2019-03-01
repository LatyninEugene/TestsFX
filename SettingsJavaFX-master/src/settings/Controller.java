package settings;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public GridPane grid;
    public TextField col;
    public TextField row;

    @FXML
    private
    ImageView img;

    int x=0;
    int y=0;

    int presX;
    int presY;

    @FXML
    public void presImg(MouseEvent event){

        System.out.println("press");
        img =  (ImageView) event.getSource();
        presX = (int) ((int) ((ImageView)event.getSource()).getFitWidth()/1.5);
        presY = (int) ((int) ((ImageView)event.getSource()).getFitHeight()/1.5);
    }

    @FXML
    public void dragImg(MouseEvent event) {
        if(!(((AnchorPane)img.getParent()).getWidth()<(event.getSceneX()-presX))
                && !(0>(event.getSceneX()-presX/2))){
            img.setLayoutX(event.getSceneX() - presX);
        }
        if(!(((AnchorPane)img.getParent()).getHeight()<(event.getSceneY()))
                && !(0>(event.getSceneY()-presY/2))){
            img.setLayoutY(event.getSceneY() - presY);
        }

    }
    @FXML
    public void clickedImg(MouseEvent event){
        img.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCode());
                System.out.println(event.getSource());
            }
        });
        System.out.println("end");
        if(img.getBoundsInParent().intersects(grid.getBoundsInParent())) {
            ImageView view = new ImageView();
            ArrayList<ImageView> l = new ArrayList<>();
            System.out.println("img = "+img.getBoundsInParent());
            double ax = img.getLayoutX();
            double ay = img.getLayoutY();
            System.out.println("ax="+ax);
            System.out.println("ay="+ay);
            int rast = Integer.MAX_VALUE;
            for (Node n : grid.getChildren()) {
                try {
                    double bx = n.getLayoutX()+n.getBoundsInParent().getWidth()/2;
                    double by = n.getLayoutY()+n.getBoundsInParent().getHeight();
                    System.out.println(bx+"-"+by);
                    System.out.println(n.getId()+"-"+Math.sqrt(Math.pow(bx-ax,2)+Math.pow(by-ay,2)));
                    if(rast>(int) Math.sqrt(Math.pow(bx-ax,2)+Math.pow(by-ay,2))) {
                        rast = (int) Math.sqrt(Math.pow(bx-ax,2)+Math.pow(by-ay,2));
                        view = (ImageView) n;
                    }
                    System.out.println(rast);
                }catch (Exception e){
                    System.out.println("not a image");
                }
            }
            view.setImage(img.getImage());
        }
    }

    @FXML
    public void createTable(ActionEvent actionEvent) {
        int icol = 0;
        int irow = 0;
        try {
            icol = Integer.parseInt(col.getCharacters().toString());
            irow = Integer.parseInt(row.getCharacters().toString());
            if (icol<1 || icol>12 || irow<1 || irow>7){
                throw new Exception();
            }
            Group g = (Group) grid.getChildren().get(grid.getChildren().size()-1);
            grid.getChildren().clear();
            grid.getColumnConstraints().clear();
            grid.getRowConstraints().clear();

            for (int i = 0; i < icol; i++) {
                ColumnConstraints c = new ColumnConstraints(75,75,75);
                c.setMinWidth(10);
                c.setHgrow(Priority.SOMETIMES);
                grid.getColumnConstraints().add(c);
            }
            for (int j = 0; j < irow; j++) {
                RowConstraints r = new RowConstraints(75,75,75);
                r.setMinHeight(10);
                r.setVgrow(Priority.SOMETIMES);
                grid.getRowConstraints().add(r);
            }
            ImageView v;
            for (int i = 0; i < icol; i++) {
                for (int j = 0; j < irow; j++) {
                    v = new ImageView();
                    v.setFitHeight(75);
                    v.setFitWidth(75);
                    v.setOnMouseClicked(this::onRem);
                    grid.add(v,i,j);
                }
            }
            grid.add(g,0,0);
            grid.setGridLinesVisible(true);
        }catch (Exception e){
            System.out.println("Noooooo");
        }
    }

    @FXML
    public void onRem(MouseEvent event){
        System.out.println(((ImageView)event.getSource()).getProperties());
        ((ImageView) event.getSource()).setImage(new Image(String.valueOf(getClass().getResource("def.jpg"))));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
