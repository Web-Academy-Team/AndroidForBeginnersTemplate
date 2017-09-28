package com.example.student.myapplication;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class DeleteTask extends AsyncTask<Long, Void, Boolean> {

    private WeakReference<Context> mContext;
    private Runnable mRunnable;
    //private DeleteListener mListener;

    public DeleteTask(Context context, Runnable runnable){
        mContext = new WeakReference<>(context);
       // mListener = listener;
        mRunnable = runnable;
    }

    @Override
    protected Boolean doInBackground(Long... params) {
        Context context = mContext.get();
        if(context != null){
            DataBaseHelper helper = new DataBaseHelper(context);

            return helper.deleteStudent(params[0]);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        /*if(mListener != null){
            mListener.onDelete(success);
        }*/

        if(mRunnable != null){
            mRunnable.run();
        }
    }

   /* public interface DeleteListener{
        void onDelete(Boolean success);
    }*/
}
