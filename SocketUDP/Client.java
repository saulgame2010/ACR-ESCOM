import java.io.*;
import java.net.*;

public class Client {
    // Los argumentos proporcionan el mensaje y el nombre del servidor
    public static void main(String args[]) {

        try {
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            DatagramSocket socketUDP = new DatagramSocket();
            DatagramPacket mensajeEnviado, vacio;
            System.out.println("Escribe tu mensaje");
            String mng = teclado.readLine();
            String ip = "localhost";
            byte[] mensajeBytes = mng.getBytes();
            InetAddress hostServidor = InetAddress.getByName(ip);
            int puertoServidor = 4321;
            int tam = 20, tamo = 20;
            int offset = 0;
            int tamBuff = mensajeBytes.length - offset;

            while (tamBuff > 0) {
                if(tamBuff < tamo) {
                    //Construimos un datagrama para enviar el mensaje al servidor
                    mensajeEnviado = new DatagramPacket(mensajeBytes, offset, tamBuff, hostServidor, puertoServidor);
                    socketUDP.send(mensajeEnviado);
                    tamBuff = 0;
                } else {                                         
                    //Construimos un datagrama para enviar el mensaje al servidor
                    mensajeEnviado = new DatagramPacket(mensajeBytes, offset, tamBuff, hostServidor, puertoServidor);
                    socketUDP.send(mensajeEnviado);
                    //El offset suma 20
                    offset += tamo;
                    //el tamBuff será el tamaño del mensaje menos el offset y así se va a restar hasta que se haga 0, que quiere decir que ya acabó de
                    //enviar el mensaje y como tamBuff = 0 se acabará el while
                    tamBuff = mensajeBytes.length - offset;                                    
                }
            }
            
            if(mensajeBytes.length % tamo == 0) {
                String obj = "";
                byte[] objBytes = obj.getBytes();
                tamBuff = objBytes.length;
                mensajeEnviado = new DatagramPacket(mensajeBytes, tamBuff, hostServidor, puertoServidor);
                socketUDP.send(mensajeEnviado);
            }

            StringBuffer msgPartes = new StringBuffer();
            offset = 0;
            byte[] bufer = new byte[tamo];
            while(tam >= tamo) {
                // Construimos el DatagramPacket que contendrá la respuesta
                DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
                socketUDP.receive(respuesta);
                String data = new String(respuesta.getData(),0,respuesta.getLength());
                msgPartes.insert(offset, data);
                offset += tamo;
                tam = respuesta.getLength();
            }
            // Enviamos la respuesta del servidor a la salida estandar
            System.out.println("Respuesta: " + new String(msgPartes));

            // Cerramos el socket
            socketUDP.close();

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}
