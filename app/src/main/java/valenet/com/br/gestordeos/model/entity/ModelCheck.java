package valenet.com.br.gestordeos.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;

public class ModelCheck extends RealmObject {

    @SerializedName("osId")
    @Expose
    private Integer osId;
    @SerializedName("codUser")
    @Expose
    private Integer codUser;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("isCheckin")
    @Expose
    private boolean isCheckin;
    @SerializedName("checkinDate")
    @Expose
    private Date checkinDate;
    @SerializedName("checkoutDate")
    @Expose
    private Date checkoutDate;

    public ModelCheck() {

    }

    public ModelCheck(Integer osId, Integer codUser, Double latitude, Double longitude, boolean isCheckin, Date checkinDate, Date checkoutDate) {
        this.osId = osId;
        this.codUser = codUser;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isCheckin = isCheckin;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
    }

    public Integer getOsId() {
        return osId;
    }

    public void setOsId(Integer osId) {
        this.osId = osId;
    }

    public Integer getCodUser() {
        return codUser;
    }

    public void setCodUser(Integer codUser) {
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

    public boolean isCheckin() {
        return isCheckin;
    }

    public void setCheckin(boolean checkin) {
        isCheckin = checkin;
    }

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }
}
