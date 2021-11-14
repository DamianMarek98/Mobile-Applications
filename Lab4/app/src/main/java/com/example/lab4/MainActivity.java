package com.example.lab4;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private EditText editTextPath;
    private TextView title;
    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;
    private Button recordButton;
    private boolean wasStopped;
    private boolean recordingStarted;
    private Uri fileUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/test.mp3");
    ActivityResultLauncher<Intent> chooseFileActivity;
    private static final int CREATE_FILE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        setContentView(R.layout.activity_main);
        prepareMediaButtons();
        Button buttonBrowse = findViewById(R.id.button_browse);
        buttonBrowse.setOnClickListener(a -> doBrowseFile());
        this.editTextPath = findViewById(R.id.editText_path);
        prepareMusicFile();
        prepareChooseFileActivity();
        this.recordButton = findViewById(R.id.record);
        this.recordButton.setOnClickListener(a -> {
            onRecord(recordingStarted);
            if (recordingStarted) {
                this.recordButton.setText("Start recording");
            } else {
                this.recordButton.setText("Stop recording");
            }
            recordingStarted = !recordingStarted;
        });
    }

    private void prepareChooseFileActivity() {
        this.chooseFileActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            this.fileUri = data.getData();
                            try {
                                File file = new File(this.fileUri.getPath());
                                this.editTextPath.setText(file.getPath());
                                this.title.setText(file.getName());
                                if (mediaPlayer != null) {
                                    mediaPlayer.stop();
                                    mediaPlayer.release();
                                    mediaPlayer = null;
                                }
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), fileUri);
                                wasStopped = false;
                                mediaPlayer.start();
                            } catch (Exception e) {
                                Log.e("Error", Arrays.toString(e.getStackTrace()));
                                this.editTextPath.setText("Wrong type of media!");
                            }
                        }
                    }
                });
    }

    private void prepareMediaButtons() {
        Button start = findViewById(R.id.start);
        Button stop = findViewById(R.id.stop);
        Button pause = findViewById(R.id.pause);

        start.setOnClickListener(a -> {
            if (wasStopped) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(),
                        fileUri);
                wasStopped = false;
            }
            mediaPlayer.start();
        });
        stop.setOnClickListener(a -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            wasStopped = true;
        });
        pause.setOnClickListener(a -> mediaPlayer.pause());
        this.title = findViewById(R.id.title);
    }

    private void prepareMusicFile() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), fileUri);
        File file = new File(this.fileUri.getPath());
        this.title.setText(file.getName());
    }

    private void doBrowseFile() {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("audio/*");
        // Only return URIs that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        this.chooseFileActivity.launch(chooseFileIntent);
    }

    private void onRecord(boolean inProgress) {
        if (inProgress) {
            stopRecording();
        } else {
            createFile();
        }
    }

    @SuppressLint("WrongConstant")
    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    public void startRecording(FileDescriptor fd) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(AudioFormat.ENCODING_PCM_16BIT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setAudioEncodingBitRate(128000);
        mediaRecorder.setAudioSamplingRate(48000);
        this.mediaRecorder.setOutputFile(fd);

        try {
            this.mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e("error", "dsa", e);
        }

        this.mediaRecorder.start();
        Snackbar.make(findViewById(R.id.main), "Recording started!", Snackbar.LENGTH_SHORT).show();
    }

    private void stopRecording() {
        Snackbar.make(findViewById(R.id.main), "Recording finished!", Snackbar.LENGTH_SHORT).show();
        this.mediaRecorder.stop();
        this.mediaRecorder.release();
        this.mediaRecorder = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CREATE_FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    FileDescriptor fd = this.getContentResolver().openFileDescriptor(data.getData(),
                            "rw").getFileDescriptor();
                    MainActivityPermissionsDispatcher.startRecordingWithPermissionCheck(this, fd);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        intent.putExtra(Intent.EXTRA_TITLE, "rec.wav");
        startActivityForResult(intent, CREATE_FILE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}