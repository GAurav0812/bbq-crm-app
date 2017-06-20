package bbq.com.app;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bbq.com.app.utils.AsyncRequest;

public class ReservationListFragment extends Fragment {
    ListView sessionListView;
    private ArrayList<CustomerInfoObject> customerObjectArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ArrayList<CustomerInfoObject> customerArr = getArguments().getParcelableArrayList("customerObjectArrayList");
        View view = inflater.inflate(R.layout.activity_reservation_list, container, false);
        sessionListView = (ListView) view.findViewById(R.id.sessionListView);

        SessionListAdapter customAdapter = new SessionListAdapter(ReservationListFragment.this, customerArr);
        sessionListView.setAdapter(customAdapter);
        return view;


    }
}
