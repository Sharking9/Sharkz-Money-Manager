package com.Sharkz.Money_Manager.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask; // Import AsyncTask
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Sharkz.Money_Manager.R;
import com.Sharkz.Money_Manager.Settings.GDriveRestore;
import com.Sharkz.Money_Manager.helper.Helper;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import android.app.Activity;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Collections;

import java.io.FileOutputStream;
import java.io.OutputStream;


public class SettingFragment extends Fragment {

    private static final int REQUEST_CODE_SIGN_IN = 0;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private GoogleSignInClient mGoogleSignInClient;
    private Drive driveService;
    private String action;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        view.findViewById(R.id.GDBackUp).setOnClickListener(v -> {
            action = "backup";
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
        });
        view.findViewById(R.id.GDRestore).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), GDriveRestore.class);
            intent.putExtra("type", "EXP");
            startActivity(intent);
        });
//        view.findViewById(R.id.GDRestore).setOnClickListener(v -> {
//            action = "restore";
//            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//            startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                new AccessGoogleDriveTask().execute(account);
            } catch (ApiException e) {
                Log.w("GoogleSignIn", "Sign in failed", e);
            }
        } else if (requestCode == REQUEST_AUTHORIZATION) {
            if (resultCode == Activity.RESULT_OK) {
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
                new AccessGoogleDriveTask().execute(account);
            } else {
                Log.w("GoogleAuth", "User declined authorization");
            }
        }
    }

    private class AccessGoogleDriveTask extends AsyncTask<GoogleSignInAccount, Void, GoogleAccountCredential> {
        @Override
        protected GoogleAccountCredential doInBackground(GoogleSignInAccount... accounts) {
            GoogleSignInAccount account = accounts[0];
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    getContext(), Collections.singleton(DriveScopes.DRIVE_FILE));
            credential.setSelectedAccount(account.getAccount());
            try {
                credential.getToken();
            } catch (UserRecoverableAuthIOException e) {
                Intent intent = e.getIntent();
                startActivityForResult(intent, REQUEST_AUTHORIZATION);
            } catch (IOException | GoogleAuthException e) {
                Log.e("GoogleDrive", "Error getting token", e);
            }
            return credential;
        }

        @Override
        protected void onPostExecute(GoogleAccountCredential credential) {
            if (credential != null) {
                initializeDriveService(credential);
                if ("backup".equals(action)) {
                    Log.d("Drive", "Masuk Proses Backup");
                    checkAndUploadFileToDrive();
                } else if ("restore".equals(action)) {

                    listFilesOnDrive(); // Mencari file backupdp.db di Google Drive
                }
            }
        }
    }

    private void initializeDriveService(GoogleAccountCredential credential) {
        driveService = new Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                credential)
                .setApplicationName("Sharkz Money Manager")
                .build();
    }

    private void checkAndUploadFileToDrive() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                java.io.File filePath = new java.io.File(getContext().getDatabasePath("moneydb").getPath());

                if (!filePath.exists()) {
                    Log.e("Backup", "Database file does not exist.");
                    return null; // Jika file tidak ada, hentikan proses
                }

                try {
                    // Cari file dengan nama yang sama
                    FileList files = driveService.files().list()
                            .setQ("name='Sharkz_Money_Backup.db'")
                            .setSpaces("drive")
                            .setFields("files(id, name)")
                            .execute();
                    for (File file : files.getFiles()) {
                        // Hapus file yang ada jika ditemukan
                        driveService.files().delete(file.getId()).execute();
                        Log.d("Drive", "File deleted: " + file.getName() + " (ID: " + file.getId() + ")");
                    }
                    // Lanjutkan dengan mengunggah file baru
                    uploadFileToDrive(filePath);
                } catch (IOException e) {
                    Log.e("Drive", "Error checking or deleting files from Google Drive", e);
                }
                return null;
            }
        }.execute();
    }


    private void uploadFileToDrive(java.io.File filePath) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                File fileMetadata = new File();
                fileMetadata.setName("Sharkz_Money_Backup.db");
                fileMetadata.setParents(Collections.singletonList("root"));

                FileContent mediaContent = new FileContent("application/octet-stream", filePath);

                try {
                    File file = driveService.files().create(fileMetadata, mediaContent)
                            .setFields("id")
                            .execute();
                    Log.d("Backup", "File uploaded successfully, file ID: " + file.getId());
                } catch (UserRecoverableAuthIOException e) {
                    // Meminta izin dari pengguna
                    Intent intent = e.getIntent();
                    startActivityForResult(intent, REQUEST_AUTHORIZATION);
                } catch (IOException e) {
                    Log.e("Backup", "Error uploading file to Google Drive", e);
                }
                return null;
            }
        }.execute();
    }


    private void listFilesOnDrive() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    FileList files = driveService.files().list()
                            .setQ("name='Sharkz_Money_Backup.db'")
                            .setSpaces("drive")
                            .setFields("files(id, name)")
                            .execute();

                    for (File file : files.getFiles()) {
                        Log.d("Drive", "File found: " + file.getName() + " (ID: " + file.getId() + ")");
                        downloadFileFromDrive(file.getId());  // Mengunduh file jika ditemukan
                    }
                } catch (IOException e) {
                    Log.e("Drive", "Error retrieving files from Google Drive", e);
                }
                return null;
            }
        }.execute();
    }

    private void downloadFileFromDrive(String fileId) {
        new AsyncTask<String, Void, java.io.File>() {
            @Override
            protected java.io.File doInBackground(String... params) {
                String fileId = params[0];
                try {
                    File file = driveService.files().get(fileId).execute();
                    java.io.File outputFile = new java.io.File(getContext().getDatabasePath("moneydb").getPath());

                    OutputStream outputStream = new FileOutputStream(outputFile);
                    driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
                    outputStream.close();

                    return outputFile;
                } catch (IOException e) {
                    Log.e("Drive", "Error downloading file", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(java.io.File result) {
                if (result != null) {
                    Log.d("Restore", "File restored successfully");
                    restoreDatabase(result);
                } else {
                    Log.e("Restore", "Error restoring file");
                }
            }
        }.execute(fileId);
    }

    private synchronized void restoreDatabase(java.io.File downloadedFile) {
        java.io.File targetFile = new java.io.File(getContext().getDatabasePath("moneydb").getPath());

        if (!downloadedFile.exists()) {
            Log.e("Restore", "Downloaded file not found.");
            return; // Jika file yang diunduh tidak ada, hentikan proses
        }

        try {
            // Menggunakan FileChannel untuk transfer data
            FileChannel src = new FileInputStream(downloadedFile).getChannel();
            FileChannel dst = new FileOutputStream(targetFile).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();

            Log.d("Restore", "Database restored successfully");
        } catch (IOException e) {
            Log.e("Restore", "Error restoring database", e);
        }
    }

}













