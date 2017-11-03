package tmumj3.helloworld;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.widget.RelativeLayout;



public class MainActivity extends Activity implements Runnable, SensorEventListener {
    SensorManager manager;
    Ball ball;
    Handler handler;
    int width, height, time;
    float gx, gy, dpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout linearlayout = new LinearLayout(this);
        linearlayout.setBackgroundColor(Color.GREEN);
        setContentView(linearlayout);

        time = 10;
        handler = new Handler();
        handler.postDelayed(this, 3000);
        dpi = getResources().getDisplayMetrics().densityDpi;

        WindowManager windowManager =
                (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        ball = new Ball(this);
        ball.x = width / 2;
        ball.y = height / 2;
        linearlayout.addView(ball);
    }
    @Override
    public void run() {
        ball.vx += (float) (-gx * time / 1000 /10);
        ball.vy += (float) (gy * time / 1000 /10);
        ball.x += dpi * ball.vx * time / 25.4 ;
        ball.y += dpi * ball.vy * time / 25.4 ;

        if (ball.x <= ball.radius) {
            ball.x = ball.radius;
            ball.vx = -ball.vx / 3;
        } else if (ball.x >= width - ball.radius) {
            ball.x = width - ball.radius;
            ball.vx = -ball.vx / 3;
        }
        if (ball.y <= ball.radius) {
            ball.y = ball.radius;
            ball.vy = -ball.vy / 3;
        } else if (ball.y >= height - ball.radius) {
            ball.y = height - ball.radius;
            ball.vy = -ball.vy / 3;
        }
        ball.invalidate();
        handler.postDelayed(this, time);
    }
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors =
                manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (0 < sensors.size()) {
            manager.registerListener(
                    this, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        gx = event.values[0];
        gy = event.values[1];
    }
    @Override
    public void onAccuracyChanged(
            Sensor sensor, int accuracy) {
    }
}