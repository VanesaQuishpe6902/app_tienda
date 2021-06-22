package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
/*
autor: Vanesa Quishpe
creado: 14/06/2021
modificado: 14/06/2021
descripcion: Actividad para gestionar los productos dentro la base de datos SQLite
*/

public class ProductosActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    Cursor productosObtenidos;

    //ENTRADA DE DATOS
    EditText txtNombreProducto, txtPrecioProducto, txtStockProducto;
    CheckBox btnIvaProducto;
    //SALIDA
    ListView lstProducto;
    BaseDatos bdd;
    ArrayList<String> listaProducto = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        //MAPEO DE ELEMENTOS
        txtNombreProducto = (EditText) findViewById(R.id.txtNombreProducto);
        txtPrecioProducto = (EditText) findViewById(R.id.txtPrecioProducto);
        btnIvaProducto = (CheckBox) findViewById(R.id.btnIvaProducto);
        txtStockProducto = (EditText) findViewById(R.id.txtStockProducto);
        lstProducto = (ListView) findViewById(R.id.lstProducto);
        //Calendario
        initDatePicker();
        dateButton = findViewById(R.id.btnCaducidadProducto);
        dateButton.setText(getTodaysDate());
        bdd = new BaseDatos(getApplicationContext());
        //llamando al metodo para cargar  los datos productos en el ListView
        consultarDatosProducto();
        // Funcion onClic
        lstProducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                productosObtenidos.moveToPosition(position);
                String idProd = productosObtenidos.getString(0);
                //Toast.makeText(ProductosActivity.this, "ID" + idProd, Toast.LENGTH_SHORT).show();
                finish();
                //
                Intent detalleProducto = new Intent(getApplicationContext(), EditarProductoActivity.class);
                //
                detalleProducto.putExtra("id_pro", idProd);
                //
                startActivity(detalleProducto);
            }
        });

    }

    //Funcion: Obtener la fecha de hoy
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    //Funcion: Selector de fecha de inicio
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
    }

    //Hacer de la fecha una cadena y presentarla
    private String makeDateString(int day, int month, int year) {
        return day + "-" + month + "-" + year;
    }


    //Metodo: seleccionar fecha de caducidad del producto

    public void OpenDatePicker(View vista) {
        datePickerDialog.show();

    }


    //Metodo: Limpiar los campos del formulario de registro del producto
    public void limpiarCamposProducto(View vista) {
        txtNombreProducto.setText("");
        txtPrecioProducto.setText("");
        txtStockProducto.setText("");
        txtNombreProducto.requestFocus();
        btnIvaProducto.setChecked(false);
        dateButton.setText(getTodaysDate());
    }

    //Validacion para numero enteros
    private boolean isNumberInt(String word) {
        return Pattern.matches("^[0-9]+$", word);
    }

    //Validacion para numero decimal
    private boolean isNumberDec(String word) {
        return Pattern.matches("^[0-9.]+$", word);
    }

    //PROCESO 2: metodo para guardar producto
    public void guardarProducto(View vista) {
        String nombre = txtNombreProducto.getText().toString();
        String precio = txtPrecioProducto.getText().toString();
        String stock = txtStockProducto.getText().toString();
        int cont = 0;
        //Validar los datos

        if (TextUtils.isEmpty(nombre)) {
            cont++;
            txtNombreProducto.setError("Campo vacio no permitido");
            txtNombreProducto.requestFocus();
        }
        if (TextUtils.isEmpty(precio) || !isNumberDec(precio)) {
            cont++;
            txtPrecioProducto.setError("Formato no valido ejemplo 1,12");
            txtPrecioProducto.requestFocus();
        } else if (Double.parseDouble(precio) < 0) {
            cont++;
            txtPrecioProducto.setError("El precio debe ser mayor que 0");
            txtPrecioProducto.requestFocus();
        }
        if (TextUtils.isEmpty(stock) || !isNumberInt(stock)) {
            cont++;
            txtStockProducto.setError("Solo se admite valores enteros");
            txtStockProducto.requestFocus();
        } else if (Integer.parseInt(stock) < 0) {
            cont++;
            txtStockProducto.setError("El precio debe ser mayor que 0");
            txtStockProducto.requestFocus();
        }
        if (cont == 0) {
            double precioP = Double.parseDouble(precio);
            int iva = 0;
            if (btnIvaProducto.isChecked()) {
                iva = 1;
            }
            int stockP = Integer.parseInt(stock);
            String date = dateButton.getText().toString();
            String[] parts = date.split("-");
            String sDay = parts[0];
            String sMonth = parts[1];
            String sYear = parts[2];
            int iYear = Integer.parseInt(sYear) - 1900;
            int iMonth = Integer.parseInt(sMonth) - 1;
            int iDay = Integer.parseInt(sDay);
            Date fecha = new Date(iYear, iMonth, iDay);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fechaCaducidadP = simpleDateFormat.format(fecha);
            //GAURDAR EN LA BDD
            bdd.agregarProducto(nombre, precioP, iva, stockP, fechaCaducidadP);
            Toast.makeText(getApplicationContext(), "Producto registrado exitosamente", Toast.LENGTH_LONG).show();
            limpiarCamposProducto(null);
            consultarDatosProducto(); //Recargando la lista luego de la insercion
        }
    }
    //PORCESO 3: metodo para Consultar los producctos existentes en SQLite y presentarlos en una lista

    public void consultarDatosProducto() {
        listaProducto.clear();//Vaciando el listado de productos
        productosObtenidos = bdd.obtenerProducto(); //Consultando producto y guardandolo en un cursor
        if (productosObtenidos != null)//verificando que haya datos
        {
            //Proceso para cuando se encuentren productos registrados
            do {
                String id = productosObtenidos.getString(0).toString(); //capturando el string del cliente
                String nombre = productosObtenidos.getString(1).toString(); //Capturando el nombre del producto
                String precio = productosObtenidos.getString(2).toString(); //Capturando el precio del producto
                //construyendo las filas para presentar datos en el list view
                listaProducto.add(id + ": " + nombre + " " + precio);
                //Creando un adaptador para poder presentar los datos del listado de productos (Java) en una lista simple XML
                ArrayAdapter<String> adaptadorProducto = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaProducto);
                lstProducto.setAdapter(adaptadorProducto);//presentando el adaptador producto dentro del list view
            } while (productosObtenidos.moveToNext());//validando si aun existe productos dentro del cursor
        } else {
            Toast.makeText((getApplicationContext()), "No se ha registrado algun producto", Toast.LENGTH_LONG).show();
        }
    }

    public void volver(View vista) {
        finish();
    }
}
