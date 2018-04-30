package valenet.com.br.gestordeos.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OsTypeModel implements Parcelable
{

    @SerializedName("tipoAtividadeOsId")
    @Expose
    private Integer tipoAtividadeOsId;
    @SerializedName("descricao")
    @Expose
    private String descricao;
    @SerializedName("tipoMercantil")
    @Expose
    private Boolean tipoMercantil;
    public final static Parcelable.Creator<OsTypeModel> CREATOR = new Creator<OsTypeModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public OsTypeModel createFromParcel(Parcel in) {
            return new OsTypeModel(in);
        }

        public OsTypeModel[] newArray(int size) {
            return (new OsTypeModel[size]);
        }

    }
            ;

    protected OsTypeModel(Parcel in) {
        this.tipoAtividadeOsId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.descricao = ((String) in.readValue((String.class.getClassLoader())));
        this.tipoMercantil = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public OsTypeModel() {
    }

    public Integer getTipoAtividadeOsId() {
        return tipoAtividadeOsId;
    }

    public void setTipoAtividadeOsId(Integer tipoAtividadeOsId) {
        this.tipoAtividadeOsId = tipoAtividadeOsId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getTipoMercantil() {
        return tipoMercantil;
    }

    public void setTipoMercantil(Boolean tipoMercantil) {
        this.tipoMercantil = tipoMercantil;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(tipoAtividadeOsId);
        dest.writeValue(descricao);
        dest.writeValue(tipoMercantil);
    }

    public int describeContents() {
        return 0;
    }

}