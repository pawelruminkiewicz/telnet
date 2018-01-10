package put.sk;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.*;

import java.io.IOException;
import java.security.Key;

import static javafx.scene.input.KeyCode.C;
import static javafx.scene.input.KeyCode.CONTROL;

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

    private Connection connection = null;

    private Thread watek;


    public TextField getEditPort() {
        return editPort;
    }

    public TextField getEditIp() {
        return editIp;
    }

    public TextField getEditCommand() {
        return editCommand;
    }


    public void connectToServer() throws IOException {
        ScrollBar scrollBarv = (ScrollBar) textArea.lookup(".scroll-bar:vertical");
        scrollBarv.setDisable(false);
        if (connection == null) {
            String port = editPort.getText();
            String ip = editIp.getText();
            connection = new Connection();
            boolean isConnected = connection.startConnection(port, ip);
            if (!isConnected) {
                connection = null;
                return;
            }
            buttonConnect.setText("Disconnect");
            buttonSend.setDisable(false);
        } else {
            watek.stop();
            connection.stopConnection();
            buttonConnect.setText("Connect");
            connection = null;
            buttonSend.setDisable(true);
            textArea.clear();
            return;
        }
        watek = new Thread(this);
        watek.setDaemon(true);
        watek.start();
        textArea.textProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> textArea.setScrollTop(Double.MAX_VALUE));
    }

    public void sendCommandToServer() throws IOException {
        String command = editCommand.getText();

        if (command.equals("exit") ) {
            System.out.println("asaaa");
            connection.sendCommand("exit");
            watek.stop();
            connection.stopConnection();
            buttonConnect.setText("Connect");
            connection = null;
            buttonSend.setDisable(true);
            textArea.clear();
        } else {
            connection.sendCommand(command);
        }
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
            sendCommandToServer();
        }
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
