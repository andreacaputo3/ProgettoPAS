package it.unisalento.pas.smartcitywastemanagement.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "cassonetti")
public class Cassonetto {
    @Id
    private String id;
    private String tipoRifiuto;
    private LocalDateTime dataOraConferimento;
    private String cittadinoId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoRifiuto() {
        return tipoRifiuto;
    }

    public void setTipoRifiuto(String tipoRifiuto) {
        this.tipoRifiuto = tipoRifiuto;
    }

    public LocalDateTime getDataOraConferimento() {
        return dataOraConferimento;
    }

    public void setDataOraConferimento(LocalDateTime dataOraConferimento) {
        this.dataOraConferimento = dataOraConferimento;
    }

    public String getCittadinoId() {
        return cittadinoId;
    }

    public void setCittadinoId(String cittadinoId) {
        this.cittadinoId = cittadinoId;
    }
}
