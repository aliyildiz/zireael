package com.speciale.zireael.Model;

public class Event {

    private String eventID,className, classDay, classStime, classEtime;

    public Event() {
    }

    public Event(String eventID, String className, String classDay, String classStime,
                 String classEtime) {
        this.className = className;
        this.classDay = classDay;
        this.classStime = classStime;
        this.classEtime = classEtime;
        this.eventID = eventID;
    }
    public String getEventID(){
        return eventID;
    }

    public String getClassName() {
        return className;
    }

    public String getClassDay() {
        return classDay;
    }

    public String getClassStime() {
        return classStime;
    }

    public String getClassEtime() {
        return classEtime;
    }

}
