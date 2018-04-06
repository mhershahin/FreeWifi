package wifi.free.am.freewifi.tabhelper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import wifi.free.am.freewifi.R;
import wifi.free.am.freewifi.fragment.mywifi.MyWifiFragment;
import wifi.free.am.freewifi.fragment.scan.ScanFragment;
import wifi.free.am.freewifi.fragment.search.SearchFragment;


/**
 * This adapter provides two types of fragments as an example.
 * {@linkplain #createItem(int)} should be modified if you use this example for your app.
 */
public class TabsNavigationAdapter extends CacheFragmentStatePagerAdapter {


    private List<Tabs> tabsList;

    private int mScrollY;
    private Context mContext;


    public TabsNavigationAdapter(FragmentManager fm, Context context) {
        super(fm);
        // update(allProductsList);
        this.mContext = context;

        tabsList = new ArrayList<>();

        Tabs tabScans = new Tabs();
        tabScans.setName(context.getString(R.string.scans));

        Tabs tabMyWifi = new Tabs();
        tabMyWifi.setName(context.getString(R.string.my_wifi));

        Tabs tabSearch = new Tabs();
        tabSearch.setName(context.getString(R.string.search));

        tabsList.add(tabScans);
        tabsList.add(tabMyWifi);
        tabsList.add(tabSearch);


    }

    public void update(List<Tabs> tabsList) {
        this.tabsList = tabsList;
        notifyDataSetChanged();
    }

    public void setScrollY(int scrollY) {
        mScrollY = scrollY;
    }

    @Override
    protected Fragment createItem(int position) {
        Fragment f = null;

        final int pattern = position % 3;
        switch (pattern) {
            case 0: {
                f = new ScanFragment();
                Bundle args = new Bundle();
                if (0 < mScrollY) {
                    args.putInt(ScanFragment.ARG_INITIAL_POSITION, 1);
                }
                f.setArguments(args);
                break;
            }
            case 1:{
                f = new MyWifiFragment();
                Bundle args = new Bundle();

                if (0 < mScrollY) {
                    args.putInt(MyWifiFragment.ARG_INITIAL_POSITION, 1);
                }
                f.setArguments(args);
                break;
            }
            case 2: {
                f = new SearchFragment();
                Bundle args = new Bundle();

                if (0 < mScrollY) {
                    args.putInt(ScanFragment.ARG_INITIAL_POSITION, 1);
                }
                f.setArguments(args);
                break;
            }


        }


        return f;
    }

    @Override
    public int getCount() {
        return tabsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabsList.get(position).getName();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}


