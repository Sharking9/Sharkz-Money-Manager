package com.Sharkz.Money_Manager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Sharkz.Money_Manager.adapter.Adapter;
import com.Sharkz.Money_Manager.helper.Helper;
import com.Sharkz.Money_Manager.model.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TagEdit extends AppCompatActivity {

    Helper db = new Helper(this);
    List<Data> lists = new ArrayList<>();
    Adapter adapter;
    GridLayout Grid_label_014;

    Button Save_labelBtn,Back_btn;
    TextView LabelTempView, LabelTempDrawid;
    EditText LabelNametxt;
    String name_Label, type, Category;
    int DrawBtnInt;
    String DrawBtnStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_edit);

        LabelTempView = findViewById(R.id.LabelTemp);
        LabelTempDrawid = findViewById(R.id.LabelTempDrawId);
        LabelNametxt = findViewById(R.id.nama_labelTemp);

        type = "EXP";
        Category = "Food";


        String namelbl = getIntent().getStringExtra("namelbl");
        String tipelbl = getIntent().getStringExtra("tipelbl");
        String DrawBtnStr1 = getIntent().getStringExtra("DrawBtnStr1");
        String DrawBtnInt1 = getIntent().getStringExtra("DrawBtnInt1");
        if (namelbl != null && !namelbl.isEmpty() && DrawBtnInt1 != null && !DrawBtnInt1.isEmpty()){
            LabelNametxt.setText(namelbl);
            type = tipelbl;
            Log.d("TAG", "Edit Label( "+ DrawBtnInt1 + ") = laporan ");
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), Integer.parseInt(DrawBtnInt1)); //Convert Int Drawable Ke Drawable
            LabelTempView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null); ///set Ke Tempview
            LabelTempDrawid.setText(DrawBtnStr1); ///simpan DrawStr ke Item Hide
        }



        Grid_label_014 = findViewById(R.id.Grid_Label14);

        getDataGrid("label", "EXP");


        int index = 0;
        while (index < lists.size()) {
            // Membuat satu baris dengan 4 tombol
            for (int i = 0; i < 4 && index < lists.size(); i++) {
                final Data data = lists.get(index);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.columnSpec = GridLayout.spec(i);
                params.rowSpec = GridLayout.spec(index / 4);
                params.setMargins(5, 5, 5, 5);

                Button button = new Button(this);

                name_Label = data.getName();
                type = data.getTypeEI();
                DrawBtnStr = data.getDrawableStr(); ///Ambil Data String Drawable
                DrawBtnInt = data.getDrawableId(); ///Ambil Data Int Drawable
                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), DrawBtnInt); //Convert Int Drawable Ke Drawable
                button.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null); ///Set ke Button

                button.setText(name_Label);
                button.setLayoutParams(params);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Tambahkan kode untuk aksi ketika tombol diklik

                        LabelTempView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null); ///set Ke Tempview
                        LabelTempDrawid.setText(DrawBtnStr); ///simpan DrawStr ke Item Hide
                    }
                });

                Grid_label_014.addView(button);
                Log.d("TAG", "add GridLabel( "+ data.getName() + ") =  " +params+ " by record ");
                index++;
            }
        }










        Back_btn = findViewById(R.id.Backbtn);
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Save_labelBtn = findViewById(R.id.Save_label);
        Save_labelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btndrawable = LabelTempDrawid.getText().toString(); // get Drawable int Dari TempDrawId yg telah diset dan hidden
                save(LabelNametxt.getText().toString(), type, btndrawable, Category);
            }
        });

    }

    private void  save(String namaLabel, String Type, String Drawable, String Category){
        if (namaLabel.equals("") ||  Drawable.equals("")){
            Toast.makeText(getApplicationContext(), "Silakan isi semua data!", Toast.LENGTH_SHORT).show();
        } else {
            db.insertLabel(namaLabel, Type, Drawable, Category);
            finish();
            Log.d("TAG", "Save Label "+ namaLabel + "+" +Drawable+ " by rec ");
        }
    }

    private void getDataGrid(String Label, String TypeEI){


        ArrayList<HashMap<String, String>> rows = db.getAllLabel_Exp_Inc("EXP");

        for (int i = 0; i < rows.size(); i++){
            String id = rows.get(i).get("id");
            String name = rows.get(i).get("name_label");
            String typeEI = rows.get(i).get("type");
            String drawable = rows.get(i).get("drawable");
            String category = rows.get(i).get("category");

            // Mengambil ID drawable dari label
            int drawableId = getDrawableIdFromLabel(drawable);

            Data data = new Data();
            data.setId(id);
            data.setName(name);
            data.setTypeEI(typeEI);
            data.setDrawableId(drawableId); // Mengatur ID drawable ke objek Data
            data.setDrawableStr(drawable); //Simpan Data String Draw
            data.setLabel(category);
            lists.add(data);

        }



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



}