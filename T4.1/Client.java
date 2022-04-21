import java.net.*;
import javax.swing.*;
import java.io.*;

public class Client {
    public void init() {
        String mgs;
        InetAddress host;
        int puerto;
        DatagramSocket socketUDP;
        try {
            socketUDP = new DatagramSocket();
            mgs = JOptionPane.showInputDialog("Ingresa tu mensaje");
            byte b[] = mgs.getBytes();
            host = InetAddress.getByName("localhost");
            puerto = 1234;

            DatagramPacket pet = new DatagramPacket(b, mgs.length(), host, puerto);
            socketUDP.send(pet);
            // Construimos el DatagramPacket que contendrá la respuesta
            byte[] bufer = new byte[1000];
            DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
            socketUDP.receive(respuesta);

            // Enviamos la respuesta del servidor a la salida estandar
            System.out.println("Respuesta: " + new String(respuesta.getData()));

            // Cerramos el socket
            socketUDP.close();
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(null, "Error de socket en el cliente: " + e.getMessage(), "Error en cliente", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error de socket en el cliente: " + e.getMessage(), "Error en cliente", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        Client obj = new Client();
        obj.init();
    }
}