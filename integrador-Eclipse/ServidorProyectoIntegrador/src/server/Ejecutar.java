package server;
import processing.core.PApplet;

public class Ejecutar extends PApplet {
	PApplet app;
	Logica logica;
	
	public static void main(String[] args){
		PApplet.main("server.Ejecutar", args);
	}
	
	public void settings(){
		size(200,200);
	}
	
	public void setup(){
		logica = new Logica(this);
	}
	
	public void draw(){
		background(255);
		logica.dibujar();
	}

}
