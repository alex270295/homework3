package com.example.homework3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;


public class MyActivity extends Activity {
    final static String apiKey = "trnsl.1.1.20130924T092211Z.d765c0eee4cd302e.969cc6f111ebd870eef1be82aa2ef1ecb1aa06e2";
    final static String apiURL = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
    String source;
    Task myTask;


    class Task extends AsyncTask<Void, Void, String> {

        String translate() throws IOException {
            String requestUrl = apiURL + "key=" + apiKey + "&text=" + URLEncoder.encode(source, "UTF-8") + "&lang=en" + "&format=plain" + "&options=1";
            URL url = new URL(requestUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.connect();
            int rc = httpConnection.getResponseCode();
            if (rc == 200) {
                String line = null;
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                StringBuilder strBuilder = new StringBuilder();
                while ((line = buffReader.readLine()) != null) {
                    strBuilder.append(line + '\n');
                }
                return strBuilder.toString();
            }
            return null;
        }

        String translateFromJSON(String source) throws JSONException {
            JSONObject object = (JSONObject) new JSONTokener(source).nextValue();
            String result = object.getString("text");
            result = result.subSequence(2, result.length() - 2).toString();
            return result;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            try {
                result = translate();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (result != null) {
                try {
                    result = translateFromJSON(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button button = (Button) findViewById(R.id.btTranslate);
        final EditText editText = (EditText) findViewById(R.id.eTSourceText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                source = editText.getText().toString();
                myTask = new Task();
                myTask.execute();
                String result = null;
                try {
                    result = myTask.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (result == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Ошибка перевода", 2000);
                    toast.show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("translate", result);
                    intent.setClass(getApplicationContext(), SecondActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
