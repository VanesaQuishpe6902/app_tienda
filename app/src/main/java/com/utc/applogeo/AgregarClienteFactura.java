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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AgregarClienteFactura extends AppCompatActivity {
    BaseDatos bdd;
    ListView lstSeleccionarCliente;
    ArrayList<String> listaCliente = new ArrayList<>();
    Cursor clientes;
    EditText txtBuscador;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cliente_factura);
        bdd = new BaseDatos(getApplicationContext());
        lstSeleccionarCliente = (ListView) findViewById(R.id.lstSeleccionarCliente);
        txtBuscador = (EditText) findViewById(R.id.txtBuscador);
        obtenerCliente();
        // Generar acciones cuando se hace clic sobre una lista
        lstSeleccionarCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Mover el cursor al item seleccionado
                clientes.moveToPosition(position);
                String idCliente = clientes.getString(0).toString();
                // Generar nueva venta a partir de este cliente
                //Agregar una nueva venta al consumidor final
                //Obturar fecha del sistema
                String fechaRegistro = simpleDateFormat.format(new Date());
                //Guardar en la Bdd
                try {
                    bdd.registrarVenta(fechaRegistro, idCliente);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Error: " + ex.toString(), Toast.LENGTH_SHORT).show();
                }
                Intent pantallaFacturacion = new Intent(getApplicationContext(), FacturacionActivity.class);//Creando un Intent para invocar a Cliente Activity
                pantallaFacturacion.putExtra("id_cli", idCliente);
                // Obtener id de la venta
                Cursor venta = bdd.buscarVentasFecha(fechaRegistro);
                String id_vent = venta.getString(0);
                pantallaFacturacion.putExtra("id_venta", id_vent);
                startActivity(pantallaFacturacion); //Iniciando la pantalla Clientes
            }
        });
    }

    private void obtenerCliente() {
        listaCliente.clear();//Vaciando el listado de clientes
        clientes = bdd.obtenerClientes(); //Consultando clientes y guardandolo en un cursor
        if (clientes != null)//verificando que haya datos
        {
            //Proceso para cuando se encuentren clientes registrados
            do {
                String id = clientes.getString(0).toString(); //capturando el string del cliente
                String ci = clientes.getString(1).toString(); //capturando el string del cliente
                String apellido = clientes.getString(2).toString(); //Capturando el apellido del cliente
                String nombre = clientes.getString(3).toString(); //Capturando el nombre del cliente
                String direccion = clientes.getString(5).toString(); //Capturando el nombre del cliente
                //construyendo las filas para presentar datos en el list view
                listaCliente.add(ci + " -- " + nombre + " " + apellido);
                //Creando un adaptador para poder presentar los datos del listado de clientes (Java) en una lista simple XML
                ArrayAdapter<String> adaptadorCliente = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCliente);
                lstSeleccionarCliente.setAdapter(adaptadorCliente);//presentando el adaptador cliente dentro del list view
            } while (clientes.moveToNext());//validando si aun existe clientes dentro del cursor
        } else {
            Toast.makeText((getApplicationContext()), "No existe clientes registrados", Toast.LENGTH_LONG).show();
        }
    }

    //Metodo para volver al la ventana de Seleccionar el metodo de venta
    public void volverNuevaVenta(View vista) {
        finish();//cerrando ventana de nueva venta
        //creando un objeto para manejar la ventana de ventas
        Intent ventanaNuevaVenta = new Intent(getApplicationContext(), MetodoVentaActivity.class);
        startActivity(ventanaNuevaVenta);//solicitando que se abra la ventana de gestion clientes

    }

    public void consultarCliente(View vista) {
        String datos = txtBuscador.getText().toString();
        if (!datos.equals("") && datos.length() == 10) {
            listaCliente.clear();//Vaciando el listado de clientes
            clientes = bdd.consultarDatosClientesPorNombre(datos); //Consultando clientes y guardandolo en un cursor
            if (clientes != null)//verificando que haya datos
            {
                //Proceso para cuando se encuentren clientes registrados
                do {
                    String id = clientes.getString(0).toString(); //capturando el string del cliente
                    String ci = clientes.getString(1).toString(); //capturando el string del cliente
                    String apellido = clientes.getString(2).toString(); //Capturando el apellido del cliente
                    String nombre = clientes.getString(3).toString(); //Capturando el nombre del cliente
                    String direccion = clientes.getString(5).toString(); //Capturando el nombre del cliente
                    //construyendo las filas para presentar datos en el list view
                    listaCliente.add(ci + " -- " + nombre + " " + apellido);
                    //Creando un adaptador para poder presentar los datos del listado de clientes (Java) en una lista simple XML
                    ArrayAdapter<String> adaptadorCliente = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCliente);
                    lstSeleccionarCliente.setAdapter(adaptadorCliente);//presentando el adaptador cliente dentro del list view
                } while (clientes.moveToNext());//validando si aun existe clientes dentro del cursor
            } else {
                Toast.makeText((getApplicationContext()), "No existe clientes registrados", Toast.LENGTH_LONG).show();
            }

        } else {
            txtBuscador.setError("Ingrese un numero de cédula válido");
            txtBuscador.requestFocus();
        }
    }
}
