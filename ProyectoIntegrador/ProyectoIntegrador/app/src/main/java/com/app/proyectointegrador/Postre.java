package com.app.proyectointegrador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Postre extends AppCompatActivity {

    String usuarioLogeado;
    TextView textoUsuario;
    Button sunday;
    Button choco;
    Button continuar;
    Button terminar;

    int contadorSun=0;
    int contadorChoco=0;

    TextView textoSun;
    TextView textoChoco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postre);

        Intent lanzador = getIntent();
        usuarioLogeado = lanzador.getStringExtra("usuario_log");

        textoUsuario= (TextView)findViewById(R.id.nomus);
        textoUsuario.setText(usuarioLogeado);

        textoSun = (TextView)findViewById(R.id.textsun);
        textoChoco = (TextView)findViewById(R.id.textchoco);

        sunday = (Button)findViewById(R.id.sun);
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(contadorSun<2) {
                    contadorSun += 1;
                    textoSun.setText(Integer.toString(contadorSun));
                }

                System.out.println("imprime " + contadorSun);

            }
        });

        choco = (Button)findViewById(R.id.choc);
        choco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(contadorChoco<2) {
                    contadorChoco += 1;
                    textoChoco.setText(Integer.toString(contadorChoco));
                }

                System.out.println("imprime " + contadorChoco);

            }
        });

        continuar = (Button)findViewById(R.id.cont);
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comunicacion.getInstance().enviar("Postre" + "/" + contadorSun + "/" + contadorChoco, Comunicacion.getInstance().IP,Comunicacion.getInstance().port);
                Log.d("Comunicacion", "Mensaje Enviado" );

                Intent i = new Intent(Postre.this,PlatoFuerte.class);
                i.putExtra("usuario_log", usuarioLogeado);
                startActivity(i);

            }
        });

        terminar = (Button)findViewById(R.id.termin);
        terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contadorSun<1 && contadorChoco<1) {
                    Comunicacion.getInstance().enviar("Terminar" + "/" + "null" + "/" + "null", Comunicacion.getInstance().IP,Comunicacion.getInstance().port);
                    Log.d("Comunicacion", "Mensaje Enviado");

                    Intent i = new Intent(Postre.this, Final.class);
                    i.putExtra("usuario_log", usuarioLogeado);
                    startActivity(i);
                }

                if(contadorSun>0 || contadorChoco>0) {
                    Comunicacion.getInstance().enviar("Plato" + "/" + contadorSun + "/" + contadorChoco, Comunicacion.getInstance().IP,Comunicacion.getInstance().port);
                    Comunicacion.getInstance().enviar("Terminar" + "/" + "null" + "/" + "null", Comunicacion.getInstance().IP,Comunicacion.getInstance().port);
                    Log.d("Comunicacion", "Mensaje Enviado");

                    Intent i = new Intent(Postre.this, Final.class);
                    i.putExtra("usuario_log", usuarioLogeado);
                    startActivity(i);
                }
            }
        });
    }
}
