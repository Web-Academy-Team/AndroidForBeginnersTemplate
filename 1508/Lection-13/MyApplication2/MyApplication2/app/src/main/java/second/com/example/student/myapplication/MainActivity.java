package second.com.example.student.myapplication;


import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Student>> {

    private ListView mListView;
    private ArrayList<Student> mStudents;

    private CreateTask mCreateTask;

    public static final String EXTRA_STUDENT = "second.com.example.student.myapplication.extra.STUDENT";
    private static final int REQUEST_CODE_EDIT = 1;
    private static final int REQUEST_CODE_CREATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);

        getLoaderManager().initLoader(0, null, this);

        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student = new Student();

                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra(EXTRA_STUDENT, student);

                startActivityForResult(intent, REQUEST_CODE_CREATE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCreateTask != null) {
            mCreateTask.cancel(true);
        }
    }

    @Override
    public Loader<ArrayList<Student>> onCreateLoader(int id, Bundle args) {
        return new StudentsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Student>> loader, ArrayList<Student> data) {
        mStudents = data;

        ArrayAdapter<Student> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mStudents);

        mListView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Student>> loader) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CREATE) {
                Student student = data.getParcelableExtra(EXTRA_STUDENT);

                mCreateTask = new CreateTask();
                mCreateTask.execute(student);
            }
        }
    }


    private class CreateTask extends AsyncTask<Student, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Student... params) {
            boolean result = false;

            try {
                Student student = params[0];

                ContentValues values = new ContentValues();
                values.put(Student.COLUMN_FIRST_NAME, student.FirstName);
                values.put(Student.COLUMN_LAST_NAME, student.LastName);
                values.put(Student.COLUMN_AGE, student.Age);

                Uri uri = Uri.parse("content://com.example.student.myapplication/students");

                result = getContentResolver().insert(uri, values) != null;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            getLoaderManager().restartLoader(0, null, MainActivity.this);
        }
    }
}
