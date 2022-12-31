package com.example;

import java.io.Serializable;
import java.util.ArrayList;

public class Schedule implements Serializable {
    private String Term;
    private Section[] sections;
    


    public Schedule(String Term,Section[] sections){
        this.Term = Term;
        this.sections =sections;
    }

    public Section[] getSections(){
        return sections;
    } 
}
