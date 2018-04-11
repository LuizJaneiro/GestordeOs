package valenet.com.br.gestordeos.model.entity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

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
    private Object admEquipeFisica;
    @SerializedName("AgentePlanetFone")
    @Expose
    private String agentePlanetFone;
    @SerializedName("infra")
    @Expose
    private Object infra;
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
    private Object inicioJornada;
    @SerializedName("fimJornada")
    @Expose
    private Object fimJornada;
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
    private Object ambiente;
    @SerializedName("QuantidadeLimiteEquipamentos")
    @Expose
    private Integer quantidadeLimiteEquipamentos;
    @SerializedName("AtendimentosCriados")
    @Expose
    private List<Object> atendimentosCriados = null;
    @SerializedName("AtendimentosResponsavel")
    @Expose
    private List<Object> atendimentosResponsavel = null;
    @SerializedName("DepartamentosGestor")
    @Expose
    private List<Object> departamentosGestor = null;
    @SerializedName("Departamento")
    @Expose
    private Object departamento;
    @SerializedName("Logins")
    @Expose
    private List<Object> logins = null;
    @SerializedName("AtendimentosRecebidos")
    @Expose
    private List<Object> atendimentosRecebidos = null;
    @SerializedName("AtendimentosEncaminhados")
    @Expose
    private List<Object> atendimentosEncaminhados = null;
    @SerializedName("OrdensDeServico")
    @Expose
    private List<Object> ordensDeServico = null;
    @SerializedName("OrdensDeServicoExecutadas")
    @Expose
    private List<Object> ordensDeServicoExecutadas = null;
    @SerializedName("Roles")
    @Expose
    private List<Object> roles = null;
    @SerializedName("WATCHER")
    @Expose
    private List<Object> wATCHER = null;
    @SerializedName("Filas")
    @Expose
    private List<Object> filas = null;
    @SerializedName("ServicoInstalacao")
    @Expose
    private List<Object> servicoInstalacao = null;
    @SerializedName("RegistroUsoCaboUTP")
    @Expose
    private List<Object> registroUsoCaboUTP = null;
    @SerializedName("Clientes")
    @Expose
    private List<Object> clientes = null;
    @SerializedName("PosicaoEquipamento")
    @Expose
    private List<Object> posicaoEquipamento = null;
    @SerializedName("EstoqueAction")
    @Expose
    private List<Object> estoqueAction = null;
    @SerializedName("ContratoEventos")
    @Expose
    private List<Object> contratoEventos = null;
    @SerializedName("PendenciasFaturamento")
    @Expose
    private List<Object> pendenciasFaturamento = null;
    @SerializedName("PendenciasFaturamento1")
    @Expose
    private List<Object> pendenciasFaturamento1 = null;
    @SerializedName("ServicoInstalacao1")
    @Expose
    private List<Object> servicoInstalacao1 = null;
    @SerializedName("ServicoInstalacao11")
    @Expose
    private List<Object> servicoInstalacao11 = null;
    @SerializedName("Regiao")
    @Expose
    private List<Object> regiao = null;
    @SerializedName("OrdemDeServicoAgendadas")
    @Expose
    private List<Object> ordemDeServicoAgendadas = null;
    @SerializedName("Bonificacao")
    @Expose
    private List<Object> bonificacao = null;
    @SerializedName("Agenda")
    @Expose
    private List<Object> agenda = null;
    @SerializedName("AgendaEvento")
    @Expose
    private List<Object> agendaEvento = null;
    @SerializedName("TipoRede")
    @Expose
    private List<Object> tipoRede = null;
    @SerializedName("MapaAtividades")
    @Expose
    private List<Object> mapaAtividades = null;
    @SerializedName("REGIAODESLOCAMENTO")
    @Expose
    private List<Object> rEGIAODESLOCAMENTO = null;
    @SerializedName("EquipesFisicas")
    @Expose
    private List<Object> equipesFisicas = null;
    @SerializedName("UsuarioProgramador")
    @Expose
    private Object usuarioProgramador;
    @SerializedName("ExceptionLog")
    @Expose
    private List<Object> exceptionLog = null;
    @SerializedName("Notificacao")
    @Expose
    private List<Object> notificacao = null;
    @SerializedName("RegistroAgente")
    @Expose
    private List<Object> registroAgente = null;
    @SerializedName("LogEvento")
    @Expose
    private List<Object> logEvento = null;
    @SerializedName("AgendaEventosDetalhes")
    @Expose
    private List<Object> agendaEventosDetalhes = null;

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

    public Object getAdmEquipeFisica() {
        return admEquipeFisica;
    }

    public void setAdmEquipeFisica(Object admEquipeFisica) {
        this.admEquipeFisica = admEquipeFisica;
    }

    public String getAgentePlanetFone() {
        return agentePlanetFone;
    }

    public void setAgentePlanetFone(String agentePlanetFone) {
        this.agentePlanetFone = agentePlanetFone;
    }

    public Object getInfra() {
        return infra;
    }

    public void setInfra(Object infra) {
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

    public Object getInicioJornada() {
        return inicioJornada;
    }

    public void setInicioJornada(Object inicioJornada) {
        this.inicioJornada = inicioJornada;
    }

    public Object getFimJornada() {
        return fimJornada;
    }

    public void setFimJornada(Object fimJornada) {
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

    public Object getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Object ambiente) {
        this.ambiente = ambiente;
    }

    public Integer getQuantidadeLimiteEquipamentos() {
        return quantidadeLimiteEquipamentos;
    }

    public void setQuantidadeLimiteEquipamentos(Integer quantidadeLimiteEquipamentos) {
        this.quantidadeLimiteEquipamentos = quantidadeLimiteEquipamentos;
    }

    public List<Object> getAtendimentosCriados() {
        return atendimentosCriados;
    }

    public void setAtendimentosCriados(List<Object> atendimentosCriados) {
        this.atendimentosCriados = atendimentosCriados;
    }

    public List<Object> getAtendimentosResponsavel() {
        return atendimentosResponsavel;
    }

    public void setAtendimentosResponsavel(List<Object> atendimentosResponsavel) {
        this.atendimentosResponsavel = atendimentosResponsavel;
    }

    public List<Object> getDepartamentosGestor() {
        return departamentosGestor;
    }

    public void setDepartamentosGestor(List<Object> departamentosGestor) {
        this.departamentosGestor = departamentosGestor;
    }

    public Object getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Object departamento) {
        this.departamento = departamento;
    }

    public List<Object> getLogins() {
        return logins;
    }

    public void setLogins(List<Object> logins) {
        this.logins = logins;
    }

    public List<Object> getAtendimentosRecebidos() {
        return atendimentosRecebidos;
    }

    public void setAtendimentosRecebidos(List<Object> atendimentosRecebidos) {
        this.atendimentosRecebidos = atendimentosRecebidos;
    }

    public List<Object> getAtendimentosEncaminhados() {
        return atendimentosEncaminhados;
    }

    public void setAtendimentosEncaminhados(List<Object> atendimentosEncaminhados) {
        this.atendimentosEncaminhados = atendimentosEncaminhados;
    }

    public List<Object> getOrdensDeServico() {
        return ordensDeServico;
    }

    public void setOrdensDeServico(List<Object> ordensDeServico) {
        this.ordensDeServico = ordensDeServico;
    }

    public List<Object> getOrdensDeServicoExecutadas() {
        return ordensDeServicoExecutadas;
    }

    public void setOrdensDeServicoExecutadas(List<Object> ordensDeServicoExecutadas) {
        this.ordensDeServicoExecutadas = ordensDeServicoExecutadas;
    }

    public List<Object> getRoles() {
        return roles;
    }

    public void setRoles(List<Object> roles) {
        this.roles = roles;
    }

    public List<Object> getWATCHER() {
        return wATCHER;
    }

    public void setWATCHER(List<Object> wATCHER) {
        this.wATCHER = wATCHER;
    }

    public List<Object> getFilas() {
        return filas;
    }

    public void setFilas(List<Object> filas) {
        this.filas = filas;
    }

    public List<Object> getServicoInstalacao() {
        return servicoInstalacao;
    }

    public void setServicoInstalacao(List<Object> servicoInstalacao) {
        this.servicoInstalacao = servicoInstalacao;
    }

    public List<Object> getRegistroUsoCaboUTP() {
        return registroUsoCaboUTP;
    }

    public void setRegistroUsoCaboUTP(List<Object> registroUsoCaboUTP) {
        this.registroUsoCaboUTP = registroUsoCaboUTP;
    }

    public List<Object> getClientes() {
        return clientes;
    }

    public void setClientes(List<Object> clientes) {
        this.clientes = clientes;
    }

    public List<Object> getPosicaoEquipamento() {
        return posicaoEquipamento;
    }

    public void setPosicaoEquipamento(List<Object> posicaoEquipamento) {
        this.posicaoEquipamento = posicaoEquipamento;
    }

    public List<Object> getEstoqueAction() {
        return estoqueAction;
    }

    public void setEstoqueAction(List<Object> estoqueAction) {
        this.estoqueAction = estoqueAction;
    }

    public List<Object> getContratoEventos() {
        return contratoEventos;
    }

    public void setContratoEventos(List<Object> contratoEventos) {
        this.contratoEventos = contratoEventos;
    }

    public List<Object> getPendenciasFaturamento() {
        return pendenciasFaturamento;
    }

    public void setPendenciasFaturamento(List<Object> pendenciasFaturamento) {
        this.pendenciasFaturamento = pendenciasFaturamento;
    }

    public List<Object> getPendenciasFaturamento1() {
        return pendenciasFaturamento1;
    }

    public void setPendenciasFaturamento1(List<Object> pendenciasFaturamento1) {
        this.pendenciasFaturamento1 = pendenciasFaturamento1;
    }

    public List<Object> getServicoInstalacao1() {
        return servicoInstalacao1;
    }

    public void setServicoInstalacao1(List<Object> servicoInstalacao1) {
        this.servicoInstalacao1 = servicoInstalacao1;
    }

    public List<Object> getServicoInstalacao11() {
        return servicoInstalacao11;
    }

    public void setServicoInstalacao11(List<Object> servicoInstalacao11) {
        this.servicoInstalacao11 = servicoInstalacao11;
    }

    public List<Object> getRegiao() {
        return regiao;
    }

    public void setRegiao(List<Object> regiao) {
        this.regiao = regiao;
    }

    public List<Object> getOrdemDeServicoAgendadas() {
        return ordemDeServicoAgendadas;
    }

    public void setOrdemDeServicoAgendadas(List<Object> ordemDeServicoAgendadas) {
        this.ordemDeServicoAgendadas = ordemDeServicoAgendadas;
    }

    public List<Object> getBonificacao() {
        return bonificacao;
    }

    public void setBonificacao(List<Object> bonificacao) {
        this.bonificacao = bonificacao;
    }

    public List<Object> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<Object> agenda) {
        this.agenda = agenda;
    }

    public List<Object> getAgendaEvento() {
        return agendaEvento;
    }

    public void setAgendaEvento(List<Object> agendaEvento) {
        this.agendaEvento = agendaEvento;
    }

    public List<Object> getTipoRede() {
        return tipoRede;
    }

    public void setTipoRede(List<Object> tipoRede) {
        this.tipoRede = tipoRede;
    }

    public List<Object> getMapaAtividades() {
        return mapaAtividades;
    }

    public void setMapaAtividades(List<Object> mapaAtividades) {
        this.mapaAtividades = mapaAtividades;
    }

    public List<Object> getREGIAODESLOCAMENTO() {
        return rEGIAODESLOCAMENTO;
    }

    public void setREGIAODESLOCAMENTO(List<Object> rEGIAODESLOCAMENTO) {
        this.rEGIAODESLOCAMENTO = rEGIAODESLOCAMENTO;
    }

    public List<Object> getEquipesFisicas() {
        return equipesFisicas;
    }

    public void setEquipesFisicas(List<Object> equipesFisicas) {
        this.equipesFisicas = equipesFisicas;
    }

    public Object getUsuarioProgramador() {
        return usuarioProgramador;
    }

    public void setUsuarioProgramador(Object usuarioProgramador) {
        this.usuarioProgramador = usuarioProgramador;
    }

    public List<Object> getExceptionLog() {
        return exceptionLog;
    }

    public void setExceptionLog(List<Object> exceptionLog) {
        this.exceptionLog = exceptionLog;
    }

    public List<Object> getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(List<Object> notificacao) {
        this.notificacao = notificacao;
    }

    public List<Object> getRegistroAgente() {
        return registroAgente;
    }

    public void setRegistroAgente(List<Object> registroAgente) {
        this.registroAgente = registroAgente;
    }

    public List<Object> getLogEvento() {
        return logEvento;
    }

    public void setLogEvento(List<Object> logEvento) {
        this.logEvento = logEvento;
    }

    public List<Object> getAgendaEventosDetalhes() {
        return agendaEventosDetalhes;
    }

    public void setAgendaEventosDetalhes(List<Object> agendaEventosDetalhes) {
        this.agendaEventosDetalhes = agendaEventosDetalhes;
    }

}
