package com.example.handsonfirebase;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.handsonfirebase.model.Lecturer;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LecturerDetail extends AppCompatActivity {
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Toolbar bar;
    DatabaseReference dbLecturer;
    ArrayList<Lecturer> listLecturer = new ArrayList<>();
    int pos = 0;
    TextView lbl_name, lbl_gender, lbl_expertise;
    Lecturer lecturer;
    ImageView btn_edit, btn_del;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_detail);
        bar = findViewById(R.id.tb_lecturer_detail);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbLecturer = FirebaseDatabase.getInstance().getReference("lecturer");
        lbl_name = findViewById(R.id.lbl_nama_lecturer_detail);
        lbl_gender = findViewById(R.id.lbl_gender_lecturer_detail);
        lbl_expertise = findViewById(R.id.lbl_expertise_lecturer_detail);
        btn_edit = findViewById(R.id.img_lecturer_edit);
        btn_del = findViewById(R.id.img_lecturer_delete);
        dialog = Glovar.loadingDialog(LecturerDetail.this);

        Intent intent = getIntent();
        pos = intent.getIntExtra("position",0);
        lecturer = intent.getParcelableExtra("data_lecturer");

        lbl_name.setText(lecturer.getName());
        lbl_gender.setText(lecturer.getGender());
        lbl_expertise.setText(lecturer.getExpertise());

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                new AlertDialog.Builder(LecturerDetail.this)
                        .setTitle("Konfirmasi")
                        .setMessage("Are you sure to delete "+lecturer.getName()+" data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
//                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                        dbLecturer.child(lecturer.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                Intent in = new Intent(LecturerDetail.this, LecturerData.class);
                                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                Toast.makeText(LecturerDetail.this, "Delete success!", Toast.LENGTH_SHORT).show();
                                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerDetail.this);
                                                startActivity(in, options.toBundle());
                                                finish();
                                                dialogInterface.cancel();
                                            }
                                        });

                                    }
                                }, 2000);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent in = new Intent(LecturerDetail.this, LecturerActivity.class);
//                Lecturer lect = new Lecturer(lecturer.getId(), lecturer.getName(), lecturer.getGender(), lecturer.getExpertise());
                in.putExtra("action", "edit");
                in.putExtra("edit_data_lect", lecturer);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerDetail.this);
                startActivity(in, options.toBundle());
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(LecturerDetail.this, LecturerData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerDetail.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(LecturerDetail.this, LecturerData.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerDetail.this);
        startActivity(intent, options.toBundle());
        finish();
    }
}
