package wifi.free.am.freewifi.fragment.scan;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import wifi.free.am.freewifi.R;
import wifi.free.am.freewifi.recycler.AbstractCard;
import wifi.free.am.freewifi.model.Data;

/**
 * Created by mhers on 17/02/2018.
 */

public class ScanCard extends AbstractCard {

    @BindView(R.id.scan_card_usser)
    AppCompatTextView textUsser;
    @BindView(R.id.search_liner)
    LinearLayout searchFrame;
    @BindView(R.id.image_check)
    AppCompatImageView checkView;
    @BindView(R.id.scan_card_pass)
    AppCompatTextView textPass;
    @BindView(R.id.progressWifi)
    ProgressBar progressBar;

    private Context mContext;
    Data myData;


    public ScanCard(Context context, ViewGroup parent) {
        this( context, LayoutInflater.from(context).inflate(R.layout.scan_card, parent, false));

    }

    public ScanCard( Context context,View view) {
        super(view,context);
        this.mContext=context;
        ButterKnife.bind(this, view);

    }
    @Override
    public void bind(Object data) {
        myData = (Data) data;
        if(myData.isConect()!=true){
            checkView.setVisibility(View.INVISIBLE);
        }else {
            checkView.setVisibility(View.VISIBLE);
        }
        progressBar.setProgress(((Data) data).getLevel());
        textUsser.setText(myData.getUsername());
        textPass.setText(myData.getPassword());
        final int poss = getAdapterPosition();
        searchFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClick(poss);
            }
        });

    }

}
