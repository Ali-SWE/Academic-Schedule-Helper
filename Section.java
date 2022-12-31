package com.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Section implements Serializable{
    private String courseName;
    private String crn;
    private String sectionNumber;
    private String activity;
    private String time;
    private String days;
    private String location;
    

    public Section(String courseName,String sectionNumber,String crn,String Activity, String time, String days,String location){
        this.courseName = courseName;this.sectionNumber= sectionNumber;this.crn=crn; this.activity = Activity;
        this.time = time; this.days= days; this.location = location;
    }

    public String toString(){
        return courseName + "\n" + activity + "\n" + days + "\n" + time + "      " + location;
    }
    
    public String toStringOffering(){
        return courseName+"      "+activity+"        "+ days+"            "+time+"             "+location+" ";
        
    }
    
    public String getCourseName(){
        return courseName;
    }

    public String getSectionNumber(){
        return sectionNumber;
    }

    public String getActivity(){
        return activity;
    }

    public String getTime(){
        return time;
    }
    public int getStartTime(){
        String[] time = this.time.split("-");
        return Integer.parseInt(time[0]); 
    }
    public int getEndTime(){
        String[] time = this.time.split("-");
        return Integer.parseInt(time[1]);
    }

    public String getDays(){
        return days;
    }
    
    public boolean equalsTo(Section section){
        return courseName.equals(section.courseName) && crn.equals(section.crn) && activity.equals(section.activity);
    }

    public String checkConflict(Section section2){
        if(courseConflict(section2)){
            if(activityConflict(section2)){
                return "Same Course Conflict !";
            }
            else{
                if(dayConflict(section2)){
                    if(timeConflict(section2)){
                        return "Time Conflict !";
                    }
                    else{
                        return "";
                    }
                }
                else{
                    return "";
                }
            }
        }
        else{
            if(dayConflict(section2)){
                if(timeConflict(section2)){
                    return "Time Conflict !";
                }
                else{
                    return "";
                }
            }
            else{
                return "";
            } 
        }         
    }

    public boolean dayConflict(Section section2){
        String[] daysArray1 = new String[days.length()];
        for(int  i = 0 ; i < days.length() ; i++){
            daysArray1[i] =  days.substring(i,i+1);
        }
        String[] daysArray2 = new String[section2.days.length()];
        for(int  i = 0 ; i < section2.days.length() ; i++){
            daysArray2[i] =  section2.days.substring(i,i+1);
        }

        for(String day1 : daysArray1){
            for(String day2 : daysArray2){
                if(day1.equals(day2))
                    return true;
            }
        }
        return false;
    }

    public boolean timeConflict(Section section2){
        String[] timeArray1 = time.split("-");
        int startTime1 = Integer.parseInt(timeArray1[0]);
        int endTime1 = Integer.parseInt(timeArray1[1]);

        String[] timeArray2 = section2.time.split("-");
        int startTime2 = Integer.parseInt(timeArray2[0]);
        int endTime2 = Integer.parseInt(timeArray2[1]);
        if( ( ( startTime2 <= startTime1 ) && ( startTime1 <= endTime2 ) ) | ( ( startTime2 <= endTime1 ) && ( endTime1 <= endTime2 ) ) | ( ( startTime1 <= startTime2 ) && ( startTime2 <= endTime1) ) | ( (startTime1 <= endTime2 ) && ( endTime2 <= endTime1) ) ){
            return true;
        }
        return false;
    }

    public boolean courseConflict(Section section2){
        if(courseName.split("-")[0].equals(section2.courseName.split("-")[0]))
            return true;
        return false;

    }

    public boolean activityConflict(Section section2){
        if(activity.equals(section2.activity))
            return true;
        return false;
    }

    public static ArrayList<Section> arrayToArrayListSections(Section[] sections){
        ArrayList<Section> returnNew = new ArrayList<>();
        for(int i=0;i<sections.length;i++){
            returnNew.add(sections[i]);
        }
        return returnNew;

    }

    public static Section[] arrayListToArrayOfSections(ArrayList<Section> arrayList){
        Section[] returned = new Section[arrayList.size()];
        int position =0;
        for(Section E: arrayList){
            returned[position]= E;
            position++;
        }
        return returned;
    }

    public static ArrayList<Section> displayedSections(File courseOfferingFile ,ArrayList<String> CourseOfferingArray, Course[] DegreePlan, Student student){
        
        //Creating a string of the finished courses
        String finishedCoursesString ="";
        ArrayList<String> finishedCourses = student.getFinishedCourses();
        for(int i = 0 ; i < finishedCourses.size() ; i++){
            finishedCoursesString += finishedCourses.get(i);
        }
        
        // adding the courses that satisfy the prerequisite into an array
        ArrayList<String> courses = new ArrayList<>();
            for(int j =0; j<DegreePlan.length;++j){
            String[] preReq =  DegreePlan[j].getPreReq().split(";");
            int x = 0;
            for(int k = 0 ; k < preReq.length;k++){
                if( finishedCoursesString.contains(preReq[k]) |preReq[k].equals("None")){
                    x++;
                }
           }
                if(x == preReq.length && !finishedCoursesString.contains(DegreePlan[j].getName()) ){
                courses.add(DegreePlan[j].getName());
            }
        }

        // adding the courses that satisfy the corequisite into an array
        ArrayList<String> finalCourses = new ArrayList<>();
        for(int i = 0 ; i < courses.size() ; i++){
            for(int j = 0 ; j < DegreePlan.length ; j++){
                // Hussain made changes here 1- Adding the for loop h 2- the or condition in if statment
                for(int h = 0 ; h < finishedCourses.size(); h ++){
                if(courses.get(i).equals(DegreePlan[j].getName()) ){ 
                    if(DegreePlan[j].getCoReq().equals("None")){
                        finalCourses.add(courses.get(i));
                    }}
                    else{
                        for(int k = 0 ; k < courses.size() ; k++){
                            if(DegreePlan[j].getCoReq().equals(courses.get(k))){
                                finalCourses.add(courses.get(i));
                                


                            }
                        }
                    }
                }
            }
        }
    
        // Filter the sections subject by gender
        boolean gender = true;
        ArrayList<String> approvedSecName  = new ArrayList<>();
        if(student.getGender().toUpperCase().equals("F"))
        gender =false;

        for( int i = 0 ; i < finalCourses.size() ; i++){
            for(int j = 0 ; j<CourseOfferingArray.size(); j++){
                if(CourseOfferingArray.get(j).contains(finalCourses.get(i))){
                    if(gender){
                        if (!CourseOfferingArray.get(j).contains("-F")){
                            approvedSecName.add(CourseOfferingArray.get(j));
                        }
                    }
                    else{
                        if (CourseOfferingArray.get(j).contains("-F")){
                            approvedSecName.add(CourseOfferingArray.get(j)) ;
                        }
                    }
                } 
                    
                
            }
            
        }

    // Adding sections:
    
    int count = 0;
    ArrayList<Section> finalSections = new ArrayList<>();
    try{
        Scanner sectionScanner = new Scanner(courseOfferingFile);
        sectionScanner.nextLine();
        count = 0;
        while(sectionScanner.hasNextLine()){
            sectionScanner.nextLine();
            count++;
        }
        sectionScanner.close();
        Scanner sectionScanner2 = new Scanner(courseOfferingFile);
        sectionScanner2.nextLine();
        Section[] allSections = new Section[count];
        for(int i = 0 ; i < allSections.length ; i++){
            String[] info = sectionScanner2.nextLine().split(",");
            String [] nameSection = info[0].split("-");
            allSections[i] = new Section(info[0], nameSection[1],info[2], info[1], info[6], info[5], info[7]);
        }

        for(int i = 0; i < allSections.length ; i++ ){
            for(int j = 0 ; j < approvedSecName.size() ; j++){
                if(approvedSecName.get(j).contains(allSections[i].getCourseName())){
                    finalSections.add(allSections[i]) ;
                    break;
                }
            }
        }
        sectionScanner2.close();
        

        return finalSections;

    }
    catch(FileNotFoundException ex){
        System.out.println(ex);
    }
    return finalSections;

    
    }


}
