package wifi.free.am.freewifi.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wifi.free.am.freewifi.IWifiChange;
import wifi.free.am.freewifi.model.Data;

import static wifi.free.am.freewifi.util.ConsKey.EAP;
import static wifi.free.am.freewifi.util.ConsKey.OPEN;
import static wifi.free.am.freewifi.util.ConsKey.PSK;
import static wifi.free.am.freewifi.util.ConsKey.WEP;

/**
 * Created by mhers on 13/02/2018.
 */

public class Scan {
    private static Scan INSTANCE = null;

    public static Scan getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new Scan();
        }
        return INSTANCE;
    }


    public List<Data> getScanDataList(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo myWiFi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        List<Data> scanDataList = new ArrayList<Data>();
        scanDataList.clear();
        boolean isConectSpecWifi = false;
        List<ScanResult> results = getScanResualt(context);
        WifiInfo info = wifi.getConnectionInfo();

        if (results.size() > 0) {
            for (ScanResult result : results) {
                int level = WifiManager.calculateSignalLevel(result.level, 5);
                Data data = new Data(result.SSID.toString(), null, level);
                if (info != null) {
                    isConectSpecWifi = ((info.getSSID() != null && info.getSSID().toString().equalsIgnoreCase("\"" + result.SSID.toString() + "\"")) ? true : false);
                    data.setConect(isConectSpecWifi);

                }
                scanDataList.add(data);
            }

        }
        if (!myWiFi.isConnected() && wifi.isWifiEnabled()) {
            conectInternet(context, results, scanDataList);
        }


        return scanDataList;
    }

    private void conectInternet(Context activity, List<ScanResult> results, List<Data> scanDataList) {
        List<List<Data>> cleanInfo = new ArrayList<>();
        cleanInfo.clear();
        WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        for (Data data : scanDataList) {
            if (OpenData.getInstance(activity).cleanData(activity, data.getUsername()).size() > 0) {
                cleanInfo.add(OpenData.getInstance(activity).cleanData(activity, data.getUsername()));
            }
        }

        for (List<Data> smolList : cleanInfo) {
            for (ScanResult res : results) {
                if (res.SSID.equalsIgnoreCase(smolList.get(0).getUsername())) {
                    boolean isConect = tryConect(activity,(IWifiChange) activity, res, smolList,false);
                    if (isConect) {
                        wifiManager.setWifiEnabled(isConect);
                        break;
                    }
                }
            }
        }
    }


    public boolean tryConect(Context context, IWifiChange iWifiChange, ScanResult res, List<Data> smolList, boolean isWifichange) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String securityMode = getScanResultSecurity(res);
        WifiConfiguration wifiConfiguration = new WifiConfiguration();

        ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo myWiFi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (smolList.size() > 0) {
            String networkSSID = smolList.get(0).getUsername();

            switch (securityMode) {

                case OPEN: {
                    wifiConfiguration.SSID = "\"" + networkSSID + "\"";
                    wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                }

                case WEP: {
                    wifiConfiguration.SSID = "\"" + networkSSID + "\"";
                    wifiConfiguration.wepTxKeyIndex = 0;
                    wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                }

                default: {
                    wifiConfiguration.SSID = "\"" + networkSSID + "\"";
                    wifiConfiguration.hiddenSSID = true;
                    wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
                    wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                }
            }
        }


        for (Data data : smolList) {
            String networkPass = data.getPassword();

            if (securityMode.equals(WEP)) {
                wifiConfiguration.wepKeys[0] = "\"" + networkPass + "\"";
            } else {
                wifiConfiguration.preSharedKey = "\"" + networkPass + "\"";
            }
//            wifiManager.disconnect();
            Integer ress = wifiManager.addNetwork(wifiConfiguration);
//            if (res.level < -77) {
                if(isWifichange){
                    wifiManager.disconnect();
                }
                if (wifiManager.enableNetwork(ress, false)) {
                  iWifiChange.iWifiChange();
                    return true;
                } else {
                    wifiManager.disableNetwork(ress);
                    wifiManager.removeNetwork(ress);
                    wifiManager.reconnect();
                }
        }
        return false;
    }

    public List<ScanResult> getScanResualt(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifi.isWifiEnabled()) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.startScan();
            return wifi.getScanResults();
        } else {
            return new ArrayList<ScanResult>();
        }
    }

    private String getScanResultSecurity(ScanResult scanResult) {
        final String cap = scanResult.capabilities;
        final String[] securityModes = {WEP, PSK, EAP};

        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (cap.contains(securityModes[i])) {
                return securityModes[i];
            }
        }

        return OPEN;
    }
}


