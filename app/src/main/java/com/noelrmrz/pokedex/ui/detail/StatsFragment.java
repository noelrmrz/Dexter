package com.noelrmrz.pokedex.ui.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.noelrmrz.pokedex.databinding.FragmentStatsBinding;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.pojo.Stat;
import com.noelrmrz.pokedex.utilities.GsonClient;

import java.text.DecimalFormat;
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
    private FragmentStatsBinding bind;
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
            String mParam1 = getArguments().getString(ARG_PARAM1);
            Pokemon savedPokemon = GsonClient.getGsonClient().fromJson(mParam1, Pokemon.class);
            statList = savedPokemon.getStatList();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = FragmentStatsBinding.inflate(inflater, container, false);
        bind.setStats(statList);
        bind.executePendingBindings();
        return bind.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        final float X_GRANULARITY = 2f;
        final float Y_AXIS_MIN = 0f;

        bind.horizontalChart.setDrawGridBackground(false);
        bind.horizontalChart.setDrawBarShadow(false);
        bind.horizontalChart.setDrawValueAboveBar(true);
        // scaling can now only be done on x- and y-axis separately
        bind.horizontalChart.setPinchZoom(false);

        // Setup the X-axis
        XAxis xAxis = bind.horizontalChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(X_GRANULARITY);

        xAxis.setValueFormatter(new XAxisFormatter());

        // Setup the Y-axis
        YAxis yAxisLeft = bind.horizontalChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(Y_AXIS_MIN); // this replaces setStartAtZero(true)
        yAxisLeft.setDrawAxisLine(true);
        yAxisLeft.setDrawGridLines(true);

        YAxis yAxisRight = bind.horizontalChart.getAxisRight();
        yAxisRight.setAxisMinimum(Y_AXIS_MIN);
        yAxisRight.setDrawAxisLine(true);
        yAxisRight.setDrawGridLines(false);

        yAxisRight.setValueFormatter(new YAxisFormatter());
        yAxisLeft.setValueFormatter(new YAxisFormatter());

        bind.horizontalChart.setFitBars(true);
        bind.horizontalChart.animateY(1500);

        // Setup the Legend
        Legend legend = bind.horizontalChart.getLegend();
        legend.setEnabled(false);

        setChartData(bind.horizontalChart);
    }

    public void setChartData(HorizontalBarChart horizontalChart) {
        final float BAR_WIDTH = 1f;

        // Setup the dataset
        BarDataSet barDataset = new BarDataSet(setChartEntries(statList, horizontalChart), "");
        barDataset.setValueTextColor(Color.BLACK);
        barDataset.setValueFormatter(new YAxisFormatter());
        barDataset.setColor(Color.RED, 100);

        // Hookup the chart
        BarData barData = new BarData(barDataset);
        barData.addDataSet(barDataset);
        barData.setBarWidth(BAR_WIDTH);

        horizontalChart.setData(barData);
        horizontalChart.fitScreen();
    }

    public List<BarEntry> setChartEntries(Stat[] stats, HorizontalBarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();
        int sum = 0;

        for (int i = 0; i < stats.length; i++) {
            final float BAR_SPACE = 2f;
            entries.add(new BarEntry(i * BAR_SPACE, (float) stats[i].getBaseStat()));
            sum += stats[i].getBaseStat();
        }

        setChartDescription(barChart, sum);
        return entries;
    }

    public void setChartDescription(HorizontalBarChart chart, int sum) {
        final float DESC_Y_POS = 1425f;
        final float DESC_X_POS = 220f;
        Description description = chart.getDescription();
        description.setEnabled(true);
        description.setText("Total: " + sum);
        description.setPosition(DESC_X_POS, DESC_Y_POS);
        chart.setDescription(description);
    }

    public static class XAxisFormatter extends ValueFormatter {
        String[] labels = new String[]{"HP", "Atk", "SpAtk", "Def", "SpDef", "Spd"};
        DecimalFormat decimalFormat = new DecimalFormat("###");

        @Override
        public String getBarLabel(BarEntry barEntry) {
            return decimalFormat.format(barEntry.getY());
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // value is in increments of 10
            return labels[Math.round(value / 2)];
        }
    }

    public static class YAxisFormatter extends ValueFormatter {
        DecimalFormat decimalFormat = new DecimalFormat("###");

        @Override
        public String getBarLabel(BarEntry barEntry) {
            return decimalFormat.format(barEntry.getY());
        }

       @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return Integer.toString((int) value);
        }
    }
}