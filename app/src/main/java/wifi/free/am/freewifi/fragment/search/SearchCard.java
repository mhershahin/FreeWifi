package wifi.free.am.freewifi.fragment.search;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import wifi.free.am.freewifi.R;
import wifi.free.am.freewifi.recycler.AbstractCard;
import wifi.free.am.freewifi.model.Data;

/**
 * Created by mhers on 17/02/2018.
 */

public class SearchCard extends AbstractCard {

    @BindView(R.id.search_card_usser)
    AppCompatTextView textUsser;
    @BindView(R.id.search_card_pass)
    AppCompatTextView textPass;

    private Context mContext;
    Data myData;


    public SearchCard(Context context, ViewGroup parent) {
        this( context, LayoutInflater.from(context).inflate(R.layout.search_card, parent, false));

    }

    public SearchCard( Context context,View view) {
        super(view,context);
        ButterKnife.bind(this, view);
        this.mContext=context;
    }
    @Override
    public void bind(Object data) {
        myData = (Data) data;
        textUsser.setText(myData.getUsername());
        textPass.setText(myData.getPassword());
    }
}
