package valenet.com.br.gestordeos.model.entity.os_location_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;

public class OsLocationData extends RealmObject {

    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("dataColeta")
    @Expose
    private Date dataColeta;
    @SerializedName("codUser")
    @Expose
    private Integer codUser;

    public OsLocationData(){

    }

    public OsLocationData(Double latitude, Double longitude, Date date, Integer codUser) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataColeta = date;
        this.codUser = codUser;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getDate() {
        return dataColeta;
    }

    public void setDate(Date date) {
        this.dataColeta = date;
    }

    public Integer getCodUser() {
        return codUser;
    }

    public void setCodUser(Integer codUser) {
        this.codUser = codUser;
    }
    @Override
    public String toString() {
        return "Post{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", date=" + dataColeta +
                ", codUser=" + codUser +
                '}';
    }

}