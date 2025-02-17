package com.Sharkz.Money_Manager.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.Sharkz.Money_Manager.R;
import com.Sharkz.Money_Manager.model.Data;

import java.util.List;
import java.util.Objects;

public class Adapter extends BaseAdapter {
    private final Activity activity;
    private LayoutInflater inflater;
    private final List<Data> lists;
    private final String isListViewUse;

    public Adapter(Activity activity, List<Data> lists, String isListView){
        this.activity = activity;
        this.lists = lists;
        this.isListViewUse  = isListView;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null && inflater != null){
//            view = inflater.inflate(R.layout.list_users, null);
            if (Objects.equals(isListViewUse, "RecordsListView")) {
                view = inflater.inflate(R.layout.list_users, null, false);
            } else if (Objects.equals(isListViewUse, "AsetListView")) {
                view = inflater.inflate(R.layout.list_aset, null, false);
            } else if (Objects.equals(isListViewUse, "Select Aset")){
                view = inflater.inflate(R.layout.list_select_aset, null, false);
            }
        }
        if (view != null) {
            // Mengisi data ke tampilan sesuai dengan jenis tampilan
            if (Objects.equals(isListViewUse, "RecordsListView")) {
                TextView name = view.findViewById(R.id.text_name);
                TextView jumlah = view.findViewById(R.id.text_jumlah);
                TextView tgl = view.findViewById(R.id.text_tgl);
                TextView label = view.findViewById(R.id.text_label);
                TextView type = view.findViewById(R.id.txttypeEI);
                TextView aset = view.findViewById(R.id.txtaset);
                TextView plusminus = view.findViewById(R.id.plusminus);

                Data data = lists.get(i);
                name.setText(data.getName());
                jumlah.setText(data.getJumlah());
                tgl.setText(data.getTanggal());
                label.setText(data.getLabel());
                type.setText(data.getTypeEI());
                aset.setText(data.getAset());
                plusminus.setText(data.getPlusminus());

                // Mendapatkan Drawable dari ID Drawable
                Drawable drawable = ContextCompat.getDrawable(activity, data.getDrawableId());
                label.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

            } else if (Objects.equals(isListViewUse, "Select Aset")){
                // Mengisi data untuk ListView kedua
                TextView name_aset = view.findViewById(R.id.txt_name_aset);
                TextView tgl_buat = view.findViewById(R.id.txt_tgl_buat);
                TextView total = view.findViewById(R.id.txtTotal);

                Data data = lists.get(i);
                name_aset.setText(data.getAset());
                tgl_buat.setText(data.getCreate_date());
                total.setText(data.getTotal());

            } else if (Objects.equals(isListViewUse, "AsetListView")) {
                //isi data List view FragmentAset
                TextView name_aset = view.findViewById(R.id.fragaset_name_aset);
                TextView total = view.findViewById(R.id.fragaset_total_aset);

                Data data = lists.get(i);
                name_aset.setText(data.getAset());
                total.setText(data.getTotal());
            }

        }
        return view;

    }


}
