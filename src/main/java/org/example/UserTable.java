package org.example;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users", schema = "", catalog = "")
public class UserTable {
    private int id;
    private String login;
    private String pass;
    private double balance;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true, length = 255)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "login", nullable = false, insertable = true, updatable = true, length = 255)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "pass", nullable = false, insertable = true, updatable = true, length = 255)
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Column(name = "balance", nullable = false, insertable = true, updatable = true)
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
