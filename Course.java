package com.example;
import java.io.*;
import java.util.*;



public class Course {
    private String Name;
    private int credits;
    private String PreReq;
    private String CoReq;

    public Course(String Name,int credits,String PreReq,String CoReq){
        this.Name = Name;this.credits= credits; this.PreReq =PreReq; this.CoReq= CoReq;
    }

    public String toString(){
        return Name+" "+credits;
    }

    public String getName(){
        return Name;
    }

    public String getPreReq(){
        return PreReq;
    }

    public String getCoReq(){
        return CoReq;
    }

    public static Course[] planExtractor(File degreePlanFile){

        // counting the courses in the degree plan file
        int courses = -1; // We start with -1 to ignore the first line
        try{
            Scanner count = new Scanner(degreePlanFile);
            while(count.hasNextLine()){
                count.nextLine();
                ++courses;
        }


        Course[] degreePlanCourses = new Course[courses];
        Scanner planScanner = new Scanner(degreePlanFile);
        planScanner.nextLine();
        for(int i = 0 ; i < courses ; ++i){
            String[] s = planScanner.nextLine().split(",");
            String name = s[0]; int credits = Integer.parseInt(s[1]); String Pre = s[2]; String Coreq = s[3];
            degreePlanCourses[i] =new Course(name, credits, Pre, Coreq);
        }

        count.close();
        planScanner.close();
        return degreePlanCourses.clone();

        }
        

        catch(FileNotFoundException ex){
            System.out.println(ex);
        }
        catch(InputMismatchException ex)
        {System.out.println(ex);}
        catch(NumberFormatException ex){System.out.println(ex);}
        return new Course[1];

    }


    public static ArrayList<String> offeredCoursesArrayList(File courseOffering){
        try{
        ArrayList<String> offeredCourses = new ArrayList<>();
        Scanner offeringScanner = new Scanner(courseOffering);
        offeringScanner.nextLine();
        while(offeringScanner.hasNext()){
            String[] s = offeringScanner.nextLine().split(",");
            offeredCourses.add(s[0]); 
        }
        offeringScanner.close();
        return offeredCourses;
        }
           catch(FileNotFoundException ex){
            System.out.println(ex);
        }
        catch(InputMismatchException ex)
        {System.out.println(ex);}
        catch(NumberFormatException ex){System.out.println(ex);}
        return new ArrayList<>();}



}

