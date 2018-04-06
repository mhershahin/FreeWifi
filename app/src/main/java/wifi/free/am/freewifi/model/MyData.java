package wifi.free.am.freewifi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mhers on 13/02/2018.
 */

public class MyData {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("data_list")
    @Expose
    private List<Data> dataList;

    public MyData(String type, List<Data> dataList) {
        this.type = type;
        this.dataList = dataList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }
}
