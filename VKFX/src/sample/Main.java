package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class Main extends Application {

    ListView<String> listView;
    static ObservableList<String> list;
    static ArrayList<String> cityList = new ArrayList<>();
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        VBox root = new VBox(10);
        root.setPadding(new Insets(50));
        Label label = new Label("click the name");
        root.setAlignment(Pos.CENTER);
        listView = new ListView<>(list);
        listView.setPrefSize(120,150);
        listView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                label.setText(list.get((Integer) newValue)+" live in "+cityList.get((Integer) newValue));
            }
        });
        root.getChildren().addAll(listView,label);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    static String url = "jdbc:mysql://localhost:3306/pr15?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static String user = "root";
    static String pass = "admin";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        ArrayList<String> arr = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(url,user,pass);
            System.out.println("Ok");
            PreparedStatement p = con.prepareStatement("select * from customers;");
            ResultSet r = p.executeQuery();
            ResultSetMetaData meta = r.getMetaData();
            while (r.next()){
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    System.out.print(r.getString(i)+"\t");
                    if(i==2){
                        arr.add(r.getString(i));
                        cityList.add(r.getString(3));
                    }
                }
                System.out.println();
            }
            list = FXCollections.observableList(arr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        launch(args);
    }
}
