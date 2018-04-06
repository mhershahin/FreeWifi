package wifi.free.am.freewifi.fragment.mywifi;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by mhers on 17/02/2018.
 */

public class MyWifiFragment extends Fragment implements ICardClick {
    @BindView(R.id.my_wifi_recycler)
    public RecyclerViewEmptySupport mRecyclerView;

    @BindView(R.id.my_wifi_emptyText)
    public AppCompatTextView emptyText;

    private MyWifiAdapter adapter;
    private List<Data> myWifiList;
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
        View view = inflater.inflate(R.layout.my_wifi_fragment, container, false);
        ButterKnife.bind(this, view);
        myWifiList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(inflater.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(emptyText);
        adapter = new MyWifiAdapter(activity.getApplicationContext(), this, myWifiList);
        mRecyclerView.setAdapter(adapter);
        refreshList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("haha ","onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshList();
    }

    private void refreshList() {
        myWifiList.clear();
        for (Data d : OpenData.getInstance(activity).getWifiConfig(activity)) {
            myWifiList.add(d);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void cardClik(int position) {
        myWifiList.remove(position);
        WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configuredList = wifiManager.getConfiguredNetworks();
        wifiManager.removeNetwork(configuredList.get(position).networkId);
        wifiManager.saveConfiguration();
        adapter.notifyDataSetChanged();

    }
}
