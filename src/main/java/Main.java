import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = new File("main.fxml").toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Мой Склад");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(675);
        primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root, 900, 675));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        try {
            System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());
            //if (System.currentTimeMillis()/1000 < 1544121857) {
                Main.launch(args);
            /*}else{
                System.out.println("Surprise");
                System.exit(1);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getStackTrace());
            try {
                PrintWriter pw = new PrintWriter(new File("errorMain.txt"));
                e.printStackTrace(pw);
                pw.close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }
}
