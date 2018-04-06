package wifi.free.am.freewifi.fragment.mywifi;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import wifi.free.am.freewifi.R;
import wifi.free.am.freewifi.model.Data;
import wifi.free.am.freewifi.recycler.AbstractCard;

/**
 * Created by mhers on 30/03/2018.
 */

public class MyWifiCard extends AbstractCard {
    private Context context;
    private Data myData;

    @BindView(R.id.my_wifi_card)
    CardView myWifiCard;
    @BindView(R.id.my_wifi_usser)
    AppCompatTextView textUsser;
    @BindView(R.id.my_wifi_card_pass)
    AppCompatTextView textPass;

    public MyWifiCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.my_wifi_card, parent, false));

    }

    public MyWifiCard(Context context, View cardView) {
        super(cardView, context);
        this.context = context;
        ButterKnife.bind(this, cardView);
    }

    @Override
    public void bind(Object data) {
        myData = (Data) data;
        textUsser.setText(myData.getUsername());
        textPass.setText(myData.getPassword());
        final int poz = getAdapterPosition();
        myWifiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClick(poz);
            }
        });
    }



}
