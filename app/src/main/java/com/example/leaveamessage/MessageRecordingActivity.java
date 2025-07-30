package com.example.leaveamessage;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.Locale;

public class MessageRecordingActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_CODE_SPEECH_INPUT = 123;

    private TextView selectedProfessorName, recordingStatusText, transcribedText;
    private ImageView selectedProfessorPhoto;
    private ImageButton microphoneButton, backButton;
    private Button tryAgainButton, sendMessageButton;
    private CardView transcriptCard;

    private String professorName, professorEmail, professorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_recording);

        professorName = getIntent().getStringExtra("PROFESSOR_NAME");
        professorEmail = getIntent().getStringExtra("PROFESSOR_EMAIL");
        professorId = getIntent().getStringExtra("PROFESSOR_ID");

        selectedProfessorName = findViewById(R.id.selectedProfessorName);
        selectedProfessorPhoto = findViewById(R.id.selectedProfessorPhoto);
        recordingStatusText = findViewById(R.id.recordingStatusText);
        transcribedText = findViewById(R.id.transcribedText);
        microphoneButton = findViewById(R.id.microphoneButton);
        backButton = findViewById(R.id.backButton);
        tryAgainButton = findViewById(R.id.tryAgainButton);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        transcriptCard = findViewById(R.id.transcriptCard);

        selectedProfessorName.setText(professorName);
        setProfilePhoto();

        backButton.setOnClickListener(v -> finish());

        microphoneButton.setOnClickListener(v -> startSpeechRecognition());

        tryAgainButton.setOnClickListener(v -> resetRecording());

        sendMessageButton.setOnClickListener(v -> sendMessage());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
            recordingStatusText.setText("Listening...");
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Speech recognition not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String message = results.get(0);
                transcribedText.setText(message);
                transcriptCard.setVisibility(View.VISIBLE);
                recordingStatusText.setText("Tap microphone to record again");
            }
        }
    }

    private void resetRecording() {
        transcriptCard.setVisibility(View.GONE);
        transcribedText.setText("");
        recordingStatusText.setText("Tap to start recording");
    }

    private void sendMessage() {
        Toast.makeText(this, "Message sent to " + professorName, Toast.LENGTH_LONG).show();
        String messageText = transcribedText.getText().toString().trim();
        if (messageText.isEmpty()) {
            Toast.makeText(this, "Message is empty. Please record something first.", Toast.LENGTH_SHORT).show();
            return;
        }
        sendEmail(messageText);

    }

    private void setProfilePhoto() {
        int photoResId;
        switch (professorId) {
            case "dr_abana":
                photoResId = R.drawable.dr_abana_image;
                break;
            case "dr_bowens":
                photoResId = R.drawable.dr_bowens_image;
                break;
            case "dr_carter":
                photoResId = R.drawable.dr_carter_image;
                break;
            case "dr_daniels":
                photoResId = R.drawable.dr_daniels_image;
                break;
            case "dr_donahue":
                photoResId = R.drawable.dr_donahue_image;
                break;
            case "dr_dotson":
                photoResId = R.drawable.dr_dotson_image;
                break;
            case "dr_ijagbemi":
                photoResId = R.drawable.dr_ijagbemi_image;
                break;
            case "dr_pierce":
                photoResId = R.drawable.dr_pierce_image;
                break;
            case "dr_zenenga":
                photoResId = R.drawable.dr_praise_image; // replace with dr_zenenga_image if available
                break;
            case "dr_w-charles":
                photoResId = R.drawable.dr_wcharles_image; // adjust file name if needed
                break;
            case "mr_castro":
                photoResId = R.drawable.mr_castro_image; // replace if you have castro image
                break;
            default:
                photoResId = R.drawable.ic_launcher_foreground;
        }
        selectedProfessorPhoto.setImageResource(photoResId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private void sendEmail(String messageContent) {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("name", "Visitor via TEMI");
            jsonParams.put("message", messageContent);
            jsonParams.put("to", "treyharrison@arizona.edu"); // Use the professor's actual email address
        } catch (JSONException e) {
            e.printStackTrace();
            speakErrorOccurred();
            return;
        }

        RequestBody body = RequestBody.create(
                jsonParams.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("https://temi-email-server.vercel.app/api/send-email")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                speakErrorOccurred();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try (response) {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            Toast.makeText(MessageRecordingActivity.this, "Message sent to " + professorName, Toast.LENGTH_LONG).show();
                            finish();
                        });
                    } else {
                        String errorBody = response.body() != null ? response.body().string() : "No error body";
                        System.err.println("Resend Server Error: " + response.code() + " " + errorBody);
                        speakErrorOccurred();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    speakErrorOccurred();
                }
            }
        });
    }

    private void speakErrorOccurred() {
        runOnUiThread(() -> Toast.makeText(this, "Failed to send message.", Toast.LENGTH_LONG).show());
    }


}
