package com.Sharkz.Money_Manager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.Sharkz.Money_Manager.adapter.Adapter;
import com.Sharkz.Money_Manager.helper.Helper;
import com.Sharkz.Money_Manager.model.Data;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    InterstitialAd mInterstitialAd;
    ListView listView;
    AlertDialog.Builder dialog;
    List<Data> lists = new ArrayList<>();
    Adapter adapter;
    Helper db = new Helper(this);

    int Expensbln, Incomebln;
    TextView txtexpensbln, txtincomebln;
    FloatingActionButton btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        banner adds
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//        banner ads
//        intersial ads
//        AdRequest adRequest = new AdRequest.Builder().build(); dioff karena request udh dipanngil diatas

        InterstitialAd.load(this,"ca-app-pub-5293973152030630/1989562413", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });

        ImageView adsbutton = findViewById(R.id.btnads);
        adsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
        });

//        intersial ads

        txtexpensbln = findViewById(R.id.txtexpenBulanan);
        txtincomebln = findViewById(R.id.txtincomBulanan);


        db = new Helper(getApplicationContext());
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        listView = findViewById(R.id.list_item);
        adapter = new Adapter(MainActivity.this, lists);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id2) {
                final String id = lists.get(position).getId();
                final String name = lists.get(position).getName();
                final String jumlah = lists.get(position).getJumlah();
                final String tanggal = lists.get(position).getTanggal();
                final String label = lists.get(position).getLabel();
                final CharSequence[] dialogItem = {"Edit", "Hapus"};
                dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i1) {
                        switch (i1){
                            case 0:
                                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("name", name);
                                intent.putExtra("jumlah", jumlah);
                                intent.putExtra("tanggal", tanggal);
                                intent.putExtra("label", label);
                                startActivity(intent);
                                break;
                            case 1:
                                db.delete(Integer.parseInt(id));
                                lists.clear();
                                getData();
                                break;
                        }
                    }
                }).show();
            }
        });
        getData();
        txtexpensbln.setText(String.valueOf(Expensbln));
        // Menampilkan pesan toast
        Toast.makeText(getApplicationContext(), "pesan toast "+ Expensbln, Toast.LENGTH_LONG).show();

    }



    private void getData(){
        ArrayList<HashMap<String, String>> rows = db.getAll();
        for (int i = 0; i < rows.size(); i++){
            String id = rows.get(i).get("id");
            String name = rows.get(i).get("name");
            String jumlah = rows.get(i).get("jumlah");
            String tanggal = rows.get(i).get("tanggal");
            String label = rows.get(i).get("label");

            // Mengambil ID drawable dari label
            int drawableId = getDrawableIdFromLabel(label);

            Data data = new Data();
            data.setId(id);
            data.setName(name);
            data.setJumlah(jumlah);
            data.setTanggal(tanggal);
            data.setLabel(label);
            data.setDrawableId(drawableId); // Mengatur ID drawable ke objek Data
            lists.add(data);

            Expensbln = Expensbln + Integer.parseInt(jumlah);
        }


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
    protected void onResume(){
        super.onResume();
        lists.clear();
        getData();
    }

}