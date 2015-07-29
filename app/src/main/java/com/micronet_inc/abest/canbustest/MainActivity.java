package com.micronet_inc.abest.canbustest;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {


    private CanTest canTest;

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

    static int interval = 0;
    private void updateCounts()
    {
        //static int interval = 0;
        TextView textView = (TextView) findViewById(R.id.textView);
        interval++;
        String s = "J1939 Frames/Bytes:" + canTest.getCanbusFrameCount() + "/" + canTest.getCanbusByteCount() + "\n"
                + " Rollovers/MaxDiff: " + canTest.getCanbusRollovers() + "/" + canTest.getCanbusMaxdiff() + "\n"
                + "J1708 Frames/Bytes:" + canTest.getJ1939FrameCount() + "/" + canTest.getJ1939ByteCount() + "\n";

        textView.setText(s);

    }

    private void startTimerThread()
    {
        Thread th = new Thread(new Runnable() {

            //private long startTime = System.currentTimeMillis();

            @Override
            public void run() {
                while(true) {
                    //System.out.println("tick");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateCounts();
                        }
                    });
                    try {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        th.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getScreenRes();

        canTest = new CanTest();
        canTest.CreateInterface();

        startTimerThread();

        final Button test1Button = (Button) findViewById(R.id.test1);
        test1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canTest.runCanTest1();
            }
        });
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
