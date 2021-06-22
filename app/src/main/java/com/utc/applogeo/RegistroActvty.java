package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.regex.Pattern;

/*
autor: Vanesa Quishpe
creado: 12/05/2021
modificado: 13/05/2021
descripcion: Pantalla para regstrar usuarios y almacenar en una base de datos sqlite
*/
public class RegistroActvty extends AppCompatActivity {
    //ENTRADA
    EditText txtApellidoRegistro, txtNombreRegistro, txtEmailRegistro, txtPasswordRegistro, txtPasswordConfirmada; //Definiendo objetos  para capturar datos de la vista
    BaseDatos miBDD; //Creando un objeto para acceder a los procesos de la base de datos


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_actvty);
        //mapeo de elementos
        txtApellidoRegistro = (EditText) findViewById(R.id.txtApellidoRegistro);
        txtNombreRegistro = (EditText) findViewById(R.id.txtNombreRegistro);
        txtEmailRegistro = (EditText) findViewById(R.id.txtEmailRegistro);
        txtPasswordRegistro = (EditText) findViewById(R.id.txtPasswordRegistro);
        txtPasswordConfirmada = (EditText) findViewById(R.id.txtPasswordConfirmada);
        miBDD = new BaseDatos(getApplicationContext()); //Instanciar/construirr la base de datos en el objeto miBDD
    }

    //PROCES 2
    public void cerrarPantallaRegistro(View vsta) {
        finish(); //Cerrarndola pantalla de registro
    }

    //PROCESO 3: Registrar usuario
    public void registrarUsuario(View vista) {
        String apellido = txtApellidoRegistro.getText().toString();
        String nombre = txtNombreRegistro.getText().toString();
        String email = txtEmailRegistro.getText().toString();
        String password = txtPasswordRegistro.getText().toString();
        String passwordConfirmada = txtPasswordConfirmada.getText().toString();
        if (password.equals(passwordConfirmada)) //valida que las contraseñas sean iguales
        {//cuando la condicion es vdd se realiza el proceso de insercion
            miBDD.agregarUsuario(apellido, nombre, email, password);
            Toast.makeText(getApplicationContext(), "Usuario alamacenado exitosamente", Toast.LENGTH_LONG).show(); //Mostrando un msm de confirmacion
        } else {//cuando la condicion es falsase envia un msm de errror
            Toast.makeText(getApplicationContext(), "La contraseña ingresasda no coincide", Toast.LENGTH_LONG).show();//MUESTRA EL MSM DE ERROR
        }
        //Cerrar la pantalla de registro y regresar a la pantalla de inicio
        this.cerrarPantallaRegistro(vista);

    }

    private boolean isWord(String word) {
        return Pattern.matches(".*[ a-zA-Z-ñÑáéíóúÁÉÍÓÚ].*", word);
    }

    // Validar que solo contenga texto y espacios
    private boolean isEmailInst(String correo) {
        return Pattern.matches("^[\\w-]+(\\.[\\w-]+)*[@a-zA-Z]+.*", correo);
    }

    // Validar que solo contenga texto y espacios
    private boolean isNumberPhone(String number) {
        return Pattern.matches("^09.*[0-9]$", number);
    }

    // validar que la contraseña tenga letras y numeros
    private boolean isPassLetterNumber(String pass) {
        return Pattern.matches(".*[a-zA-Z]+.*", pass) && Pattern.matches(".*[0-9]+.*", pass);

    }

}
