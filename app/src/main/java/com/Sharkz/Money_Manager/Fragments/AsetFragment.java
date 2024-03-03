package com.Sharkz.Money_Manager.Fragments;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
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
import com.Sharkz.Money_Manager.model.MyColorTemplate;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class AsetFragment extends Fragment {
    Helper db = new Helper(getContext());
    ListView listView;
    List<Data> lists = new ArrayList<>();
    Adapter adapter;

    PieChart pieChart;
    PieDataSet pieDataSet;
    ArrayList<PieEntry> entries22;
    LineChart lineChart3;
    ArrayList<Entry> dataVals1, dataVals2, dataVals3, dataVals4;
    LineDataSet lineDataSet1,lineDataSet2,lineDataSet3,lineDataSet4;
    TextView txttotalasets, txttotalliabilities;
    int Total_Aset;
    int fixpm, fixpm2;
    HashMap<String, Integer> dataAset;
    String blnAset = "09", blnAset2 = "09";

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
        lineChart3 = view.findViewById(R.id.linechart3);


        db = new Helper(getContext());
        getDataAsetkePieChart();
        SetupPieChart_legend_CustomLegendEntry();
        getDataAsetkeLineChart();
        SetupLineChart3();

        listView = view.findViewById(R.id.list_main_aset);
        adapter = new Adapter(getActivity(), lists, "AsetListView");
        listView.setAdapter(adapter);
        getDataListAset();





    }


    private void getDataAsetkePieChart(){
        Total_Aset = 0;

        entries22 = new ArrayList<>();

        ArrayList<HashMap<String, String>> rows = db.getAllAset();
        for (int i = 0; i < rows.size(); i++){
            String name_aset = rows.get(i).get("name_aset");
            String total = rows.get(i).get("total");
            if (Integer.parseInt(total)>=0){
                entries22.add(new PieEntry(Integer.parseInt(total),name_aset));
            }
            Total_Aset = Total_Aset + Integer.parseInt(total);
        }

        pieDataSet = new PieDataSet(entries22, "Your Assets");
        pieDataSet.setColors(MyColorTemplate.MATERIAL_COLORS);

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
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXOffset(20);
        legend.setYOffset(-100);

        // Tambahkan legend
        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        List<Integer> colors = pieDataSet.getColors(); // Mengambil warna dari setiap entri pie chart

        for (int i = 0; i < entries22.size(); i++) {
            PieEntry entry = entries22.get(i);
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

    private void SetupLineChart3(){
        lineDataSet1 = new LineDataSet(dataVals1, "Cash");
        lineDataSet1.setColor(Color.BLUE);
        lineDataSet2 = new LineDataSet(dataVals2, "Invest");
        lineDataSet2.setColor(Color.RED);
        lineDataSet3 = new LineDataSet(dataVals3, "Bank Jp");
        lineDataSet3.setColor(Color.GREEN);
        lineDataSet4 = new LineDataSet(dataVals4, "Smiles");
        lineDataSet4.setColor(Color.YELLOW);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        dataSets.add(lineDataSet3);
        dataSets.add(lineDataSet4);

        lineChart3.setBackgroundColor(Color.GRAY);
        lineChart3.setNoDataText("No Data");
        lineChart3.setNoDataTextColor(Color.BLUE);
        lineChart3.setDrawGridBackground(true);
        lineChart3.setDrawBorders(false);
        lineChart3.setBorderColor(Color.RED);
        lineChart3.setBorderWidth(5);

        LineData data = new LineData(dataSets);
        lineChart3.setData(data);
        lineChart3.invalidate();
    }
    private void getDataAsetkeLineChart(){
        dataVals1 = new ArrayList<Entry>();
        dataVals2 = new ArrayList<Entry>();
        dataVals3 = new ArrayList<Entry>();
        dataVals4 = new ArrayList<Entry>();

        getAllAset();

        ArrayList<HashMap<String, String>> rows = db.getAll("ASC");
        int Tempcash = 0, TempInvest = 0;
        int Temp = 0, Temp2 = 0;
        for (int i = 0; i < rows.size(); i++) {
            String aset = rows.get(i).get("aset");
            String tanggal = rows.get(i).get("tanggal");
            String jumlah = rows.get(i).get("jumlah");
            String typeEI = rows.get(i).get("type");
            String tanggalnext, asetnext;
            // Memastikan kita tidak mencoba mengakses elemen setelah indeks terakhir
            if (i < rows.size() - 1) {
                tanggalnext = rows.get(i + 1).get("tanggal");
                // Lakukan operasi lain dengan tanggalnext
            } else {
                tanggalnext = "2024-09-24 17:53:40";
            }

            fixpmfunc(typeEI, jumlah); //2500 invest 2  Tcash ada Tinves ada
            pisahbln(tanggal);  //02
            pisahbln2(tanggalnext);  //03

            if (Objects.equals(blnAset, blnAset2)) {
                if (dataAset.containsKey(aset)) {
                    Temp = dataAset.get(aset) + fixpm;
                    dataAset.put(aset, Temp); //Simpan Sementara diHash data Label
                    Log.d("TAG", "add dataAset bln sama( "+ aset + ") =  " +Temp+ " by record " +fixpm);
                    Temp = 0;
                }
//                if (Objects.equals(aset, "Cash")) {
//                    fixpm = Tempcash + fixpm;
//                    Tempcash = fixpm;
//                }else if (Objects.equals(aset, "Invest")) {
//                    fixpm = TempInvest + fixpm;
//                    TempInvest = fixpm;
//                }
            } else if (!Objects.equals(blnAset, blnAset2)) {
                if (dataAset.get(aset) !=0){
                    Temp = dataAset.get(aset) + fixpm;
                    dataAset.put(aset, Temp); //Last Data Simpan Sementara diHash data Label
                    Log.d("TAG", "dataAset bln beda, Tempada= "+ aset + " masukin " +Temp+ "Temp Reset");
                } else if (dataAset.get(aset) == 0) {
                    dataAset.put(aset, fixpm);
                    Log.d("TAG", "dataAset bln beda, Temp0= "+ aset + " input record " +fixpm);
                }

                for (String label24 : dataAset.keySet()) {
                    int Temp3 = dataAset.get(label24);
                    if (Temp3 != 0){
                        ///Pilih Listview dan tambahkan
                        if (label24.equals("Cash")){
                            dataVals1.add(new Entry(Float.parseFloat(blnAset), Float.parseFloat(String.valueOf(Temp3))));
                            Log.d("TAG", "Foradd dataline Cash = "+ label24 + " = " +Temp3);
                        }else if (label24.equals("Invest")){
                            dataVals2.add(new Entry(Float.parseFloat(blnAset), Float.parseFloat(String.valueOf(Temp3))));
                            Log.d("TAG", "Foradd dataline Invest = "+ label24 + " = " +Temp3);
                        }else if (label24.equals("Bank Jp")){
                            dataVals3.add(new Entry(Float.parseFloat(blnAset), Float.parseFloat(String.valueOf(Temp3))));
                            Log.d("TAG", "Foradd dataline Bank Jp = "+ label24 + " = " +Temp3);
                        }else if (label24.equals("Smiles")){
                            dataVals4.add(new Entry(Float.parseFloat(blnAset), Float.parseFloat(String.valueOf(Temp3))));
                            Log.d("TAG", "Foradd dataline Smiles = "+ label24 + " = " +Temp3);
                        }
                        dataAset.put(label24,0); //nilai sudah dimasukan ke linedata jadi reset utk tampung data bln depan
                    }

                }
//                if (Tempcash != 0){
//                    if (Objects.equals(aset, "Cash")){
//                        fixpm = Tempcash + fixpm;
//                    } else if (Objects.equals(aset, "Invest")){
//                        fixpm2 = Tempcash;
//                        dataVals1.add(new Entry(Float.parseFloat(blnAset), Float.parseFloat(String.valueOf(fixpm2))));
////                        Log.d("TAG", "Masuk getdataLine TempCash dari fix aset "+aset+" "+ blnAset+" "+blnAset2+" "+fixpm2);
//                    }
//                    Tempcash = 0; //total diset dan temp reset
//                }
//                if (TempInvest != 0){
//                    if (Objects.equals(aset, "Invest")){
//                        fixpm = TempInvest + fixpm;
//                    } else if (Objects.equals(aset, "Cash")){
//                        fixpm2 = TempInvest;
//                        dataVals2.add(new Entry(Float.parseFloat(blnAset), Float.parseFloat(String.valueOf(fixpm2))));
////                        Log.d("TAG", "Masuk getdataLine TempInvest dari fix aset "+aset+" "+ blnAset+" "+blnAset2+" "+fixpm2);
//                    }
//                    TempInvest = 0; //total diset dan temp reset
//                }
//                if (Tempcash == 0 && TempInvest == 0) {
//                    if (Objects.equals(aset, "Cash")) {
//                        dataVals1.add(new Entry(Float.parseFloat(blnAset), Float.parseFloat(String.valueOf(fixpm))));
////                        Log.d("TAG", "Masuk For getdataLine21 "+aset+" "+ blnAset+" "+blnAset2+" "+fixpm);
//
//                    }else if (Objects.equals(aset, "Invest")) {
//                        dataVals2.add(new Entry(Float.parseFloat(blnAset), Float.parseFloat(String.valueOf(fixpm))));
////                        Log.d("TAG", "Masuk For getdataLine22 "+aset+" "+ blnAset+" "+blnAset2+" "+fixpm);
//
//                    }
//                }
            }


        }


    }
    private void fixpmfunc(String typeEI2, String jumlah){
        fixpm = 0;
        if (Objects.equals(typeEI2, "EXP")){
            fixpm = fixpm - Integer.parseInt(jumlah);
        }
        else if (typeEI2.equals("INC")){
            fixpm = fixpm + Integer.parseInt(jumlah);
        }
    }
    private void pisahbln(String dateTime){
        // Memisahkan tanggal dan waktu menggunakan spasi sebagai pemisah
        String[] parts = dateTime.split(" ");
        String tanggaledit = parts[0];
        String waktu = parts[1];
        String[] parts2 = tanggaledit.split("-");
        blnAset = parts2[1];


        // Memeriksa apakah tanggal kurang dari 10 dan menambahkan angka 0 di depannya jika ya
//        if (tanggaledit.charAt(8) == ' ') {
//            tanggaledit = "0" + tanggaledit.substring(9); // Mengganti tanggal dengan angka 0 di depannya
//        }
    }
    private void pisahbln2(String dateTime){
        // Memisahkan tanggal dan waktu menggunakan spasi sebagai pemisah
        String[] parts = dateTime.split(" ");
        String tanggaledit = parts[0];
        String waktu = parts[1];
        String[] parts2 = tanggaledit.split("-");
        blnAset2 = parts2[1];


        // Memeriksa apakah tanggal kurang dari 10 dan menambahkan angka 0 di depannya jika ya
//        if (tanggaledit.charAt(8) == ' ') {
//            tanggaledit = "0" + tanggaledit.substring(9); // Mengganti tanggal dengan angka 0 di depannya
//        }
    }
    private void getAllAset(){
        ArrayList<HashMap<String, String>> rowslabel = db.getAllAset();
        dataAset = new HashMap<>();

        for (int i = 0; i < rowslabel.size(); i++){
            String name_aset = rowslabel.get(i).get("name_aset");
            dataAset.put(name_aset, 0);
//            Log.d("TAG", "dataAset, "+ name_aset + " nilai "+total+" Diadd.");
        }
    }

    private void getDataListAset(){
        ArrayList<HashMap<String, String>> rows = db.getAllAset();
        for (int i = 0; i < rows.size(); i++){
            String id = rows.get(i).get("id");
            String name_aset = rows.get(i).get("name_aset");
            String create_date = rows.get(i).get("create_date");
            String label = rows.get(i).get("label");
            String total = rows.get(i).get("total");

            // Mengambil ID drawable dari label
//            int drawableId = getDrawableIdFromLabel(label);

            Data data = new Data();
            data.setId(id);
            data.setAset(name_aset);
//            data.setCreate_date(create_date);
//            data.setLabel(label);
//            data.setDrawableId(drawableId); // Mengatur ID drawable ke objek Data
            data.setTotal(total);
            lists.add(data);

        }


        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();
        getDataAsetkePieChart();
        SetupPieChart_legend_CustomLegendEntry();
        getDataAsetkeLineChart();
        SetupLineChart3();
    }




}