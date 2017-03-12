package com.app.proyectointegrador;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registro extends Activity {

    private static final String TAG = "Comunicacion";

    Button registro;
    String usuario;
    String contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //boton para el registro
        registro = (Button)findViewById(R.id.regreg);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText user=(EditText)findViewById(R.id.nomuser);
                EditText pass=(EditText)findViewById(R.id.password);
                usuario = user.getText().toString();
                contrasena = pass.getText().toString();
                if(!usuario.equals("") && !contrasena.equals("")) {
                    Comunicacion.getInstance().enviar("Registro" + "/" + usuario + "/" + contrasena, Comunicacion.getInstance().IP,Comunicacion.getInstance().port);
                    Log.d(TAG, "Mensaje Enviado" + usuario + contrasena);

                    Intent i = new Intent(Registro.this,Ingreso.class);
                    startActivity(i);
                }
            }
        });

    }
}
