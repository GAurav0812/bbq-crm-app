package bbq.com.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
    public View getView(final int i, View convertView, ViewGroup parent) {
        View listView;

        final CustomerInfoObject CustomerInfoObject = customerInfoObjects.get(i);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listView = inflater.inflate(R.layout.session_item_list, null);


        TextView customerName = (TextView) listView.findViewById(R.id.txt_cust_name);
        TextView pax = (TextView) listView.findViewById(R.id.txt_cust_pax);
        TextView customerMobile = (TextView) listView.findViewById(R.id.txt_customer_mobile);
        TextView tno = (TextView) listView.findViewById(R.id.txt_tno);
        TextView customerStatus = (TextView) listView.findViewById(R.id.txt_status);
        customerName.setText(CustomerInfoObject.getCustomerName());
        pax.setText(CustomerInfoObject.getPAX());
        customerMobile.setText(CustomerInfoObject.getMobileNo());
        tno.setText(CustomerInfoObject.getTNo());
        customerStatus.setText(CustomerInfoObject.getStatus());
        return listView;
    }
}