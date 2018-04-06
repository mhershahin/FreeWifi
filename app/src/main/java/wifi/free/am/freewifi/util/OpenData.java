package wifi.free.am.freewifi.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import wifi.free.am.freewifi.model.Data;
import wifi.free.am.freewifi.model.MyData;

/**
 * Created by mhers on 13/02/2018.
 */
public class OpenData {
    private static OpenData INSTANCE = null;
    private static List<Data> allList ;

    public static OpenData getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new OpenData();
            allList = INSTANCE.getData(context).getDataList();

        }
        return INSTANCE;
    }

    public MyData getData(Context context) {
        MyData data = null;
        try {
            AssetManager manager = context.getAssets();
            InputStream ins = manager.open("wifi.data.json");
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(ins);
            data = gson.fromJson(reader, MyData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<Data> getWifiConfig(Context context) {
         List<Data> myWifiList = new ArrayList<Data>();
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configuredList = wifiManager.getConfiguredNetworks();
        if (configuredList != null) {
            for (WifiConfiguration config : configuredList) {
                String pwd = null;
                if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
                    pwd = config.preSharedKey;
                } else {
                    pwd = config.wepKeys[config.wepTxKeyIndex];
                }
                if (pwd == null || pwd.length() < 3) {
                    pwd = "root";
                }
                myWifiList.add(new Data(config.SSID.toString(), pwd));
            }
        }
        return myWifiList;
    }


    public List<Data> cleanData(Context context,String searchString) {
        List<Data> searchList = new ArrayList<Data>();
        searchList.clear();
        if (searchString.equals("")) {
            for (Data d : allList) {
                searchList.add(d);
            }
        } else {
            for (Data data : allList) {
                if (data.getUsername().equalsIgnoreCase(searchString.toString())) {
                    searchList.add(data);
                }
            }
        }
            return searchList;
        }
    }


