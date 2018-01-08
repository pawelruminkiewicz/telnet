package put.sk;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Connection  {


    private void setZywy(boolean zywy) {
        this.zywy = zywy;
    }

    private boolean zywy;


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



    public void startConnection(String _port, String _ip) {

        try {

            socket = new Socket(_ip, Integer.parseInt(_port));

            // Uzyskanie strumieni do komunikacji
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sockOut = socket.getOutputStream();
            sockIn = socket.getInputStream();


        } catch (IOException exc) {
            // inne wyjątki we/wy
        }
    }

    public void sendCommand(String command) throws IOException {
        sockOut.write(command.getBytes());

    }

    public void stopConnection() throws IOException {
        sockOut.close();
        sockIn.close();
        socket.close();
    }





}
