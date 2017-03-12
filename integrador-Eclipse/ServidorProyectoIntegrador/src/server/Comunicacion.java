package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Observable;

public class Comunicacion extends Observable implements Runnable{
	
	private DatagramSocket socket;
	private int puerto;
	private InetAddress ip;
	
	public Comunicacion(){
		try{
			System.out.println("llego");
			puerto = 5000;
			socket = new DatagramSocket(puerto);
			ip= InetAddress.getByName("10.0.2.15");
		}catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			
			if (socket != null) {
				DatagramPacket data = recibir();

				// Validate that there are no errors with the data
				if (data != null) {
					// Notify the observers that new data has arrived and pass
					// the data to them
					setChanged();
					notifyObservers(data);
					clearChanged();
				}
			}
		}
		
	}
	
	public DatagramPacket recibir(){
		byte[] buffer = new byte[2500000];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		try{
			socket.receive(packet);
			System.out.println("Recibido " + packet.getAddress() + ":" + packet.getPort());
			return packet;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//metodo para enviar mensajes
		public void enviar(String message, InetAddress destAddress, int destPort) {
			
			byte[] data = message.getBytes();
			
			DatagramPacket packet = new DatagramPacket(data, data.length, destAddress, destPort);
			try {
				System.out.println("Sending data to " + destAddress.getHostAddress() + ":" + destPort);
				socket.send(packet);
				System.out.println("Data was sent");
			} catch (IOException e) {
				// Error sending the packet
				e.printStackTrace();
			}
		}
		
		public int getPuerto(){
			return puerto;
		}
		
		public InetAddress getip(){
			return ip;
		}

}
