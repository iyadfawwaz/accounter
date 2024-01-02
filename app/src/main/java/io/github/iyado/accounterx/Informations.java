package io.github.iyado.accounterx;



public class Informations {

    private String uid;
    private Currency account;
    private String key;
    private String username;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public Currency getAccount() {
        return account;
    }

    public void setAccount(Currency account) {
        this.account = account;
    }

    public Informations(String uid, Currency account) {
        this.uid = uid;
        this.account = account;
    }
    public Informations(){

    }
}
