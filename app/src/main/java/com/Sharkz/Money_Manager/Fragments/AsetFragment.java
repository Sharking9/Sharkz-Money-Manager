package com.Sharkz.Money_Manager.Fragments;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Sharkz.Money_Manager.R;
import com.Sharkz.Money_Manager.helper.Helper;
import com.Sharkz.Money_Manager.model.Data;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class AsetFragment extends Fragment {

    Helper db = new Helper(getContext());
    PieChart pieChart;
    PieDataSet pieDataSet;
    ArrayList<PieEntry> entries1;
    TextView txttotalasets, txttotalliabilities;
    int Total_Aset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aset, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txttotalasets = view.findViewById(R.id.txttotalassets);
        txttotalliabilities = view.findViewById(R.id.txttotalliabilities);
        pieChart = view.findViewById(R.id.piechart_aset);
        db = new Helper(getContext());
        getDataAsetkePieChart();
        SetupPieChart_legend_CustomLegendEntry();





    }


    private void getDataAsetkePieChart(){
        Total_Aset = 0;

        entries1 = new ArrayList<>();

        ArrayList<HashMap<String, String>> rows = db.getAllAset();
        for (int i = 0; i < rows.size(); i++){
            String name_aset = rows.get(i).get("name_aset");
            String total = rows.get(i).get("total");

            entries1.add(new PieEntry(Integer.parseInt(total),name_aset));

            Total_Aset = Total_Aset + Integer.parseInt(total);
        }

        pieDataSet = new PieDataSet(entries1, "Your Assets");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        txttotalasets.setText(String.valueOf(Total_Aset));

    }
    private void SetupPieChart_legend_CustomLegendEntry(){
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.animateY(1000);


        //legend attributes
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);

        // Tambahkan legend
        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        List<Integer> colors = pieDataSet.getColors(); // Mengambil warna dari setiap entri pie chart

        for (int i = 0; i < entries1.size(); i++) {
            PieEntry entry = entries1.get(i);
            // Buat sebuah objek LegendEntry baru tanpa argumen
            LegendEntry legendEntry = new LegendEntry();

            // Tetapkan label, bentuk, dan nilai untuk LegendEntry
            legendEntry.label = entry.getLabel();
            // Tetapkan formColor sesuai dengan warna dari setiap entri pie chart
            legendEntry.formColor = colors.get(i);
            legendEntry.formSize = 12f; // Atur ukuran bentuk legenda sesuai kebutuhan
            legendEntry.formLineWidth = 3f; // Atur lebar garis bentuk legenda sesuai kebutuhan
            legendEntry.formLineDashEffect = new DashPathEffect(new float[]{10f, 5f}, 0f); // Atur efek dash untuk garis bentuk legenda sesuai kebutuhan
//            legendEntry.formLineDashPhase = 0f; // Atur fase dash untuk garis bentuk legenda sesuai kebutuhan
            // Dapatkan nilai dan persentase dari entri
            float value = entry.getValue();
            int valueint = Math.round(value); // Mengubah nilai float menjadi integer 12.8328373 %
            int percentage = (int) (((float) valueint / Total_Aset) * 100); //  mengubah satu dari operand menjadi float　lalu Hitung persentase

            // Format label legenda dengan nilai dan persentase
            String formattedLabel = entry.getLabel() + " (" + valueint + ", " + percentage + "%)";
            legendEntry.label = formattedLabel; // Tetapkan label yang telah diformat

            // Tambahkan LegendEntry yang telah dikonfigurasi ke dalam ArrayList legendEntries
            legendEntries.add(legendEntry);
        }
        pieChart.getLegend().setCustom(legendEntries);

        // Panggil invalidate() untuk memperbarui tampilan pie chart
        pieChart.invalidate();
    }

    @Override
    public void onResume(){
        super.onResume();
        getDataAsetkePieChart();
        pieChart.invalidate();
    }




}