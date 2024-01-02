package io.github.iyado.accounterx;

public class Currency {
    private String dolar;
    private String euro;
    private String tl;
    private String sy;
    private String real;
    private String dinar;
    @SuppressWarnings("unused")
    private Currency(){

    }

    public Currency(String dolar,String euro,String sy,String tl,String real,String dinar){
        this.dinar = dinar;
        this.sy = sy;
        this.euro = euro;
        this.real = real;
        this.tl = tl;
        this.dolar = dolar;
    }

    public String getDolar() {
        return dolar;
    }

    public void setDolar(String dolar) {
        this.dolar = dolar;
    }

    public String getEuro() {
        return euro;
    }

    public void setEuro(String euro) {
        this.euro = euro;
    }

    public String getTl() {
        return tl;
    }

    public void setTl(String tl) {
        this.tl = tl;
    }

    public String getSy() {
        return sy;
    }

    public void setSy(String sy) {
        this.sy = sy;
    }

    public String getReal() {
        return real;
    }

    public void setReal(String real) {
        this.real = real;
    }

    public String getDinar() {
        return dinar;
    }

    public void setDinar(String dinar) {
        this.dinar = dinar;
    }
}
