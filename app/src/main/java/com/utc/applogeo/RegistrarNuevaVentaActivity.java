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
    //Mtodo para abrir la pantalla de facturacion
    public void abrirPantallaFacturacion(View vista)
    {
        Intent pantallaFacturacion = new Intent(getApplicationContext(), FacturacionActivity.class);//Creando un Intent para invocar a Cliente Activity
        startActivity(pantallaFacturacion); //Iniciando la pantalla Clientes
    }
    //Mtodo para abrir la pantalla de Consumidor Final
    public void abrirPantallaConsumidorFinal(View vista)
    {
        Intent pantallaConsumidorFinal = new Intent(getApplicationContext(), ConsumidorFinalActivity.class);//Creando un Intent para invocar a Cliente Activity
        startActivity(pantallaConsumidorFinal); //Iniciando la pantalla Clientes
    }
}