package Risorse;

public class PuntoVendita {
    
    private int idPuntoVendita;
    private String denominazione;
    private String indirizzo;
    private int cap;
    private String comune;
    private String codProvincia;
    private String urlSito;
    private String telefonoPrincipale;
    private String telefonoSecondario;
    private String email;
    private String latitudine;
    private String longitudine;
    private boolean flagFisicoOnline;
    private int idEsercente;
    private String ragioneSociale;
    
    // constructor empty and not
    public PuntoVendita() {
    }

    public PuntoVendita(int idPuntoVendita, String denominazione, String indirizzo, int cap, String comune, String codProvincia, String urlSito, String telefonoPrincipale, String telefonoSecondario, String email, String latitudine, String longitudine, boolean flagFisicoOnline, int idEsercente, String ragioneSociale) {
        this.idPuntoVendita = idPuntoVendita;
        this.denominazione = denominazione;
        this.indirizzo = indirizzo;
        this.cap = cap;
        this.comune = comune;
        this.codProvincia = codProvincia;
        this.urlSito = urlSito;
        this.telefonoPrincipale = telefonoPrincipale;
        this.telefonoSecondario = telefonoSecondario;
        this.email = email;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.flagFisicoOnline = flagFisicoOnline;
        this.idEsercente = idEsercente;
        this.ragioneSociale = ragioneSociale;
    }

    // Getter&Setter
    public int getIdPuntoVendita() {
        return idPuntoVendita;
    }

    public void setIdPuntoVendita(int idPuntoVendita) {
        this.idPuntoVendita = idPuntoVendita;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public int getCap() {
        return cap;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getCodProvincia() {
        return codProvincia;
    }

    public void setCodProvincia(String codProvincia) {
        this.codProvincia = codProvincia;
    }

    public String getUrlSito() {
        return urlSito;
    }

    public void setUrlSito(String urlSito) {
        this.urlSito = urlSito;
    }

    public String getTelefonoPrincipale() {
        return telefonoPrincipale;
    }

    public void setTelefonoPrincipale(String telefonoPrincipale) {
        this.telefonoPrincipale = telefonoPrincipale;
    }

    public String getTelefonoSecondario() {
        return telefonoSecondario;
    }

    public void setTelefonoSecondario(String telefonoSecondario) {
        this.telefonoSecondario = telefonoSecondario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(String latitudine) {
        this.latitudine = latitudine;
    }

    public String getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(String longitudine) {
        this.longitudine = longitudine;
    }

    public boolean isFlagFisicoOnline() {
        return flagFisicoOnline;
    }

    public void setFlagFisicoOnline(boolean flagFisicoOnline) {
        this.flagFisicoOnline = flagFisicoOnline;
    }

    public int getIdEsercente() {
        return idEsercente;
    }

    public void setIdEsercente(int idEsercente) {
        this.idEsercente = idEsercente;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }
}
