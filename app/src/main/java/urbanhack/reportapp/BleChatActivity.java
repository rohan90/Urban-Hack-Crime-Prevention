package urbanhack.reportapp;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import urbanhack.reportapp.ble.RBLService;

public class BleChatActivity extends AppCompatActivity {

    private static final String TAG = "BLE";
    private String mDeviceName;
    private String mDeviceAddress;
    private RBLService mBluetoothLeService;
    private Map<UUID, BluetoothGattCharacteristic> map = new HashMap<UUID, BluetoothGattCharacteristic>();
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_chat);
        report = getIntent().getExtras().getParcelable("report");
        initBle();
    }

    private void initBle() {
        mDeviceAddress = "FB:07:88:6A:62:EC";
        mDeviceName = "BLE Shield";
        connectToDevice();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeBleService();
    }

    private void closeBleService() {
        mBluetoothLeService.disconnect();
        mBluetoothLeService.close();
    }

    private void connectToDevice(){
        Intent gattServiceIntent = new Intent(this, RBLService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void retry() {
        initBle();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mBluetoothLeService = ((RBLService.LocalBinder) service)
                    .getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
             if (RBLService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Logger.logError("Disconnected trying again!");
                finish();
            } else if (RBLService.ACTION_GATT_SERVICES_DISCOVERED
                    .equals(action)) {
                getGattService(mBluetoothLeService.getSupportedGattService());
            } else if (RBLService.ACTION_DATA_AVAILABLE.equals(action)) {
//                displayData(intent.getByteArrayExtra(RBLService.EXTRA_DATA));
            }else if (RBLService.ACTION_GATT_CONNECTED.equals(action)) {
                 new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         sendReportTItle();
                     }
                 },2000);
             }
        }
    };

    private void sendReportTItle() {
        String str = report.getTitle()+" Report created!";
        Logger.logError("title " + str);
        byte b = 0x00;
        byte[] tmp = str.getBytes();
        byte[] tx = new byte[tmp.length + 1];
        tx[0] = b;
        for (int i = 1; i < tmp.length + 1; i++) {
            tx[i] = tmp[i - 1];
        }
        sendMessage(tx);
//        Toast.makeText(getApplicationContext(), "Successfully posted report anonymously!", Toast.LENGTH_LONG).show();
        finish();
    }


    private void sendMessage(byte[] tx) {
        BluetoothGattCharacteristic characteristic = map
                .get(RBLService.UUID_BLE_SHIELD_TX);
        characteristic.setValue(tx);
        mBluetoothLeService.writeCharacteristic(characteristic);
    }

//    private void displayData(byte[] byteArray) {
//        if (byteArray != null) {
//            String data = new String(byteArray);
//            tv.append(data);
//            // find the amount we need to scroll. This works by
//            // asking the TextView's internal layout for the position
//            // of the final line and then subtracting the TextView's height
//            final int scrollAmount = tv.getLayout().getLineTop(
//                    tv.getLineCount())
//                    - tv.getHeight();
//            // if there is no need to scroll, scrollAmount will be <=0
//            if (scrollAmount > 0)
//                tv.scrollTo(0, scrollAmount);
//            else
//                tv.scrollTo(0, 0);
//        }
//    }

    private void getGattService(BluetoothGattService gattService) {
        if (gattService == null)
            return;

        BluetoothGattCharacteristic characteristic = gattService
                .getCharacteristic(RBLService.UUID_BLE_SHIELD_TX);
        map.put(characteristic.getUuid(), characteristic);

        BluetoothGattCharacteristic characteristicRx = gattService
                .getCharacteristic(RBLService.UUID_BLE_SHIELD_RX);
        mBluetoothLeService.setCharacteristicNotification(characteristicRx,
                true);
        mBluetoothLeService.readCharacteristic(characteristicRx);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(RBLService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(RBLService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(RBLService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(RBLService.ACTION_DATA_AVAILABLE);

        return intentFilter;
    }

}
