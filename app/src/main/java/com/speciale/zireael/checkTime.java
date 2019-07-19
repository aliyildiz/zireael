package com.speciale.zireael;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class checkTime {

    String anlikSaat, today, gunEn;
    Date gun1;
    Integer day;

    public String checkEvent(){

        SimpleDateFormat bicim2 = new SimpleDateFormat("E");
        gun1 = new Date();
        today = bicim2.format(gun1);
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);

        if(day==1)
            gunEn="Sunday";
        else if(day==2)
            gunEn="Monday";
        else if(day==3)
            gunEn="Tuesday";
        else if(day==4)
            gunEn="Wednesday";
        else if(day==5)
            gunEn="Thursday";
        else if(day==6)
            gunEn="Friday";
        else if(day==7)
            gunEn="Saturday";
        return gunEn;
    }

    public boolean checkTime(String start, String end){

        SimpleDateFormat bicim =  new SimpleDateFormat("HHmm");
        Date saat = new Date();
        anlikSaat = bicim.format(saat);

        int s = Integer.parseInt(start);
        int e = Integer.parseInt(end);
        int anlik = Integer.parseInt(anlikSaat);

        if(s<anlik && e>anlik){

            return true;
        }
        return false;
    }


}
