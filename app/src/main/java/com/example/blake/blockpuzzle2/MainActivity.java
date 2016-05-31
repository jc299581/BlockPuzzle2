package com.example.blake.blockpuzzle2;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private PictureWorker worker;
    private TextView imageText;
    private SoundPool soundPool;
    int beepSound = R.raw.beep;
    private ImageView[] imgViews = new ImageView[5];

    private int[] drawablesColours = {R.drawable.purple, R.drawable.green, R.drawable.white, R.drawable.yellow};

    public MainActivity() {
        worker = new PictureWorker(this);
        worker.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imageText = (TextView) findViewById(R.id.imagetext);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleID, int status) {
                if (status != 0) {
                    Log.e("soundPool", "Error Loading Resource " + sampleID);
                    return;
                } else {
                    Log.e("soundPool", "Resource Loaded " + sampleID);
                }
            }


        });
        soundPool.load(this, beepSound, 1);


        //pass this entire array into controller
        imgViews[0] = (ImageView) findViewById(R.id.preview);
        imgViews[1] = (ImageView) findViewById(R.id.topLeft);
        imgViews[2] = (ImageView) findViewById(R.id.topRight);
        imgViews[3] = (ImageView) findViewById(R.id.bottomLeft);
        imgViews[4] = (ImageView) findViewById(R.id.bottomRight);


        Handler handler = new Handler();
        ImageViewController.setViews(imgViews);

        int length = drawablesColours.length;
        int[] drawables = drawablesColours;


        worker.totalImages = length;
        for (int drawable : drawables) {
            worker.loadResource(drawable, handler);
        }
        showImageStartup();

    }

    public void showImageStartup() {

        imgViews[0].setVisibility(View.VISIBLE);
//                imageText.setVisibility(View.INVISIBLE);
        imgViews[1].setVisibility(View.INVISIBLE);
        imgViews[2].setVisibility(View.INVISIBLE);
        imgViews[3].setVisibility(View.INVISIBLE);
        imgViews[4].setVisibility(View.INVISIBLE);

        new CountDownTimer(5000, 1000) { // 5000 = 5 sec

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                imgViews[0].setVisibility(View.INVISIBLE);
//                imageText.setVisibility(View.INVISIBLE);
                imgViews[1].setVisibility(View.VISIBLE);
                imgViews[2].setVisibility(View.VISIBLE);
                imgViews[3].setVisibility(View.VISIBLE);
                imgViews[4].setVisibility(View.VISIBLE);

            }
        }.start();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void click(View view) {

        ImageViewController.reset();
        showImageStartup();
        soundPool.play(beepSound, 2, 2, 1, 0, 1);
    }

    public void nextImage(View view) {
        switch (view.getId()) {
            case R.id.topLeft:
                ImageViewController.nextImage(1);
                break;
            case R.id.topRight:
                ImageViewController.nextImage(2);
                break;
            case R.id.bottomLeft:
                ImageViewController.nextImage(3);
                break;
            case R.id.bottomRight:
                ImageViewController.nextImage(4);
                break;
        }

        if (ImageViewController.isComplete()) {
            soundPool.play(beepSound, 1, 1, 1, 0, 1);
            System.out.println("complete");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        worker.quit();

        this.finish();
        Runtime.getRuntime().gc();
    }

}



