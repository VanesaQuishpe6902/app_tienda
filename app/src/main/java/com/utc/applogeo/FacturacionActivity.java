package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FacturacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);
    }
    //Metodo para volver al la ventana de Tipo de facturacion
    public void volverVentanaTipoFacturacion(View vista) {
        finish();//cerrando ventana de nueva venta
        //creando un objeto para manejar la ventana de ventas
        Intent ventanaTipoFacturacion = new Intent(getApplicationContext(), RegistrarNuevaVentaActivity.class);
        startActivity(ventanaTipoFacturacion);//solicitando que se abra la ventana de gestion clientes

    }
}