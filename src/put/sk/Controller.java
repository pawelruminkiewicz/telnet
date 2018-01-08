package put.sk;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class Controller implements Runnable {

    @FXML
    private Button buttonSend;

    @FXML
    private Button buttonConnect;

    @FXML
    private TextField editPort;

    @FXML
    private TextField editIp;

    @FXML
    private TextField editCommand;

    @FXML
    private TextArea textArea;

    private Connection connection;

    private Thread watek;

    private String messageFromServer = "";

    public TextField getEditPort() {
        return editPort;
    }

    public TextField getEditIp() {
        return editIp;
    }

    public TextField getEditCommand() {
        return editCommand;
    }


    public void connectToServer(MouseEvent mouseEvent) throws IOException {
        ScrollBar scrollBarv = (ScrollBar) textArea.lookup(".scroll-bar:vertical");
        scrollBarv.setDisable(false);
        if (connection == null) {
            String port = editPort.getText();
            String ip = editIp.getText();
            connection = new Connection();
            connection.startConnection(port, ip);
            buttonConnect.setText("Disconnect");
            buttonSend.setDisable(false);
        } else {
            watek.stop();
            connection.stopConnection();
            buttonConnect.setText("Connect");
            connection = null;
            buttonSend.setDisable(true);
            return;
        }
        watek = new Thread(this);
        watek.setDaemon(true);
        watek.start();
        textArea.textProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> textArea.setScrollTop(Double.MAX_VALUE));
    }

    public void sendCommandToServer(MouseEvent mouseEvent) throws IOException {
        String command = editCommand.getText();
        connection.sendCommand(command);
        editCommand.clear();
    }

    @FXML
    public void exitApplication() throws IOException {
        if (connection != null) {
            watek.stop();
            connection.stopConnection();
        }
    }

    public void enterOnSendCommand(KeyEvent key) throws IOException {
        if (key.getCode().equals(KeyCode.ENTER) && !buttonSend.isDisable()) {
            String command = editCommand.getText();
            connection.sendCommand(command);
            editCommand.clear();
        }
    }

    public TextArea getTextArea() {
        return textArea;
    }

    @Override
    public void run() {
        try {
            int value;
            while (!Thread.currentThread().isInterrupted()) {
                char val;
                value = connection.getReader().read();
                val = (char) value;
                Platform.runLater(() -> textArea.appendText(String.valueOf(val)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
