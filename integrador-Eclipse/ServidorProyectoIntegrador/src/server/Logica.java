package server;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class Logica implements Observer {
	private PApplet app;
	private Comunicacion com;
	private InetAddress destAddress;
	private int destPort;
	public String usuario;
	public String contra;
	public int carne = 0;
	public int pollo = 0;
	public int sunday = 0;
	public int choco = 0;

	public int precioCarne = 28000;
	public int precioPollo = 23000;
	public int precioSunday = 10000;
	public int precioChoco = 12000;
	
	public int precio;

	public Logica(PApplet app) {
		this.app = app;

		usuario = "daniel";
		contra = "1";

		com = new Comunicacion();
		com.addObserver(this);
		(new Thread(this.com)).start();
	}

	public void dibujar() {

	}

	@Override
	public void update(Observable obs, Object data) {
		DatagramPacket recibido = (DatagramPacket) data;
		String realData = new String(recibido.getData(), 0, recibido.getLength());

		destAddress = recibido.getAddress();
		destPort = recibido.getPort();

		String partido[] = realData.split("/");

		System.out.println("Tipo:" + partido[0] + " Usuario:" + partido[1] + " Contraseña:" + partido[2]);

		// Validamos si el usuario y la contraseña son correctos
		if (partido[0].equals("Ingreso")) {
			if (partido[1].equalsIgnoreCase(usuario)) {
				System.out.println("usuario: " + partido[1]);

				if (partido[2].equalsIgnoreCase(contra)) {
					System.out.println("contraseña: " + partido[2]);

					com.enviar("Acceso", destAddress, destPort);
					System.out.println("Acceso");

				} else {
					System.out.println("Error de Contraseña");
				}

			} else {
				System.out.println("Error de Usuario");
			}
		}

		// Reemplazamos los String de usuario y contraseña por los nuevos
		if (partido[0].equals("Registro")) {
			usuario = partido[1];
			contra = partido[2];

			System.out.println("Usario Registrado:" + usuario + "   Contraseña:" + contra);
		}

		if (partido[0].equals("Plato")) {
			System.out.println("carne= " + partido[1] + "Pollo= " + partido[2]);

			carne = Integer.parseInt(partido[1]);
			pollo = Integer.parseInt(partido[2]);

			System.out.println("ahora carne es=" + carne + " y " + "pollo es=" + pollo);

		}

		if (partido[0].equals("Postre")) {
			System.out.println("sunday= " + partido[1] + "choco= " + partido[2]);

			sunday = Integer.parseInt(partido[1]);
			choco = Integer.parseInt(partido[2]);

			System.out.println("ahora sunday es=" + sunday + " y " + "choco es=" + choco);

		}

		if (partido[0].equals("Terminar")) {
			carne = carne * precioCarne;
			pollo = pollo * precioPollo;
			sunday = sunday * precioSunday;
			choco = choco * precioChoco;

			precio = carne + pollo + sunday + choco;
			System.out.println("precio final= " + precio);

			com.enviar("Precio/", destAddress, destPort);

			carne = 0;
			pollo = 0;
			sunday = 0;
			choco = 0;

		}
		
		if(partido[0].equals("Finalizar")){
			com.enviar("Precio" + "/" + precio, destAddress, destPort);
		}
	}

}
