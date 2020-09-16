package project.bridgetek.com.applib.main.toos;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.BluetoothContext;

/**
 * Created by dingjikerbo on 2016/8/27.
 */
public class ClientManager {

    private static BluetoothClient mClient;

    public static BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(BluetoothContext.get());
                }
            }
        }
        return mClient;
    }
}
