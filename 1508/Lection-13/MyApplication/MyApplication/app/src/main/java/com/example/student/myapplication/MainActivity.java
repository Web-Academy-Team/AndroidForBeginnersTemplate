package com.example.student.myapplication;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Student>> {

    private SaveTask mTask;
    private DeleteTask mDeleteTask;
    private ListView mListView;
    private ArrayList<Student> mStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);

        getLoaderManager().initLoader(0, null, this);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = mStudents.get(position);

                /*mDeleteTask = new DeleteTask(MainActivity.this, new DeleteTask.DeleteListener() {
                    @Override
                    public void onDelete(Boolean success) {
                        getLoaderManager().restartLoader(0, null, MainActivity.this);
                    }
                });*/
                mDeleteTask = new DeleteTask(MainActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        getLoaderManager().restartLoader(0, null, MainActivity.this);
                    }
                });
                mDeleteTask.execute(student.id);

                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTask != null) {
            mTask.cancel(true);
        }
        if (mDeleteTask != null) {
            mDeleteTask.cancel(true);
        }
    }

    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                mTask = new SaveTask();
                mTask.execute(new Student("Ivan", "Ivanov", 22),
                        new Student("Ivan", "Ivanov", 22),
                        new Student("Ivan", "Ivanov", 22),
                        new Student("Ivan", "Ivanov", 22),
                        new Student("Ivan", "Ivanov", 22),
                        new Student("Ivan", "Ivanov", 22),
                        new Student("Ivan", "Ivanov", 22),
                        new Student("Ivan", "Ivanov", 22));
                break;
            case R.id.button2:
                break;
            case R.id.button3:
                break;
            case R.id.button4:
                break;
            case R.id.button5:
                break;
            case R.id.button6:
                break;
            case R.id.button7:
                break;
            case R.id.button8:
                break;
            case R.id.button9:
                break;
            case R.id.button10:
                break;
        }
    }

    @Override
    public Loader<ArrayList<Student>> onCreateLoader(int id, Bundle args) {
        return new StudentsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Student>> loader, ArrayList<Student> students) {
        mStudents = students;
        ArrayAdapter<Student> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                students);

        mListView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Student>> loader) {

    }

    public class SaveTask extends AsyncTask<Student, Integer, Long[]> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Long[] doInBackground(Student... params) {
            Long[] ids = new Long[params.length];
            DataBaseHelper helper = new DataBaseHelper(MainActivity.this);

            for (int i = 0; i < params.length; i++) {
                if (!isCancelled()) {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    ids[i] = helper.insertStudent(params[i]);
                    publishProgress(i + 1, params.length);
                }
            }

            return ids;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mDialog.setMessage(String.format("Saved %d from %d students", values[0], values[1]));
        }

        @Override
        protected void onPostExecute(Long[] ids) {
            if (mDialog != null) {
                mDialog.dismiss();
            }

            StringBuilder builder = new StringBuilder();
            for (Long l : ids) {
                builder.append(String.valueOf(l) + " ");
            }

            Toast.makeText(MainActivity.this, builder.toString(), Toast.LENGTH_LONG).show();

            getLoaderManager().restartLoader(0, null, MainActivity.this);
        }
    }


}
