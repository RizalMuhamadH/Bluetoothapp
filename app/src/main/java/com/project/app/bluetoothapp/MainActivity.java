package com.project.app.bluetoothapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private Button refresh;

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
        refresh = (Button)findViewById(R.id.refresh);
        list = (ListView)findViewById(R.id.list_id);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        turnOn.setOnClickListener(this);
        turnOff.setOnClickListener(this);
        disc.setOnClickListener(this);
        refresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.turn_on:
                if (!bluetoothAdapter.isEnabled()){
                    Toast.makeText(this, "Turning On Bluetooth", Toast.LENGTH_LONG).show();

                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),0);
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
            case R.id.refresh :
                if (bluetoothAdapter.isEnabled()){
                    foundDevices();
                }
                break;
        }
    }
    private void foundDevices(){
        device = bluetoothAdapter.getBondedDevices();

        final ArrayList<String> arrayList = new ArrayList();

        for (BluetoothDevice bluetoothDevice : device){
            arrayList.add(bluetoothDevice.getName());
        }
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    arrayList.add(bluetoothDevice.getName());
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver,filter);
        if (arrayList.isEmpty()){
            Toast.makeText(this, "Paired Not Device", Toast.LENGTH_SHORT).show();
        }   else {
            Toast.makeText(this, "Showing Paired Device", Toast.LENGTH_SHORT).show();
        }
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);

        list.setAdapter(adapter);
    }



    @Override
    protected void onDestroy() {
//        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
