package valenet.com.br.gestordeos.model.entity.google_distance;

import android.os.Parcel;
import android.os.Parcelable;

public class OsDistanceAndPoints implements Parcelable{

    private Integer distance;
    private String encodedStringPoints;

    public OsDistanceAndPoints(Integer distance, String encodedStringPoints) {
        this.distance = distance;
        this.encodedStringPoints = encodedStringPoints;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getEncodedStringPoints() {
        return encodedStringPoints;
    }

    public void setEncodedStringPoints(String encodedStringPoints) {
        this.encodedStringPoints = encodedStringPoints;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(distance);
        dest.writeValue(encodedStringPoints);
    }

    protected OsDistanceAndPoints(Parcel in) {
        this.distance = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.encodedStringPoints = ((String) in.readValue((String.class.getClassLoader())));
    }

    public static final Creator<OsDistanceAndPoints> CREATOR = new Creator<OsDistanceAndPoints>() {
        @Override
        public OsDistanceAndPoints createFromParcel(Parcel in) {
            return new OsDistanceAndPoints(in);
        }

        @Override
        public OsDistanceAndPoints[] newArray(int size) {
            return new OsDistanceAndPoints[size];
        }
    };

}
