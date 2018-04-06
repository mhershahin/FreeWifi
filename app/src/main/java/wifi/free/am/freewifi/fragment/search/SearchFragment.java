package wifi.free.am.freewifi.fragment.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wifi.free.am.freewifi.MainActivity;
import wifi.free.am.freewifi.R;
import wifi.free.am.freewifi.recycler.RecyclerViewEmptySupport;
import wifi.free.am.freewifi.model.Data;
import wifi.free.am.freewifi.util.OpenData;

/**
 * Created by mhers on 17/02/2018.
 */

public class SearchFragment extends Fragment  {

    @BindView(R.id.search_wifi_recycler)
    public RecyclerViewEmptySupport mRecyclerView;
    @BindView(R.id.edit_search)
    public AppCompatEditText searchText;
    @BindView(R.id.search_emptyText)
    public AppCompatTextView emptyText;
    private SearchAdapter adapter;
//    private List<Data> allList;
    private List<Data> searchList;
//    private List<Data> tempList;

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
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, view);
//        allList = OpenData.getInstance().getData(activity).getDataList();
//        tempList = new ArrayList<>();
//        dataList= OpenData.getInstance().getData(activity).getDataList();
        searchList = new ArrayList<Data>();


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(inflater.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(emptyText);
        adapter = new SearchAdapter(activity.getApplicationContext(), searchList);
        mRecyclerView.setAdapter(adapter);


        serchbutton();


        return view;
    }

    private void serchbutton() {
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch() {
        searchList.clear();
        for(Data datasearch:OpenData.getInstance(activity).cleanData(activity,searchText.getText().toString())){
            searchList.add(datasearch);
        }

//        searchList = new ArrayList<Data>();
//        searchList.clear();
//        for (Data data : allList) {
//            if (data.getUsername().equalsIgnoreCase(searchText.getText().toString())) {
//                searchList.add(data);
//            }
//
//
//        }
//        tempList.clear();
//        if (searchText.getText().toString().equals("all")) {
//            for(Data d:allList){
//                tempList.add(d);
//            }
//        }else {
//            for(Data k:searchList){
//                tempList.add(k);
//            }
//        }
adapter.notifyDataSetChanged();
    }

}
