package wifi.free.am.freewifi;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.nineoldandroids.view.ViewHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import wifi.free.am.freewifi.tabhelper.SlidingTabLayout;
import wifi.free.am.freewifi.tabhelper.TabsNavigationAdapter;
public class MainActivity extends AppCompatActivity implements IWifiChange {
    private TabsNavigationAdapter mPagerAdapter;
    private  WifiManager wifi ;
    @BindView(R.id.header)
    LinearLayout mHeaderView;
    @BindView(R.id.pager)
    ViewPager mPager;
    @BindView(R.id.sliding_tabs)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.mainToolbar)
    Toolbar toolbar;
    @BindView(R.id.switch_wifi)
    SwitchCompat switchWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        inicalizacia();


    }


    private void inicalizacia() {
        wifi = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mPagerAdapter = new TabsNavigationAdapter(getSupportFragmentManager(), getApplicationContext());
        mPager.setAdapter(mPagerAdapter);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        slidingTabLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(getApplicationContext(), R.color.black));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);

        // When the page is selected, other fragments' scrollY should be adjusted
        // according to the toolbar status(shown/hidden)
        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                propagateToolbarState(toolbarIsShown());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        propagateToolbarState(toolbarIsShown());
    }


    protected void propagateToolbarState(boolean isShown) {
        int toolbarHeight = toolbar.getHeight();

        // Set scrollY for the fragments that are not created yet
        mPagerAdapter.setScrollY(isShown ? 0 : toolbarHeight);

        // Set scrollY for the active fragments
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            // Skip current item
            if (i == mPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            Fragment f = mPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            View view = f.getView();
            if (view == null) {
                continue;
            }
            propagateToolbarState(isShown, view, toolbarHeight);
        }
    }

    protected void propagateToolbarState(boolean isShown, View view, int toolbarHeight) {
        Scrollable scrollView = (Scrollable) view.findViewById(R.id.scrollable);
        if (scrollView == null) {
            return;
        }
        if (isShown) {
            // Scroll up
            if (0 < scrollView.getCurrentScrollY()) {
                scrollView.scrollVerticallyTo(0);
            }
        } else {
            // Scroll down (to hide padding)
            if (scrollView.getCurrentScrollY() < toolbarHeight) {
                scrollView.scrollVerticallyTo(toolbarHeight);
            }
        }
    }

    private void switchchange() {
        switchWifi.setChecked(wifi.isWifiEnabled());
        switchWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            wifi.setWifiEnabled(isChecked);

            }
        });
    }

    @Override
    public void onResume() {
        switchchange();
        super.onResume();
    }

    protected boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mHeaderView) == 0;
    }

    public int getCurrentItem() {
        return mPager.getCurrentItem();
    }

    @Override
    public void iWifiChange() {
        Fragment frg = mPagerAdapter.getItemAt(1);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }
}
