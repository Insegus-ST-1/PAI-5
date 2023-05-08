package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLContext;


public class MainActivity extends AppCompatActivity {

    // Setup Server information
    protected static String server = "192.168.1.133";
    protected static int port = 7070;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Capturamos el boton de Enviar
        View button = findViewById(R.id.button_send);

        // Llama al listener del boton Enviar
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


    }

    // Creación de un cuadro de dialogo para confirmar pedido
    private void showDialog() throws Resources.NotFoundException {
        //Checkboxes
        final CheckBox sabanas = (CheckBox) findViewById(R.id.checkBox_sabanas);
        final CheckBox camas = (CheckBox) findViewById(R.id.checkBox_camas);
        final CheckBox mesas = (CheckBox) findViewById(R.id.checkBox_mesas);
        final CheckBox sillas = (CheckBox) findViewById(R.id.checkBox_sillas);
        final CheckBox sillones = (CheckBox) findViewById(R.id.checkBox_sillones);

        //Text
        final EditText sabanas_num = (EditText) findViewById(R.id.number_sabanas);
        final EditText camas_num = (EditText) findViewById(R.id.number_camas);
        final EditText mesas_num = (EditText) findViewById(R.id.number_mesas);
        final EditText sillas_num = (EditText) findViewById(R.id.number_sillas);
        final EditText sillones_num = (EditText) findViewById(R.id.number_sillones);

        final int sabanas_int = Integer.parseInt(sabanas_num.getText().toString());
        final int camas_int = Integer.parseInt(camas_num.getText().toString());
        final int mesas_int = Integer.parseInt(mesas_num.getText().toString());
        final int sillas_int = Integer.parseInt(sillas_num.getText().toString());
        final int sillones_int = Integer.parseInt(sillones_num.getText().toString());

        final String[] spinner_customers = {"Customer 1","Customer 2", "Customer 3", "Customer 4", "Customer 5"};
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,spinner_customers);
        spinner.setAdapter(adapter);

        sabanas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sabanas_num.setEnabled(true);
                }else{
                    sabanas_num.setEnabled(false);
                }
            }
        });

        camas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    camas_num.setEnabled(true);
                }else{
                    camas_num.setEnabled(false);
                }
            }
        });

        mesas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mesas_num.setEnabled(true);
                }else{
                    mesas_num.setEnabled(false);
                }
            }
        });

        sillas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sillas_num.setEnabled(true);
                }else{
                    sillas_num.setEnabled(false);
                }
            }
        });

        sillones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sillones_num.setEnabled(true);
                }else{
                    sillones_num.setEnabled(false);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        if (!sabanas.isChecked() && !camas.isChecked() && !mesas.isChecked() && !sillas.isChecked() && !sillones.isChecked()) {
            // Mostramos un mensaje emergente;
            Toast.makeText(getApplicationContext(), "Selecciona al menos un elemento", Toast.LENGTH_SHORT).show();
        } else if(sabanas_int<0 || sabanas_int>300 || camas_int<0 || camas_int>300 || mesas_int<0 || mesas_int>300 || sillas_int<0 || sillas_int>300 || sillones_int<0 || sillones_int>300){
            Toast.makeText(getApplicationContext(), "Selecciona una cantidad entre 0 y 300", Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Enviar")
                    .setMessage("Se va a proceder al envio")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                // Catch ok button and send information
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    // 1. Extraer los datos de la vista

                                    String message = String.format("Pedido enviado por %s:\n",spinner.getSelectedItem().toString());

                                    if(sabanas.isChecked() && sabanas_int>0){
                                        message += String.format("\tSabanas: %d\n",sabanas_int);
                                    }
                                    if(camas.isChecked() && camas_int>0){
                                        message += String.format("\tSabanas: %d\n",camas_int);
                                    }
                                    if(mesas.isChecked() && mesas_int>0){
                                        message += String.format("\tSabanas: %d\n",mesas_int);
                                    }
                                    if(sillas.isChecked() && sillas_int>0){
                                        message += String.format("\tSabanas: %d\n",sillas_int);
                                    }
                                    if(sillones.isChecked() && sillones_int>0){
                                        message += String.format("\tSabanas: %d\n",sillones_int);
                                    }

                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                    // 2. Firmar los datos
                                    // TODO: Hacer que haya tantos pares de clave como clientes seleccionables
                                    KeyPairGenerator keygen = null;
                                    try {
                                        keygen = KeyPairGenerator.getInstance("RSA");
                                        keygen.initialize(2048);
                                        KeyPair keys = keygen.generateKeyPair();

                                        Signature sg = Signature.getInstance("SHA256withRSA");
                                        sg.initSign(keys.getPrivate());
                                        sg.update(message.getBytes());

                                        byte[] firma = sg.sign();
                                        System.out.println(firma.toString());

                                        // 3. Enviar los datos
                                        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                                        SSLSocket socket = (SSLSocket) factory.createSocket(server,port);
                                        String[] protocols = {"TLSv1.3"};
                                        socket.setEnabledProtocols(protocols);


                                        // Create BufferedReader for reading server response
                                        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                                        // Create PrintWriter for sending login to server
                                        PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                                        output.println(message);
                                        output.println(keys.getPublic());
                                        output.println(firma);
                                        Toast.makeText(MainActivity.this, "Petición enviada correctamente", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                            }

                    )
                    .

                            setNegativeButton(android.R.string.no, null)

                    .

                            show();
        }
    }


}
