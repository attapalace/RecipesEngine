package com.examples.apps.atta.recipesengine.UI;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.examples.apps.atta.recipesengine.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.about_text_view) TextView aboutTextView;
    private static final String LOG_TAG = AboutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);

        new FetchAboutTask(aboutTextView).execute();
    }

    public class FetchAboutTask extends AsyncTask<Void , Void, String>{

        private final String JSON_ABOUT = "about";

        private TextView abouttv;

        public FetchAboutTask(TextView abouttv) {
            this.abouttv = abouttv;
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String aboutJsonStr = null;

            try {
                String aboutUri = "https://api.myjson.com/bins/ij7lm";

                Uri builtUri = Uri.parse(aboutUri).buildUpon().build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if (inputStream == null){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null ){
                    stringBuffer.append(line + "\n");
                }

                if (stringBuffer.length() == 0){
                    return null;
                }

                aboutJsonStr = stringBuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG , "error :" + e);
                    }
                }
            }

            try {
                return getDataFromJson(aboutJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            abouttv.setText(s);
        }

        private String getDataFromJson(String jsonStr) throws JSONException {

            JSONObject object = null;
            if(jsonStr != null){
                object = new JSONObject(jsonStr);
            }

            String aboutRespone = "";
            if (object.has(JSON_ABOUT)){
                aboutRespone = object.getString(JSON_ABOUT);
            }
            return aboutRespone;
        }
    }
}
