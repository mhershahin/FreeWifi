package wifi.free.am.freewifi.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import wifi.free.am.freewifi.recycler.interfaces.ICardClick;

/**
 * Created by mhers on 17/02/2018.
 */

public abstract class AbstractCard extends RecyclerView.ViewHolder {
    protected Context context;

    public AbstractCard(View cardView, Context context) {
        super(cardView);
        this.context = context;

    }

    public abstract void bind(Object data);


    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private ICardClick iCardClick;
    public void cardClick(int position) {
        if (iCardClick != null) {
            iCardClick.cardClik(position);
        }
    }

    public void setiCardClick(ICardClick iCardClick) {
        this.iCardClick = iCardClick;
    }
}
