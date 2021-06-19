package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/*
autor: Vanesa Quishpe
creado: 12/05/2021
modificado: 27/05/2021
descripcion: Ventana de inico de seson y acceso a las pantallas de Regstro y recuperacion de contraseña
*/
public class MainActivity extends AppCompatActivity {
    //Entrada
    EditText txtEmailLogin, txtPasswordLogin;
    BaseDatos bdd;
    RadioButton estado; //Declaramos la variable del radiobutton

    //PROCESO 1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Mapeo de elementos XML a objetos Java
        txtEmailLogin = (EditText) findViewById(R.id.txteEmailLogin);
        txtPasswordLogin = (EditText) findViewById(R.id.txtPasswordLogin);
        bdd=new BaseDatos(getApplication()); //Instanciando una base de datos dentro del objeto bdd
        estado = (RadioButton) findViewById(R.id.estadoSesion);
    }
    //PROCESO 2
    public void abrirPantallaRegistro(View vista)
    {
        Intent pantallaRegistro = new Intent(getApplicationContext(), RegistroActvty.class);//Creando un Intent para invocar a RegistroActivity
        startActivity(pantallaRegistro);//niciando la pantalla registro
    }



    public void iniciarSesion(View vista) {
        //logica de negocio para capturar los valores ingresados en el campo Email y password y consultarlos
        //en la base de datos
        String email = txtEmailLogin.getText().toString(); //campturando el valor del email ingresado
        String password = txtPasswordLogin.getText().toString();//campturando el valor del password ingresado
        //Consultando el usuario en la base de datos
        Cursor usuarioEncontrado = bdd.obtenerUsuarioporEmailPassword(email,password);
        if (usuarioEncontrado!=null)
        {
            //Para el caso de que el email y contraseña ingresados sean correctos
            //Obteniendo el valor del email almacenado en la base de datos
            String emailbdd = usuarioEncontrado.getString(3).toString();
            //Obteniendo el valor del password almacenado en la base de datos
            String nombrebdd  = usuarioEncontrado.getString(2).toString();
            //Obteniendo el valor del apellido almacenado en la base de datos
            String apellidobdd  = usuarioEncontrado.getString(1).toString();

            ///Dentro de la estructura de control if comprueba el estado del boton, si el radioButton esta activado realiza lo siguiente
            if (estado.isChecked())
            {
                //GUARDAR EN UN SHARED PREFERENCES
                //instancia de clase y objeto por medio del metodo, nombre del archivo de preferencia
                SharedPreferences prefs = getSharedPreferences("inicioSesion", Context.MODE_PRIVATE);
                //A traves del la clase SharedPreferences y del metodo de clase editor se instancia un objeto editor donde se editan las preferencias
                SharedPreferences.Editor editor = prefs.edit();
                //A traves del objeto editor establece la clave llamada estado sesion con un valor de 1
                editor.putString("estadoSesion", "1");
                editor.commit(); //Guardando el SharedPreferences
            }
            //Mostrando un mensaje de bienvenida
            Toast.makeText(getApplicationContext(), "Bienvenido"+nombrebdd, Toast.LENGTH_LONG).show();
            finish();
            //Creando un objeto para manejar la ventana activity del menu
            Intent ventanaMenu = new Intent(getApplicationContext(),MenuActivity.class);
            startActivity (ventanaMenu); //Abriendo la ventana / activity del menu de opciones
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Email o contraseña incorrectos", Toast.LENGTH_LONG).show();
            txtPasswordLogin.setText("");//Limpiando el campo de la contraseña
        }
    }

}