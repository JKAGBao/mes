package project.bridgetek.com.applib.main.toos;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentConfig implements Parcelable {
    public boolean isTmp;
    public int sample_emi;
    public int sample_freq;
    public int sample_nums;
    public String sample_type;
    public String sensor_mac;
    public String sensor_name;
    public  CurrentConfig(){}
    public CurrentConfig(Parcel in) {
        isTmp = in.readByte() != 0;
        sample_emi = in.readInt();
        sample_freq = in.readInt();
        sample_nums = in.readInt();
        sample_type = in.readString();
        sensor_mac = in.readString();
        sensor_name = in.readString();
    }

    public static final Creator<CurrentConfig> CREATOR = new Creator<CurrentConfig>() {
        @Override
        public CurrentConfig createFromParcel(Parcel in) {

            return new CurrentConfig(in);
        }

        @Override
        public CurrentConfig[] newArray(int size) {
            return new CurrentConfig[size];
        }
    };
   public void isTmp(boolean paramValue){ this.isTmp = paramValue;}
    public void setEmi(int paramInt) {
        this.sample_emi = paramInt;
    }

    public void setFreq(int paramInt) {
        this.sample_freq = paramInt;
    }

    public void setMac(String paramString) {
        this.sensor_mac = paramString;
    }

    public void setName(String paramString) {
        this.sensor_name = paramString;
    }

    public void setNums(int paramInt) {
        this.sample_nums = paramInt;
    }

    public void setType(String paramString) {
        this.sample_type = paramString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isTmp ? 1 : 0));
        dest.writeInt(sample_emi);
        dest.writeInt(sample_freq);
        dest.writeInt(sample_nums);
        dest.writeString(sample_type);
        dest.writeString(sensor_mac);
        dest.writeString(sensor_name);
    }
}
