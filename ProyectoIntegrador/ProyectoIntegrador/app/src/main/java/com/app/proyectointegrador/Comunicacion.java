package com.app.proyectointegrador;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

public class Comunicacion extends Observable implements Runnable {
    private static final String TAG = "Comunicacion";

    private static  Comunicacion com;

    private DatagramSocket socket=null;
    private InetAddress ip=null;
    public static final int port=5000;
    private boolean corriendo;
    private boolean conectando;
    private boolean reset;

    public static final String IP = "192.168.1.104";

    private boolean notificoError;

    String text="";

    private Comunicacion(){
        socket = null;
        corriendo = true;
        conectando = true;
        reset=false;
    }

    private boolean conectar(){
        if(socket==null&&ip==null) {
            try {
                socket = new DatagramSocket(port);
                //aqui va la direccion ip del pc
                ip=InetAddress.getByName("192.168.1.104");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    //inicia el hilo de comunicacion
    public static Comunicacion getInstance(){
        if(com==null){
            com=new Comunicacion();
            Thread t=new Thread(com);
            t.start();
        }
        return com;
    }

    //metodo run
    public void run() {
        Log.d(TAG, "[ HILO DE COMUNICACIÓN INICIADO ]");
        while (corriendo) {
            try {
                if (conectando) {
                    if (reset) {
                        if (socket != null) {
                            try {
                                socket.close();
                            } finally {
                                socket = null;
                            }
                        }
                        reset = false;
                    }
                    conectando = !conectar();
                } else {
                    if (socket != null) {
                        //metodo para la recepcion de mensajes
                        //recibir();
                        DatagramPacket p = recibir2();

                        if(p!= null){
                            // Transform packet bytes to understandable data
                            String message = new String(p.getData(), 0, p.getLength());

                            System.out.println("llega mensaje");

                            // Notify the observers that new data has arrived and pass the data to them
                            setChanged();
                            notifyObservers(message);
                            clearChanged();
                        }
                    }
                }
                Thread.sleep(500);
            }  catch (InterruptedException e) {

                Log.d(TAG, "[ INTERRUPCIÓN ]");
            }
        }

        try {
            socket.close();
        }  finally {
            socket = null;
        }
    }

    public void reintentar() {
        conectando = true;
        reset = true;
        notificoError = false;
    }


    public void enviar(final String message, final String destAddress, final int destPort) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket != null) {

                    try {
                        // Validate destAddress
                        InetAddress ia = InetAddress.getByName(destAddress);
                        byte[] data = message.getBytes();
                        DatagramPacket packet = new DatagramPacket(data, data.length, ia, destPort);

                        System.out.println("Sending data to " + ia.getHostAddress() + ":" + destPort);
                        socket.send(packet);
                        System.out.println("Data was sent");

                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    setChanged();
                    notifyObservers("Not connected");
                    clearChanged();
                }
            }
        }).start();

    }

    //metodo para la recepcion de objetos
    public void recibir() throws IOException {
        byte[]datos=new byte[25000000];
        DatagramPacket dp=new DatagramPacket(datos,datos.length);
        socket.receive(dp);
        Object c=deserializar(datos);
        System.out.println("llega");
    }

    public DatagramPacket recibir2() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
            System.out.println("Data received from " + packet.getAddress() + ":" + packet.getPort());
            return packet;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    //metodo para el manejo de los distintos mensajes que llegan del servidor despues de la activacion del boton de ingreso
    public void manejarLogin(String recibido){
        if(recibido.contains("log_resp")){
            String []separar=recibido.split(":");
            int resultado=Integer.parseInt(separar[1]);

            setChanged();
            switch (resultado){
                //Error de usuario
                case 0:
                    notifyObservers("wrong_user");
                    break;
                //Usuario y Contraseña correctos
                case 1:
                    notifyObservers("login_ok");
                    break;
                //Error de contraseña
                case 2:
                    notifyObservers("wrong_pass");
                    break;
            }
            clearChanged();
        }
        //recibe una notificacion de un registro en la aplicacion
        if(recibido.contains("Acceso")){
            String []separar=recibido.split(":");
            System.out.println(recibido.toString());
            int resultado=Integer.parseInt(separar[1]);
            setChanged();
            switch (resultado){
                case 0:
                    break;
                //Registro correcto
                case 1:
                    System.out.println("sirve");
                    notifyObservers("reg_ok");
                    break;
                //Usuario ya existente
                case 2:
                    notifyObservers("change_user");
                    break;
            }
        }

    }

    //metodos de serializar y deserializar para el envio de mensajes por red
    public  byte[] seralizar(Object c){

        ByteArrayOutputStream bs=new ByteArrayOutputStream();
        ObjectOutputStream os;
        try {
            os=new ObjectOutputStream(bs);
            os.writeObject(c);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  bs.toByteArray();
    }
    public Object deserializar(byte[] datos){

        Object c=null;
        ByteArrayInputStream bs= new ByteArrayInputStream(datos);
        ObjectInputStream oi;
        try {
            oi=new ObjectInputStream(bs);
            c=(Object)oi.readObject();
            oi.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return c;
    }

    public String getIp(){
        return ip.toString();
    }

    public int getPort(){
        return port;
    }

}
