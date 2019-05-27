package com.speciale.zireael.Model;

public class Event {

    private String eventID,className, classDay, classStime, classEtime;

    public Event() {
    }

    public Event(String eventID, String className, String classDay, String classStime, String classEtime) {
        this.className = className;
        this.classDay = classDay;
        this.classStime = classStime;
        this.classEtime = classEtime;
        this.eventID = eventID;
    }
    public String getEventID(){
        return eventID;
    }

    public void setEventID(String eventID){
        this.eventID = eventID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDay() {
        return classDay;
    }

    public void setClassDay(String classDay) {
        this.classDay = classDay;
    }

    public String getClassStime() {
        return classStime;
    }

    public void setClassStime(String classStime) {
        this.classStime = classStime;
    }

    public String getClassEtime() {
        return classEtime;
    }

    public void setClassEtime(String classEtime) {
        this.classEtime = classEtime;
    }
}
