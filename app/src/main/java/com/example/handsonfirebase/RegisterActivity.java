package com.example.handsonfirebase;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.handsonfirebase.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements TextWatcher {

    Toolbar bar;
    Dialog dialog;
    TextInputLayout input_email, input_pass, input_name, input_nim, input_age, input_address;
    RadioGroup rg_gender;
    RadioButton radioButton;
    Button btn_register;
    String uid = "",  name = "", email = "", pass = "", gender = "", nim = "", age = "", address = "", action = "";
    Student student;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        bar = findViewById(R.id.toolbar_register);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dialog = Glovar.loadingDialog(RegisterActivity.this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("student");

        input_email = findViewById(R.id.input_email);
        input_pass = findViewById(R.id.input_password);
        input_name = findViewById(R.id.input_name);
        input_nim = findViewById(R.id.input_nim);
        input_age = findViewById(R.id.input_age);
        input_address = findViewById(R.id.input_address);

        input_name.getEditText().addTextChangedListener(this);
        input_email.getEditText().addTextChangedListener(this);
        input_pass.getEditText().addTextChangedListener(this);
        input_nim.getEditText().addTextChangedListener(this);
        input_age.getEditText().addTextChangedListener(this);
        input_address.getEditText().addTextChangedListener(this);

        btn_register = findViewById(R.id.button_register_final);

        rg_gender = findViewById(R.id.radioGroup_register);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = findViewById(i);
                gender = radioButton.getText().toString();
            }
        });

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if (action.equalsIgnoreCase("add")){
            getSupportActionBar().setTitle(R.string.regstudent);
            btn_register.setText(R.string.regstudent);
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addStudent();
                }
            });
        }else if(action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("login")){
            getSupportActionBar().setTitle("Edit Student");
            student = intent.getParcelableExtra("edit_data_stud");
            input_name.getEditText().setText(student.getName());
            input_email.getEditText().setText(student.getEmail());
            input_pass.getEditText().setText(student.getPassword());
            input_email.getEditText().setEnabled(false);
            input_pass.getEditText().setEnabled(false);
            if(student.getGender().equalsIgnoreCase("male")){
                rg_gender.check(R.id.radioButton_male);
            }else{
                rg_gender.check(R.id.radioButton_female);
            }
            input_nim.getEditText().setText(student.getNim());
            input_age.getEditText().setText(student.getAge());
            input_address.getEditText().setText(student.getAddress());
            btn_register.setText("Edit Student");
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    name = input_name.getEditText().getText().toString().trim();
                    input_email.getEditText().setText(student.getEmail());
                    input_pass.getEditText().setText(student.getPassword());
                    nim = input_nim.getEditText().getText().toString().trim();
                    age = input_age.getEditText().getText().toString().trim();
                    address = input_address.getEditText().getText().toString().trim();
                    Map<String,Object> params = new HashMap<>();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("password", pass);
                    params.put("gender", gender);
                    params.put("nim", nim);
                    params.put("age", age);
                    params.put("address", address);
                    mDatabase.child(student.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.cancel();
                            Intent intent;
                            if (action.equalsIgnoreCase("login")){
                                intent = new Intent(RegisterActivity.this, NavMain.class);
                                intent.putExtra("action", "login");
                            }else{
                                intent = new Intent(RegisterActivity.this, StudentData.class);
                                intent.putExtra("action", "edit");
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }
    }

    public void addStudent(){
        getFormValue();
        dialog.show();
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            dialog.cancel();
                            uid = mAuth.getCurrentUser().getUid();
                            Student student = new Student(uid, name, email, pass, gender, nim, age, address);
                            mDatabase.child(uid).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Student Register Successfull", Toast.LENGTH_SHORT).show();
                                }
                            });
                            mAuth.signOut();
                            finish();
                        }else{
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthInvalidCredentialsException malformed){
                                Toast.makeText(RegisterActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                            }catch (FirebaseAuthUserCollisionException existEmail){
                                Toast.makeText(RegisterActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                            }catch (Exception e){
                                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                            }
                            dialog.cancel();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(RegisterActivity.this, StarterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }else if(id == R.id.student_list){
            Intent intent;
            intent = new Intent(RegisterActivity.this, StudentData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (!action.equalsIgnoreCase("login")) {
            intent = new Intent(RegisterActivity.this, StarterActivity.class);
        }else{
            intent = new Intent(RegisterActivity.this, NavMain.class);
            intent.putExtra("action", "login");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this);
        startActivity(intent, options.toBundle());
        finish();
    }

    public void getFormValue(){
        name = input_name.getEditText().getText().toString().trim();
        email = input_email.getEditText().getText().toString().trim();
        pass = input_pass.getEditText().getText().toString().trim();
        nim = input_nim.getEditText().getText().toString().trim();
        age = input_age.getEditText().getText().toString().trim();
        address = input_address.getEditText().getText().toString().trim();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        getFormValue();

        if (!email.isEmpty() && !pass.isEmpty() && !name.isEmpty() && !nim.isEmpty() && !age.isEmpty() && !address.isEmpty()){
            btn_register.setEnabled(true);
        }else {
            btn_register.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
