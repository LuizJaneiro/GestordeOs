package valenet.com.br.gestordeos.model.entity;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ReasonRefuseOs extends RealmObject implements Serializable, Parcelable
{

    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("Descricao")
    @Expose
    private String descricao;
    public final static Parcelable.Creator<ReasonRefuseOs> CREATOR = new Creator<ReasonRefuseOs>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ReasonRefuseOs createFromParcel(Parcel in) {
            return new ReasonRefuseOs(in);
        }

        public ReasonRefuseOs[] newArray(int size) {
            return (new ReasonRefuseOs[size]);
        }

    }
            ;
    private final static long serialVersionUID = 4091262568112528655L;

    protected ReasonRefuseOs(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.descricao = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ReasonRefuseOs() {
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(descricao);
    }

    public int describeContents() {
        return 0;
    }

}