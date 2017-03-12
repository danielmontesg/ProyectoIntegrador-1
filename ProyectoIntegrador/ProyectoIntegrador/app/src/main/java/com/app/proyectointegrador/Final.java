package com.app.proyectointegrador;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class Final extends Activity implements Observer {

    String usuarioLogeado;
    TextView textoUsuario;

    TextView textoPrecio;

    Button precioFinal;

    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Intent lanzador = getIntent();
        usuarioLogeado = lanzador.getStringExtra("usuario_log");

        textoUsuario= (TextView)findViewById(R.id.usfinal);
        textoUsuario.setText(usuarioLogeado);



        Comunicacion.getInstance().addObserver(this);

        Comunicacion.getInstance().enviar("Finalizar" + "/" + "null" + "/" + "null", Comunicacion.getInstance().IP,Comunicacion.getInstance().port);

       // textoPrecio = (TextView)findViewById(R.id.textprice);
       //textoPrecio.setText(total);

        precioFinal = (Button)findViewById(R.id.button);
        precioFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textoPrecio = (TextView)findViewById(R.id.textprice);
                textoPrecio.setText(Integer.toString(total));
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg != null){
            System.out.print("Recibo el mensaje " + arg);
            String[] partido = arg.toString().split("/");
            if(partido[0].contains("Precio")){

                System.out.println("llegamos aqui " + partido[1]);

                total = Integer.parseInt(partido[1]);

            }

        }
    }
}
