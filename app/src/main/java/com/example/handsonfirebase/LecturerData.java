package com.example.handsonfirebase;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handsonfirebase.adapter.LecturerAdapter;
import com.example.handsonfirebase.model.Lecturer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.ItemClickSupport;

import java.util.ArrayList;

public class LecturerData extends AppCompatActivity {
    Toolbar toolbar;
    DatabaseReference dbLecturer;
    ArrayList<Lecturer> listLecturer = new ArrayList<>();
    RecyclerView rv_lecturer_data;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_activity);
        toolbar = findViewById(R.id.toolbar_lect_data);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rv_lecturer_data = findViewById(R.id.rv_lect_data);
        dbLecturer = FirebaseDatabase.getInstance().getReference("lecturer");

        fetchLecturerData();
    }

    public void fetchLecturerData(){
        dbLecturer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLecturer.clear();
                rv_lecturer_data.setAdapter(null);
                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    Lecturer lecturer = childSnapshot.getValue(Lecturer.class);
                    listLecturer.add(lecturer);
                }
                showLecturerData(listLecturer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showLecturerData(final ArrayList<Lecturer> list){
        rv_lecturer_data.setLayoutManager(new LinearLayoutManager(LecturerData.this));
        LecturerAdapter lecturerAdapter = new LecturerAdapter(LecturerData.this);
        lecturerAdapter.setListLecturer(list);
        rv_lecturer_data.setAdapter(lecturerAdapter);

        ItemClickSupport.addTo(rv_lecturer_data).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                v.startAnimation(klik);
                Intent intent = new Intent(LecturerData.this, LecturerDetail.class);
                Lecturer lecturer = new Lecturer(list.get(position).getId(), list.get(position).getName(), list.get(position).getGender(), list.get(position).getExpertise());
                intent.putExtra("data_lecturer", lecturer);
                intent.putExtra("position", position);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerData.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(LecturerData.this, LecturerActivity.class);
            intent.putExtra("action", "add");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerData.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
