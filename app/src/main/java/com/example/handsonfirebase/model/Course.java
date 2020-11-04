package com.example.handsonfirebase.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {
    private String id, name, day, start, end, lecturer;

    public Course(){}

    public Course(String id, String name, String day, String start, String end, String lecturer) {
        this.id = id;
        this.name = name;
        this.day = day;
        this.start = start;
        this.end = end;
        this.lecturer = lecturer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.day);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeString(this.lecturer);
    }

    protected Course(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.day = in.readString();
        this.start = in.readString();
        this.end = in.readString();
        this.lecturer = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel source) {
            return new Course(source);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
}
