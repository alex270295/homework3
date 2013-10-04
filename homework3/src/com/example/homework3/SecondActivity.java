package com.example.homework3;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecondActivity extends Activity {

    Task myTask;
    Bundle bundle;
    TextView textView;
    public ImageView[] imageview = new ImageView[10];
    public Bitmap[] bitmap = new Bitmap[10];
    int temp = 0;

    String source;

    class Task extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            String result = "";
            String requestUrl;
            URL url;
            URLConnection connection;
            Scanner scanner;
            try
            {
                requestUrl = "http://images.yandex.ru/yandsearch?text=" + URLEncoder.encode(source, "UTF-8");
                url = new URL(requestUrl);
                connection = url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNext()) {
                    result += scanner.nextLine();
                }
            }
            catch (IOException e) {
            }
            Pattern pattern = Pattern.compile("<img class=\"[^\"]*preview[^\"]*\" (?:alt=\".*?\" )*src=\"(.*?)\"");
            Matcher matcher = pattern.matcher(result);
            for(int i = 0; i < 10; i++)
            {
                if(!matcher.find())
                    return i;
                try {
                    bitmap[i] = BitmapFactory.decodeStream(new URL(matcher.group(1)).openStream());
                } catch (IOException e) {
                    i--;
                }
                publishProgress(i + 1);
            }
            return 10;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress[0]);
            for(int i = temp; i < progress[0]; i++)
                imageview[i].setImageBitmap(bitmap[i]);
            temp = progress[0];
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            textView.setText(textView.getText() + " - here " + result + " picturies");
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondlayout);

        temp = 0;
        bundle = getIntent().getExtras();
        source = bundle.getString("translate");

        textView = (TextView) findViewById(R.id.textView);
        textView.setText(source);

        imageview[0] = (ImageView) findViewById(R.id.imageView);
        imageview[1] = (ImageView) findViewById(R.id.imageView1);
        imageview[2] = (ImageView) findViewById(R.id.imageView2);
        imageview[3] = (ImageView) findViewById(R.id.imageView3);
        imageview[4] = (ImageView) findViewById(R.id.imageView4);
        imageview[5] = (ImageView) findViewById(R.id.imageView5);
        imageview[6] = (ImageView) findViewById(R.id.imageView6);
        imageview[7] = (ImageView) findViewById(R.id.imageView7);
        imageview[8] = (ImageView) findViewById(R.id.imageView8);
        imageview[9] = (ImageView) findViewById(R.id.imageView9);

        myTask = new Task();
        myTask.execute();
    }
}
