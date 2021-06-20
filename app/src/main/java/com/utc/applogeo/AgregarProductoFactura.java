package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AgregarProductoFactura extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto_factura);
    }
    //Metodo para volver al la ventana de Facturacion
    public void volverFacturacion(View vista) {
        finish();//cerrando ventana de nueva venta
        //creando un objeto para manejar la ventana de ventas
        Intent ventanaFacturacion = new Intent(getApplicationContext(), FacturacionActivity.class);
        startActivity(ventanaFacturacion);//solicitando que se abra la ventana de gestion clientes

    }
}