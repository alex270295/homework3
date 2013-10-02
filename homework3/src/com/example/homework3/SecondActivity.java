package com.example.homework3;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: Alexei
 * Date: 02.10.13
 * Time: 20:39
 * To change this template use File | Settings | File Templates.
 */
public class SecondActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondlayout);

        Bundle bundle = getIntent().getExtras();
        String result = bundle.getString("translate");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(result);
}
}