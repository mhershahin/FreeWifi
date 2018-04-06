package wifi.free.am.freewifi.fragment.scan;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wifi.free.am.freewifi.MainActivity;
import wifi.free.am.freewifi.R;
import wifi.free.am.freewifi.recycler.RecyclerViewEmptySupport;
import wifi.free.am.freewifi.model.Data;
import wifi.free.am.freewifi.recycler.interfaces.ICardClick;
import wifi.free.am.freewifi.util.OpenData;
import wifi.free.am.freewifi.util.Scan;

/**
 * Created by mhers on 17/02/2018.
 */

public class ScanFragment extends Fragment implements ICardClick {
    @BindView(R.id.scan_recycler)
    public RecyclerViewEmptySupport mRecyclerView;

    @BindView(R.id.scan_emptyText)
    public AppCompatTextView scanEmptyText;


    private ScanAdapter adapter;
    private List<Data> scanList;
    private MainActivity activity;
    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scan_fragment, container, false);
        ButterKnife.bind(this, view);

        scanList = new ArrayList<>();


//        dataList= OpenData.getInstance().getData(activity).getDataList();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(inflater.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(scanEmptyText);
        adapter = new ScanAdapter(activity.getApplicationContext(), this, scanList);
        mRecyclerView.setAdapter(adapter);
        startUpdating();


        return view;
    }


    private void startUpdating() {

        new CountDownTimer(8001, 8000) {

            public void onTick(long millisUntilFinished) {

//do nothing
            }

            public void onFinish() {
                cancel();
                updaitAdapter();
                Log.e("updait", "updait");
            }
        }.start();

    }

    private void updaitAdapter() {
        scanList.clear();
        for (Data data : Scan.getInstance().getScanDataList(activity)) {
            scanList.add(data);
        }
        adapter.notifyDataSetChanged();
        startUpdating();
    }

    @Override
    public void cardClik(int position) {
        String user = scanList.get(position).getUsername();
        ScanResult result = null;
        List<ScanResult> scanResultList = Scan.getInstance().getScanResualt(activity);
        for (ScanResult res : scanResultList) {
            if (res.SSID.equalsIgnoreCase(user)) {
                result = res;
                break;
            }
        }
        boolean isConect = false;
        WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connManager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo myWiFi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


//        if (!myWiFi.isConnected()) {
        isConect = Scan.getInstance().tryConect(activity, activity,result, OpenData.getInstance(activity).cleanData(activity, user),true);
//        }
        if (!isConect) {
            Toast.makeText(activity, "Not password  " + user, Toast.LENGTH_SHORT).show();
        }

    }


}
