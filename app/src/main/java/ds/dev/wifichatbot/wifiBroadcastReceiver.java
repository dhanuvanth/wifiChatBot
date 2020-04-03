package ds.dev.wifichatbot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

public class wifiBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mContext;
    private ListActivity lContext;
    private MessageActivity msgContext;

    public wifiBroadcastReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel mChannel, MainActivity mContext) {
        this.wifiP2pManager = wifiP2pManager;
        this.mChannel = mChannel;
        this.mContext = mContext;
    }

    public wifiBroadcastReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel mChannel, ListActivity lContext) {
        this.wifiP2pManager = wifiP2pManager;
        this.mChannel = mChannel;
        this.lContext = lContext;
    }

    public wifiBroadcastReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel mChannel, MessageActivity msgContext) {
        this.wifiP2pManager = wifiP2pManager;
        this.mChannel = mChannel;
        this.msgContext = msgContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);

            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                Toast.makeText(context, "Wifi is On", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Wifi is Off", Toast.LENGTH_SHORT).show();
            }
        }else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){

            if (wifiP2pManager != null){
                wifiP2pManager.requestPeers(mChannel,lContext.peerListListener);
            }else{
                return;
            }
        }else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){

            if (wifiP2pManager == null)
                return;

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()){
                wifiP2pManager.requestConnectionInfo(mChannel,msgContext.infoListener);
            }else{
                msgContext.connectionState.setText("Device Disconnected");
            }
        }else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){

        }
    }
}
