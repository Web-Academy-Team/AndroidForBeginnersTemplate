package com.example.student.myapplication;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;


public class StudentsLoader extends AsyncTaskLoader<ArrayList<Student>> {
    public StudentsLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Student> loadInBackground() {
        Log.d("Loader", "Load students");

        return new DataBaseHelper(getContext()).getStudents();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
