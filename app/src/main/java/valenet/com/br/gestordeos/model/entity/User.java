package valenet.com.br.gestordeos.model.entity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class User extends RealmObject{

    @SerializedName("coduser")
    @Expose
    private Integer coduser;
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("nome")
    @Expose
    private String nome;
    @SerializedName("passwd")
    @Expose
    private String passwd;
    @SerializedName("datacad")
    @Expose
    private String datacad;
    @SerializedName("ativo")
    @Expose
    private Integer ativo;
    @SerializedName("ramal")
    @Expose
    private String ramal;
    @SerializedName("departamentoid")
    @Expose
    private Integer departamentoid;
    @SerializedName("equipeFisica")
    @Expose
    private Integer equipeFisica;
    @SerializedName("PermissaoBoleto")
    @Expose
    private String permissaoBoleto;
    @SerializedName("pppoepassword")
    @Expose
    private String pppoepassword;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("LoginAD")
    @Expose
    private String loginAD;
    @SerializedName("AdmEquipeFisica")
    @Expose
    private RealmString admEquipeFisica;
    @SerializedName("AgentePlanetFone")
    @Expose
    private String agentePlanetFone;
    @SerializedName("infra")
    @Expose
    private RealmString infra;
    @SerializedName("PontuacaoMinima")
    @Expose
    private Integer pontuacaoMinima;
    @SerializedName("MetaPontuacao")
    @Expose
    private Integer metaPontuacao;
    @SerializedName("AdminVendas")
    @Expose
    private Integer adminVendas;
    @SerializedName("addcontrato")
    @Expose
    private Boolean addcontrato;
    @SerializedName("Nascimento")
    @Expose
    private String nascimento;
    @SerializedName("RG")
    @Expose
    private String rG;
    @SerializedName("CPF")
    @Expose
    private String cPF;
    @SerializedName("TelefoneResidencial")
    @Expose
    private String telefoneResidencial;
    @SerializedName("TelefoneCelular")
    @Expose
    private String telefoneCelular;
    @SerializedName("CadastroAD")
    @Expose
    private Integer cadastroAD;
    @SerializedName("EditarOS")
    @Expose
    private Integer editarOS;
    @SerializedName("TempoVidaEquipamento")
    @Expose
    private Integer tempoVidaEquipamento;
    @SerializedName("IgnoraLimiteQuantitativo")
    @Expose
    private Boolean ignoraLimiteQuantitativo;
    @SerializedName("IgnoraLimiteTempo")
    @Expose
    private Boolean ignoraLimiteTempo;
    @SerializedName("CodCaixaProtheus")
    @Expose
    private String codCaixaProtheus;
    @SerializedName("FilialProtheus")
    @Expose
    private String filialProtheus;
    @SerializedName("ResponsavelCarteira")
    @Expose
    private Integer responsavelCarteira;
    @SerializedName("inicioJornada")
    @Expose
    private RealmString inicioJornada;
    @SerializedName("fimJornada")
    @Expose
    private RealmString fimJornada;
    @SerializedName("coduser_programador")
    @Expose
    private Integer coduserProgramador;
    @SerializedName("receberEmail")
    @Expose
    private Boolean receberEmail;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("ambiente")
    @Expose
    private RealmString ambiente;
    @SerializedName("QuantidadeLimiteEquipamentos")
    @Expose
    private Integer quantidadeLimiteEquipamentos;
    @SerializedName("AtendimentosCriados")
    @Expose
    private RealmList<RealmString> atendimentosCriados = null;
    @SerializedName("AtendimentosResponsavel")
    @Expose
    private RealmList<RealmString> atendimentosResponsavel = null;
    @SerializedName("DepartamentosGestor")
    @Expose
    private RealmList<RealmString> departamentosGestor = null;
    @SerializedName("Departamento")
    @Expose
    private RealmString departamento;
    @SerializedName("Logins")
    @Expose
    private RealmList<RealmString> logins = null;
    @SerializedName("AtendimentosRecebidos")
    @Expose
    private RealmList<RealmString> atendimentosRecebidos = null;
    @SerializedName("AtendimentosEncaminhados")
    @Expose
    private RealmList<RealmString> atendimentosEncaminhados = null;
    @SerializedName("OrdensDeServico")
    @Expose
    private RealmList<RealmString> ordensDeServico = null;
    @SerializedName("OrdensDeServicoExecutadas")
    @Expose
    private RealmList<RealmString> ordensDeServicoExecutadas = null;
    @SerializedName("Roles")
    @Expose
    private RealmList<RealmString> roles = null;
    @SerializedName("WATCHER")
    @Expose
    private RealmList<RealmString> wATCHER = null;
    @SerializedName("Filas")
    @Expose
    private RealmList<RealmString> filas = null;
    @SerializedName("ServicoInstalacao")
    @Expose
    private RealmList<RealmString> servicoInstalacao = null;
    @SerializedName("RegistroUsoCaboUTP")
    @Expose
    private RealmList<RealmString> registroUsoCaboUTP = null;
    @SerializedName("Clientes")
    @Expose
    private RealmList<RealmString> clientes = null;
    @SerializedName("PosicaoEquipamento")
    @Expose
    private RealmList<RealmString> posicaoEquipamento = null;
    @SerializedName("EstoqueAction")
    @Expose
    private RealmList<RealmString> estoqueAction = null;
    @SerializedName("ContratoEventos")
    @Expose
    private RealmList<RealmString> contratoEventos = null;
    @SerializedName("PendenciasFaturamento")
    @Expose
    private RealmList<RealmString> pendenciasFaturamento = null;
    @SerializedName("PendenciasFaturamento1")
    @Expose
    private RealmList<RealmString> pendenciasFaturamento1 = null;
    @SerializedName("ServicoInstalacao1")
    @Expose
    private RealmList<RealmString> servicoInstalacao1 = null;
    @SerializedName("ServicoInstalacao11")
    @Expose
    private RealmList<RealmString> servicoInstalacao11 = null;
    @SerializedName("Regiao")
    @Expose
    private RealmList<RealmString> regiao = null;
    @SerializedName("OrdemDeServicoAgendadas")
    @Expose
    private RealmList<RealmString> ordemDeServicoAgendadas = null;
    @SerializedName("Bonificacao")
    @Expose
    private RealmList<RealmString> bonificacao = null;
    @SerializedName("Agenda")
    @Expose
    private RealmList<RealmString> agenda = null;
    @SerializedName("AgendaEvento")
    @Expose
    private RealmList<RealmString> agendaEvento = null;
    @SerializedName("TipoRede")
    @Expose
    private RealmList<RealmString> tipoRede = null;
    @SerializedName("MapaAtividades")
    @Expose
    private RealmList<RealmString> mapaAtividades = null;
    @SerializedName("REGIAODESLOCAMENTO")
    @Expose
    private RealmList<RealmString> rEGIAODESLOCAMENTO = null;
    @SerializedName("EquipesFisicas")
    @Expose
    private RealmList<RealmString> equipesFisicas = null;
    @SerializedName("UsuarioProgramador")
    @Expose
    private RealmString usuarioProgramador;
    @SerializedName("ExceptionLog")
    @Expose
    private RealmList<RealmString> exceptionLog = null;
    @SerializedName("Notificacao")
    @Expose
    private RealmList<RealmString> notificacao = null;
    @SerializedName("RegistroAgente")
    @Expose
    private RealmList<RealmString> registroAgente = null;
    @SerializedName("LogEvento")
    @Expose
    private RealmList<RealmString> logEvento = null;
    @SerializedName("AgendaEventosDetalhes")
    @Expose
    private RealmList<RealmString> agendaEventosDetalhes = null;

    public Integer getCoduser() {
        return coduser;
    }

    public void setCoduser(Integer coduser) {
        this.coduser = coduser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getDatacad() {
        return datacad;
    }

    public void setDatacad(String datacad) {
        this.datacad = datacad;
    }

    public Integer getAtivo() {
        return ativo;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    public String getRamal() {
        return ramal;
    }

    public void setRamal(String ramal) {
        this.ramal = ramal;
    }

    public Integer getDepartamentoid() {
        return departamentoid;
    }

    public void setDepartamentoid(Integer departamentoid) {
        this.departamentoid = departamentoid;
    }

    public Integer getEquipeFisica() {
        return equipeFisica;
    }

    public void setEquipeFisica(Integer equipeFisica) {
        this.equipeFisica = equipeFisica;
    }

    public String getPermissaoBoleto() {
        return permissaoBoleto;
    }

    public void setPermissaoBoleto(String permissaoBoleto) {
        this.permissaoBoleto = permissaoBoleto;
    }

    public String getPppoepassword() {
        return pppoepassword;
    }

    public void setPppoepassword(String pppoepassword) {
        this.pppoepassword = pppoepassword;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLoginAD() {
        return loginAD;
    }

    public void setLoginAD(String loginAD) {
        this.loginAD = loginAD;
    }

    public RealmString getAdmEquipeFisica() {
        return admEquipeFisica;
    }

    public void setAdmEquipeFisica(RealmString admEquipeFisica) {
        this.admEquipeFisica = admEquipeFisica;
    }

    public String getAgentePlanetFone() {
        return agentePlanetFone;
    }

    public void setAgentePlanetFone(String agentePlanetFone) {
        this.agentePlanetFone = agentePlanetFone;
    }

    public RealmString getInfra() {
        return infra;
    }

    public void setInfra(RealmString infra) {
        this.infra = infra;
    }

    public Integer getPontuacaoMinima() {
        return pontuacaoMinima;
    }

    public void setPontuacaoMinima(Integer pontuacaoMinima) {
        this.pontuacaoMinima = pontuacaoMinima;
    }

    public Integer getMetaPontuacao() {
        return metaPontuacao;
    }

    public void setMetaPontuacao(Integer metaPontuacao) {
        this.metaPontuacao = metaPontuacao;
    }

    public Integer getAdminVendas() {
        return adminVendas;
    }

    public void setAdminVendas(Integer adminVendas) {
        this.adminVendas = adminVendas;
    }

    public Boolean getAddcontrato() {
        return addcontrato;
    }

    public void setAddcontrato(Boolean addcontrato) {
        this.addcontrato = addcontrato;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getRG() {
        return rG;
    }

    public void setRG(String rG) {
        this.rG = rG;
    }

    public String getCPF() {
        return cPF;
    }

    public void setCPF(String cPF) {
        this.cPF = cPF;
    }

    public String getTelefoneResidencial() {
        return telefoneResidencial;
    }

    public void setTelefoneResidencial(String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public Integer getCadastroAD() {
        return cadastroAD;
    }

    public void setCadastroAD(Integer cadastroAD) {
        this.cadastroAD = cadastroAD;
    }

    public Integer getEditarOS() {
        return editarOS;
    }

    public void setEditarOS(Integer editarOS) {
        this.editarOS = editarOS;
    }

    public Integer getTempoVidaEquipamento() {
        return tempoVidaEquipamento;
    }

    public void setTempoVidaEquipamento(Integer tempoVidaEquipamento) {
        this.tempoVidaEquipamento = tempoVidaEquipamento;
    }

    public Boolean getIgnoraLimiteQuantitativo() {
        return ignoraLimiteQuantitativo;
    }

    public void setIgnoraLimiteQuantitativo(Boolean ignoraLimiteQuantitativo) {
        this.ignoraLimiteQuantitativo = ignoraLimiteQuantitativo;
    }

    public Boolean getIgnoraLimiteTempo() {
        return ignoraLimiteTempo;
    }

    public void setIgnoraLimiteTempo(Boolean ignoraLimiteTempo) {
        this.ignoraLimiteTempo = ignoraLimiteTempo;
    }

    public String getCodCaixaProtheus() {
        return codCaixaProtheus;
    }

    public void setCodCaixaProtheus(String codCaixaProtheus) {
        this.codCaixaProtheus = codCaixaProtheus;
    }

    public String getFilialProtheus() {
        return filialProtheus;
    }

    public void setFilialProtheus(String filialProtheus) {
        this.filialProtheus = filialProtheus;
    }

    public Integer getResponsavelCarteira() {
        return responsavelCarteira;
    }

    public void setResponsavelCarteira(Integer responsavelCarteira) {
        this.responsavelCarteira = responsavelCarteira;
    }

    public RealmString getInicioJornada() {
        return inicioJornada;
    }

    public void setInicioJornada(RealmString inicioJornada) {
        this.inicioJornada = inicioJornada;
    }

    public RealmString getFimJornada() {
        return fimJornada;
    }

    public void setFimJornada(RealmString fimJornada) {
        this.fimJornada = fimJornada;
    }

    public Integer getCoduserProgramador() {
        return coduserProgramador;
    }

    public void setCoduserProgramador(Integer coduserProgramador) {
        this.coduserProgramador = coduserProgramador;
    }

    public Boolean getReceberEmail() {
        return receberEmail;
    }

    public void setReceberEmail(Boolean receberEmail) {
        this.receberEmail = receberEmail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RealmString getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(RealmString ambiente) {
        this.ambiente = ambiente;
    }

    public Integer getQuantidadeLimiteEquipamentos() {
        return quantidadeLimiteEquipamentos;
    }

    public void setQuantidadeLimiteEquipamentos(Integer quantidadeLimiteEquipamentos) {
        this.quantidadeLimiteEquipamentos = quantidadeLimiteEquipamentos;
    }

    public RealmList<RealmString> getAtendimentosCriados() {
        return atendimentosCriados;
    }

    public void setAtendimentosCriados(RealmList<RealmString> atendimentosCriados) {
        this.atendimentosCriados = atendimentosCriados;
    }

    public RealmList<RealmString> getAtendimentosResponsavel() {
        return atendimentosResponsavel;
    }

    public void setAtendimentosResponsavel(RealmList<RealmString> atendimentosResponsavel) {
        this.atendimentosResponsavel = atendimentosResponsavel;
    }

    public RealmList<RealmString> getDepartamentosGestor() {
        return departamentosGestor;
    }

    public void setDepartamentosGestor(RealmList<RealmString> departamentosGestor) {
        this.departamentosGestor = departamentosGestor;
    }

    public RealmString getDepartamento() {
        return departamento;
    }

    public void setDepartamento(RealmString departamento) {
        this.departamento = departamento;
    }

    public RealmList<RealmString> getLogins() {
        return logins;
    }

    public void setLogins(RealmList<RealmString> logins) {
        this.logins = logins;
    }

    public RealmList<RealmString> getAtendimentosRecebidos() {
        return atendimentosRecebidos;
    }

    public void setAtendimentosRecebidos(RealmList<RealmString> atendimentosRecebidos) {
        this.atendimentosRecebidos = atendimentosRecebidos;
    }

    public RealmList<RealmString> getAtendimentosEncaminhados() {
        return atendimentosEncaminhados;
    }

    public void setAtendimentosEncaminhados(RealmList<RealmString> atendimentosEncaminhados) {
        this.atendimentosEncaminhados = atendimentosEncaminhados;
    }

    public RealmList<RealmString> getOrdensDeServico() {
        return ordensDeServico;
    }

    public void setOrdensDeServico(RealmList<RealmString> ordensDeServico) {
        this.ordensDeServico = ordensDeServico;
    }

    public RealmList<RealmString> getOrdensDeServicoExecutadas() {
        return ordensDeServicoExecutadas;
    }

    public void setOrdensDeServicoExecutadas(RealmList<RealmString> ordensDeServicoExecutadas) {
        this.ordensDeServicoExecutadas = ordensDeServicoExecutadas;
    }

    public RealmList<RealmString> getRoles() {
        return roles;
    }

    public void setRoles(RealmList<RealmString> roles) {
        this.roles = roles;
    }

    public RealmList<RealmString> getWATCHER() {
        return wATCHER;
    }

    public void setWATCHER(RealmList<RealmString> wATCHER) {
        this.wATCHER = wATCHER;
    }

    public RealmList<RealmString> getFilas() {
        return filas;
    }

    public void setFilas(RealmList<RealmString> filas) {
        this.filas = filas;
    }

    public RealmList<RealmString> getServicoInstalacao() {
        return servicoInstalacao;
    }

    public void setServicoInstalacao(RealmList<RealmString> servicoInstalacao) {
        this.servicoInstalacao = servicoInstalacao;
    }

    public RealmList<RealmString> getRegistroUsoCaboUTP() {
        return registroUsoCaboUTP;
    }

    public void setRegistroUsoCaboUTP(RealmList<RealmString> registroUsoCaboUTP) {
        this.registroUsoCaboUTP = registroUsoCaboUTP;
    }

    public RealmList<RealmString> getClientes() {
        return clientes;
    }

    public void setClientes(RealmList<RealmString> clientes) {
        this.clientes = clientes;
    }

    public RealmList<RealmString> getPosicaoEquipamento() {
        return posicaoEquipamento;
    }

    public void setPosicaoEquipamento(RealmList<RealmString> posicaoEquipamento) {
        this.posicaoEquipamento = posicaoEquipamento;
    }

    public RealmList<RealmString> getEstoqueAction() {
        return estoqueAction;
    }

    public void setEstoqueAction(RealmList<RealmString> estoqueAction) {
        this.estoqueAction = estoqueAction;
    }

    public RealmList<RealmString> getContratoEventos() {
        return contratoEventos;
    }

    public void setContratoEventos(RealmList<RealmString> contratoEventos) {
        this.contratoEventos = contratoEventos;
    }

    public RealmList<RealmString> getPendenciasFaturamento() {
        return pendenciasFaturamento;
    }

    public void setPendenciasFaturamento(RealmList<RealmString> pendenciasFaturamento) {
        this.pendenciasFaturamento = pendenciasFaturamento;
    }

    public RealmList<RealmString> getPendenciasFaturamento1() {
        return pendenciasFaturamento1;
    }

    public void setPendenciasFaturamento1(RealmList<RealmString> pendenciasFaturamento1) {
        this.pendenciasFaturamento1 = pendenciasFaturamento1;
    }

    public RealmList<RealmString> getServicoInstalacao1() {
        return servicoInstalacao1;
    }

    public void setServicoInstalacao1(RealmList<RealmString> servicoInstalacao1) {
        this.servicoInstalacao1 = servicoInstalacao1;
    }

    public RealmList<RealmString> getServicoInstalacao11() {
        return servicoInstalacao11;
    }

    public void setServicoInstalacao11(RealmList<RealmString> servicoInstalacao11) {
        this.servicoInstalacao11 = servicoInstalacao11;
    }

    public RealmList<RealmString> getRegiao() {
        return regiao;
    }

    public void setRegiao(RealmList<RealmString> regiao) {
        this.regiao = regiao;
    }

    public RealmList<RealmString> getOrdemDeServicoAgendadas() {
        return ordemDeServicoAgendadas;
    }

    public void setOrdemDeServicoAgendadas(RealmList<RealmString> ordemDeServicoAgendadas) {
        this.ordemDeServicoAgendadas = ordemDeServicoAgendadas;
    }

    public RealmList<RealmString> getBonificacao() {
        return bonificacao;
    }

    public void setBonificacao(RealmList<RealmString> bonificacao) {
        this.bonificacao = bonificacao;
    }

    public RealmList<RealmString> getAgenda() {
        return agenda;
    }

    public void setAgenda(RealmList<RealmString> agenda) {
        this.agenda = agenda;
    }

    public RealmList<RealmString> getAgendaEvento() {
        return agendaEvento;
    }

    public void setAgendaEvento(RealmList<RealmString> agendaEvento) {
        this.agendaEvento = agendaEvento;
    }

    public RealmList<RealmString> getTipoRede() {
        return tipoRede;
    }

    public void setTipoRede(RealmList<RealmString> tipoRede) {
        this.tipoRede = tipoRede;
    }

    public RealmList<RealmString> getMapaAtividades() {
        return mapaAtividades;
    }

    public void setMapaAtividades(RealmList<RealmString> mapaAtividades) {
        this.mapaAtividades = mapaAtividades;
    }

    public RealmList<RealmString> getREGIAODESLOCAMENTO() {
        return rEGIAODESLOCAMENTO;
    }

    public void setREGIAODESLOCAMENTO(RealmList<RealmString> rEGIAODESLOCAMENTO) {
        this.rEGIAODESLOCAMENTO = rEGIAODESLOCAMENTO;
    }

    public RealmList<RealmString> getEquipesFisicas() {
        return equipesFisicas;
    }

    public void setEquipesFisicas(RealmList<RealmString> equipesFisicas) {
        this.equipesFisicas = equipesFisicas;
    }

    public RealmString getUsuarioProgramador() {
        return usuarioProgramador;
    }

    public void setUsuarioProgramador(RealmString usuarioProgramador) {
        this.usuarioProgramador = usuarioProgramador;
    }

    public RealmList<RealmString> getExceptionLog() {
        return exceptionLog;
    }

    public void setExceptionLog(RealmList<RealmString> exceptionLog) {
        this.exceptionLog = exceptionLog;
    }

    public RealmList<RealmString> getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(RealmList<RealmString> notificacao) {
        this.notificacao = notificacao;
    }

    public RealmList<RealmString> getRegistroAgente() {
        return registroAgente;
    }

    public void setRegistroAgente(RealmList<RealmString> registroAgente) {
        this.registroAgente = registroAgente;
    }

    public RealmList<RealmString> getLogEvento() {
        return logEvento;
    }

    public void setLogEvento(RealmList<RealmString> logEvento) {
        this.logEvento = logEvento;
    }

    public RealmList<RealmString> getAgendaEventosDetalhes() {
        return agendaEventosDetalhes;
    }

    public void setAgendaEventosDetalhes(RealmList<RealmString> agendaEventosDetalhes) {
        this.agendaEventosDetalhes = agendaEventosDetalhes;
    }

}
