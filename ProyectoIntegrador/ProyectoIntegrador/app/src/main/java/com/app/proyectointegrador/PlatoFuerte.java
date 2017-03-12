package com.app.proyectointegrador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlatoFuerte extends Activity {

    String usuarioLogeado;
    TextView textoUsuario;
    Button carne;
    Button pollo;
    Button continuar;
    Button terminar;

    int contadorCarne=0;
    int contadorPollo=0;

    TextView textoCarne;
    TextView textoPollo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plato_fuerte);

        Intent lanzador = getIntent();
        usuarioLogeado = lanzador.getStringExtra("usuario_log");

        textoUsuario= (TextView)findViewById(R.id.usuarioplato);
        textoUsuario.setText(usuarioLogeado);

        textoCarne = (TextView)findViewById(R.id.textcarne);
        textoPollo = (TextView)findViewById(R.id.textpollo);


        carne = (Button)findViewById(R.id.butcarne);
        carne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(contadorCarne<2) {
                    contadorCarne += 1;
                    textoCarne.setText(Integer.toString(contadorCarne));
                }

                System.out.println("imprime " + contadorCarne);

            }
        });

        pollo = (Button)findViewById(R.id.butpollo);
        pollo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(contadorPollo<2) {
                    contadorPollo += 1;
                    textoPollo.setText(Integer.toString(contadorPollo));
                }
            }
        });

        continuar = (Button)findViewById(R.id.cont);
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comunicacion.getInstance().enviar("Plato" + "/" + contadorCarne + "/" + contadorPollo, Comunicacion.getInstance().IP,Comunicacion.getInstance().port);
                Log.d("Comunicacion", "Mensaje Enviado" );

                Intent i = new Intent(PlatoFuerte.this,Postre.class);
                i.putExtra("usuario_log", usuarioLogeado);
                startActivity(i);

            }
        });

        terminar = (Button)findViewById(R.id.term);
        terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contadorCarne<1 && contadorPollo<1) {
                    Comunicacion.getInstance().enviar("Terminar" + "/" + "null" + "/" + "null", Comunicacion.getInstance().IP,Comunicacion.getInstance().port);
                    Log.d("Comunicacion", "Mensaje Enviado");

                    Intent i = new Intent(PlatoFuerte.this, Final.class);
                    i.putExtra("usuario_log", usuarioLogeado);
                    startActivity(i);
                }

                if(contadorCarne>0 || contadorPollo>0) {
                    Comunicacion.getInstance().enviar("Plato" + "/" + contadorCarne + "/" + contadorPollo, Comunicacion.getInstance().IP,Comunicacion.getInstance().port);
                    Comunicacion.getInstance().enviar("Terminar" + "/" + "null" + "/" + "null", Comunicacion.getInstance().IP,Comunicacion.getInstance().port);
                    Log.d("Comunicacion", "Mensaje Enviado");

                    Intent i = new Intent(PlatoFuerte.this, Final.class);
                    i.putExtra("usuario_log", usuarioLogeado);
                    startActivity(i);
                }
            }
        });



    }
}
