package com.utc.applogeo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/*
autor: Vanesa Quishpe,Vaca Alex, Angel Tapia
creado: 16/06/2021
modificado: 17/06/2021
descripcion: Actividad para procesar el edicion o eliminacio de clientes en SQLite se reciben como parametros los datos del
cliente seleccionado en el list view(ListadoClientes) de la ventana Gestion clentes
 */


public class EditarClienteActivity extends AppCompatActivity {
    //Definicion de variables
    String id, cedula, apellido, nombre, telefono, direccion; //Variables para capturar los valores que viene como parametros
    //Definicion de los objetos que vienen de XML
    TextView txtIdClienteEditar;
    EditText txtCedulaClienteEditar, txtApellidoClienteEditar, txtNombreClienteEditar, txtTelefonoClienteEditar, txtDireccionClienteEditar;
    BaseDatos bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cliente);
        //MAPEO DE ELEMENTOS XML A OBJETOS JAVA
        txtIdClienteEditar = (TextView) findViewById(R.id.txtIdClienteEditar);
        txtCedulaClienteEditar = (EditText) findViewById(R.id.txtCedulaClienteEditar);
        txtApellidoClienteEditar = (EditText) findViewById(R.id.txtApellidoClienteEditar);
        txtNombreClienteEditar = (EditText) findViewById(R.id.txtNombreClienteEditar);
        txtTelefonoClienteEditar = (EditText) findViewById(R.id.txtTelefonoClienteEditar);
        txtDireccionClienteEditar = (EditText) findViewById(R.id.txtDireccionClienteEditar);
        Bundle parametrosExtra = getIntent().getExtras(); //Capturando los parametros que se han pasado ha esta actividad
        if (parametrosExtra != null) {
            try {
                //Intente realizar estas lineas de codigo
                id = parametrosExtra.getString("id");
                cedula = parametrosExtra.getString("cedula");
                apellido = parametrosExtra.getString("apellido");
                nombre = parametrosExtra.getString("nombre");
                telefono = parametrosExtra.getString("telefono");
                direccion = parametrosExtra.getString("direccion");

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud" + ex.toString(), Toast.LENGTH_LONG).show();

            }
        }
        txtIdClienteEditar.setText(id);
        txtCedulaClienteEditar.setText(cedula);
        txtApellidoClienteEditar.setText(apellido);
        txtNombreClienteEditar.setText(nombre);
        txtTelefonoClienteEditar.setText(telefono);
        txtDireccionClienteEditar.setText(direccion);

        bdd = new BaseDatos(getApplicationContext()); //instanciando el objeto a traves del cual se llaman los procesos de la bdd

    }

    //Metodo para volver al la ventana de gestion de clientess
    public void volver(View vista) {
        finish();//cerrando ventana editar/eliminarcliente
        //creando un obj para manejar la ventana gestion clientes
        Intent ventanaGestionCliente = new Intent(getApplicationContext(), ClientesActivity.class);
        startActivity(ventanaGestionCliente);//solicitando que se abra la ventana de gestion clientes

    }

    public void eliminarCliente(View vista) {
        // Recuperar datos de la vista
        AlertDialog.Builder estructuraConfirmacion = new AlertDialog.Builder(this)
                .setTitle("Eliminar cliente")
                .setMessage("Esta seguro de eliminar la información del cliente?")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bdd.eliminarCliente(id);
                        Toast.makeText(getApplicationContext(), "Eliminado con éxito!", Toast.LENGTH_LONG).show();
                        volver(null);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Se canceló la acción", Toast.LENGTH_SHORT).show();
                    }
                }).setCancelable(true);
        // Mostrar Cuadro de dialogo
        AlertDialog cuadroDialogo = estructuraConfirmacion.create();
        cuadroDialogo.show();
    }
    //metodo para actualizar el registro del cliente
    public void actualizarCliente(View vista) {
        //capturando los valores nuevos ingresadps por el usuario
        String cedula = txtCedulaClienteEditar.getText().toString();
        String apellido = txtApellidoClienteEditar.getText().toString();
        String nombre = txtNombreClienteEditar.getText().toString();
        String telefono= txtTelefonoClienteEditar.getText().toString();
        String direccion = txtDireccionClienteEditar.getText().toString();
        if(!cedula.equals("") && !apellido.equals("") && !nombre.equals("") && !telefono.equals("") && !direccion.equals(""))
        {
            bdd.acualizarCliente(cedula,apellido,nombre,telefono,direccion,id); //procesando la actualizacion en la bdd
            //mostrando el mensaje de actualizacion exitosa
            Toast.makeText(getApplicationContext(),"Actualizacion exitosa",Toast.LENGTH_SHORT).show();
            volver(null);//invocando al metodo volver
        }
        else
            {
                Toast.makeText(getApplicationContext(), "Complete todos los campos",Toast.LENGTH_LONG).show();
            }
    }
}
