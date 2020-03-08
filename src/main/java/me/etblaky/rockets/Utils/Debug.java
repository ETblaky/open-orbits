package me.etblaky.rockets.Utils;

public class Debug {

    public static boolean debug = true;

    public static void log(String name, double a){
        System.out.println(name + ": " + String.format("%f", a));
    }
    public static void log(String name, String a){
        System.out.println(name + ": " + a);
    }

    public static void space(){
        System.out.println();
    }

}
