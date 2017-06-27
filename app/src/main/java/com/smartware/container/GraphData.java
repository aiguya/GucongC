package com.smartware.container;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Juho.S on 2017-06-27.
 */
public class GraphData implements Parcelable{

    public final static String GRAPH_DATA = "graph_data";
    public final static String GRAPH_DATA_2ND = "graph_data_2nd";

    public GraphData(){
        mainGraph = new ArrayList<>();
        detailGraph = new ArrayList<>();
    }

    public GraphData(Parcel in) {
    }

    public static final Creator<GraphData> CREATOR = new Creator<GraphData>() {
        @Override
        public GraphData createFromParcel(Parcel in) {
            return new GraphData(in);
        }

        @Override
        public GraphData[] newArray(int size) {
            return new GraphData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }



    private ArrayList<GraphValue> mainGraph;
    private ArrayList<GraphValue> detailGraph;

    public ArrayList<GraphValue> getMainGraph() {
        return mainGraph;
    }

    public void setMainGraph(ArrayList<GraphValue> mainGraph) {
        this.mainGraph = mainGraph;
    }

    public ArrayList<GraphValue> getDetailGraph() {
        return detailGraph;
    }

    public void setDetailGraph(ArrayList<GraphValue> detailGraph) {
        this.detailGraph = detailGraph;
    }
}
