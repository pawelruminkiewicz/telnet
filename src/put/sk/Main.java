package put.sk;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    FXMLLoader loader;



    public FXMLLoader getLoader() {
        return loader;
    }

    static Controller controller;
    public static Controller getController() {
        return controller;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
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
