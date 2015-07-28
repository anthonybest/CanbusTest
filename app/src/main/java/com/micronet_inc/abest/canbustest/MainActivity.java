package com.micronet_inc.abest.canbustest;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {


    private void getScreenRes()
    {
/*
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double)width/(double)dens;
        double hi = (double)height/(double)dens;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);


        text.setText("stuff");
        */
        /*
        Display d = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        TextView text = (TextView) findViewById(R.id.hello_text);
        String rez = d.getWidth() + " x " + d.getHeight();
        text.setText(rez);
        */

        /*
        TextView text = (TextView) findViewById(R.id.hello_text);

        text.setMovementMethod(new ScrollingMovementMethod());
        String s = "";
        for(int i = 0; i < 1000; ++i)
        {
            s += "line" + i + "\n";
        }
        text.setText(s);
        */

        //ScrollView sv = (ScrollView)findViewById(R.id.scrollView);



        /*
        int scrollamt = text.getLayout().getLineTop(text.getLineCount()) - text.getHeight();
        if(scrollamt > 0)
            text.scrollTo(0, scrollamt);
        else
            text.scrollTo(0,0);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getScreenRes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
