package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VentasActivity extends AppCompatActivity {
    // Lista
    ListView lstVentas;
    ArrayList<String> listaVentas = new ArrayList<>();
    ArrayList<String> listaVentasOp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        // Mapear
        lstVentas = (ListView) findViewById(R.id.lstVentasRealizadas);
        obtenerVentas();
    }

    public void obtenerVentas() {
        listaVentas.clear();
        listaVentas.add("Prueba Titulo 1");
        listaVentasOp.add("Prueba Titulo 2");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaVentasOp);
        lstVentas.setAdapter(adapter);
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