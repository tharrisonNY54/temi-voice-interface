package com.example.leaveamessage;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnRobotReadyListener {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1234;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private Robot robot;
    private TextView statusText;
    private ImageButton recordButton;
    private Button retryButton;
    private Spinner professorSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.statusText);
        recordButton = findViewById(R.id.buttonRecord);
        retryButton = findViewById(R.id.buttonRetry);
        professorSpinner = findViewById(R.id.professorSpinner);

        robot = Robot.getInstance();
        robot.addOnRobotReadyListener(this);

        setupProfessorDropdown();
        checkMicrophonePermission();

        recordButton.setOnClickListener(v -> {
            retryButton.setVisibility(View.GONE);
            speakThenListen();
        });

        retryButton.setOnClickListener(v -> {
            retryButton.setVisibility(View.GONE);
            speakThenListen();
        });
    }

    private void setupProfessorDropdown() {
        String[] professors = {"Choose professor...", "Dr. Brown", "Prof. Jackson", "Dr. Okafor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, professors);
        professorSpinner.setAdapter(adapter);
    }

    private void checkMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            robot.hideTopBar();
        }
    }

    private void speakThenListen() {
        robot.speak(TtsRequest.create("Welcome, please say your message.", true));

        new Handler().postDelayed(() -> {
            statusText.setText("Listening...");
            startSpeechRecognition();
        }, 4000);
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.setPackage("com.google.android.googlequicksearchbox");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Google Speech Recognition not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String message = results.get(0);
                statusText.setText("You said: " + message);
                robot.speak(TtsRequest.create("You said: " + message, true));
                retryButton.setVisibility(View.VISIBLE);
                // TODO: Forward message via email or text here
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        robot.removeOnRobotReadyListener(this);
    }
}
