package org.example;

public class User {

    private Data data = new Data();
    public String userName;
    public int id;
    public double balance;
    public double sumBalance;

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getSumBalance() {
        return sumBalance;
    }
}
