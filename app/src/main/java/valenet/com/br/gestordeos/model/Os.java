package valenet.com.br.gestordeos.model;

import java.util.Date;

public class Os {

    private double distance;
    private Date date;
    private String type;
    private String client;

    public Os() {

    }

    public Os(double distance, Date date, String type, String client) {
        this.distance = distance;
        this.date = date;
        this.type = type;
        this.client = client;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

}
