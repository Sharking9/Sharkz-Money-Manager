package com.Sharkz.Money_Manager;

import static com.Sharkz.Money_Manager.R.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.Sharkz.Money_Manager.helper.Helper;
import com.Sharkz.Money_Manager.model.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tag_Manager extends AppCompatActivity {
    Helper db = new Helper(this);
    List<Data> lists = new ArrayList<>();

    GridLayout Grid_label_01;

    Button Back_btn;

    String name_Label, type, Category;
    int DrawBtnInt;
    String DrawBtnStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_manager);



        Grid_label_01 = findViewById(R.id.Grid_Label1);
        getDataGrid("label", "EXP");


        int index = 0;
        while (index < lists.size()) {
            // Membuat satu baris dengan 4 tombol
            for (int i = 0; i < 3 && index < lists.size(); i++) {
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

                        // Membuat dialog pilihan
                        AlertDialog.Builder builder = new AlertDialog.Builder(Tag_Manager.this);
                        builder.setTitle("Edit or Delete");

                        // Menambahkan pilihan untuk Edit dan Delete
                        builder.setItems(new CharSequence[] {"Edit", "Delete"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: // Edit
                                        // Menambahkan logika untuk Edit
                                        Intent intent = new Intent(Tag_Manager.this, TagEdit.class);

                                        intent.putExtra("namelbl", data.getName());
                                        intent.putExtra("tipelbl", data.getTypeEI());
                                        intent.putExtra("DrawBtnStr1", data.getDrawableStr());
                                        intent.putExtra("DrawBtnInt1", String.valueOf(data.getDrawableId()));
                                        startActivity(intent);
                                        break;

                                    case 1: // Delete
                                        // Menambahkan logika untuk Delete

                                        break;
                                }
                            }
                        });
                        // Menampilkan dialog
                        builder.show();


                    }
                });



                Grid_label_01.addView(button);
                Log.d("TAG", "add GridLabel( "+ data.getName() + ") =  " +params+ " by record ");
                index++;
            }
        }





        Back_btn = findViewById(id.Backbtn);
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



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