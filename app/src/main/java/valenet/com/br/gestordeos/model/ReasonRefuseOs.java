package valenet.com.br.gestordeos.model;

public class ReasonRefuseOs {
    private Integer id;
    private String key;
    private String description;

    public ReasonRefuseOs(Integer id, String description) {
        this.id = id;
        this.key = "MOTCANAGE";
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}
