package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
/*
 * Autores: Vanesa Quishpe, Angel Tapia, Alex Vaca
 * Creado: 20/06/2021
 * Editado: 20/06/2021
 * Descripción: Actividad para procesar el edicion o eliminacio de clientes en SQLite se reciben como parametros los datos del
producto seleccionado en el list view(ListadoProductos) de la ventana Gestion Productos
 *
 * */

public class EditarProductoActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private Button btnCaducidadProductoEditar;
    TextView txtIDProductoEditar;
    EditText txtNombreProductoEditar, txtPrecioProductoEditar, txtStockProductoEditar;
    CheckBox btnIvaProductoEditar;
    String idProd, nombreProd, fechaCadProd;
    Double precioProd;
    int ivaProd, stockProd;
    BaseDatos bdd;
    Cursor infoProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);
        //Mapeo
        txtNombreProductoEditar = (EditText) findViewById(R.id.txtNombreProductoEditar);
        txtPrecioProductoEditar = (EditText) findViewById(R.id.txtPrecioProductoEditar);
        txtStockProductoEditar = (EditText) findViewById(R.id.txtStockProductoEditar);

        btnIvaProductoEditar = (CheckBox) findViewById(R.id.btnIvaProductoEditar);

        btnCaducidadProductoEditar = (Button) findViewById(R.id.btnCaducidadProductoEditar);

        txtIDProductoEditar = (TextView) findViewById(R.id.txtIDProductoEditar);
        bdd = new BaseDatos(getApplicationContext());
        //Calendario
        initDatePicker();
        btnCaducidadProductoEditar.setText(getTodaysDate());
        //
        Bundle parametrosExtra = getIntent().getExtras();
        if (parametrosExtra != null) {
            try {
                //Intente realizar estas lineas de codigo
                idProd = parametrosExtra.getString("id_pro");
                txtIDProductoEditar.setText(idProd);
                int idProducto = Integer.parseInt(idProd);
                infoProducto = bdd.obtenerProductoId(idProducto);
                if (infoProducto != null) {
                    nombreProd = infoProducto.getString(1);
                    precioProd = Double.parseDouble(infoProducto.getString(2));
                    ivaProd = Integer.parseInt(infoProducto.getString(3));
                    stockProd = Integer.parseInt(infoProducto.getString(4));
//                    fechaCadProd = infoProducto.getString(5);
                    String date = infoProducto.getString(5);
                    String[] parts = date.split("-");
                    String sYear = parts[0];
                    String sMonth = parts[1];
                    String sDay = parts[2];
                    String auxDay[] = sDay.split(" ");
                    sDay = auxDay[0];
                    btnCaducidadProductoEditar.setText(Integer.parseInt(sDay) + "-" + Integer.parseInt(sMonth) + "-" + sYear);
                    txtNombreProductoEditar.setText(infoProducto.getString(1));
                    txtPrecioProductoEditar.setText(infoProducto.getString(2));
                    // IVA
                    String ivaStatus = infoProducto.getString(3);

                    if (ivaStatus.equals("1")) {
                        btnIvaProductoEditar.setChecked(true);
                    }
                    txtStockProductoEditar.setText(infoProducto.getString(4));
                }

                //Toast.makeText(this, "ID " + idPro, Toast.LENGTH_SHORT).show();

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud" + ex.toString(), Toast.LENGTH_LONG).show();

            }

        }
    }


    //Validacion para numero enteros
    private boolean isNumberInt(String word) {
        return Pattern.matches("^[0-9]+$", word);
    }

    //Validacion para numero decimal
    private boolean isNumberDec(String word) {
        return Pattern.matches("^[0-9.]+$", word);
    }

    //
    public void editarProducto(View vista) {
        String nombre = txtNombreProductoEditar.getText().toString();
        String precio = txtPrecioProductoEditar.getText().toString();
        String stock = txtStockProductoEditar.getText().toString();
        int cont = 0;
        //Validar los datos

        if (TextUtils.isEmpty(nombre)) {
            cont++;
            txtNombreProductoEditar.setError("Campo vacio no permitido");
            txtNombreProductoEditar.requestFocus();
        }
        if (TextUtils.isEmpty(precio) || !isNumberDec(precio)) {
            cont++;
            txtPrecioProductoEditar.setError("Formato no valido ejemplo 1,12");
            txtPrecioProductoEditar.requestFocus();
        } else if (Double.parseDouble(precio) < 0) {
            cont++;
            txtPrecioProductoEditar.setError("El precio debe ser mayor que 0");
            txtPrecioProductoEditar.requestFocus();
        }
        if (TextUtils.isEmpty(stock) || !isNumberInt(stock)) {
            cont++;
            txtStockProductoEditar.setError("Solo se admite valores enteros");
            txtStockProductoEditar.requestFocus();
        } else if (Integer.parseInt(stock) < 0) {
            cont++;
            txtStockProductoEditar.setError("El precio debe ser mayor que 0");
            txtStockProductoEditar.requestFocus();
        }
        if (cont == 0) {
            double precioP = Double.parseDouble(precio);
            int iva = 0;
            if (btnIvaProductoEditar.isChecked()) {
                iva = 1;
            }
            int stockP = Integer.parseInt(stock);
            String date = btnCaducidadProductoEditar.getText().toString();
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
            int idEditar = Integer.parseInt(idProd);
            bdd.editarProducto(idEditar, nombre, precioP, iva, stockP, fechaCaducidadP);
            Toast.makeText(getApplicationContext(), "Producto editado exitosamente", Toast.LENGTH_LONG).show();
            volver(null);
        }
    }

    public void eliminarProducto(View vista) {
        // Recuperar datos de la vista
        androidx.appcompat.app.AlertDialog.Builder estructuraConfirmacion = new androidx.appcompat.app.AlertDialog.Builder(this) // Instanciar una ventana tipo dialogo
                .setTitle("Eliminar Producto") // Establecer un titulo del dialogo
                .setMessage("Esta seguro de eliminar la información del producto?") // Establecer un mensaje en el dialogo
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() { // Establecer la funcion en caso de ser una accion positiva
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            bdd.eliminarProducto(idProd);
                            Toast.makeText(EditarProductoActivity.this, "Producto eliminado con éxito!", Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Toast.makeText(EditarProductoActivity.this, "Error: " + ex, Toast.LENGTH_SHORT).show();
                        }
                        volver(null);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() { // Establecer la accion en caso de cancelar
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Mostrar un mensaje de cancelacion al usuario
                        Toast.makeText(getApplicationContext(), "Se canceló la acción", Toast.LENGTH_SHORT).show();
                    }
                }).setCancelable(true);
        // Mostrar Cuadro de dialogo
        androidx.appcompat.app.AlertDialog cuadroDialogo = estructuraConfirmacion.create();
        cuadroDialogo.show();


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
                btnCaducidadProductoEditar.setText(date);
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

    public void volver(View vista) {
        finish();
        Intent volverMenuProducto = new Intent(getApplicationContext(), ProductosActivity.class);
        startActivity(volverMenuProducto);
    }

}