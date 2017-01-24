package com.project.app.bluetoothapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView output;
    private ListView list;
    private Button turnOn;
    private Button turnOff;
    private Button disc;

    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice>device;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = (TextView)findViewById(R.id.output);
        turnOn = (Button)findViewById(R.id.turn_on);
        turnOff = (Button)findViewById(R.id.turn_off);
        disc = (Button)findViewById(R.id.discoverable);
        list = (ListView)findViewById(R.id.list_id);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        turnOn.setOnClickListener(this);
        turnOff.setOnClickListener(this);
        disc.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.turn_on:
                if (!bluetoothAdapter.isEnabled()){
                    Toast.makeText(this, "Turning On Bluetooth", Toast.LENGTH_LONG).show();

                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),0);
                    listId();
                }
                break;
            case R.id.turn_off:
                bluetoothAdapter.disable();
                Toast.makeText(this, "Turning Off Bluetoooth", Toast.LENGTH_LONG).show();
                break;
            case R.id.discoverable:
                if (!bluetoothAdapter.isDiscovering()){
                    Toast.makeText(this, "Making Your Device Discoverable", Toast.LENGTH_SHORT).show();

                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE),0);
                }
                break;
        }
    }
    private void listId(){
        device = bluetoothAdapter.getBondedDevices();

        ArrayList arrayList = new ArrayList();

        for (BluetoothDevice bluetoothDevice : device){
            arrayList.add(bluetoothDevice.getName());
        }
        if (arrayList.isEmpty()){
            Toast.makeText(this, "Paired Not Device", Toast.LENGTH_SHORT).show();
        }   else {
            Toast.makeText(this, "Showing Paired Device", Toast.LENGTH_SHORT).show();
        }
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);

        list.setAdapter(adapter);
    }
}
