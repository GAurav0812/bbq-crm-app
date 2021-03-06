package bbq.com.app;

        import android.os.Parcel;
        import android.os.Parcelable;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by System4 on 4/20/2017.
 */
public class SessionsObject implements Parcelable {
    private String Slot;
    private String SlotStartTime;
    private String SlotEndTime;
    private String IsActive;
    public String getSlot() {
        return Slot;
    }

    public void setSlot(String slot) {
        Slot = slot;
    }

    public String getSlotStartTime() {
        return SlotStartTime;
    }

    public void setSlotStartTime(String slotStartTime) {
        SlotStartTime = slotStartTime;
    }

    public String getSlotEndTime() {
        return SlotEndTime;
    }

    public void setSlotEndTime(String slotEndTime) {
        SlotEndTime = slotEndTime;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }



    private ArrayList<CustomerInfoObject> customers;

    public ArrayList<CustomerInfoObject> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<CustomerInfoObject> customers) {
        this.customers = customers;
    }




    @Override
    public String toString() {
        return "SessionsObject{" +
                "Slot='" + Slot + '\'' +
                ", SlotStartTime='" + SlotStartTime + '\'' +
                ", SlotEndTime='" + SlotEndTime + '\'' +
                ", IsActive='" + IsActive + '\'' +
                ", customers=" + customers +
                '}';
    }

    public SessionsObject(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<SessionsObject> CREATOR = new Parcelable.Creator<SessionsObject>() {
        public SessionsObject createFromParcel(Parcel in) {
            return new SessionsObject(in);
        }

        public SessionsObject[] newArray(int size) {

            return new SessionsObject[size];
        }

    };

    public void readFromParcel(Parcel in) {
        Slot = in.readString();
        SlotStartTime = in.readString();
        SlotEndTime = in.readString();
        IsActive = in.readString();

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Slot);
        dest.writeString(SlotStartTime);
        dest.writeString(SlotEndTime);
        dest.writeString(IsActive);
    }

    public SessionsObject(String slot, String slotStartTime, String slotEndTime,String isActive) {
        Slot = slot;
        SlotStartTime = slotStartTime;
        SlotEndTime = slotEndTime;
        IsActive = isActive;
    }

}
