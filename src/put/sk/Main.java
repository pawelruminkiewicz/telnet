package put.sk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Klasa główna projektu
 */
public class Main extends Application {

    private FXMLLoader loader;
    private Controller controller;

    /**
     * Metoda ładująca aplikację
     * @param primaryStage scena
     * @throws Exception exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        loader = new FXMLLoader(getClass().getResource("app.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        primaryStage.setTitle("Telnet console");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }

    /**
     * Metoda zamykająca aplikację
     * @throws IOException exception
     */
    @Override
    public void stop() throws IOException {
        controller.exitApplication();
    }

    /**
     * Metoda main
     * @param args argumenty
     */
    public static void main(String[] args) {
        launch(args);
    }
}
