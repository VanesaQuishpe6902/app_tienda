package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VentasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
    }

    //Metodo para abrir la pantalla para realizar una nueva venta
    public void abrirPantallaNuevaVenta(View vista) {
        Intent pantallaNuevaVenta = new Intent(getApplicationContext(), RegistrarNuevaVentaActivity.class);//Creando un Intent para invocar a Ventas Activity
        startActivity(pantallaNuevaVenta);//Iniciando la pantalla Ventas
    }
    //Metodo para volver al menu principal
    public void volverMenuPrincipal(View vista) {
        finish();//cerrando ventana de nueva venta
        //creando un objeto para manejar la ventana de ventas
        Intent ventanaMenu = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(ventanaMenu);//solicitando que se abra la ventana de gestion clientes

    }
}