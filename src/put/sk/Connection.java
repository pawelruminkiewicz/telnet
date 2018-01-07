package put.sk;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {
    public String getMessageFromServer() {
        return messageFromServer;
    }

    private String messageFromServer = "";
    private InputStream sockIn;
    private OutputStream sockOut;
    private BufferedReader reader;
    private Socket socket;

    public void startConnection(String _port, String _ip) {

        try {
            System.out.println("Start connection..");
            // Utworzenie gniazda
            String serverHost = _ip; // adres IP serwera ("cyfrowo" lub z użyciem DNS)
            int serverPort = Integer.parseInt(_port);      // numer portu na którym nasłuchuje serwer

            socket = new Socket(serverHost, serverPort);
            System.out.println("Ready for read..");

            // Uzyskanie strumieni do komunikacji
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sockOut = socket.getOutputStream();
            sockIn = socket.getInputStream();


            // Po zakończeniu komunikacji - zamkniecie strumieni i gniazda


        } catch (UnknownHostException exc) {
            // nieznany host
        } catch (SocketException exc) {
            // wyjątki związane z komunikacją przez gniazda
        } catch (IOException exc) {
            // inne wyjątki we/wy
        }
    }
    public void sendCommand(String command) throws IOException {
        sockOut.write(command.getBytes());
        String val = "";
        boolean asd = false;
        do{
            System.out.println(reader.ready());
            val = reader.readLine();
            System.out.println(val);
            messageFromServer=messageFromServer+val+'\n';
            }
        while(reader.ready());
        System.out.println("za");
        Main.getController().getTextArea().setText(messageFromServer);
    }

    public void stopConnection() throws IOException {
        sockOut.close();
        sockIn.close();
        socket.close();
    }
}
