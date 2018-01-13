package put.sk;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
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

    /***
     * Metoda przypisana do przycisku Connect
     * Uruchamia połączenie na podstawie danych wprowadzonych przez użytkownika
     * @throws IOException IOException
     */
    public void connectToServer() throws IOException {
        //wlaczony scroll
        ScrollBar scrollBarv = (ScrollBar) textArea.lookup(".scroll-bar:vertical");
        scrollBarv.setDisable(false);
        textArea.setEditable(false);
        if (connection == null) {
            String port = editPort.getText();
            String ip = editIp.getText();
            if(port.isEmpty() || ip.isEmpty() || Integer.parseInt(port)>65535) return; //zabezpieczenie przed blednymi danymi
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
    }

    /**
     * Metoda wysyłająca komendę do serwera, podpięta pod
     * przycisk Send command.
     *
     * @throws IOException IOException
     */
    public void sendCommandToServer() throws IOException {
        String command = editCommand.getText();

        if (command.equals("exit")) {
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

    /***
     * Metoda wywoływana przy zamykaniu aplikacji.
     * Zamyka połączenie z serwerem, zatrzymuje wątek odczytu.
     * @throws IOException IOException
     */
    @FXML
    public void exitApplication() throws IOException {
        if (connection != null) {
            watek.stop();
            connection.stopConnection();
        }
    }

    /**
     * Metoda pozwalająca na wysłanie komendy do serwera,
     * poprzez zatwierdzenie przyciskiem enter
     *
     * @param key przycisk wciśnięty przez użytkownika. Metoda reaguje na enter.
     * @throws IOException IOException
     */
    public void enterOnSendCommand(KeyEvent key) throws IOException {
        if (key.getCode().equals(KeyCode.ENTER) && !buttonSend.isDisable()) {
            sendCommandToServer();
        }
    }


    /***
     * Metoda odpowiedzialna za wątek odczytu danych z serwera
     * i prezentowania ich na obiekcie typu textArea
     */
    @Override
    public void run() {
        try {
            int value;
            while (!Thread.currentThread().isInterrupted()) {
                char val;
                value = connection.getReader().read();
                val = (char) value;
                textArea.textProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> textArea.setScrollTop(Double.MAX_VALUE));
                Platform.runLater(() -> textArea.appendText(String.valueOf(val)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
