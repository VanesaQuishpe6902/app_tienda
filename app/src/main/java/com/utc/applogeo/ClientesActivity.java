package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
/*
autor: Vanesa Quishpe
creado: 09/06/2021
modificado: 16/06/2021
descripcion: Actividad para gestionar los clientes dentro la base de datos SQLite
*/

public class ClientesActivity extends AppCompatActivity {
    //Entrada de datos
    EditText txtCedulaCliente, txtApellidoCliente, txtNombreCliente, txtTelefonoCliente, txtDireccionCliente;
    //Salida
    ListView lstCliente;
    BaseDatos bdd;
    ArrayList<String> listaCliente = new ArrayList<>();
    //declarar el cursor
    Cursor clientesObtenidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        //Mapeo de datos de elementos XML a objetos JAVA:
        txtCedulaCliente = (EditText) findViewById(R.id.txtCedulaCliente);
        txtApellidoCliente = (EditText) findViewById(R.id.txtApellidoClienteEditar);
        txtNombreCliente = (EditText) findViewById(R.id.txtNombreClienteEditar);
        txtTelefonoCliente = (EditText) findViewById(R.id.txtTelefonoClienteEditar);
        txtDireccionCliente = (EditText) findViewById(R.id.txtDireccionClienteEditar);
        lstCliente = (ListView) findViewById(R.id.lstClientes);
        bdd = new BaseDatos(getApplicationContext());
        //lamando al metodo para cargar  los datos clientes en el ListView
        consultarDatos();
        //generar acciones cuando se da clic sobre un elemento de la lista clientes--NUEVO
        lstCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Programacion de acciones cuando se da click en un elemento/item de la lista de clientes
                //Toast.makeText(getApplicationContext(), "Seleccionaste la posicion"+position,
                //Toast.LENGTH_LONG).show();
                clientesObtenidos.moveToPosition(position); //moviendo el cursor hacia la posicion donde se da clic
                String idCliente = clientesObtenidos.getString(0);//obteniendo la informacion de cada uno de los campos de la fila cliente seleecionada
                String cedulaCliente = clientesObtenidos.getString(1);
                String apellidoCliente = clientesObtenidos.getString(2);
                String nombreCliente = clientesObtenidos.getString(3);
                String telefonoCliente = clientesObtenidos.getString(4);
                String direccionCliente = clientesObtenidos.getString(5);
                //Generando el objeto para abrir la ventana de edicion/eliminacion del cliente
                //enviando parametros(id,cedula,apellido,nombre,telefonoo,direccion)
                Intent ventanaEditarCliente = new Intent(getApplicationContext(), EditarClienteActivity.class);
                ventanaEditarCliente.putExtra("id", idCliente);//pasando el id del cliente como parametro
                ventanaEditarCliente.putExtra("cedula", cedulaCliente);//pasando la cedula del cliente como parametro
                ventanaEditarCliente.putExtra("apellido", apellidoCliente);//pasando el apellido del cliente como parametro
                ventanaEditarCliente.putExtra("nombre", nombreCliente);//pasando el nombre del cliente como parametro
                ventanaEditarCliente.putExtra("telefono", telefonoCliente);//pasando el telefono del cliente como parametro
                ventanaEditarCliente.putExtra("direccion", direccionCliente);//pasando la direccion del cliente como parametro
                startActivity(ventanaEditarCliente);//solicitando que se abra la ventana de edicion o eliminacion del clientes
                finish();
                //Toast.makeText(getApplicationContext(), idCliente+"-"+cedulaCliente+"-"+nombreCliente+"-"+
                //      telefonoCliente+"-"+direccionCliente,Toast.LENGTH_LONG).show();


            }
        });
    }

    //PROCESO 1: metodo para vaciar cualquier datos que este en los campos del formulario registro cliente
    public void limpiarCampos(View vista) {
        txtCedulaCliente.setText("");
        txtApellidoCliente.setText("");
        txtNombreCliente.setText("");
        txtTelefonoCliente.setText("");
        txtDireccionCliente.setText("");
        txtCedulaCliente.requestFocus();
    }

    //PROCESO 2: metodo para guardar cliente
    public void guardarCliente(View vista) {
        String cedula = txtCedulaCliente.getText().toString();
        String apellido = txtApellidoCliente.getText().toString();
        String nombre = txtNombreCliente.getText().toString();
        String telefono = txtTelefonoCliente.getText().toString();
        String direccion = txtDireccionCliente.getText().toString();
        //Validar los datos
        if (!cedula.equals("") && !apellido.equals("") && !nombre.equals("") && !telefono.equals("") && !direccion.equals("")) {
            bdd.agregarCliente(cedula, apellido, nombre, telefono, direccion);
            limpiarCampos(null);
            Toast.makeText(getApplicationContext(), "Cliente registrado exitosamente", Toast.LENGTH_LONG).show();
            consultarDatos(); //Recargando la lista luego de la insercion
        } else {
            Toast.makeText(getApplicationContext(), "Para guardar complete los campos",
                    Toast.LENGTH_LONG).show();
        }
    }

    //PORCESO 3: metodo para Consultar los clientes existentes en SQLite y presentarlos en una lista

    public void consultarDatos() {
        listaCliente.clear();//Vaciando el listado de clientes
        clientesObtenidos = bdd.obtenerClientes(); //Consultando clientes y guardandolo en un cursor
        if (clientesObtenidos != null)//verificando que haya datos
        {
            //Proceso para cuando se encuentren clientes registrados
            do {
                String id = clientesObtenidos.getString(0).toString(); //capturando el string del cliente
                String apellido = clientesObtenidos.getString(2).toString(); //Capturando el apellido del cliente
                String nombre = clientesObtenidos.getString(3).toString(); //Capturando el nombre del cliente
                String direccion = clientesObtenidos.getString(5).toString(); //Capturando el nombre del cliente
                //construyendo las filas para presentar datos en el list view
                listaCliente.add(id + ": " + apellido + " " + nombre + " " + direccion);
                //Creando un adaptador para poder presentar los datos del listado de clientes (Java) en una lista simple XML
                ArrayAdapter<String> adaptadorCliente = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCliente);
                lstCliente.setAdapter(adaptadorCliente);//presentando el adaptador cliente dentro del list view
            } while (clientesObtenidos.moveToNext());//validando si aun existe clientes dentro del cursor
        } else {
            Toast.makeText((getApplicationContext()), "No existe clientes registrados", Toast.LENGTH_LONG).show();
        }
    }

    public void volver(View vista) {
        finish();
    }
}