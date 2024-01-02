package io.github.iyado.accounterx;

public class Prog {
    private String notice;
    private String customer;
    private String sender;
    private String reciever;
    private String sprofit;
    private String rprofit;
    private String ex;
    private String count;
    private String sumAll;
    private String key;
    private String date;
    @SuppressWarnings("unused")
    private Prog(){

    }
    public Prog(String sender,String reciever,String count,
                String ex, String sprofit,String rprofit,
                String customer,String notice,String sumAll,
                String key,String date) {
        this.date = date;
        this.key = key;
        this.count = count;
        this.ex = ex;
        this.notice = notice;
        this.customer = customer;
        this.reciever = reciever;
        this.sender = sender;
        this.rprofit = rprofit;
        this.sprofit = sprofit;
        this.sumAll = sumAll;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getName() {
        return notice;
    }

    public String getCount() {
        return count;
    }

    public String getEx() {
        return ex;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getSprofit() {
        return sprofit;
    }

    public void setSprofit(String sprofit) {
        this.sprofit = sprofit;
    }

    public String getRprofit() {
        return rprofit;
    }

    public void setRprofit(String rprofit) {
        this.rprofit = rprofit;
    }

    public String getSumAll() {
        return sumAll;
    }

    public void setSumAll(String sumAll) {
        this.sumAll = sumAll;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
