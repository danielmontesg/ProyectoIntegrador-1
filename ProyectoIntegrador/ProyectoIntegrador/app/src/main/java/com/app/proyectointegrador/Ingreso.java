package com.app.proyectointegrador;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Observable;
import java.util.Observer;

public class Ingreso extends Activity implements Observer {
    private static final String TAG = "Comunicacion";

    Comunicacion com;


    Button registro;
    Button ingreso;
    String usuario;
    String contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Comunicacion.getInstance().addObserver(this);


        //boton para el registro
        registro = (Button)findViewById(R.id.reg);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inicio la actividad de Registro
                Intent i = new Intent(Ingreso.this,Registro.class);
                startActivity(i);
            }
        });

        ingreso = (Button)findViewById(R.id.ingreso);
        ingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText user=(EditText)findViewById(R.id.usu);
                EditText pass=(EditText)findViewById(R.id.contra);
                usuario = user.getText().toString();
                contrasena = pass.getText().toString();
                if(!usuario.equals("") && !contrasena.equals("")) {

                    Comunicacion.getInstance().enviar("Ingreso" + "/" + usuario + "/" + contrasena, Comunicacion.getInstance().IP,Comunicacion.getInstance().port);
                    Log.d(TAG, "Mensaje Enviado" + usuario + contrasena);
                }

            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg != null){
            System.out.print("Recibo el mensaje " + arg);
            if(arg.toString().contains("Acceso")){
            System.out.println("Usuario y Contraseña Correctos" + arg);
                Intent i = new Intent(Ingreso.this,Tienda.class);
                i.putExtra("usuario_log", usuario);
                startActivity(i);
            }

        }
    }
}
