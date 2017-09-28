package com.example.student.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context) {
        super(context, "MyDB1.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Student.TABLE_NAME + "("
                + Student.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Student.COLUMN_LAST_NAME + " TEXT NOT NULL,"
                + Student.COLUMN_FIRST_NAME + " TEXT NOT NULL,"
                + Student.COLUMN_AGE + " INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE Students RENAME TO Students_old");

        db.execSQL("CREATE TABLE " + Student.TABLE_NAME + "("
                + Student.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Student.COLUMN_LAST_NAME + " TEXT NOT NULL,"
                + Student.COLUMN_FIRST_NAME + " TEXT NOT NULL,"
                + "INN" + " TEXT NOT NULL,"
                + Student.COLUMN_AGE + " INTEGER NOT NULL);");

        db.execSQL("INSERT INTO Students(_id, FirstName, LastName, Age, INN)"
                + "SELECT _id, FirstName, LastName, Age, '000' FROM Students_old");

        db.execSQL("DROP TABLE Students_old");
    }

    public long insertStudent(Student student) {
        long id = 0;
        SQLiteDatabase db = getWritableDatabase();

        try {
            ContentValues values = new ContentValues();

            values.put(Student.COLUMN_FIRST_NAME, student.FirstName);
            values.put(Student.COLUMN_LAST_NAME, student.LastName);
            values.put(Student.COLUMN_AGE, student.Age);

            id = db.insert(Student.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public ArrayList<Student> getStudents() {
        ArrayList<Student> students = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(Student.TABLE_NAME, null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Student student = new Student();

                    student.id = cursor.getLong(cursor.getColumnIndex(Student.COLUMN_ID));
                    student.FirstName = cursor.getString(cursor.getColumnIndex(Student.COLUMN_FIRST_NAME));
                    student.LastName = cursor.getString(cursor.getColumnIndex(Student.COLUMN_LAST_NAME));
                    student.Age = cursor.getLong(cursor.getColumnIndex(Student.COLUMN_AGE));

                    students.add(student);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return students;
    }

    public boolean deleteStudent(long id) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();

        try {
            count = db.delete(Student.TABLE_NAME, Student.COLUMN_ID + "=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count > 0;
    }
}
