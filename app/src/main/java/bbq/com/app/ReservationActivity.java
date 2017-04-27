package bbq.com.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import bbq.com.app.utils.AsyncRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ReservationActivity extends AppCompatActivity implements AsyncRequest.OnAsyncRequestComplete, View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ListView sessionListView;
    ArrayList<SessionsObject> sessionObjectArrayList;
    ArrayList<CustomerInfoObject> customerInfoObjectArrayList;
    private String[] title;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String txtDate;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageView date;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_view);


        viewPager = (ViewPager) findViewById(R.id.viewpager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#4cefe7"));
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        date = (ImageView) findViewById(R.id.ic_calender);
        date.setOnClickListener(this);
        String address = "reservation.json";
        AsyncRequest requestList = new AsyncRequest(ReservationActivity.this, "GET");
        requestList.execute(address);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        for (int i = 0; i < title.length; i++) {
            Bundle bundle = new Bundle();

            bundle.putParcelableArrayList("customerObjectArrayList", sessionObjectArrayList.get(i).getCustomers());
            ReservationListFragment sessionIndex = new ReservationListFragment();
            sessionIndex.setArguments(bundle);
            adapter.addFragment(sessionIndex, sessionObjectArrayList.get(i).getSlot());
            viewPager.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                txtDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_reservation_screen_settings, menu);
        return true;
    }

/*    private void loadInfo(){
        String address = "reservation.json" ;
        AsyncRequest requestList = new AsyncRequest(ReservationActivity.this,"GET");
        requestList.execute(address);
    }*/

    public void asyncResponse(String response, String label) {

        System.out.println("Resaponse:::::" + response);
        if (response != null) {
            try {
                sessionObjectArrayList = new ArrayList<SessionsObject>();

                JSONObject jsonObj = new JSONObject(response);
                JSONArray reservationJSONList = jsonObj.getJSONArray("Sessions");
                System.out.println("Session:::::" + reservationJSONList);
                title = new String[reservationJSONList.length()];

                for (int i = 0; i < reservationJSONList.length(); i++) {
                    JSONObject reservationObject = reservationJSONList.getJSONObject(i);
                    System.out.println("Object:::::" + reservationObject);
                    SessionsObject newObject = new SessionsObject(reservationObject.getString("Slot"), reservationObject.getString("SlotStartTime"), reservationObject.getString("SlotEndTime"));
                    title[i] = reservationObject.getString("Slot");
                    System.out.println("Title:::::" + title[i]);
                    JSONArray customerJSONList = reservationObject.getJSONArray("customers");
                    ArrayList<CustomerInfoObject> customerInfoObjectArrayList = new ArrayList<CustomerInfoObject>();
                    for (int c = 0; c < customerJSONList.length(); c++) {
                        JSONObject customerObject = customerJSONList.getJSONObject(c);
                        System.out.println("cust obj:::::" + customerObject);
                        CustomerInfoObject customerInfoObject = new CustomerInfoObject(customerObject.getString("CustomerName"), customerObject.getString("MobileNo"), customerObject.getString("PAX"), customerObject.getString("ETA"), customerObject.getString("Status"),
                                customerObject.getString("Record"), customerObject.getString("TNo"), customerObject.getString("Flag"), customerObject.getString("Occasion"));
                        customerInfoObjectArrayList.add(customerInfoObject);
                    }
                    newObject.setCustomers(customerInfoObjectArrayList);
                    sessionObjectArrayList.add(newObject);
                }
                System.out.println("array:::::" + title);
                setupViewPager(viewPager);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
