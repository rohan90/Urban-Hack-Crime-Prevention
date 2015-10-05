package urbanhack.reportapp;

/**
 * Created by tusharchoudhary on 9/27/15.
 */

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class MyBluetoothReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MyBluetoothReceiver"," boradcast received");
        String action = intent.getAction();
        Integer taskCode = -1;
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                    == BluetoothAdapter.STATE_ON) {
                Log.e("MyBluetoothReceiver", " state On ");
                taskCode = 1;
                startService(taskCode,context);
            }
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                    == BluetoothAdapter.STATE_OFF){
                Log.e("MyBluetoothReceiver"," state Off");
                taskCode = 0;
                startService(taskCode,context);
            }

        }
    }

    private void startService(Integer taskCode,Context context) {
        Intent serviceIntent = new Intent(context, ScanService.class);
        serviceIntent.putExtra("taskcode", taskCode);
        context.startService(serviceIntent);
    }
}