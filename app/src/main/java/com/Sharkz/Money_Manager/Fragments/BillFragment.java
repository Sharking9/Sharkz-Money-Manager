package com.Sharkz.Money_Manager.Fragments;

import android.graphics.DashPathEffect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.Sharkz.Money_Manager.R;
import com.Sharkz.Money_Manager.adapter.Adapter;
import com.Sharkz.Money_Manager.helper.Helper;
import com.Sharkz.Money_Manager.model.Data;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BillFragment extends Fragment {
    Helper db = new Helper(getContext());
    ListView listView;
    List<Data> lists = new ArrayList<>();
    Adapter adapter;

    PieChart pieChart_expens, pieChart_incom;
    PieDataSet pieDataSet_expens;
    ArrayList<PieEntry> entries1;
    LineChart lineChart3;
    ArrayList<Entry> dataVals1, dataVals2;
    LineDataSet lineDataSet1,lineDataSet2;
    TextView txttotalExpens, txttotalIncome;
    int Total_Expens;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txttotalExpens = view.findViewById(R.id.txttotalExpens);
        txttotalIncome = view.findViewById(R.id.txttotalIncome);
        pieChart_expens = view.findViewById(R.id.piechart_expesens);

        db = new Helper(getContext());
        getDataRecordsExpenskePieChart();
        SetupPieChartExpens_legend_CustomLegendEntry();

    }

    private void getDataRecordsExpenskePieChart(){
        Total_Expens = 0;
        int TempFood = 0, TempSport = 0, TempHealth = 0;

        entries1 = new ArrayList<>();

        ArrayList<HashMap<String, String>> rows = db.getAll_Exp_Inc("EXP");
        for (int i = 0; i < rows.size(); i++){
            String name_label = rows.get(i).get("label");
            String jumlah = rows.get(i).get("jumlah");
            int jumlahC = Integer.parseInt(jumlah);

            Log.d("TAG", "Masuk data "+name_label+" "+ jumlah);
            //jika next rows masih ada 1 sport TempFood ok2 TempSport ok2
                    if(i < rows.size() - 1){
                        if (Objects.equals(name_label, "Food")) {
                            TempFood = TempFood + jumlahC;
                        }else if (Objects.equals(name_label, "Sport")) {
                            TempSport = TempSport + jumlahC;
                        }else if (Objects.equals(name_label, "Health")) {
                            TempHealth = TempHealth + jumlahC;
                        }
                    }else {
                            if (TempFood != 0 || TempSport != 0) {
                                if (Objects.equals(name_label, "Food")) {
                                    TempFood = TempFood + jumlahC;
                                    entries1.add(new PieEntry(TempFood, "Food"));
                                    TempFood = 0;
                                    if (TempSport != 0){
                                        entries1.add(new PieEntry(TempSport, "Sport"));
                                    }
                                    if (TempHealth != 0){
                                        entries1.add(new PieEntry(TempHealth, "Health"));
                                    }
                                }else if (Objects.equals(name_label, "Sport")) {
                                    TempSport = TempSport + jumlahC;
                                    entries1.add(new PieEntry(TempSport, "Sport"));
                                    TempSport = 0;
                                    if (TempFood != 0){
                                        entries1.add(new PieEntry(TempFood, "Food"));
                                    }
                                    if (TempHealth != 0){
                                        entries1.add(new PieEntry(TempHealth, "Health"));
                                    }
                                }else if (Objects.equals(name_label, "Health")) {
                                    TempHealth = TempHealth + jumlahC;
                                    entries1.add(new PieEntry(TempHealth, "Health"));
                                    TempHealth = 0;
                                    if (TempFood != 0){
                                        entries1.add(new PieEntry(TempFood, "Food"));
                                    }
                                    if (TempSport != 0){
                                        entries1.add(new PieEntry(TempSport, "Sport"));
                                    }
                                }

                            } else {
                                int Rowscuma1 = jumlahC;
                                entries1.add(new PieEntry(Rowscuma1, name_label));
                            }
                    }


            Total_Expens = Total_Expens + Integer.parseInt(jumlah);
        }

        pieDataSet_expens = new PieDataSet(entries1, "Expense this month");
        pieDataSet_expens.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(pieDataSet_expens);
        pieChart_expens.setData(pieData);

        txttotalExpens.setText(String.valueOf(Total_Expens));

    }

    private void SetupPieChartExpens_legend_CustomLegendEntry(){
        pieChart_expens.getDescription().setEnabled(false);
        pieChart_expens.setUsePercentValues(true);
        pieChart_expens.setDrawEntryLabels(false);
        pieChart_expens.animateY(1000);


        //legend attributes
        Legend legend = pieChart_expens.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXOffset(20);
        legend.setYOffset(-100);

        // Tambahkan legend
        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        List<Integer> colors = pieDataSet_expens.getColors(); // Mengambil warna dari setiap entri pie chart

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
            int percentage = (int) (((float) valueint / Total_Expens) * 100); //  mengubah satu dari operand menjadi float　lalu Hitung persentase

            // Format label legenda dengan nilai dan persentase
            String formattedLabel = entry.getLabel() + " (" + valueint + ", " + percentage + "%)";
            legendEntry.label = formattedLabel; // Tetapkan label yang telah diformat

            // Tambahkan LegendEntry yang telah dikonfigurasi ke dalam ArrayList legendEntries
            legendEntries.add(legendEntry);
        }
        pieChart_expens.getLegend().setCustom(legendEntries);

        // Panggil invalidate() untuk memperbarui tampilan pie chart
        pieChart_expens.invalidate();
    }

    @Override
    public void onResume(){
        super.onResume();
        getDataRecordsExpenskePieChart();
        SetupPieChartExpens_legend_CustomLegendEntry();
    }
}