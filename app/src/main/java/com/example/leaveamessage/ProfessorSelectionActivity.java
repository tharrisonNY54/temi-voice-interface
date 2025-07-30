package com.example.leaveamessage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ProfessorSelectionActivity extends AppCompatActivity {

    private CardView cardDrAbana, cardDrBowens,cardDrCarter,cardDrDaniels,cardDrDonahue,cardDrDotson,cardDrIjagbemi,cardDrPierce,cardDrZenenga,cardDrWCharles,cardMrCastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_selection);

        // Initialize card view
        cardDrAbana = findViewById(R.id.cardDrAbana);
        cardDrBowens = findViewById(R.id.cardDrBowens);
        cardDrCarter = findViewById(R.id.cardDrCarter);
        cardDrDaniels = findViewById(R.id.cardDrDaniels);
        cardDrDonahue = findViewById(R.id.cardDrDonahue);
        cardDrDotson = findViewById(R.id.cardDrDotson);
        cardDrIjagbemi = findViewById(R.id.cardDrIjagbemi);
        cardDrPierce = findViewById(R.id.cardDrPierce);
        cardDrZenenga = findViewById(R.id.cardDrZenenga);
        cardDrWCharles = findViewById(R.id.cardDrWCharles);
        cardMrCastro = findViewById(R.id.cardMrCastro);


        cardDrAbana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfessor("Dr. Abana", "uxuf@arizona.edu", "dr_abana");
            }
        });

        cardDrBowens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfessor("Dr. Bowens", "jbowens@arizona.edu", "dr_bowens");
            }
        });

        cardDrCarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfessor("Dr. Carter", "bryancarter@arizona.edu", "dr_carter");
            }
        });

        cardMrCastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfessor("Mr. Castro", "illphonix@arizona.edu", "mr_castro");
            }
        });

        cardDrDaniels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfessor("Dr. Daniels", "ddaniels1@arizona.edu", "dr_daniels");
            }
        });

        cardDrDonahue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfessor("Dr. Donahue", "jenniferdonahue@arizona.edu", "dr_donahue");
            }
        });

        cardDrDotson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfessor("Dr. Dotson", "jkdotson@arizona.edu", "dr_dotson");
            }
        });

        cardDrIjagbemi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfessor("Dr. Ijagbemi", "ijagbemi@arizona.edu", "dr_ijagbemi");
            }
        });

        cardDrPierce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfessor("Dr. Pierce", "piercem@arizona.edu", "dr_pierce");
            }
        });

        cardDrWCharles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfessor("Dr. W-Charles", "nadiawc@arizona.edu", "dr_w-charles");
            }
        });

        cardDrZenenga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfessor("Dr. Zenenga", "zen08@arizona.edu", "dr_zenenga");
            }
        });


    }

    private void selectProfessor(String name, String email, String id) {
        Intent intent = new Intent(this, MessageRecordingActivity.class);
        intent.putExtra("PROFESSOR_NAME", name);
        intent.putExtra("PROFESSOR_EMAIL", email);
        intent.putExtra("PROFESSOR_ID", id);
        startActivity(intent);
    }
}