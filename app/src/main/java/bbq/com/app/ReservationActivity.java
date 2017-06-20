package bbq.com.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ReservationActivity extends AppCompatActivity implements AsyncRequest.OnAsyncRequestComplete {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SessionManager sessionManager;
    private String storeId;
    private Menu menu;
    ListView sessionListView;
    TextView noofvisit;
    TextView tableNumberLegend;
    TextView current_date;
    ArrayList<SessionsObject> sessionObjectArrayList;
    MenuItem menuItem;
    private SimpleDateFormat dateFormatter;
    ProgressDialog pDialog;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String txtDate;
    String date;
    Typeface noOfVisitFont;
    Typeface tableNumberFont;
    LinearLayout noDataIcon;
    JSONArray unSyncArray = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageView date;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_view);
        noOfVisitFont = Typeface.createFromAsset(getAssets(), "fonts/Athletic.ttf");
        tableNumberFont = Typeface.createFromAsset(getAssets(), "fonts/Athletic.ttf");
        sessionManager = new SessionManager(getApplicationContext());

        noofvisit = (TextView) findViewById(R.id.no_of_visits_text);
        tableNumberLegend = (TextView) findViewById(R.id.table_no_text);
        noDataIcon = (LinearLayout) this.findViewById(R.id.no_data_icon);
        noofvisit.setText("00");
        noofvisit.setTypeface(noOfVisitFont);
        tableNumberLegend.setTypeface(tableNumberFont);

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
        txtDate = dateString;
        getReservationResponse(txtDate);
    }

    private void getReservationResponse(String date) {
        String address = storeId + "/" + date;
        AsyncRequest requestList = new AsyncRequest(ReservationActivity.this, "GET");
        requestList.execute(address);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(null);
        Date d = new Date();
        Timestamp t = new Timestamp(d.getTime());
        if (sessionObjectArrayList.size() != 0) {
            int i = 0;
            int avtiveTab = 0;
            for (SessionsObject s : sessionObjectArrayList) {
                if (s.getIsActive().equals("true")) {
                    avtiveTab = i;
                }
                i++;
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("customerObjectArrayList", s.getCustomers());
                ReservationListFragment sessionIndex = new ReservationListFragment();
                sessionIndex.setArguments(bundle);
                adapter.addFragment(sessionIndex, s.getSlot());
            }
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(avtiveTab);
        }

    }
    private void noDataIconFunction(View view){
        noDataIcon.setVisibility(view.VISIBLE);

    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter {

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
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_reservation_screen_settings, menu);
        menuItem = menu.findItem(R.id.currentDate);
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
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh_session:
                //AsyncRequest get = new AsyncRequest(ReservationActivity.this, "GET", "outlet");
                //get.setPreloaderString("Syncing..");
                getReservationResponse(txtDate);
                //JSONObject respJSON = new JSONObject(sessionManager.getStoreDetails().get(SessionManager.OUTLET_DETAILS));
                //get.execute("outlet/outletInfo/" + respJSON.getString("posStoreId"));
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
            case R.id.currentDate:

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
                        txtDate = dateString;
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        dateFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.ENGLISH);
                        dateFormatter.format(newDate.getTime());
                        dateFormatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                        menuItem.setTitle(dateFormatter.format(newDate.getTime()));

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void asyncResponse(String response, String label) {
        if (response!=null) {
            try {
                sessionObjectArrayList = new ArrayList<SessionsObject>();
                JSONObject jsonObj = new JSONObject(response);
                JSONArray reservationJSONList = jsonObj.getJSONArray("Sessions");
                for (int i = 0; i < reservationJSONList.length(); i++) {
                    JSONObject reservationObject = reservationJSONList.getJSONObject(i);
                    SessionsObject newObject = new SessionsObject(reservationObject.getString("Slot"), reservationObject.getString("SlotStartTime"), reservationObject.getString("SlotEndTime"), reservationObject.getString("IsActive"));
                    JSONArray customerJSONList = reservationObject.getJSONArray("customers");
                    ArrayList<CustomerInfoObject> customerInfoObjectArrayList = new ArrayList<CustomerInfoObject>();
                    for (int c = 0; c < customerJSONList.length(); c++) {
                        JSONObject customerObject = customerJSONList.getJSONObject(c);
                        CustomerInfoObject customerInfoObject = new CustomerInfoObject(customerObject.getString("CustomerName"), customerObject.getString("MobileNo"), customerObject.getString("PAX"), customerObject.getString("ETA"), customerObject.getString("Status"),
                                customerObject.getString("Record"), customerObject.getString("TNo"), customerObject.getString("Flag"), customerObject.getString("Occasion"),
                                customerObject.getString("AppUser"), customerObject.getString("Alcohol"), customerObject.getString("MealPreference"), customerObject.getString("AccompaniedKids"),
                                customerObject.getString("VisitsCount"), customerObject.getString("ActiveVouchers"), customerObject.getString("noofvisit"), customerObject.getString("smileyface"));
                        customerInfoObjectArrayList.add(customerInfoObject);
                    }
                    newObject.setCustomers(customerInfoObjectArrayList);
                    sessionObjectArrayList.add(newObject);
                }
                if(sessionObjectArrayList.size()!=0){
                    //setupViewPager(viewPager);
                    noDataIconFunction(noDataIcon);
                }else {
                    //noDataIconFunction(noDataIcon);
                    setupViewPager(viewPager);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
