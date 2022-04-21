import java.io.*;
import java.net.*;

public class Server {
    public static void main(String args[]) {

        try{
			
			int PORT = 4321;
			DatagramSocket server = new DatagramSocket(PORT);
			System.out.println("Servidor iniciado en el puerto "+PORT);
			InetAddress LOCAL_HOST = InetAddress.getByName("127.0.0.1");
			int clientPort = PORT;

			while(true){
				StringBuffer msgBuff = new StringBuffer();	
				int msgLength = 20, tamo = 20;
				int offset = 0;
				byte[] msgBytes = new byte[tamo];				
				while(msgLength >= tamo){					
					DatagramPacket receivePacket = new DatagramPacket(msgBytes,msgBytes.length);
					server.receive(receivePacket);
					String data = new String(receivePacket.getData(),0,receivePacket.getLength());					
					msgBuff.insert(offset,data);
					System.out.println(data);
					offset += msgLength;
					msgLength = receivePacket.getLength();
					clientPort = receivePacket.getPort();
				}

				System.out.println("Mensaje recibido: "+msgBuff);
				
				String eco = "Mensaje eco  del servidor: " + msgBuff.toString();
				byte[] bytes = eco.getBytes();

				msgBytes = eco.getBytes();
				offset = 0;
				int buffLength = msgBytes.length - offset;				
				
				while(buffLength > 0){
					if(buffLength < tamo){						
						DatagramPacket sendPacket = new DatagramPacket(msgBytes,offset,buffLength,LOCAL_HOST,clientPort);
						server.send(sendPacket);
						buffLength = 0; //Salimos
					}else{
						DatagramPacket sendPacket = new DatagramPacket(msgBytes,offset,buffLength,LOCAL_HOST,clientPort);
						server.send(sendPacket);
						offset += tamo;
						buffLength = msgBytes.length - offset;							
					}
				}


				System.out.println("Respuesta enviada");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
    }
}