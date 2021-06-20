package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AgregarClienteFactura extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cliente_factura);
    }
    //Metodo para volver al la ventana de Seleccionar el metodo de venta
    public void volverNuevaVenta(View vista) {
        finish();//cerrando ventana de nueva venta
        //creando un objeto para manejar la ventana de ventas
        Intent ventanaNuevaVenta = new Intent(getApplicationContext(), MetodoVentaActivity.class);
        startActivity(ventanaNuevaVenta);//solicitando que se abra la ventana de gestion clientes

    }
}