package valenet.com.br.gestordeos.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class AppConfig extends RealmObject implements Serializable, Parcelable {

    @SerializedName("chave")
    @Expose
    private String chave;
    @SerializedName("valor")
    @Expose
    private String valor;
    public final static Parcelable.Creator<AppConfig> CREATOR = new Creator<AppConfig>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AppConfig createFromParcel(Parcel in) {
            return new AppConfig(in);
        }

        public AppConfig[] newArray(int size) {
            return (new AppConfig[size]);
        }

    };


    protected AppConfig(Parcel in) {
        this.chave = ((String) in.readValue((String.class.getClassLoader())));
        this.valor = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AppConfig() {
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(chave);
        dest.writeValue(valor);
    }

    public int describeContents() {
        return 0;
    }

}
