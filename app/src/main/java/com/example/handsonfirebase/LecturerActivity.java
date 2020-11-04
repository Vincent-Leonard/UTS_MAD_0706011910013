package com.example.handsonfirebase;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.handsonfirebase.model.Lecturer;

import java.util.HashMap;
import java.util.Map;

public class LecturerActivity extends AppCompatActivity implements TextWatcher {

    Button button_add_lecturer;
    Toolbar mtoolbar;
    RadioGroup radioGroup_lecturer;
    RadioButton radioButton;
    String name, expertise, gender="male", action = "";
    TextInputLayout input_lecturer_field, input_lecturer_expertise;
    Dialog dialog;
    String genderOnChecked;
    Lecturer lecturer;

    private DatabaseReference mDatabase;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_add);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        button_add_lecturer = findViewById(R.id.button__add_lecturer);
        button_add_lecturer.setEnabled(false);
        dialog = Glovar.loadingDialog(LecturerActivity.this);

        mtoolbar = findViewById(R.id.toolbar_lecturer);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(mtoolbar);


        input_lecturer_expertise = findViewById(R.id.input_lecturer_expertise);
        input_lecturer_field = findViewById(R.id.input_lecturer_field);
        input_lecturer_expertise.getEditText().addTextChangedListener(this);
        input_lecturer_field.getEditText().addTextChangedListener(this);

        radioGroup_lecturer = findViewById(R.id.radioGroup_lecturer);
        radioGroup_lecturer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = findViewById(i);
                gender = radioButton.getText().toString();
            }
        });

        Intent intent =getIntent();
        action = intent.getStringExtra("action");
        if (action.equals("add")){
            button_add_lecturer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    name = input_lecturer_field.getEditText().getText().toString().trim();
                    expertise = input_lecturer_expertise.getEditText().getText().toString().trim();
                    addLecturer(name, gender, expertise);
                    finish();
                }
            });
        }else{
            getSupportActionBar().setTitle("Edit Lecturer");
            lecturer = intent.getParcelableExtra("edit_data_lect");
            input_lecturer_field.getEditText().setText(lecturer.getName());
            input_lecturer_expertise.getEditText().setText(lecturer.getExpertise());
            if(lecturer.getGender().equalsIgnoreCase("male")){
                radioGroup_lecturer.check(R.id.radioButton_lecturer_male);
            }else{
                radioGroup_lecturer.check(R.id.radioButton_lecturer_female);
            }
            button_add_lecturer.setText("Edit Lecturer");
            button_add_lecturer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    name = input_lecturer_field.getEditText().getText().toString().trim();
                    expertise = input_lecturer_expertise.getEditText().getText().toString().trim();
                    Map<String,Object> params = new HashMap<>();
                    params.put("name", name);
                    params.put("expertise", expertise);
                    params.put("gender", gender);
                    mDatabase.child("lecturer").child(lecturer.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.cancel();
                            Intent intent;
                            intent = new Intent(LecturerActivity.this, LecturerData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerActivity.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }


    }

    public void addLecturer(String mnama, String mgender, String mexpertise){
        String mid = mDatabase.child("lecturer").push().getKey();
        Lecturer lecturer = new Lecturer(mid, mnama, mgender, mexpertise);
        mDatabase.child("lecturer").child(mid).setValue(lecturer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(LecturerActivity.this, "Add Lecturer Succesfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LecturerActivity.this, "Add Lecturer Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        name = input_lecturer_field.getEditText().getText().toString().trim();
        expertise = input_lecturer_expertise.getEditText().getText().toString().trim();
        if (!name.isEmpty() && !expertise.isEmpty()){
            button_add_lecturer.setEnabled(true);
        }else {
            button_add_lecturer.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lecturer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(LecturerActivity.this, StarterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerActivity.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }else if(id == R.id.lecturer_list){
            Intent intent;
            intent = new Intent(LecturerActivity.this, LecturerData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LecturerActivity.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
