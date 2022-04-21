import java.io.*;
import java.net.*;
public class Client3 {

    private MulticastSocket service;
	private InetAddress GROUP;
	private final int PORT = 9321;
	private final int BUFF_SIZE = 2000;
    private final String MESSAGE = "Hola soy el mensaje CLIENTE 3";

    public void init() {
        try {			
			service = new MulticastSocket(PORT);
            service.setReuseAddress(true);
			service.setTimeToLive(1);
            byte[] buffer = MESSAGE.getBytes();
            GROUP = InetAddress.getByName("230.1.1.1");
			service.joinGroup(GROUP);
			for (;;) {
                DatagramPacket p = new DatagramPacket(buffer,buffer.length,GROUP,9999);
                service.send(p);
                System.out.println("Enviando mensaje "+MESSAGE+ " con un TTL= "+service.getTimeToLive());
                try{
                    Thread.sleep(3000);
                }catch(InterruptedException ie){
                    ie.printStackTrace();
                }
                //DatagramPacket r = new DatagramPacket(new byte[10],10);
                service.receive(p);
                String msj = new String(p.getData());
                System.out.println("Datagrama recibido.."+msj);         
                System.out.println("Servidor descubierto:" + p.getAddress()+" puerto:"+p.getPort());
            }
		} catch(Exception e) {
			System.out.println("ERROR (Chat.Constructor):");
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
        Client obj = new Client();
        obj.init();
    }
}