package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VentasActivity extends AppCompatActivity {
    // Lista
    ListView lstVentas;
    ArrayList<String> listaVentas = new ArrayList<>();
    Cursor ventas;
    BaseDatos bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        // Mapear
        bdd = new BaseDatos(getApplicationContext());
        lstVentas = (ListView) findViewById(R.id.lstVentasRealizadas);
        obtenerVentas();
    }

    public void obtenerVentas() {
        listaVentas.clear();
        ventas = bdd.listarVentas();
        if (ventas != null) {
            do {
                String id = ventas.getString(0).toString();
                String dato1 = ventas.getString(1).toString();
                String dato2 = ventas.getString(2).toString();
                listaVentas.add("ID: " + id + " " + dato1 + " " + dato2);
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaVentas);
                lstVentas.setAdapter(adapter);
            } while (ventas.moveToNext());

        } else {
            Toast.makeText(getApplicationContext(), "Sin datos", Toast.LENGTH_LONG).show();
        }

    }

    //Metodo para abrir la pantalla para realizar una nueva venta
    public void abrirPantallaNuevaVenta(View vista) {
        Intent pantallaNuevaVenta = new Intent(getApplicationContext(), MetodoVentaActivity.class);//Creando un Intent para invocar a Ventas Activity
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