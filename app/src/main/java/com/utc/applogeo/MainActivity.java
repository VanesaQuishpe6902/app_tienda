package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
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
    CheckBox estado; //Declaramos la variable del radiobutton

    //PROCESO 1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Mapeo de elementos XML a objetos Java
        txtEmailLogin = (EditText) findViewById(R.id.txteEmailLogin);
        txtPasswordLogin = (EditText) findViewById(R.id.txtPasswordLogin);
        bdd = new BaseDatos(getApplication()); //Instanciando una base de datos dentro del objeto bdd
        estado = (CheckBox) findViewById(R.id.estadoSesion);
    }

    //PROCESO 2
    public void abrirPantallaRegistro(View vista) {
        Intent pantallaRegistro = new Intent(getApplicationContext(), RegistroActvty.class);//Creando un Intent para invocar a RegistroActivity
        startActivity(pantallaRegistro);//niciando la pantalla registro
    }

    public boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public void iniciarSesion(View vista) {
        //logica de negocio para capturar los valores ingresados en el campo Email y password y consultarlos
        //en la base de datos
        String email = txtEmailLogin.getText().toString(); //campturando el valor del email ingresado
        String password = txtPasswordLogin.getText().toString();//campturando el valor del password ingresado
        int error = 0;
        if (email.isEmpty() || password.isEmpty()) {
            error++;
            txtEmailLogin.setError("Ingrese su correo electronico");
            txtEmailLogin.requestFocus();
        }
        if (!isValidEmail(email)) {
            error++;
            txtEmailLogin.setError("Ingrese susu correo electronico");
            txtEmailLogin.requestFocus();
        }
        if (error == 0) {
            //Consultando el usuario en la base de datos
            Cursor usuarioEncontrado = bdd.obtenerUsuarioporEmailPassword(email, password);
            if (usuarioEncontrado != null) {
                String nombrebdd = usuarioEncontrado.getString(2).toString();
                ///Dentro de la estructura de control if comprueba el estado del boton, si el radioButton esta activado realiza lo siguiente
                if (estado.isChecked()) {
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
                Toast.makeText(getApplicationContext(), "Bienvenido" + nombrebdd, Toast.LENGTH_LONG).show();
                finish();
                //Creando un objeto para manejar la ventana activity del menu
                Intent ventanaMenu = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(ventanaMenu); //Abriendo la ventana / activity del menu de opciones
            } else {
                Toast.makeText(getApplicationContext(), "Email o contraseña incorrectos", Toast.LENGTH_LONG).show();
                txtPasswordLogin.setText("");//Limpiando el campo de la contraseña
            }
        }

    }
}


