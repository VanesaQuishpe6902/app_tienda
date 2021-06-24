package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.regex.Pattern;

/*
autor: Vanesa Quishpe, Vaca Alex, Angel Tapia
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
        int errors = 0;
        if (apellido.isEmpty() || !isWord(apellido)) {
            errors++;
            txtApellidoRegistro.setError("Ingrese un apellido válido");
        }
        if (nombre.isEmpty() || !isWord(nombre)) {
            errors++;
            txtNombreRegistro.setError("Ingrese un nombre válido");
        }
        if (email.isEmpty() || !isValidEmail(email)) {
            errors++;
            txtEmailRegistro.setError("Ingrese un email válido");
        }
        if (password.isEmpty() || !isPassLetterNumber(password) || password.length() < 8) {
            errors++;
            txtPasswordRegistro.setError("La contraseña debe contener al menos 8 caracteres entre letras y numeros");
        }
        if (passwordConfirmada.isEmpty()) {
            txtPasswordConfirmada.setError("Debe confirmar la contraseña");
        }
        if (!password.equals(passwordConfirmada)) {
            errors++;
            txtPasswordConfirmada.setError("Las contraseñas no coinciden");
        }
        if (errors == 0) {
            miBDD.agregarUsuario(apellido, nombre, email, password);
            Toast.makeText(getApplicationContext(), "Usuario alamacenado exitosamente", Toast.LENGTH_LONG).show(); //Mostrando un msm de confirmacion
            this.cerrarPantallaRegistro(vista);
        } else {
            Toast.makeText(this, "Upss... No se pudo registar tu información", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isWord(String word) {
        return Pattern.matches(".*[ a-zA-Z-ñÑáéíóúÁÉÍÓÚ].*", word);
    }

    public boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
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
