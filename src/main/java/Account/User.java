package Account;

import model.Person;

public class User {
    private static Person account=null;
    public static boolean check=false;
    public static Person getAcount(){
        return account;
    }
    public static void setAccount(Person user){
        account=user;
        if(account==null){
            check=false;
        }
    }
}
