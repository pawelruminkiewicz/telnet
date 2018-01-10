package put.sk;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {

    private FXMLLoader loader;
    private Controller controller;

    public FXMLLoader getLoader() {
        return loader;
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        loader = new FXMLLoader(getClass().getResource("app.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        primaryStage.setTitle("Telnet console");
        primaryStage.setScene(new Scene(root, 720, 500));
        primaryStage.show();
        //do testow
        controller.getEditIp().setText("127.0.0.1");
        controller.getEditPort().setText("1236");
        controller.getEditCommand().setText("ls -l|wc -c");
    }

    @Override
    public void stop() throws IOException {
        controller.exitApplication();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
