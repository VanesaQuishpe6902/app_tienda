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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AgregarProductoFactura extends AppCompatActivity {
    String idCli, idVenta;
    Cursor infoCliente, productos;
    BaseDatos bdd;
    ListView lstSeleccionarProducto;
    TextView txtConfirmarDatos;
    EditText txtNumProductosVenta, txtBuscarProductoFacturacion;
    ArrayList<String> listaProductos = new ArrayList<>();
    // Producto
    String nombreProducto;
    int idProd = -1, unidadesExistentes = -1, unidadesVendidas;
    double precio, precioTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto_factura);
        //Mapeo
        bdd = new BaseDatos(getApplicationContext());
        txtConfirmarDatos = (TextView) findViewById(R.id.txtConfirmarDatos);
        txtNumProductosVenta = (EditText) findViewById(R.id.txtNumProductosVenta);
        txtBuscarProductoFacturacion = (EditText) findViewById(R.id.txtBuscarProductoFacturacion);
        // Inicializar en vacio txtConfirmarDatos
        txtConfirmarDatos.setText("");
        lstSeleccionarProducto = (ListView) findViewById(R.id.lstSeleccionarProducto);
        // Llenar la lista de productos
        obtenerProductos();
        // Recuperar datos
        Bundle parametrosExtra = getIntent().getExtras();
        if (parametrosExtra != null) {
            try {
                //Intente realizar estas lineas de codigo
                idCli = parametrosExtra.getString("id_cli");
                idVenta = parametrosExtra.getString("id_venta");
                infoCliente = bdd.consultarDatosClientes(Integer.parseInt(idCli));


            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud" + ex.toString(), Toast.LENGTH_LONG).show();

            }

        }
        // OnClic
        lstSeleccionarProducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                productos.moveToPosition(position);
                idProd = Integer.parseInt(productos.getString(0));
                nombreProducto = productos.getString(1);
                unidadesExistentes = Integer.parseInt(productos.getString(4));
                precio = Double.parseDouble(productos.getString(2));
                txtConfirmarDatos.setText(nombreProducto);

            }
        });
    }

    private void obtenerProductos() {
        // Limpiar lista
        listaProductos.clear();
        productos = bdd.obtenerProducto();
        if (productos != null) {
            do {
                String nombre = productos.getString(1).toString();
                String precio = productos.getString(2).toString();
                String cantidad = productos.getString(4).toString();
                listaProductos.add(nombre + "   " + cantidad + " unidades   " + precio);
                ArrayAdapter<String> adaptadorProducto = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaProductos);
                lstSeleccionarProducto.setAdapter(adaptadorProducto);
            } while (productos.moveToNext());
        }
    }

    //Metodo para volver al la ventana de Facturacion
    public void volverFacturacion(View vista) {
        finish();//cerrando ventana de nueva venta
        //creando un objeto para manejar la ventana de ventas
        Intent ventanaFacturacion = new Intent(getApplicationContext(), FacturacionActivity.class);
        ventanaFacturacion.putExtra("id_cli", idCli);
        ventanaFacturacion.putExtra("id_venta", idVenta);
        startActivity(ventanaFacturacion);//solicitando que se abra la ventana de gestion clientes

    }

    public void comprobarExistencias(View vista) {
        String unidades = txtNumProductosVenta.getText().toString();
        int errors = 0;
        // Validar que no este vacio
        if (unidades.equals("")) {
            errors++;
            Toast.makeText(this, "Debe ingresar una cantidad", Toast.LENGTH_SHORT).show();
            txtNumProductosVenta.requestFocus();
        }
        // Validar que haya seleccionado previamente un producto
        if (!unidades.equals("") && unidadesExistentes == -1) {
            errors++;
            Toast.makeText(this, "Primero escoge un producto", Toast.LENGTH_SHORT).show();
            txtNumProductosVenta.requestFocus();
        }
        // Validar que sea mayor a 0
        if (!unidades.equals("") && Integer.parseInt(unidades) <= 0) {
            errors++;
            Toast.makeText(this, "Cantidad iválida", Toast.LENGTH_SHORT).show();
            txtNumProductosVenta.requestFocus();
        }
        // Validar que sea menor a la cantidad de productos existentes
        if (!unidades.equals("") && Integer.parseInt(unidades) > unidadesExistentes && unidadesExistentes != -1) {
            errors++;
            Toast.makeText(this, "Debe ingresar una cantidad menor o igual a la de existencias", Toast.LENGTH_SHORT).show();
            txtNumProductosVenta.requestFocus();
        }
        if (errors == 0) {
            unidadesVendidas = Integer.parseInt(unidades);
            precioTotal = precio * unidadesVendidas;
            // Concatenar resultados
            txtConfirmarDatos.setText(nombreProducto + " x " + unidades + "uni. total: $" + precioTotal);
        }
    }

    public void confirmarRegistroProducto(View vista) {
        if (idProd != -1 && unidadesVendidas > 0) {
            Toast.makeText(this, "id_vent" + idVenta + "id_pro" + idProd + "cantidad" + unidadesVendidas, Toast.LENGTH_SHORT).show();
            try {
                bdd.registrarProductoDetalle(Integer.parseInt(idVenta), idProd, unidadesVendidas);
                txtConfirmarDatos.setText("");
                txtNumProductosVenta.setText("");
                //Opcional
                txtBuscarProductoFacturacion.setText("");
                // Mensaje de confirmacion
                Toast.makeText(this, "Agregado con éxito!", Toast.LENGTH_LONG).show();

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud: " + ex.toString(), Toast.LENGTH_LONG).show();

            }
        } else {
            //Toast.makeText(this, "Algo salio mal...", Toast.LENGTH_SHORT).show();
        }
    }
}