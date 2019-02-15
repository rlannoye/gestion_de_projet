package fr.univ_littoral.remy.game2shop;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int FORMULAIRE_ACTIVITY = 2;
    private final static int REQUEST_ENABLE_BT = 1;
    private ListView listView;
    private ArrayList<String> mDeviceList = new ArrayList<String>();

    ClientBluetooth clientBluetooth;
    //BluetoothDevice device;
    //ClientBluetooth clientBluetooth=new ClientBluetooth(device);

    Button buttonA;
    Button buttonB;
    Button buttonRight;
    Button buttonLeft;
    Button buttonUp;
    Button buttonDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonA = findViewById(R.id.buttonA);
        buttonB = findViewById(R.id.buttonB);
        buttonRight = findViewById(R.id.buttonRight);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonUp = findViewById(R.id.buttonUp);
        buttonDown = findViewById(R.id.buttonDown);

        //listview pour afficher la liste des appareils recherche
        listView = (ListView) findViewById(R.id.listView);

        //BluetoothAdapter est requis pour toute activité Bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //rechercher les terminaux visibles proches
        mBluetoothAdapter.startDiscovery();

        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            System.out.println("Device doesn't support Bluetooth");
        }

        //Appelez isEnabled () pour vérifier si Bluetooth est actuellement activé.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Inscrire le BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               System.out.println("A");
               clientBluetooth.run();
               //clientBluetooth.sendData("A");
               clientBluetooth.cancel();
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("B");
                Toast.makeText(MainActivity.this, "B", Toast.LENGTH_SHORT).show();
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("R");
                Toast.makeText(MainActivity.this, "R", Toast.LENGTH_SHORT).show();
            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("L");
                Toast.makeText(MainActivity.this, "L", Toast.LENGTH_SHORT).show();
            }
        });

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("U");
                Toast.makeText(MainActivity.this, "U", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("D");
                Toast.makeText(MainActivity.this, "D", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // On crée un BroadcastReceiver pour ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device.getName() + "\n" + device.getAddress());
                Log.i("BT", device.getName() + "\n" + device.getAddress());
                listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mDeviceList));
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.choix01 :
                Intent intentAjouter = new Intent(MainActivity.this, FormulaireActivity.class);
                startActivityForResult(intentAjouter, FORMULAIRE_ACTIVITY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == FormulaireActivity.MAIN_ACTIVITY) { }
    }
}
