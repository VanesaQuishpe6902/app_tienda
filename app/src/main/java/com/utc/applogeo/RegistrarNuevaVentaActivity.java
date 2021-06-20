package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistrarNuevaVentaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_nueva_venta);
    }

    //Metodo para volver al la ventana de ventas
    public void volverVenta(View vista) {
        finish();//cerrando ventana de nueva venta
        //creando un objeto para manejar la ventana de ventas
        Intent ventanaVentas = new Intent(getApplicationContext(), VentasActivity.class);
        startActivity(ventanaVentas);//solicitando que se abra la ventana de gestion clientes

    }
    //Mtodo para abrir la pantalla de Agregar cliente
    public void abrirPantallaAgregarCliente(View vista)
    {
        Intent pantallaAgregarCliente = new Intent(getApplicationContext(), AgregarClienteFactura.class);//Creando un Intent para invocar a Cliente Activity
        startActivity(pantallaAgregarCliente); //Iniciando la pantalla Clientes
    }

}