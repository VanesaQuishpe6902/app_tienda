package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/*
autor: Vanesa Quishpe, Alex Vaca, Tapia Agel
creado: 20/06/2021
modificado: 20/06/2021
descripcion: Actividad para agregar los productos dentro la factura trayendo datos desde la base de datos
*/


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
        lstVentas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ventas.moveToPosition(position);
                //Toast.makeText(getApplicationContext(), "hola" + ventas.getString(0) + ventas.getString(6), Toast.LENGTH_SHORT).show();
                finish();
                Intent verVenta = new Intent(getApplicationContext(), FacturacionActivity.class);
                verVenta.putExtra("id_venta", ventas.getString(0));
                verVenta.putExtra("id_cli", ventas.getString(6));
                startActivity(verVenta);//Iniciando la pantalla Ventas
            }
        });
    }

    public void obtenerVentas() {
        listaVentas.clear();
        ventas = bdd.listarVentas();
        if (ventas != null) {
            do {
                String fecha = ventas.getString(1).toString();
                String nombreCliente = ventas.getString(9) + " " + ventas.getString(10);
                /*for (int i = 0; i < ventas.getColumnCount(); i++) {
                    System.out.println("COL: " + i + " Datos: " + ventas.getColumnName(i));
                }*/
                listaVentas.add(nombreCliente + " | " + fecha);
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
    }
}