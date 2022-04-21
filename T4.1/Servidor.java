import java.io.IOException;
import java.net.*;
import javax.swing.*;

public class Servidor {
    public void init() {
        try {
            DatagramSocket socketUDP = new DatagramSocket(1234);
            byte[] bufer = new byte[1000];
            while (true) {
                // Construimos el DatagramPacket para recibir peticiones
                DatagramPacket peticion = new DatagramPacket(bufer, bufer.length);
                // Leemos una petición del DatagramSocket
                socketUDP.receive(peticion);
                System.out.print("Datagrama recibido del host: " + peticion.getAddress());
                System.out.println(" desde el puerto remoto: " + peticion.getPort());
                // Construimos el DatagramPacket para enviar la respuesta
                DatagramPacket respuesta = new DatagramPacket(peticion.getData(), peticion.getLength(),
                        peticion.getAddress(), peticion.getPort());

                // Enviamos la respuesta, que es un eco
                socketUDP.send(respuesta);
            }
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(null, "Error en el servidor: " + e.getMessage(), "Error en servidor", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error en el servidor: " + e.getMessage(), "Error en servidor", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        Servidor obj = new Servidor();
        obj.init();
    }
}
