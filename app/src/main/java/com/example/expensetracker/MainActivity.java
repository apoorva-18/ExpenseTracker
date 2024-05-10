package com.example.expensetracker;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowInsetsController;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3200;
    ImageView imageView;
    //TextView textView1,textView2;
    Animation top, bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            // Check the API level for immersive full-screen mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                getWindow().setDecorFitsSystemWindows(false);
            } else {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowInsetsController controller = getWindow().getInsetsController();
                if (controller != null) {
                    controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                    controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                }
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }


            setContentView(R.layout.activity_main);

            imageView = findViewById(R.id.spendee_image);
            //textView1 = findViewById(R.id.spendee_text1);
            //textView2 = findViewById(R.id.spendee_text2);
            if (imageView == null) {
                throw new NullPointerException("UI elements are null.");
            }

            top = AnimationUtils.loadAnimation(this, R.anim.splash_top);
            bottom = AnimationUtils.loadAnimation(this, R.anim.splash_bottom);

            imageView.setAnimation(top);
            //textView1.setAnimation(bottom);
            //textView2.setAnimation(bottom);
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, home_screen.class);
                startActivity(intent);
                finish();

            }, SPLASH_TIME_OUT);
        }catch(Exception e) {
            e.printStackTrace();


        }

    }
}
