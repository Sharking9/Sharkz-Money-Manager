package com.Sharkz.Money_Manager.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.Sharkz.Money_Manager.EditorActivity;
import com.Sharkz.Money_Manager.R;
import com.Sharkz.Money_Manager.adapter.Adapter;
import com.Sharkz.Money_Manager.helper.Helper;
import com.Sharkz.Money_Manager.model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RecordsFragment extends Fragment {
    Helper db = new Helper(getContext());
    ListView listView;
    List<Data> lists = new ArrayList<>();
    Adapter adapter;

    int Expensbln, Incomebln;
    int Cash, Invest, Bank_jp;
    TextView txtexpensbln, txtincomebln;
    FloatingActionButton btnAdd;
    AlertDialog.Builder dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_records, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtexpensbln = view.findViewById(R.id.txtexpenBulanan);
        txtincomebln = view.findViewById(R.id.txtincomBulanan);


        db = new Helper(getContext());
        btnAdd = view.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditorActivity.class);
                startActivity(intent);
            }
        });
        listView = view.findViewById(R.id.list_item);
        adapter = new Adapter(getActivity(), lists, "RecordsListView");
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id2) {
                final String id = lists.get(position).getId();
                final String name = lists.get(position).getName();
                final String jumlah = lists.get(position).getJumlah();
                final String tanggal = lists.get(position).getTanggal();
                final String label = lists.get(position).getLabel();
                final String type = lists.get(position).getTypeEI();
                final String aset = lists.get(position).getAset();
                final CharSequence[] dialogItem = {"Edit", "Hapus"};
                dialog = new AlertDialog.Builder(getContext());
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i1) {
                        switch (i1){
                            case 0:
                                Intent intent = new Intent(getContext(), EditorActivity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("name", name);
                                intent.putExtra("jumlah", jumlah);
                                intent.putExtra("tanggal", tanggal);
                                intent.putExtra("label", label);
                                intent.putExtra("type", type);
                                intent.putExtra("aset", aset);
                                startActivity(intent);
                                break;
                            case 1:
                                db.deleteRecords(Integer.parseInt(id));
                                lists.clear();
                                getData();
                                break;
                        }
                    }
                }).show();
            }
        });
        getData();
        // Menampilkan pesan toast
        Toast.makeText(getContext(), "pesan toast "+ Expensbln, Toast.LENGTH_LONG).show();
        Log.d("TAG", "Masuk list2");
    }



    private void getData(){
        Expensbln = 0;
        Incomebln = 0;

        Cash = 0;
        Invest = 0;
        Bank_jp = 0;

        ArrayList<HashMap<String, String>> rows = db.getAll("DESC");
        for (int i = 0; i < rows.size(); i++){
            String id = rows.get(i).get("id");
            String name = rows.get(i).get("name");
            String jumlah = rows.get(i).get("jumlah");
            String tanggal = rows.get(i).get("tanggal");
            String label = rows.get(i).get("label");
            String typeEI = rows.get(i).get("type");
            String aset = rows.get(i).get("aset");
            String fixpm = "okk";

            // Mengambil ID drawable dari label
            int drawableId = getDrawableIdFromLabel(label);

            Data data = new Data();
            data.setId(id);
            data.setName(name);
            data.setJumlah(jumlah);
            data.setTanggal(tanggal);
            data.setLabel(label);
            data.setDrawableId(drawableId); // Mengatur ID drawable ke objek Data
            data.setTypeEI(typeEI);
            data.setAset(aset);
            if (Objects.equals(typeEI, "EXP")){ fixpm = "-"; Expensbln = Expensbln + Integer.parseInt(jumlah);}
            else if (typeEI.equals("INC")){ fixpm = "+"; Incomebln = Incomebln + Integer.parseInt(jumlah);}
            data.setPlusminus(fixpm);
            lists.add(data);


            //Update Aset Realtime
            if (aset.equals("Cash")){
                if (Objects.equals(typeEI, "EXP")){ Cash = Cash - Integer.parseInt(jumlah);}
                else if (typeEI.equals("INC")){ Cash = Cash + Integer.parseInt(jumlah);}
            }
            else if (aset.equals("Invest")){
                if (Objects.equals(typeEI, "EXP")){ Invest = Invest - Integer.parseInt(jumlah);}
                else if (typeEI.equals("INC")){ Invest = Invest + Integer.parseInt(jumlah);}
            }
            else if (aset.equals("Bank Jp")){
                if (Objects.equals(typeEI, "EXP")){ Bank_jp = Bank_jp - Integer.parseInt(jumlah);}
                else if (typeEI.equals("INC")){ Bank_jp = Bank_jp + Integer.parseInt(jumlah);}
            }

        }
        db.updateTotalAsets("Cash", String.valueOf(Cash));
        db.updateTotalAsets("Invest", String.valueOf(Invest));
        db.updateTotalAsets("Bank Jp", String.valueOf(Bank_jp));


        txtexpensbln.setText(String.valueOf(Expensbln));
        txtincomebln.setText(String.valueOf(Incomebln));


        adapter.notifyDataSetChanged();
    }
    private int getDrawableIdFromLabel(String label) {
        switch (label) {
            case "Food":
                return R.drawable.local_dining_24;
            case "Tax":
                return R.drawable.wallet_24;
            case "Health":
                return R.drawable.health_and_safety_24;
            case "Education":
                return R.drawable.menu_book_24;
            case "Transport":
                return R.drawable.directions_bus_24;
            case "Uang Jajan":
                return R.drawable.shopping_cart_24;
            case "Sport":
                return R.drawable.fitness_center_24;
            case "Other":
                return R.drawable.payment_24;
            case "Shodaqoh":
                return R.drawable.moderator_24;
            default:
                return R.drawable.payment_24;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        lists.clear();
        getData();
    }


}