package bbq.com.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import bbq.com.app.utils.AsyncRequest;
import bbq.com.app.utils.DetectConnection;
import bbq.com.app.utils.SessionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ReservationActivity extends AppCompatActivity implements AsyncRequest.OnAsyncRequestComplete, View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SessionManager sessionManager;
    private String storeId;
    ListView sessionListView;
    TextView current_date;
    ArrayList<SessionsObject> sessionObjectArrayList;
    ArrayList<CustomerInfoObject> customerInfoObjectArrayList;
    private String[] title;
    ProgressDialog pDialog;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String txtDate;
    String date;
    JSONArray unSyncArray = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageView date;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_view);
        sessionManager = new SessionManager(getApplicationContext());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#4cefe7"));
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


      /*  date = (ImageView) findViewById(R.id.ic_calender);
        date.setOnClickListener(this);*/
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        String dateString = String.valueOf(year) + (month < 10 ? ("0" + month) : month) + (day < 10 ? ("0" + day) : day);
        storeId = sessionManager.getStoreDetails().get(SessionManager.STORE_ID);

        getReservationResponse(dateString);
    }

    private void getReservationResponse(String date) {
        String address = storeId + "/" + date;
        AsyncRequest requestList = new AsyncRequest(ReservationActivity.this, "GET");
        requestList.execute(address);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Date d = new Date();
        Timestamp t = new Timestamp(d.getTime());

        int i = 0;
        int avtiveTab = 0;

        for (SessionsObject s : sessionObjectArrayList) {
           /* SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedStartDate = null;
            Date parsedEndDate = null;
            try {
                parsedStartDate = dateFormat.parse(s.getSlotStartTime());
                parsedEndDate = dateFormat.parse(s.getSlotEndTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            // Timestamp startDateTimeStamp = new java.sql.Timestamp(parsedStartDate.getTime());
            //Timestamp endDateTimeStamp = new java.sql.Timestamp(parsedEndDate.getTime());
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("customerObjectArrayList", s.getCustomers());
            ReservationListFragment sessionIndex = new ReservationListFragment();
            sessionIndex.setArguments(bundle);
            adapter.addFragment(sessionIndex, s.getSlot());
            viewPager.setAdapter(adapter);
            if (s.getIsActive().equals("true")) {
                avtiveTab = i;
            }
            i++;
        }
        viewPager.setCurrentItem(avtiveTab);
    }

    @Override
    public void onClick(View view) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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
        final MenuItem menuItem = menu.findItem(R.id.currentDate);
        //current_date = (TextView) menuItem.getActionView();
        // current_date = (TextView) findViewById(R.id.currentDate);
        Date d = new Date();
        Timestamp t = new Timestamp(d.getTime());
        String date = format(t, "MMMM d, yyyy ");
        // CharSequence s = DateFormat.format("MMMM d, yyyy ", d.getTime());
        menuItem.setTitle(date);
        return true;
    }

    public static String format(Timestamp value, String format) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormatter.format(value.getTime());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh_session:
                try {
                    AsyncRequest get = new AsyncRequest(ReservationActivity.this, "GET", "outlet");
                    get.setPreloaderString("Syncing..");
                    JSONObject respJSON = new JSONObject(sessionManager.getStoreDetails().get(SessionManager.OUTLET_DETAILS));
                    get.execute("outlet/outletInfo/" + respJSON.getString("posStoreId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.reset_setting:

                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                builder.setTitle("Account Reset Confirmation!");
                builder.setMessage("The action is irreversible, all your data will get erased. Are you sure you want to reset the account?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sessionManager.clearAccount();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                return true;
            case R.id.ic_calender:
                sessionObjectArrayList = new ArrayList<>();
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int yy = year;
                        int month = monthOfYear + 1;
                        int day = dayOfMonth;
                        //txtDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        String dateString = String.valueOf(year) + (month < 10 ? ("0" + month) : month) + (day < 10 ? ("0" + day) : day);
                        getReservationResponse(dateString);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void asyncResponse(String response, String label) {

        System.out.println("Resaponse:::::" + response);
        customerInfoObjectArrayList = new ArrayList<CustomerInfoObject>();
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
                    SessionsObject newObject = new SessionsObject(reservationObject.getString("Slot"), reservationObject.getString("SlotStartTime"), reservationObject.getString("SlotEndTime"), reservationObject.getString("IsActive"));
                    title[i] = reservationObject.getString("Slot");
                    System.out.println("Title:::::" + title[i]);
                    JSONArray customerJSONList = reservationObject.getJSONArray("customers");
                    ArrayList<CustomerInfoObject> customerInfoObjectArrayList = new ArrayList<CustomerInfoObject>();
                    for (int c = 0; c < customerJSONList.length(); c++) {
                        JSONObject customerObject = customerJSONList.getJSONObject(c);
                        System.out.println("cust obj:::::" + customerObject);
                        CustomerInfoObject customerInfoObject = new CustomerInfoObject(customerObject.getString("CustomerName"), customerObject.getString("MobileNo"), customerObject.getString("PAX"), customerObject.getString("ETA"), customerObject.getString("Status"),
                                customerObject.getString("Record"), customerObject.getString("TNo"), customerObject.getString("Flag"), customerObject.getString("Occasion"),
                                customerObject.getString("AppUser"), customerObject.getString("Alcohol"), customerObject.getString("MealPreference"), customerObject.getString("AccompaniedKids"),
                                customerObject.getString("VisitsCount"), customerObject.getString("ActiveVouchers"));
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
