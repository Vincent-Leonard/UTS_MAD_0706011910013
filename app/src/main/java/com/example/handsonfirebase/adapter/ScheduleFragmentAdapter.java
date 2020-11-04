package com.example.handsonfirebase.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handsonfirebase.Glovar;
import com.example.handsonfirebase.R;
import com.example.handsonfirebase.ScheduleFragment;
import com.example.handsonfirebase.model.Course;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ScheduleFragmentAdapter extends RecyclerView.Adapter<ScheduleFragmentAdapter.CardViewViewHolder> {

    private Context context;
    private ArrayList<Course> listSchedule;
    private ArrayList<Course> getListSchedule() { return listSchedule; }
    public void setListSchedule(ArrayList<Course> listSchedule) { this.listSchedule = listSchedule; }
    public ScheduleFragmentAdapter(Context context) {
        this.context = context;
    }
    Dialog dialog;
    DatabaseReference dbCourse;
    private FirebaseAuth mAuth;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    FirebaseAuth firebaseAuth;
    String userKey;
    String cid;
    private DatabaseReference mDatabase;

    @NonNull
    @Override
    public ScheduleFragmentAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_schedule_adapter, parent, false);
        return new ScheduleFragmentAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ScheduleFragmentAdapter.CardViewViewHolder holder, int position) {
        final Course course = getListSchedule().get(position);
        holder.lbl_subject.setText(course.getName());
        holder.lbl_day.setText(course.getDay());
        holder.lbl_start.setText(course.getStart());
        holder.lbl_end.setText(course.getEnd());
        holder.lbl_lecturer.setText(course.getLecturer());


        dbCourse = FirebaseDatabase.getInstance().getReference("course");
        dialog = Glovar.loadingDialog(context);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        userKey = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("student");

        holder.button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                cid = course.getId();
                mDatabase.child(userKey).child("course").child(cid).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return getListSchedule().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView lbl_subject, lbl_day, lbl_start, lbl_end, lbl_lecturer;
        ImageView button_remove;

        CardViewViewHolder(View itemView) {
            super(itemView);
            lbl_subject = itemView.findViewById(R.id.lbl_schedule_subject);
            lbl_day = itemView.findViewById(R.id.lbl_schedule_day);
            lbl_start = itemView.findViewById(R.id.lbl_schedule_start);
            lbl_end = itemView.findViewById(R.id.lbl_schedule_end);
            lbl_lecturer = itemView.findViewById(R.id.lbl_schedule_lecturer);

            button_remove = itemView.findViewById(R.id.btn_remove);

        }
    }
}
