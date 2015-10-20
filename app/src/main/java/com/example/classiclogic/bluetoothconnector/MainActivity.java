package com.example.classiclogic.bluetoothconnector;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private final String MYLOGTAG = "BluetoothConnectorLog";

    private Button ConnectButton;

    private Intent openBtSettings;
    private IntentFilter intentFilter;
    private BroadcastReceiver btBroadcastReceiver;
    private BluetoothDevice device;

    private void displayDeviceData()    {
        TextView nameView = (TextView) findViewById(R.id.tv_btname);
        TextView addrView = (TextView) findViewById(R.id.tv_btaddr);

        nameView.setText(device.getName());
        addrView.setText(device.getAddress());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //broadcast receivet to receive intent
        btBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                if( BluetoothDevice.ACTION_ACL_CONNECTED.equals(action) )   {

                    // Extract BluetoothDevice object referring to the remote object
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    Log.v(MYLOGTAG, " Received: " + device.getName() + " " + device.getAddress());

                    displayDeviceData();
                }
            }
        };

        ConnectButton = (Button) findViewById(R.id.connect_button);

        // Intent to open bluetooth settings of phone
        openBtSettings = new Intent();
        openBtSettings.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);

        intentFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);

        registerReceiver(btBroadcastReceiver, intentFilter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        ConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(MYLOGTAG, " Connect button pressed from MainActivity");
                startActivity(openBtSettings);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
