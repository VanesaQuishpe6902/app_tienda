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
    //Metodo para volver al la ventana de facturacion
    public void volverVentanaTipoFacturacion(View vista) {
        finish();//cerrando ventana de nueva venta
        //creando un objeto para manejar la ventana de ventas
        Intent ventanaTipoFacturacion = new Intent(getApplicationContext(), MetodoVentaActivity.class);
        startActivity(ventanaTipoFacturacion);//solicitando que se abra la ventana de gestion clientes

    }
    //Metodo para abrir la pantalla de agregar productos a la factura
    public void abrirPantallaAgregarProductos(View vista)
    {
        Intent pantallaAgregarProductosFactura = new Intent(getApplicationContext(), AgregarProductoFactura.class);//Creando un Intent para invocar a Cliente Activity
        startActivity(pantallaAgregarProductosFactura); //Iniciando la pantalla Clientes
    }
}