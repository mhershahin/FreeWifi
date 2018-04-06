package wifi.free.am.freewifi.fragment.mywifi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import wifi.free.am.freewifi.recycler.AbstractCard;
import wifi.free.am.freewifi.fragment.search.SearchCard;
import wifi.free.am.freewifi.model.Data;
import wifi.free.am.freewifi.recycler.interfaces.ICardClick;

/**
 * Created by mhers on 17/02/2018.
 */

public class MyWifiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<Data> dataList;
    private ICardClick iCardClick;
    public MyWifiAdapter(Context context,ICardClick iCardClick, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.iCardClick = iCardClick;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AbstractCard card;
        switch (viewType) {
            ///  case VIEW_TYPE_WORK:
            default:
                card = new MyWifiCard(context,parent);
                break;
        }
        return card;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Data data = (Data) dataList.get(position);
        AbstractCard card = (AbstractCard) holder;
        card.setiCardClick(iCardClick);
        card.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
