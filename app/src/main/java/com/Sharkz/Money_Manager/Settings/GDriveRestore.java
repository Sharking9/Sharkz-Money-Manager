package com.Sharkz.Money_Manager.Settings;

import static com.google.android.gms.auth.api.Auth.GoogleSignInApi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.Sharkz.Money_Manager.R;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;


public class GDriveRestore extends AppCompatActivity {

        private static final int RC_SIGN_IN = 1001;
        private GoogleSignInClient mGoogleSignInClient;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_g_drive_restore);

            // Konfigurasi Google Sign-In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            // Tombol untuk mulai sign-in
            Button btnGDRestore = findViewById(R.id.btnGDRestore);
            btnGDRestore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn();
                }
            });
        }

        // Menangani sign-in dengan Google
        private void signIn() {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                // Sign-in berhasil, dapatkan akun Google yang sign-in
                GoogleSignInAccount account = task.getResult();
                handleSignInResult(account);
            } else {
                // Sign-in gagal, tangani kegagalannya
                Log.e("GDriveRestore", "Google sign-in failed", task.getException());
            }
        }
    }


    // Handle hasil sign-in
        private void handleSignInResult(GoogleSignInAccount account) {
            // Akses Google Drive API menggunakan token akses
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Drive driveService = getDriveService(account);
                        String fileName = "Sharkz_Money_Backup.db";  // Nama file yang ingin Anda cari di Google Drive
                        downloadFileFromDriveByName(driveService, fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    // Inisialisasi Drive API dengan autentikasi Google
    private Drive getDriveService(GoogleSignInAccount account) {
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                this, Collections.singleton(DriveScopes.DRIVE_FILE));
        credential.setSelectedAccount(account.getAccount()); // Menetapkan akun pengguna yang terautentikasi

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = new GsonFactory();

        return new Drive.Builder(transport, jsonFactory, new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
                String accessToken = null;
                try {
                    accessToken = credential.getToken();
                } catch (GoogleAuthException e) {
                    throw new RuntimeException(e);
                }
                request.getHeaders().setAuthorization("Bearer " + accessToken);
            }
        }).setApplicationName("YourAppName").build();
    }

    // Mencari file berdasarkan nama dan mendownloadnya
    private void downloadFileFromDriveByName(Drive driveService, String fileName) throws IOException {
        // Menggunakan query untuk mencari file berdasarkan nama
        FileList result = driveService.files().list()
                .setQ("name='" + fileName + "'")  // Query untuk mencari file dengan nama yang cocok
                .setSpaces("drive")
                .setFields("files(id, name)")  // Mendapatkan ID dan nama file
                .execute();

        if (result.getFiles().isEmpty()) {
            Log.e("Restore", "File not found: " + fileName);
        } else {
            for (com.google.api.services.drive.model.File file : result.getFiles()) {
                String fileId = file.getId();
                Log.d("Restore", "Found file: " + file.getName() + " (ID: " + fileId + ")");
                // Setelah file ditemukan, download file tersebut
                downloadFileFromDrive(driveService, fileId);
            }
        }
    }

    // Mendownload file dari Google Drive dan menggantikan database aplikasi
    private void downloadFileFromDrive(Drive driveService, String fileId) throws IOException {
        // Mendapatkan metadata file dari Google Drive
        com.google.api.services.drive.model.File file = driveService.files().get(fileId).execute();

        // Menyimpan file yang diunduh ke storage lokal
        FileOutputStream fileOutputStream = new FileOutputStream(new File(this.getFilesDir(), "moneydb"));

        // Mengunduh file dan menyimpannya ke dalam storage lokal
        driveService.files().get(fileId).executeMediaAndDownloadTo(fileOutputStream);
        fileOutputStream.close();

        // Menimpa file database aplikasi dengan file yang baru saja diunduh
        File currentDbFile = new File(this.getFilesDir(), "moneydb");
        File downloadedDbFile = new File(this.getFilesDir(), "moneydb");
        Log.d("Restore", "File size of downloaded file: " + downloadedDbFile.length());

        if (downloadedDbFile.exists()) {
            boolean isRenamed = downloadedDbFile.renameTo(currentDbFile);
            if (isRenamed) {
                Log.d("Restore", "Current DB file exists: " + currentDbFile.exists());
                Log.d("Restore", "Downloaded DB file exists: " + downloadedDbFile.exists());

                Log.d("Restore", "Database restored successfully");
                finish();
            } else {
                Log.e("Restore", "Failed to rename the backup file to current DB");
            }
        } else {
            Log.e("Restore", "Downloaded backup file not found");
        }
    }
}