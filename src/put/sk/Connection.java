package put.sk;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Connection implements Runnable {


    private void setZywy(boolean zywy) {
        this.zywy = zywy;
    }

    private boolean zywy;

    public String getMessageFromServer() {

        return messageFromServer;
    }

    private String messageFromServer = "";
    private InputStream sockIn;
    private OutputStream sockOut;

    public BufferedReader getReader() {
        return reader;
    }

    Connection() {
        setZywy(true);
    }

    private BufferedReader reader;
    private Socket socket;
    private Thread watek;


    public void startConnection(String _port, String _ip) {

        try {

            System.out.println("Start connection..");
            // Utworzenie gniazda

            socket = new Socket(_ip, Integer.parseInt(_port));
            System.out.println("Ready for read..");

            // Uzyskanie strumieni do komunikacji
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sockOut = socket.getOutputStream();
            sockIn = socket.getInputStream();
            watek = new Thread(this);
            watek.setDaemon(true);
            watek.start();


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

    }

    public void stopConnection() throws IOException {
        watek.stop();
        sockOut.close();
        sockIn.close();
        socket.close();
    }


    @Override
    public void run() {
        try {
            int value;
            while (!Thread.currentThread().isInterrupted()) {
                char val;
                value = reader.read();
                val = (char) value;
                messageFromServer = messageFromServer + val;
                Main.getController().getTextArea().setText(messageFromServer);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
