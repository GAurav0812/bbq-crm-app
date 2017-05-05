package bbq.com.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import bbq.com.app.utils.AsyncRequest;
import bbq.com.app.utils.Installation;
import bbq.com.app.utils.SessionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class ConfigureActivity extends AppCompatActivity implements AsyncRequest.OnAsyncRequestComplete {


    Button btnConfigure;
    SessionManager sessionManager;
    ProgressDialog pDialog;
    EditText _storeId;
    String date = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_confgure_x);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Whitney-Book_1.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        btnConfigure = (Button) findViewById(R.id.btnConfigure);
        _storeId = (EditText) findViewById(R.id.text_storeId);
        sessionManager = new SessionManager(getApplicationContext());
        btnConfigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    if (validate()) {
                        String storeId = _storeId.getText().toString();
                        String address = "outlet/outletInfo/" + storeId;
                        AsyncRequest requestList = new AsyncRequest(ConfigureActivity.this, "GET");
                        requestList.execute(address);
                    }
                }
            }

        });
    }

    private void showError(String err) {
        Toast tst = Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG);
        tst.show();
    }

    @Override
    public void asyncResponse(String response, String label) {
        if (response != null && response.contains("SUCCESS")) {
            sessionManager.configureStore(response);
            Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
            startActivity(intent);
        } else {
            showError("Error occurred while retrieving data!");
        }
    }

    public boolean validate() {
        String storeId = _storeId.getText().toString();
        _storeId.setError(null);
        if (storeId.isEmpty()) {
            _storeId.setError("Please enter store id");
            _storeId.requestFocus();
            return false;
        }
        return true;
    }
}

