package masterung.androidthai.in.th.srrufriend.utility;

import android.content.Context;
import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;

public class AddUser extends AsyncTask<String, Void, String>{

    private Context context;

    public AddUser(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            OkHttpClient okHttpClient = new OkHttpClient();


        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
