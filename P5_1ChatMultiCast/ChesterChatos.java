import java.net.*;
import java.io.*;

public class ChesterChatos {
	
	private MulticastSocket service;
	private InetAddress GROUP;
	private final int PORT = 9000;
	private final int BUFF_SIZE = 2000; 

	public ChesterChatos(String userName){
		try {
			GROUP = InetAddress.getByName("230.1.1.1");
			service = new MulticastSocket(PORT);
			service.setTimeToLive(0);
			service.joinGroup(GROUP);
			enviar("<inicio>"+userName);
		} catch(Exception e) {
			System.out.println("ERROR (Chat.Constructor):");
			e.printStackTrace();
		}
	}

	public void enviar(String message){
		try{
			byte[] buffer = message.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(buffer,buffer.length,GROUP,PORT);
			service.send(sendPacket);
		} catch(Exception err) {
			System.out.println("ERROR (Chat.sendMessage):");
			err.printStackTrace();
		}
	}

	public String recibir(){
		try{
			byte[] buffer = new byte[BUFF_SIZE];
			DatagramPacket receivePacket = new DatagramPacket(buffer,buffer.length,GROUP,PORT);
			service.receive(receivePacket);
			return new String(buffer,0,receivePacket.getLength(),"UTF-8");
		} catch(Exception err) {
			System.out.println("ERROR (Chat.receiveMessage):");
			err.printStackTrace();
			return " ";
		}
	}

	public void salir(String userName){
		try {
			enviar("<fin>"+userName);
			service.leaveGroup(GROUP);
			service.close();
		} catch(Exception err) {
			System.out.println("ERROR (Chat.exitChat):");
			err.printStackTrace();
		}
	}

}