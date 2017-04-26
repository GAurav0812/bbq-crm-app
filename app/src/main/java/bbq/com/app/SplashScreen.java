package bbq.com.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import bbq.com.app.utils.SessionManager;

public class SplashScreen extends AppCompatActivity {
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);
        sessionManager = new SessionManager(getApplicationContext());
        Thread myThread = new Thread() {
            @Override

            public void run() {
                try {
                    sleep(3000);
                    if (sessionManager.isStoredId()) {
                        Intent intent = new Intent(getApplicationContext(),ReservationActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), ConfigureActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
