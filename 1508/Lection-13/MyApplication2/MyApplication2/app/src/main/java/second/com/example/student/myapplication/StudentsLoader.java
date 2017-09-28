package second.com.example.student.myapplication;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;


public class StudentsLoader extends AsyncTaskLoader<ArrayList<Student>> {
    public StudentsLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Student> loadInBackground() {
        ArrayList<Student> students = new ArrayList<>();
        Cursor cursor = null;

        try {
            Uri uri = Uri.parse("content://com.example.student.myapplication/students");
            cursor = getContext().getContentResolver().query(uri, null, null, null, null);

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

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
