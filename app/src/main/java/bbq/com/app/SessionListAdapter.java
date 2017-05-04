package bbq.com.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bbq.com.app.pages.WebViewActivity;

import java.util.ArrayList;

/**
 * Created by System4 on 4/20/2017.
 */
public class SessionListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CustomerInfoObject> customerInfoObjects = new ArrayList<>();

    public SessionListAdapter(ReservationListFragment sessionActivity, ArrayList<CustomerInfoObject> CustomerInfoObjectArrayList) {
        this.mContext = sessionActivity.getContext();
        this.customerInfoObjects = CustomerInfoObjectArrayList;


    }

    public SessionListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return customerInfoObjects.size();
    }

    @Override
    public Object getItem(int i) {
        return customerInfoObjects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, final ViewGroup parent) {
        View listView;

        final CustomerInfoObject CustomerInfoObject = customerInfoObjects.get(i);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listView = inflater.inflate(R.layout.session_list_item, null);


        TextView customerName = (TextView) listView.findViewById(R.id.txt_cust_name);
        TextView pax = (TextView) listView.findViewById(R.id.txt_cust_pax);
        final TextView customerMobile = (TextView) listView.findViewById(R.id.txt_customer_mobile);
        TextView tno = (TextView) listView.findViewById(R.id.txt_tno);
        TextView occasion = (TextView) listView.findViewById(R.id.txt_occasion);

        customerName.setText(CustomerInfoObject.getCustomerName());
        pax.setText(CustomerInfoObject.getPAX());
        customerMobile.setText(CustomerInfoObject.getMobileNo());
        //final String mobile1 = customerMobile.getText().toString();
        final String mobile1 = "9711163739";
        tno.setText(CustomerInfoObject.getTNo());
        occasion.setText(CustomerInfoObject.getOccassion());
        listView.findViewById(R.id.ic_arrived).setVisibility(View.GONE);
        listView.findViewById(R.id.ic_expected).setVisibility(View.GONE);
        listView.findViewById(R.id.ic_seated).setVisibility(View.GONE);
        listView.findViewById(R.id.ic_cancel).setVisibility(View.GONE);
        if (CustomerInfoObject.getStatus().equals("Arrived")) {
            listView.findViewById(R.id.ic_arrived).setVisibility(View.VISIBLE);
        }
        if (CustomerInfoObject.getStatus().equals("Expected")) {
            listView.findViewById(R.id.ic_expected).setVisibility(View.VISIBLE);
        }
        if (CustomerInfoObject.getStatus().equals("Seated")) {
            listView.findViewById(R.id.ic_seated).setVisibility(View.VISIBLE);
        }
        if (CustomerInfoObject.getStatus().equals("Cancel")) {
            listView.findViewById(R.id.ic_cancel).setVisibility(View.VISIBLE);
        }


        ImageView mobile = (ImageView) listView.findViewById(R.id.ic_mobile);
        mobile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), WebViewActivity.class);
                intent.putExtra("mobileNo", mobile1);
                parent.getContext().startActivity(intent);
            }
        });
        return listView;
    }
}
