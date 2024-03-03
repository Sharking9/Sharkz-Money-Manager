package com.Sharkz.Money_Manager.Fragments;

import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class BillFragment extends Fragment {
    Helper db = new Helper(getContext());
    ListView listViewExpen, listViewIncome;
    List<Data> lists, lists2;
    Adapter adapter, adapter2;

    PieChart pieChart_expense, pieChart_income;
    PieDataSet pieDataSet_expense, pieDataSet_income;
    ArrayList<PieEntry> entries1;
    LineChart lineChart_ExIn;
    ArrayList<Entry> dataVals1, dataVals2;
    LineDataSet lineDataSet1,lineDataSet2;

    // Menggunakan HashMap untuk menyimpan pasangan label dan jumlah
    HashMap<String, Integer> dataLabel;
    TextView txttotalExpense, txttotalIncome;
    int Total_Exp_inc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new Helper(getContext());

        txttotalExpense = view.findViewById(R.id.txttotalExpens);
        txttotalIncome = view.findViewById(R.id.txttotalIncome);

        listViewExpen = view.findViewById(R.id.list_percent_expens);
        lists = new ArrayList<>();
        adapter = new Adapter(getActivity(), lists, "AsetListView");
        listViewExpen.setAdapter(adapter);

        pieChart_expense = view.findViewById(R.id.piechart_expesens);

        getDataRecordsExp_InckePieChartdanListExpen("EXP");
        SetupPieChartExpens_legend_CustomLegendEntry();

        listViewIncome = view.findViewById(R.id.list_percent_Incom);
        lists2 = new ArrayList<>();
        adapter2 = new Adapter(getActivity(), lists2, "AsetListView");
        listViewIncome.setAdapter(adapter2);

        pieChart_income = view.findViewById(R.id.piechart_Incomes);

        getDataRecordsExp_InckePieChartdanListExpen("INC");
        SetupPieChartIncome_legend_CustomLegendEntry();

        lineChart_ExIn = view.findViewById(R.id.linechart_ExpenXIncom);


    }

    private void getDataRecordsExp_InckePieChartdanListExpen(String ambildata){
        Total_Exp_inc = 0;

        entries1 = new ArrayList<>();

        ArrayList<HashMap<String, String>> rows = db.getAll_Exp_Inc(ambildata);
        getAllLabel(rows);

        for (int i = 0; i < rows.size(); i++){
            String name_label = rows.get(i).get("label");
            String jumlah = rows.get(i).get("jumlah");
            int jumlahC = Integer.parseInt(jumlah);

//            Log.d("TAG", "Masuk data "+name_label+" "+ jumlah);
            //jika next rows masih ada 1 Food TempFood ok TempSport ok
                    if(i < rows.size() - 1){

                        if (dataLabel.containsKey(name_label)) {
                            int Temp = dataLabel.get(name_label) + jumlahC;
                            dataLabel.put(name_label, Temp); //Simpan Sementara diHash data Label
//                          Log.d("TAG", "add dataLabel = "+ name_label + " dan " +Temp);
                        }

                    }else {
                        int Temp2 = dataLabel.get(name_label) + jumlahC;
                        dataLabel.put(name_label, Temp2); //Last Data Simpan Sementara diHash data Label
//                        Log.d("TAG", "add dataLabel = "+ name_label + " dan " +Temp2);

                        for (String label24 : dataLabel.keySet()) {
                            int Temp = dataLabel.get(label24);
                            if (Temp != 0){
                                entries1.add(new PieEntry(Temp, label24));
                                ///Pilih Listview dan tambahkan
                                if (ambildata.equals("EXP")){
                                    AddDatakeList(label24, String.valueOf(Temp), lists);
                                }else if (ambildata.equals("INC")){
                                    AddDatakeList(label24, String.valueOf(Temp), lists2);
                                }
//                                Log.d("TAG", "add dataHasil = "+ label24 + " dan " +Temp);
                            }

                        }

                    }


            Total_Exp_inc = Total_Exp_inc + Integer.parseInt(jumlah);

        }
        adapter.notifyDataSetChanged(); ///beritahu adapter list telah diset


        if (ambildata.equals("EXP")){
            pieDataSet_expense = new PieDataSet(entries1, "Expense this month");
            pieDataSet_expense.setColors(ColorTemplate.MATERIAL_COLORS);

            PieData pieData = new PieData(pieDataSet_expense);
            pieChart_expense.setData(pieData);

            txttotalExpense.setText(String.valueOf(Total_Exp_inc));
        } else if (ambildata.equals("INC")) {
            pieDataSet_income = new PieDataSet(entries1, "Income this month");
            pieDataSet_income.setColors(ColorTemplate.MATERIAL_COLORS);

            PieData pieData = new PieData(pieDataSet_income);
            pieChart_income.setData(pieData);

            txttotalIncome.setText(String.valueOf(Total_Exp_inc));
        }

    }

    private void SetupPieChartExpens_legend_CustomLegendEntry(){
        pieChart_expense.getDescription().setEnabled(false);
        pieChart_expense.setUsePercentValues(true);
        pieChart_expense.setDrawEntryLabels(false);
        pieChart_expense.animateY(1000);


        //legend attributes
        Legend legend = pieChart_expense.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXOffset(20);
        legend.setYOffset(-100);

        // Tambahkan legend
        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        List<Integer> colors = pieDataSet_expense.getColors(); // Mengambil warna dari setiap entri pie chart

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
            int percentage = (int) (((float) valueint / Total_Exp_inc) * 100); //  mengubah satu dari operand menjadi float　lalu Hitung persentase

            // Format label legenda dengan nilai dan persentase
            String formattedLabel = entry.getLabel() + " (" + valueint + ", " + percentage + "%)";
            legendEntry.label = formattedLabel; // Tetapkan label yang telah diformat

            // Tambahkan LegendEntry yang telah dikonfigurasi ke dalam ArrayList legendEntries
            legendEntries.add(legendEntry);
        }
        pieChart_expense.getLegend().setCustom(legendEntries);

        // Panggil invalidate() untuk memperbarui tampilan pie chart
        pieChart_expense.invalidate();
    }

    private void SetupPieChartIncome_legend_CustomLegendEntry(){
        pieChart_income.getDescription().setEnabled(false);
        pieChart_income.setUsePercentValues(true);
        pieChart_income.setDrawEntryLabels(false);
        pieChart_income.animateY(1000);


        //legend attributes
        Legend legend = pieChart_income.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXOffset(20);
        legend.setYOffset(-100);

        // Tambahkan legend
        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        List<Integer> colors = pieDataSet_income.getColors(); // Mengambil warna dari setiap entri pie chart

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
            int percentage = (int) (((float) valueint / Total_Exp_inc) * 100); //  mengubah satu dari operand menjadi float　lalu Hitung persentase

            // Format label legenda dengan nilai dan persentase
            String formattedLabel = entry.getLabel() + " (" + valueint + ", " + percentage + "%)";
            legendEntry.label = formattedLabel; // Tetapkan label yang telah diformat

            // Tambahkan LegendEntry yang telah dikonfigurasi ke dalam ArrayList legendEntries
            legendEntries.add(legendEntry);
        }
        pieChart_income.getLegend().setCustom(legendEntries);

        // Panggil invalidate() untuk memperbarui tampilan pie chart
        pieChart_income.invalidate();
    }
    private void AddDatakeList(String name_label, String jumlah, List<Data> nama_list){
        Data data = new Data();
        data.setAset(name_label);
//            data.setLabel(label);
//            data.setDrawableId(drawableId); // Mengatur ID drawable ke objek Data
        data.setTotal(jumlah);
        nama_list.add(data);
    }
    private void getAllLabel(ArrayList<HashMap<String, String>> rowslabel){
        dataLabel = new HashMap<>();
//        // Mendapatkan kunci dan nilai dari HashMap
//        for (String kunci : dataLabel.keySet()) {
//            int nilai = dataLabel.get(kunci);
//            System.out.println("Kunci: " + kunci + ", Nilai: " + nilai);
//        }

        for (int i = 0; i < rowslabel.size(); i++){
            String name_label = rowslabel.get(i).get("label");
            boolean found = false;

            if (dataLabel.containsKey(name_label)) {
                found = true;
//                Log.d("TAG", "dataLabel, "+ name_label + " ada dalam HashMap.");
            }
            if (!found) {
                dataLabel.put(name_label, 0);
//                Log.d("TAG", "dataLabel, "+ name_label + " Diadd.");
            }
        }
//
    }
    @Override
    public void onResume(){
        super.onResume();
        lists.clear(); //hapus data pas oncreate agar tidak double
        getDataRecordsExp_InckePieChartdanListExpen("EXP");
        SetupPieChartExpens_legend_CustomLegendEntry();
        lists2.clear(); //hapus data pas oncreate agar tidak double
        getDataRecordsExp_InckePieChartdanListExpen("INC");
        SetupPieChartIncome_legend_CustomLegendEntry();
    }
}