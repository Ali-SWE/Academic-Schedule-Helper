package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Student {
    private ArrayList<String> finishedCourses;
    private String gender;


    public Student(ArrayList<String> finishedCourses, String gender){
        this.finishedCourses = finishedCourses; this.gender =gender;
    }

    public String getGender(){
        return gender;
    }
    public ArrayList<String> getFinishedCourses(){
        return finishedCourses;
    }

    public static ArrayList<String> finishedCoursesArrayList(File finishedCoursesFile){
        ArrayList<String> finishedCourses = new ArrayList<>();
        try{
            Scanner fileScanner = new Scanner(finishedCoursesFile);
            while(fileScanner.hasNext()){
                String[] finishedCourseStrings = fileScanner.nextLine().split(",");
                if(! finishedCourseStrings[2].toUpperCase().equals("F")){
                    finishedCourses.add(finishedCourseStrings[0]);
                }
            }
            fileScanner.close();
            return finishedCourses;
        }
        catch(FileNotFoundException ex){
            System.out.println("No such file");
        }
        return finishedCourses;
    }
}
