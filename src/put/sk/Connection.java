package put.sk;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/***
 * Klasa odpowiedzialna za połączenie między klientem a serwerem
 */
public class Connection {


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


    /***
     * Metoda odpowiedzialna za rozpoczęcie połączenia
     * @param _port port serwera
     * @param _ip adres serwera
     * @return prawdę jeżeli połaczenie się powiodło, fałsz jeżeli nie
     */
    public boolean startConnection(String _port, String _ip) {

        try {

            socket = new Socket(_ip, Integer.parseInt(_port));
            // Uzyskanie strumieni do komunikacji
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sockOut = socket.getOutputStream();
            sockIn = socket.getInputStream();
            return true;

        } catch (SocketException e) {
            return false;
        } catch (UnknownHostException e) {
            return false;
        }
        catch (IOException exc) {

        }
        return true;
    }

    /***
     * Metoda wysyłająca komendę
     * @param command komenda
     * @throws IOException IOException
     */
    public void sendCommand(String command) throws IOException {
        char enter = 13;
        sockOut.write((command + enter).getBytes());

    }

    /***
     * Metoda kończąca połączenie, zamykająca sockety
     * @throws IOException IOException
     */
    public void stopConnection() throws IOException {
        sockOut.close();
        sockIn.close();
        socket.close();
    }


}
