package valenet.com.br.gestordeos.model.entity;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import retrofit2.http.Body;

public class OrdemDeServico extends RealmObject implements Parcelable {

    @SerializedName("osid")
    @Expose
    private Integer osid;
    @SerializedName("emissao")
    @Expose
    private String emissao;
    @SerializedName("solicitacao")
    @Expose
    private String solicitacao;
    @SerializedName("tipoAtividade")
    @Expose
    private String tipoAtividade;
    @SerializedName("tipoAtividadeAtributosJson")
    @Expose
    private String tipoAtividadeAtributosJson;
    @SerializedName("cliente")
    @Expose
    private String cliente;
    @SerializedName("telefoneCliente")
    @Expose
    private String telefoneCliente;
    @SerializedName("contato")
    @Expose
    private String contato;
    @SerializedName("tpLogradouro")
    @Expose
    private String tpLogradouro;
    @SerializedName("logradouro")
    @Expose
    private String logradouro;
    @SerializedName("numero")
    @Expose
    private String numero;
    @SerializedName("complemento")
    @Expose
    private String complemento;
    @SerializedName("andar")
    @Expose
    private Integer andar;
    @SerializedName("bairro")
    @Expose
    private String bairro;
    @SerializedName("cidade")
    @Expose
    private String cidade;
    @SerializedName("uf")
    @Expose
    private String uf;
    @SerializedName("cep")
    @Expose
    private String cep;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("OBSERVACAO")
    @Expose
    private String oBSERVACAO;
    @SerializedName("tipoRede")
    @Expose
    private String tipoRede;
    @SerializedName("designacaoTipo")
    @Expose
    private String designacaoTipo;
    @SerializedName("designacaoDescricao")
    @Expose
    private String designacaoDescricao;
    @SerializedName("rede")
    @Expose
    private String rede;
    @SerializedName("distance")
    @Expose
    private Integer distance;
    @SerializedName("statusOs")
    @Expose
    private String statusOs;
    @SerializedName("dataAgendamento")
    @Expose
    private String dataAgendamento;
    @SerializedName("agendadoPara")
    @Expose
    private String agendadoPara;
    @SerializedName("agendaEventoID")
    @Expose
    private Integer agendaEventoID;
    @SerializedName("DataCheckin")
    @Expose
    private String dataCheckin;
    @SerializedName("DataCheckout")
    @Expose
    private String dataCheckout;
    @SerializedName("cancelado")
    @Expose
    private Boolean cancelado;
    @SerializedName("OSPescada")
    @Expose
    private Boolean osPescada;


    public final static Parcelable.Creator<OrdemDeServico> CREATOR = new Creator<OrdemDeServico>() {


        @SuppressWarnings({
                "unchecked"
        })
        public OrdemDeServico createFromParcel(Parcel in) {
            return new OrdemDeServico(in);
        }

        public OrdemDeServico[] newArray(int size) {
            return (new OrdemDeServico[size]);
        }

    };
    private final static long serialVersionUID = -2920073099637885002L;

    protected OrdemDeServico(Parcel in) {
        this.osid = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.emissao = ((String) in.readValue((String.class.getClassLoader())));
        this.solicitacao = ((String) in.readValue((String.class.getClassLoader())));
        this.tipoAtividade = ((String) in.readValue((String.class.getClassLoader())));
        this.tipoAtividadeAtributosJson = ((String) in.readValue((String.class.getClassLoader())));
        this.cliente = ((String) in.readValue((String.class.getClassLoader())));
        this.telefoneCliente = ((String) in.readValue((String.class.getClassLoader())));
        this.contato = ((String) in.readValue((String.class.getClassLoader())));
        this.tpLogradouro = ((String) in.readValue((String.class.getClassLoader())));
        this.logradouro = ((String) in.readValue((String.class.getClassLoader())));
        this.numero = ((String) in.readValue((String.class.getClassLoader())));
        this.complemento = ((String) in.readValue((String.class.getClassLoader())));
        this.andar = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.bairro = ((String) in.readValue((String.class.getClassLoader())));
        this.cidade = ((String) in.readValue((String.class.getClassLoader())));
        this.uf = ((String) in.readValue((String.class.getClassLoader())));
        this.cep = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.longitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.oBSERVACAO = ((String) in.readValue((String.class.getClassLoader())));
        this.tipoRede = ((String) in.readValue((String.class.getClassLoader())));
        this.designacaoTipo = ((String) in.readValue((String.class.getClassLoader())));
        this.designacaoDescricao = ((String) in.readValue((String.class.getClassLoader())));
        this.rede = ((String) in.readValue((Object.class.getClassLoader())));
        this.distance = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.statusOs = ((String) in.readValue((String.class.getClassLoader())));
        this.dataAgendamento = ((String) in.readValue((String.class.getClassLoader())));
        this.agendadoPara = ((String) in.readValue((String.class.getClassLoader())));
        this.agendaEventoID = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.dataCheckin = ((String) in.readValue((String.class.getClassLoader())));
        this.dataCheckout = ((String) in.readValue((String.class.getClassLoader())));
        this.cancelado = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.osPescada = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public OrdemDeServico() {
    }

    public Integer getOsid() {
        return osid;
    }

    public void setOsid(Integer osid) {
        this.osid = osid;
    }

    public String getEmissao() {
        return emissao;
    }

    public void setEmissao(String emissao) {
        this.emissao = emissao;
    }

    public String getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(String solicitacao) {
        this.solicitacao = solicitacao;
    }

    public String getTipoAtividade() {
        return tipoAtividade;
    }

    public void setTipoAtividade(String tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    public String getTipoAtividadeAtributosJson() {
        return tipoAtividadeAtributosJson;
    }

    public void setTipoAtividadeAtributosJson(String tipoAtividadeAtributosJson) {
        this.tipoAtividadeAtributosJson = tipoAtividadeAtributosJson;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getTpLogradouro() {
        return tpLogradouro;
    }

    public void setTpLogradouro(String tpLogradouro) {
        this.tpLogradouro = tpLogradouro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Integer getAndar() {
        return andar;
    }

    public void setAndar(Integer andar) {
        this.andar = andar;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
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

    public String getOBSERVACAO() {
        return oBSERVACAO;
    }

    public void setOBSERVACAO(String oBSERVACAO) {
        this.oBSERVACAO = oBSERVACAO;
    }

    public String getTipoRede() {
        return tipoRede;
    }

    public void setTipoRede(String tipoRede) {
        this.tipoRede = tipoRede;
    }

    public String getDesignacaoTipo() {
        return designacaoTipo;
    }

    public void setDesignacaoTipo(String designacaoTipo) {
        this.designacaoTipo = designacaoTipo;
    }

    public String getDesignacaoDescricao() {
        return designacaoDescricao;
    }

    public void setDesignacaoDescricao(String designacaoDescricao) {
        this.designacaoDescricao = designacaoDescricao;
    }

    public String getRede() {
        return rede;
    }

    public void setRede(String rede) {
        this.rede = rede;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getStatusOs() {
        return statusOs;
    }

    public void setStatusOs(String statusOs) {
        this.statusOs = statusOs;
    }

    public String getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(String dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public String getAgendadoPara() {
        return agendadoPara;
    }

    public void setAgendadoPara(String agendadoPara) {
        this.agendadoPara = agendadoPara;
    }

    public Integer getAgendaEventoID() {
        return agendaEventoID;
    }

    public void setAgendaEventoID(Integer agendaEventoID) {
        this.agendaEventoID = agendaEventoID;
    }

    public String getoBSERVACAO() {
        return oBSERVACAO;
    }

    public void setoBSERVACAO(String oBSERVACAO) {
        this.oBSERVACAO = oBSERVACAO;
    }

    public String getDataCheckin() {
        return dataCheckin;
    }

    public void setDataCheckin(String dataCheckin) {
        this.dataCheckin = dataCheckin;
    }

    public String getDataCheckout() {
        return dataCheckout;
    }

    public void setDataCheckout(String dataCheckout) {
        this.dataCheckout = dataCheckout;
    }

    public Boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public Boolean getOsPescada() {
        return osPescada;
    }

    public void setOsPescada(Boolean osPescada) {
        this.osPescada = osPescada;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(osid);
        dest.writeValue(emissao);
        dest.writeValue(solicitacao);
        dest.writeValue(tipoAtividade);
        dest.writeValue(tipoAtividadeAtributosJson);
        dest.writeValue(cliente);
        dest.writeValue(telefoneCliente);
        dest.writeValue(contato);
        dest.writeValue(tpLogradouro);
        dest.writeValue(logradouro);
        dest.writeValue(numero);
        dest.writeValue(complemento);
        dest.writeValue(andar);
        dest.writeValue(bairro);
        dest.writeValue(cidade);
        dest.writeValue(uf);
        dest.writeValue(cep);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeValue(oBSERVACAO);
        dest.writeValue(tipoRede);
        dest.writeValue(designacaoTipo);
        dest.writeValue(designacaoDescricao);
        dest.writeValue(rede);
        dest.writeValue(distance);
        dest.writeValue(statusOs);
        dest.writeValue(dataAgendamento);
        dest.writeValue(agendadoPara);
        dest.writeValue(agendaEventoID);
        dest.writeValue(dataCheckin);
        dest.writeValue(dataCheckout);
        dest.writeValue(cancelado);
        dest.writeValue(osPescada);
    }

    public int describeContents() {
        return 0;
    }

}

