package org.example;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Collections;
import java.util.Optional;


public class App {
    public static User user = new User();
    public static Data data = new Data();
    public static void main( String[] args )
    {

        Javalin javalin = Javalin.create().start(3022);
        javalin.get("/", ctx ->{
            System.out.println(ctx.status());
            ctx.render("login.jte");
        });
        javalin.get("/changelog", ctx ->{
            System.out.println(ctx.status());
            ctx.render("changelog.jte");
        });
        javalin.get("/registration", ctx ->{
            System.out.println(ctx.status());
            ctx.render("regist.jte");
        });
        javalin.get("/login", ctx ->{
            System.out.println(ctx.status());
            ctx.render("login.jte");
        });
        javalin.get("/balance/:user_id", ctx ->{
            String userId = ctx.pathParam("user_id");
            try{
                Optional<Double> balance = data.getBalanceId(userId);
                if (balance.isPresent()){
                    ctx.json(balance.get());
                    ctx.status(200);
                }else {
                    ctx.json("Not Found");
                    ctx.status(404);
                }
            }catch (Exception e){
                ctx.status(400);
                ctx.json("Unexpected error" + e.getMessage());
            }
        });
        javalin.post("/api/auth/", ctx ->{
            System.out.println(ctx.formParam("login"));
            System.out.println(ctx.formParam("pass"));
            String login = ctx.formParam("login");
            String pass = ctx.formParam("pass");
            data.connect(login);
            if (data.auth(login, pass)){
                System.out.println("GOOD");
                ctx.cookie("login", login);
                ctx.redirect("/home");
            }else {
                System.out.println("BAD");
                ctx.redirect("/incorrectpass");
            }
        });
        javalin.get("/topupbalance", ctx ->{
            System.out.println(ctx.status());
            ctx.render("topupbalance.jte");
        });
        javalin.get("/sum", ctx ->{
            System.out.println(ctx.status());
            ctx.render("sum.jte");
        });
        javalin.get("/fullsum", App::renderFullSumPage);
        javalin.post("/api/fullsum/", ctx -> {
            String log1 = ctx.formParam("log1");
            System.out.println("1-st log: " + log1);
            String log2 = ctx.formParam("log2");
            System.out.println("2-nd log: " + log2);
            System.out.println("Balance 1 log: " + data.getBalanceName(log1));
            System.out.println("Balance 2 log: " + data.getBalanceName(log2));
            user.sumBalance = data.sum(log1, log2);
            System.out.println("Sum: " + user.sumBalance);
            ctx.redirect("/fullsum");
        });
        javalin.get("/mybills", App::renderBillsPage);
        javalin.get("/myinfo", App::renderInfoPage);
        javalin.get("/settings", App::renderSettingsPage);
        javalin.get("/incorrectpass", ctx ->{
            ctx.render("incorrect_password.jte");
        });
        javalin.post("/api/transfer/", ctx->{
           String id = ctx.formParam("id");
           String balanace = ctx.formParam("amount");
           if(data.getId("login") != Integer.valueOf(id) &&
                   data.getBalanceName(ctx.cookie("login")) >= Double.valueOf(balanace)){
               data.transfer(data.getId(ctx.cookie("login"))
                       ,Integer.valueOf(id),
                       data.getBalanceName(ctx.cookie("login")),
                       Double.valueOf(balanace));
               ctx.redirect("/home");
           }else {
               ctx.redirect("/home");
               System.out.println("Balance > your balance");
           }
        });
        javalin.post("/api/changelog/", ctx->{
            String cLogin = ctx.formParam("cLogin");
            String fLogin = ctx.formParam("fLogin");
            if(!cLogin.equals(fLogin)){
                data.changelogin(cLogin, fLogin);
                ctx.redirect("/login");
            }
            System.out.println(cLogin);
            System.out.println(fLogin);
        });
        javalin.post("/api/topupbalance/", ctx->{
            String login = ctx.formParam("cLogin");
            double balance = Double.valueOf(ctx.formParam("balance"));
            System.out.println(login);
            System.out.println(balance);
            data.setbalance(balance, login);
            ctx.redirect("/login");
        });
        javalin.get("/home", App::renderMainPage);
        javalin.post("/api/register/", ctx ->{
            System.out.println(ctx.formParam("login"));
            System.out.println(ctx.formParam("pass"));
            System.out.println(ctx.formParam("repass"));
            String login = ctx.formParam("login");
            String pass = ctx.formParam("pass");
            String repass = ctx.formParam("repass");
            data.connect(login);
            if (data.register(login, pass, repass) == true){
                System.out.println("GOOD");
                ctx.cookie("login", login);
                ctx.redirect("/home");
            }else {
                System.out.println("BAD");
            }
        });
    }
    public static void renderMainPage(Context ctx){
        user.userName = data.getUserName(ctx.cookie("login"));
        user.id = data.getId(ctx.cookie("login"));
        user.balance = data.getBalanceName(ctx.cookie("login"));
        ctx.render("home.jte", Collections.singletonMap("user", user));
    }
    public static void renderBillsPage(Context ctx){
        user.balance = data.getBalanceName(ctx.cookie("login"));
        ctx.render("mybills.jte", Collections.singletonMap("user", user));
    }
    public static void renderFullSumPage(Context ctx){
        ctx.render("fullsum.jte", Collections.singletonMap("user", user));
    }
    public static void renderInfoPage(Context ctx){
        user.userName = data.getUserName(ctx.cookie("login"));
        user.id = data.getId(ctx.cookie("login"));
        ctx.render("myinfo.jte", Collections.singletonMap("user", user));
    }
    public static void renderSettingsPage(Context ctx){
        user.userName = data.getUserName(ctx.cookie("login"));
        user.id = data.getId(ctx.cookie("login"));
        user.balance = data.getBalanceName(ctx.cookie("login"));
        ctx.render("settings.jte", Collections.singletonMap("user", user));
    }
}
