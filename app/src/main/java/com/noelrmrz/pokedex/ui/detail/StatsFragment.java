package com.noelrmrz.pokedex.ui.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.pojo.Stat;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.utilities.GsonClient;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private Pokemon savedPokemon;
    private Stat[] statList;
    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment StatsFragment.
     */
    public static StatsFragment newInstance(String param1) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            savedPokemon = GsonClient.getGsonClient().fromJson(mParam1, Pokemon.class);
            statList = savedPokemon.getStatList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RadarChart radarChart = view.findViewById(R.id.radar_chart);

        setChartData(radarChart, view);

        // Setup the X-axis
        XAxis xAxis = radarChart.getXAxis();
        String[] labels = new String[6];
        for (int x = 0; x < labels.length; x++) {
            labels[x] = statList[x].getStat().getName();

        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        // Setup the Y-axis
        YAxis yAxis = radarChart.getYAxis();
        yAxis.setTextSize(8f);

        // Setup the Legend
        Legend legend = radarChart.getLegend();
        legend.setEnabled(false);

    }

    public void setChartData(RadarChart radarChart, View view) {
        // Setup the dataset
        RadarDataSet radarDataset = new RadarDataSet(setChartEntries(statList, radarChart, view), "");
        //radarDataset.setColors(ColorTemplate.MATERIAL_COLORS);
        radarDataset.setValueTextColor(Color.BLACK);
        // Do not want to display the values on top of the graph
        radarDataset.setValueTextSize(0f);
        radarDataset.setDrawFilled(true);
        radarDataset.setFillColor(Color.MAGENTA);

        // Hookup the chart
        RadarData radarData = new RadarData(radarDataset);
        radarData.addDataSet(radarDataset);

        radarChart.setData(radarData);
    }

    public List<RadarEntry> setChartEntries(Stat[] stats, RadarChart radarChart, View view) {
        List<RadarEntry> entries = new ArrayList<>();
        int sum = 0;
        for (Stat stat: stats) {
            entries.add(new RadarEntry((float) stat.getBaseStat()));

            if (stat.getStat().getName().equalsIgnoreCase(getString(R.string.hp))) {
                TextView hp = view.findViewById(R.id.tv_label_hp);
                hp.append(String.valueOf(stat.getBaseStat()));
            } else if (stat.getStat().getName().equalsIgnoreCase(getString(R.string.atk))) {
                TextView atk = view.findViewById(R.id.tv_label_atk);
                atk.append(String.valueOf(stat.getBaseStat()));
            } else if (stat.getStat().getName().equalsIgnoreCase(getString(R.string.spatk))) {
                TextView spAtk = view.findViewById(R.id.tv_label_spatk);
                spAtk.append(String.valueOf(stat.getBaseStat()));
            } else if (stat.getStat().getName().equalsIgnoreCase(getString(R.string.def))) {
                TextView def = view.findViewById(R.id.tv_label_def);
                def.append(String.valueOf(stat.getBaseStat()));
            } else if (stat.getStat().getName().equalsIgnoreCase(getString(R.string.spdef))) {
                TextView spDef = view.findViewById(R.id.tv_label_spdef);
                spDef.append(String.valueOf(stat.getBaseStat()));
            } else {
                TextView spd = view.findViewById(R.id.tv_label_spd);
                spd.append(String.valueOf(stat.getBaseStat()));
            }

            sum += stat.getBaseStat();
        }
        Description description = new Description();
        description.setText("Total  " + sum);

        radarChart.setDescription(description);

        return entries;
    }
}