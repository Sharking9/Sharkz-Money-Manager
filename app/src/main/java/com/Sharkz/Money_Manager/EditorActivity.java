package com.Sharkz.Money_Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.Sharkz.Money_Manager.helper.Helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditorActivity extends AppCompatActivity {

    private EditText editName, editJumlah;
    private TextView editLabel;
    private Button editTanggal, editjam, delete;
    private Button btnSave;
    private Helper db = new Helper(this);
    private String id, name, jumlah, tanggal, label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ///Import Fungsi Class File DeklarasiButton Java
        ButtonDeclarasi.initializeButtons(this);
        ///Import Fungsi Class File DeklarasiButton Java

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan teks dari editJumlah
                String currentText = editJumlah.getText().toString();
                // Menghapus satu karakter dari belakang
                if (!currentText.isEmpty()) {
                    currentText = currentText.substring(0, currentText.length() - 1);
                }
                // Menetapkan teks yang baru ke calctxt
                editJumlah.setText(currentText);
            }
        });

        editName = findViewById(R.id.edit_name);
        editJumlah = findViewById(R.id.edit_jumlah);
        btnSave = findViewById(R.id.btn_save);
        editTanggal = findViewById(R.id.edit_tgl);
        editLabel = findViewById(R.id.edit_label);

        editjam = findViewById(R.id.edit_jam);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        jumlah = getIntent().getStringExtra("jumlah");
        tanggal = getIntent().getStringExtra("tanggal");
        label = getIntent().getStringExtra("label");

        if (id == null || id.equals("")){
            setTitle("Tambah User");
        }else {
            setTitle("Edit user");
            editName.setText(name);
            editJumlah.setText(jumlah);

            // Memisahkan tanggal dan waktu menggunakan spasi sebagai pemisah
            String[] parts = tanggal.split(" ");
            String tanggaledit = parts[0];
            String waktu = parts[1];
            editTanggal.setText(tanggaledit);
            editjam.setText(waktu);

            editLabel.setText(label);
        }

        //SET TGL default
        if (tanggal == null || tanggal.equals("")){
            // Mendapatkan tanggal dan waktu saat ini
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String dateTime = dateFormat.format(calendar.getTime());

            // Memisahkan tanggal dan waktu menggunakan spasi sebagai pemisah
            String[] parts = dateTime.split(" ");
            String tanggaledit = parts[0];
            String waktu = parts[1];

            // Memeriksa apakah tanggal kurang dari 10 dan menambahkan angka 0 di depannya jika ya
            if (tanggaledit.charAt(8) == ' ') {
                tanggaledit = "0" + tanggaledit.substring(9); // Mengganti tanggal dengan angka 0 di depannya
            }

            // Mengatur teks TextView dengan tanggal dan waktu saat ini
            editTanggal.setText(tanggaledit);
            editjam.setText(waktu);

        }
        //SET Label default
        if (label == null || label.equals("")){
            Button button1 = findViewById(R.id.food11);
            editLabel.setText(button1.getText().toString());
        }

        editTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        editjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (id == null || id.equals("")){
                        save();
                    } else {
                        edit();
                    }
                }catch (Exception e){
                    Log.e("Saving", e.getMessage());
                }
            }
        });



    }

    private void  save(){
        if (String.valueOf(editName.getText()).equals("") || String.valueOf(editJumlah.getText()).equals("") || String.valueOf(editTanggal.getText()).equals("") || String.valueOf(editjam.getText()).equals("") || String.valueOf(editLabel.getText()).equals("")){
            Toast.makeText(getApplicationContext(), "Silakan isi semua data!", Toast.LENGTH_SHORT).show();
        } else {
            String fixDatetime = editTanggal.getText().toString() + " " + editjam.getText().toString();  ///Gabung Date Time
            db.insert(editName.getText().toString(), editJumlah.getText().toString(), fixDatetime, editLabel.getText().toString());
            finish();
        }
    }

    private void  edit(){
        if (String.valueOf(editName.getText()).equals("") || String.valueOf(editJumlah.getText()).equals("") || String.valueOf(editTanggal.getText()).equals("") || String.valueOf(editjam.getText()).equals("")  || String.valueOf(editLabel.getText()).equals("")){
            Toast.makeText(getApplicationContext(), "Silakan isi semua data!", Toast.LENGTH_SHORT).show();
        } else {
            String fixDatetime = editTanggal.getText().toString() + " " + editjam.getText().toString();   ///Gabung Date Time
            db.update(Integer.parseInt(id), editName.getText().toString(), editJumlah.getText().toString(), fixDatetime, editLabel.getText().toString());
            finish();
        }
    }


    private void showDatePickerDialog() {
        final Calendar currentDate = Calendar.getInstance();
        // Memisahkan tanggal bulan thn
        String[] parts = editTanggal.getText().toString().split("-");
        int tgl24 = Integer.parseInt(parts[0]);
        int bln24 = Integer.parseInt(parts[1])-1; //aturan bln dikurangi 1
        int thn24 = Integer.parseInt(parts[2]);
//        int year = currentDate.get(Calendar.YEAR);
//        int month = currentDate.get(Calendar.MONTH);
//        int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                // Menambahkan angka 0 di depan selectedDayOfMonth jika kurang dari 10
                String formattedSelectedDay = (selectedDayOfMonth < 10) ? "0" + selectedDayOfMonth : String.valueOf(selectedDayOfMonth);
                String formattedSelectedMonth = ((selectedMonth + 1) < 10) ? "0" + (selectedMonth + 1) : String.valueOf((selectedMonth + 1));

                // Update TextView with selected date
                editTanggal.setText(selectedYear + "-" + formattedSelectedMonth + "-" + formattedSelectedDay);
            }
        }, tgl24, bln24, thn24);

        // Show DatePickerDialog
        datePickerDialog.show();
    }
    private void showTimePickerDialog() {
        final Calendar currentTime = Calendar.getInstance();
        // Memisahkan tanggal bulan thn
        String[] parts = editjam.getText().toString().split(":");
        int Jam24 = Integer.parseInt(parts[0]);
        int Menit24 = Integer.parseInt(parts[1]);
//        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                // Update TextView with selected time
                editjam.setText(String.format("%02d:%02d:%02d", selectedHour, selectedMinute, 0));
            }
        }, Jam24, Menit24, true); // true untuk mode spinner

        timePickerDialog.show();
    }

    public void appendToCalctxt(String inputan) {
        String data1 = editJumlah.getText().toString();
        editJumlah.setText(data1 + inputan);
    };

    // Handler untuk klik tombol icon label dari Deklarasi ButtonDeclarasi.java
    // Lakukan sesuatu saat tombol diklik
    public void handleButton1Click(String txt22) { editLabel.setText(txt22); }
    public void handleButton2Click(Drawable btndraw) { editLabel.setCompoundDrawablesWithIntrinsicBounds(btndraw, null, null, null);; }
    public void handleButton3Click(String txt22) { editLabel.setText(txt22); }
    public void handleButton4Click(String txt22) { editLabel.setText(txt22); }
    public void handleButton5Click(String txt22) { editLabel.setText(txt22); }
    public void handleButton6Click(String txt22) { editLabel.setText(txt22); }
    public void handleButton7Click(String txt22) { editLabel.setText(txt22); }
    public void handleButton8Click(String txt22) { editLabel.setText(txt22); }
    public void handleButton9Click(String txt22) { editLabel.setText(txt22); }

    // Handler untuk klik tombol icon label dari Deklarasi ButtonDeclarasi.java




}