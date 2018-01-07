package put.sk;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class Controller {

    @FXML
    private Button buttonSend;

    @FXML
    private Button buttonConnect;

    @FXML
    private TextField editPort;

    public TextField getEditPort() {
        return editPort;
    }

    public TextField getEditIp() {
        return editIp;
    }

    public TextField getEditCommand() {
        return editCommand;
    }

    @FXML
    private TextField editIp;

    @FXML
    private TextField editCommand;


    @FXML
    private TextArea textArea;

    private  Connection connection;

    public void connectToServer(MouseEvent mouseEvent) throws IOException {
        if(connection==null) {
            String port = editPort.getText();
            String ip = editIp.getText();
            connection = new Connection();
            connection.startConnection(port, ip);
            buttonConnect.setText("Disconnect");
            buttonSend.setDisable(false);
        }
        else{
            connection.stopConnection();
            buttonConnect.setText("Connect");
            connection = null;
            buttonSend.setDisable(true);

        }
    }


    public void sendCommandToServer(MouseEvent mouseEvent) throws IOException {
        String command = editCommand.getText();
        connection.sendCommand(command);
    }

    @FXML
    public void exitApplication() throws IOException {
        if(connection!=null)
            connection.stopConnection();
    }

    public void enterOnSendCommand(KeyEvent key) throws IOException {
        if (key.getCode().equals(KeyCode.ENTER)&& !buttonSend.isDisable())
        {
            String command = editCommand.getText();
            connection.sendCommand(command);
        }
    }


    public TextArea getTextArea() {
        return textArea;
    }
}
