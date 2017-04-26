package bbq.com.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by System4 on 4/20/2017.
 */
public class CustomerInfoObject implements Parcelable {
    private String CustomerName;
    private String MobileNo;
    private String ETA;
    private String PAX;
    private String TNo;
    private String Status;
    private String Flag;
    private String Record;
    private String Occassion;
    private String SpecialRemark;

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getETA() {
        return ETA;
    }

    public void setETA(String ETA) {
        this.ETA = ETA;
    }

    public String getPAX() {
        return PAX;
    }

    public void setPAX(String PAX) {
        this.PAX = PAX;
    }

    public String getTNo() {
        return TNo;
    }

    public void setTNo(String TNo) {
        this.TNo = TNo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getRecord() {
        return Record;
    }

    public void setRecord(String record) {
        Record = record;
    }

    public String getOccassion() {
        return Occassion;
    }

    public void setOccassion(String occassion) {
        Occassion = occassion;
    }

    public String getSpecialRemark() {
        return SpecialRemark;
    }

    public void setSpecialRemark(String specialRemark) {
        SpecialRemark = specialRemark;
    }

    @Override
    public String toString() {
        return "CustomerInfoObject{" +
                "CustomerName='" + CustomerName + '\'' +
                ", MobileNo='" + MobileNo + '\'' +
                ", ETA='" + ETA + '\'' +
                ", PAX='" + PAX + '\'' +
                ", TNo='" + TNo + '\'' +
                ", Status='" + Status + '\'' +
                ", Flag='" + Flag + '\'' +
                ", Record='" + Record + '\'' +
                ", Occassion='" + Occassion + '\'' +
                ", SpecialRemark='" + SpecialRemark + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }



    public CustomerInfoObject(Parcel in) {
        super();
        readFromParcel(in);
    }
    public static final Parcelable.Creator<CustomerInfoObject> CREATOR = new Parcelable.Creator<CustomerInfoObject>() {
        public CustomerInfoObject createFromParcel(Parcel in) {
            return new CustomerInfoObject(in);
        }

        public CustomerInfoObject[] newArray(int size) {

            return new CustomerInfoObject[size];
        }

    };
    public void readFromParcel(Parcel in) {
        CustomerName = in.readString();
        MobileNo = in.readString();
        ETA = in.readString();
        PAX = in.readString();
        TNo = in.readString();
        Status = in.readString();
        Flag = in.readString();
        Record = in.readString();
        Occassion = in.readString();
        SpecialRemark = in.readString();
    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CustomerName);
        dest.writeString(MobileNo);
        dest.writeString(ETA);
        dest.writeString(PAX);
        dest.writeString(TNo);
        dest.writeString(Status);
        dest.writeString(Flag);
        dest.writeString(Record);
        dest.writeString(Occassion);
        dest.writeString(SpecialRemark);
    }




    public CustomerInfoObject(String customerName, String mobileNo, String ETA, String PAX, String status, String record) {
        CustomerName = customerName;
        MobileNo = mobileNo;
        this.ETA = ETA;
        this.PAX = PAX;
        Status = status;
        Record = record;
    }
}
