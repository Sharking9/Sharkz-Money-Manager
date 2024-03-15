package com.Sharkz.Money_Manager;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ButtonDeclarasi {
    public static void initializeButtons(EditorActivity context) {
        // Inisialisasi tombol di sini
        Button button1 = context.findViewById(R.id.food11);
        Button button2 = context.findViewById(R.id.tax);
        Button button3 = context.findViewById(R.id.health);
        Button button4 = context.findViewById(R.id.education);
        Button button5 = context.findViewById(R.id.trasport);
        Button button6 = context.findViewById(R.id.uang_jajan);
        Button button7 = context.findViewById(R.id.sport);
        Button button8 = context.findViewById(R.id.other);
        Button button9 = context.findViewById(R.id.shodaqoh);
        // Inisialisasi tombol lainnya

        // Set listener untuk tombol
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handler untuk klik tombol 1
                String btntxt = button1.getText().toString();
                context.handleButton1Click(btntxt);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dapatkan gambar dari tombol
                Drawable btndrawable = button2.getCompoundDrawables()[0]; // Anda mungkin perlu menyesuaikan indeks gambar
                Log.d("TAG", "Data getDrawble( "+ btndrawable + " ");
                // Set gambar ke TextView
                context.handleButton2Click(btndrawable);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btntxt = button3.getText().toString();
                context.handleButton3Click(btntxt);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btntxt = button4.getText().toString();
                context.handleButton4Click(btntxt);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btntxt = button5.getText().toString();
                context.handleButton5Click(btntxt);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btntxt = button6.getText().toString();
                context.handleButton6Click(btntxt);
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btntxt = button7.getText().toString();
                context.handleButton7Click(btntxt);
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btntxt = button8.getText().toString();
                context.handleButton8Click(btntxt);
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btntxt = button9.getText().toString();
                context.handleButton9Click(btntxt);
            }
        });



        Button angka1 = context.findViewById(R.id.num1);
        Button angka2 = context.findViewById(R.id.num2);
        Button angka3 = context.findViewById(R.id.num3);
        Button angka4 = context.findViewById(R.id.num4);
        Button angka5 = context.findViewById(R.id.num5);
        Button angka6 = context.findViewById(R.id.num6);
        Button angka7 = context.findViewById(R.id.num7);
        Button angka8 = context.findViewById(R.id.num8);
        Button angka9 = context.findViewById(R.id.num9);
        Button angka0 = context.findViewById(R.id.num0);

        angka1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  context.appendToCalctxt("1"); }
        });
        angka2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  context.appendToCalctxt("2"); }
        });
        angka3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  context.appendToCalctxt("3"); }
        });
        angka4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  context.appendToCalctxt("4"); }
        });
        angka5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  context.appendToCalctxt("5"); }
        });
        angka6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  context.appendToCalctxt("6"); }
        });
        angka7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  context.appendToCalctxt("7"); }
        });
        angka8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  context.appendToCalctxt("8"); }
        });
        angka9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  context.appendToCalctxt("9"); }
        });
        angka0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  context.appendToCalctxt("0"); }
        });


    }


}

