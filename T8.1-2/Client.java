import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Client extends JFrame {
	
	private JList <String>lstNodes;
	private static DefaultListModel model;
	private static int PORT = 2000;
	private static InetAddress GROUP;
	private static ArrayList<Node> nodes;

	public Client(){
		
		model = new DefaultListModel<>();
		nodes = new ArrayList<Node>();

		lstNodes = new JList(model);
        lstNodes.setBounds(0,0,3,3);
        lstNodes.setFont(new Font("Comic Sans MS",Font.PLAIN,13));
        //lstNodes.setBackground(new Color(92,39,129));
        // lstNodes.setForeground(new Color(255,154,0));
        this.getContentPane().add(lstNodes);
		this.setSize(400,400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private static boolean nodeExists(String node){
		for (Node elem : nodes){
			if(node.equalsIgnoreCase(elem.name)){
				return true;
			}
		}
		return false;	
	}

	private static void addNode(String name){
		Node newNode = new Node(name);
		Thread counter = new Thread(newNode);
		counter.start();
		nodes.add(newNode);
	}

	public static void updateList(){
		//Primero borramos la lista actual
		model.removeAllElements();
		//Volvemos a cargar todos los elementos del arraylist
		for (Node elem : nodes){
			model.addElement(elem.name + " contador: " + elem.ttl + "s");
		}
	}

	public static void removeNode(String name){
		for (Node elem : nodes){
			if(name.equalsIgnoreCase(elem.name)){
				int index = nodes.indexOf(elem);
				nodes.remove(index);
				return;
			}
		}
	}

	private static void incrementTTL(String name){
		for (Node elem : nodes){
			if(name.equalsIgnoreCase(elem.name)){
				elem.ttl = 30;
				return;
			}
		}	
	}

	public static void main(String[] args){
		try{
			
			new Client();
			Thread service = new Thread(new MessagesService(Integer.parseInt(args[0])));
			service.start();
			
			NetworkInterface netint = NetworkInterface.getByName("eth10");
			InetSocketAddress addr = new InetSocketAddress(PORT);
			DatagramChannel channel =  DatagramChannel.open(StandardProtocolFamily.INET);
			GROUP = InetAddress.getByName("230.0.0.1");
			SocketAddress remote = new InetSocketAddress(GROUP, PORT);

			channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, netint);
			channel.join(GROUP, netint);
			channel.configureBlocking(false);
			channel.socket().bind(addr);

			Selector sel = Selector.open();
			channel.register(sel, SelectionKey.OP_READ);
			ByteBuffer buffer = ByteBuffer.allocate(4);

			System.out.println("Esperando mensaje...");
			
			while(true){
				//Aqui estoy recibiendo mensajes
				sel.select();
				Iterator<SelectionKey>it = sel.selectedKeys().iterator();
				while(it.hasNext()){
					SelectionKey k = (SelectionKey)it.next();
					it.remove();
					if(k.isReadable()){
						DatagramChannel ch = (DatagramChannel)k.channel();
						buffer.clear();
						SocketAddress emisor = ch.receive(buffer);
						buffer.flip();
						InetSocketAddress d = (InetSocketAddress)emisor;
						String name = d.getAddress()+":"+d.getPort();
						if(nodeExists(name)){
							incrementTTL(name);
						}else{
							addNode(name);
						}
						continue;
					}
				}//while
			}
		}catch(Exception e){
			System.out.println("ERROR (Client.main): ");
			e.printStackTrace();
		}
	}

}

class MessagesService implements Runnable {

	Selector SELECTOR;
	SocketAddress REMOTE;

	public MessagesService(int PORT){
		try{
			NetworkInterface netint = NetworkInterface.getByName("eth10");
			InetSocketAddress addr = new InetSocketAddress(PORT);
			DatagramChannel channel =  DatagramChannel.open(StandardProtocolFamily.INET);
			InetAddress GROUP = InetAddress.getByName("230.0.0.1");
			REMOTE = new InetSocketAddress(GROUP, 2000);

			channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, netint);
			channel.join(GROUP, netint);
			channel.configureBlocking(false);
			channel.socket().bind(addr);

			SELECTOR = Selector.open();
			channel.register(SELECTOR, SelectionKey.OP_WRITE);
		}catch(Exception e){
			System.out.println("ERROR (MessagesService.Constructor): ");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try{
			ByteBuffer buffer = ByteBuffer.allocate(4);
			while(true){
				SELECTOR.select();
				Iterator<SelectionKey>it = SELECTOR.selectedKeys().iterator();
				while(it.hasNext()){
					SelectionKey k = (SelectionKey)it.next();
					it.remove();
					if(k.isWritable()){
						DatagramChannel ch = (DatagramChannel)k.channel();
						buffer.clear();
						buffer.putInt(666);
						buffer.flip();
						ch.send(buffer, REMOTE);
						continue;
					}
				}//wh
				Thread.sleep(5000);
			}
		}catch(Exception e){
			System.out.println("ERROR (MessagesService.run): ");
			e.printStackTrace();
		}
	}
}

class Node implements Runnable {

	public String name;
	public int ttl;

	public Node(String name){
		this.name = name;
		ttl = 30;
	}

	@Override
	public void run(){
		try{
			while(ttl > 0){
				ttl--;
				Thread.sleep(1000);
				Client.updateList();
			}
			Client.removeNode(name);
		}catch(Exception e){
			System.out.println("ERROR (Node.run): ");
			e.printStackTrace();
		}
	}

}