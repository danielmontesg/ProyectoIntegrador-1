package com.app.proyectointegrador;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Tienda extends Activity {

    String usuarioLogeado;
    TextView textoUsuario;
    Button plato;
    Button postre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);
        Intent lanzador = getIntent();
        usuarioLogeado = lanzador.getStringExtra("usuario_log");

        textoUsuario= (TextView)findViewById(R.id.logeado);
        textoUsuario.setText(usuarioLogeado);

        plato = (Button)findViewById(R.id.plato);
        plato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inicio la actividad de Registro
                Intent i = new Intent(Tienda.this,PlatoFuerte.class);
                i.putExtra("usuario_log", usuarioLogeado);
                startActivity(i);
            }
        });

        postre = (Button)findViewById(R.id.postre);
        postre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inicio la actividad de Registro
                Intent i = new Intent(Tienda.this,Postre.class);
                i.putExtra("usuario_log", usuarioLogeado);
                startActivity(i);
            }
        });

    }
}
