package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
/*
autor: Vanesa Quishpe
creado: 19/05/2021
modificado: 09/06/2021
descripcion: Activity  para acceder a los diferentes opciones de la aplicacion
*/

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    //PROCESO 2
    public void abrirPantallaCliente(View vista)
    {
        Intent pantallaClientes = new Intent(getApplicationContext(), ClientesActivity.class);//Creando un Intent para invocar a Cliente Activity
        startActivity(pantallaClientes); //Iniciando la pantalla Clientes
    }
    //PROCESO 3
    public void abrirPantallaProductos(View vista)
    {
        Intent pantallaProductos = new Intent(getApplicationContext(), ProductosActivity.class);//Creando un Intent para invocar a Productos Activity
        startActivity(pantallaProductos);//Iniciando la pantalla Productos
    }
    //PROCESO 4
    public void abrirPantallaVenta(View vista)
    {
        Intent pantallaVentas = new Intent(getApplicationContext(), VentasActivity.class);//Creando un Intent para invocar a Ventas Activity
        startActivity(pantallaVentas);//Iniciando la pantalla Ventas
    }
    //PROCESO 5
    public void cerrarSesion(View vista){
        //Borrar datos del SharedPreferences
        //INSTACIAR
        SharedPreferences prefs = getSharedPreferences("inicioSesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit(); //Accediendo al metodo de la clase y editando el objeto preferencias
        //Por medio del objeto editor se borra los datos en la clave de nombre estadosesion
        editor.putString("estadoSesion", "");
        editor.commit(); //Guardando el Shared
        //Llamar a la pantalla del login
        Intent pantallaRegresar = new Intent(getApplicationContext(), MainActivity.class);//Creando un Intent para invocar a Ventas Activity
        startActivity(pantallaRegresar);//Iniciando la pantalla Main
        finish();
    }
}