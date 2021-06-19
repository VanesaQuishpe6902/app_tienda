package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;





/*
autor: Vanesa Quishpe
creado: 02/01/2021
modificado: 02/01/2021
descripcion: Ventana de bienvenida que es visible por 4segundos
*/

public class Bienvenida extends AppCompatActivity {
    //Proceso 1: metodo que se ejecuta de manera automatica cuando inicia la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        //Recuperar el SharedPreferences
        //instancia de clase y objeto por medio del metodo, nombre del archivo de preferencia
        SharedPreferences prefs = getSharedPreferences("inicioSesion", Context.MODE_PRIVATE);
        //Recuperar los datos a traves de la clave
        //almacenar en un string y accedo al metodo getString y recuperar los datos de la clave estado sesion en caso de que no exista nada si esta vacio necesito mandar un default
        String recuperar = prefs.getString("estadoSesion", "");

        //Definir un Hilo: Simula un cronometro que ejecuta una accion en base a un tiempo determinado
        new Handler().postDelayed(new Runnable()
        {
           @Override
                   public void run (){

               //Dentro de la strucutra de control if se comprueba que la variable string recuperar no este vacia para acceder al menu activity
               Intent ventanaPrincipal;//solicitar la ventana creada_ Formulario de login
               if(!recuperar.isEmpty()){
                   ventanaPrincipal = new Intent(getApplicationContext(), MenuActivity.class);
               }

               else {
                   ventanaPrincipal = new Intent(getApplicationContext(), MainActivity.class);
               }
               startActivity(ventanaPrincipal); //solicitar la ventana creada_ Formulario de login
                //FINALIZAR EL SplashScreen
               finish();//Cerrando la ventana Activity de Bienvenida
            }

        }, 4000);

    }

}