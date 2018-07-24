package valenet.com.br.gestordeos.model.entity.os_location_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;

public class OsLocationData extends RealmObject {

    @SerializedName("Latitude")
    @Expose
    private Double latitude;
    @SerializedName("Longitude")
    @Expose
    private Double longitude;
    @SerializedName("DataColeta")
    @Expose
    private Date dataColeta;
    @SerializedName("CodUser")
    @Expose
    private Integer codUser;
    @SerializedName("NivelBateria")
    @Expose
    private Integer nivelBateria;
    @SerializedName("IMEI")
    @Expose
    private String imei;

    public OsLocationData() {

    }

    public OsLocationData(Double latitude, Double longitude, Date date, Integer codUser, Integer nivelBateria, String imei) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataColeta = date;
        this.codUser = codUser;
        this.nivelBateria = nivelBateria;
        this.imei = imei;
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

    public Integer getNivelBateria() {
        return nivelBateria;
    }

    public void setNivelBateria(Integer nivelBateria) {
        this.nivelBateria = nivelBateria;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String toString() {
        return "Post{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", date=" + dataColeta +
                ", codUser=" + codUser +
                ", nivelBateria=" + nivelBateria +
                ", IMEI=" + imei + '}';
    }

}
