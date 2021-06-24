package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
/*
autor: Vanesa Quishpe, Alex Vaca, Tapia Agel
creado: 21/06/2021
modificado: 21/06/2021
descripcion: Actividad para gestionar el metodo de venta de la factura
*/

public class MetodoVentaActivity extends AppCompatActivity {
    BaseDatos bdd;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodo_venta);
        bdd = new BaseDatos(getApplicationContext());

    }

    //Metodo para volver al la ventana de ventas
    public void volverVenta(View vista) {
        finish();//cerrando ventana de nueva venta
        //creando un objeto para manejar la ventana de ventas
        Intent ventanaVentas = new Intent(getApplicationContext(), VentasActivity.class);
        startActivity(ventanaVentas);//solicitando que se abra la ventana de gestion clientes

    }

    //Mtodo para abrir la pantalla de Agregar cliente
    public void abrirPantallaAgregarCliente(View vista) {
        Intent pantallaAgregarCliente = new Intent(getApplicationContext(), AgregarClienteFactura.class);//Creando un Intent para invocar a Cliente Activity
        startActivity(pantallaAgregarCliente); //Iniciando la pantalla Clientes
    }

    //Mtodo para abrir la pantalla de Facturacion
    public void abrirPantallaFacturacion(View vista) {
        //Agregar una nueva venta al consumidor final
        //Obturar fecha del sistema
        String fechaRegistro = simpleDateFormat.format(new Date());
        //Guardar en la Bdd
        try {
            bdd.registrarVenta(fechaRegistro, "1");
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error: " + ex.toString(), Toast.LENGTH_SHORT).show();
        }
        Intent pantallaFacturacion = new Intent(getApplicationContext(), FacturacionActivity.class);//Creando un Intent para invocar a Cliente Activity
        pantallaFacturacion.putExtra("id_cli", "1");
        // Obtener id de la venta
        Cursor venta = bdd.buscarVentasFecha(fechaRegistro);
        String id_vent = venta.getString(0);
        pantallaFacturacion.putExtra("id_venta", id_vent);

        startActivity(pantallaFacturacion); //Iniciando la pantalla Clientes
    }

}