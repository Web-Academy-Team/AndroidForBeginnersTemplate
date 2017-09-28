package second.com.example.student.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class AddEditActivity extends AppCompatActivity {

    private EditText mEditTextFirstName;
    private EditText mEditTextLastName;
    private EditText mEditTextAge;

    private Student mStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        mEditTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        mEditTextLastName = (EditText) findViewById(R.id.editTextLastName);
        mEditTextAge = (EditText) findViewById(R.id.editTextAge);

        findViewById(R.id.buttonSave).setOnClickListener(saveListener);
        findViewById(R.id.buttonCancel).setOnClickListener(cancelListener);

        mStudent = getIntent().getParcelableExtra(MainActivity.EXTRA_STUDENT);

        mEditTextFirstName.setText(mStudent.FirstName);
        mEditTextLastName.setText(mStudent.LastName);
        mEditTextAge.setText(String.valueOf(mStudent.Age));
    }

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mStudent.FirstName = mEditTextFirstName.getText().toString();
            mStudent.LastName = mEditTextLastName.getText().toString();
            mStudent.Age = Long.parseLong(mEditTextAge.getText().toString());

            Intent intent = new Intent();
            intent.putExtra(MainActivity.EXTRA_STUDENT, mStudent);

            setResult(RESULT_OK, intent);
            finish();
        }
    };

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
