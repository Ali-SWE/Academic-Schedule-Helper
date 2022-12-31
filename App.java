package com.example;
import java.io.*;
import java.util.*;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class App extends Application{
    File courseOffering = new File("CourseOfferingFinal.csv");
    File degreePlan = new File("DegreePlanCSV.csv");
    File finishedCoursesFile = new File("FinishedCourses.csv");

    Student student = new Student(Student.finishedCoursesArrayList(finishedCoursesFile),"M");
    Course[] CoursesInPlan = Course.planExtractor(degreePlan); // This line creates an array of the courses that are in the degree plan File
    ArrayList<String> offeredCoursesNames = Course.offeredCoursesArrayList(courseOffering); // This line creates an array of the courses that are in the course offering File
    ArrayList<Section> sections =  Section.displayedSections(courseOffering,offeredCoursesNames, CoursesInPlan, student);// Array of Section. these sections are 1- in the degree plan 2- not in the finished courses file 3- considering the prerequest and corequest.
    Section[] addedSections = new Section[0];

    private final int STAGE_WIDTH = 1200;
    private final int STAGE_HEIGHT = 800;
    private final String BUTTON_FONT = "Helvetica";
    private final double BUTTON_FONT_SIZE = 14;
    final String[] DAYS = {"U","M","T","W","R"};
    
    
    
    ArrayList<Color> colors =new ArrayList<>(Arrays.asList(Color.BISQUE,Color.BEIGE,Color.AZURE,Color.AQUAMARINE,Color.AQUA,Color.ANTIQUEWHITE,Color.ALICEBLUE));  
    ArrayList<Section> sectionsInSchedule = new ArrayList<>();

    
    

    // Primary scene components
    BorderPane root = new BorderPane(); // main Pane
    BorderPane topPane = new BorderPane(); // border pane located in the top part of the main pane
    BorderPane bottomPane = new BorderPane(); // border pane located in the bottom part of the main pane
    ScrollPane scrollPane = new ScrollPane(); // scroll pane located in the center of the main pane
    GridPane centerPane = new GridPane(); // grid bane inside the scroll pane
    Scene primaryScene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT, Color.TRANSPARENT); // recieving the main pane which contains all of the other panes

    Label basketLabel = new Label("Add Sections to The Basket");
    Button startWithASavedScheduleButton = new Button("Start With A Saved Schedule");
    ComboBox<String> comboBox = new ComboBox<>();
    Label themeLabel = new Label("Change Theme");
    HBox hBox = new HBox(themeLabel, comboBox);
    Button nextButton = new Button("Next");
    Button[] addingButtons = new Button[sections.size()];
    Button[] removingButtons = new Button[sections.size()];
    
    // Schedule Scene componens
    BorderPane schedulePane = new BorderPane(); // main pane
    BorderPane bottomPane2 = new BorderPane(); // border pane located in the bottom part of the main pane
    ScrollPane bucket = new ScrollPane(); // scroll located in the right side of the main pane
    ScrollPane scheduleScrollPane = new ScrollPane(); //To make the schedule scrollable
    GridPane schedule = new GridPane(); // GridPane to attach sections to
    ArrayList<Button> insideBucketButtons = new ArrayList<>();
    Scene scheduleScene = new Scene(schedulePane, STAGE_WIDTH, STAGE_HEIGHT, Color.TRANSPARENT);
    Button saveButton = new Button("Save Schedule");
    Button previousButton = new Button("Previous");
    Label[] days = {new Label("    Sunday    "), new Label("    Monday    "), new Label("    Tuesday   "), new Label(" Wednesday "), new Label("   Thursday   ")};
    Label[] hours = {new Label("  7 am"), new Label("  8 am"), new Label("  9 am"), new Label("  10 am"), new Label("  11 am"), new Label("  12 pm"), new Label("  1 pm"), new Label("  2 pm"), new Label("  3 pm"), new Label("  4 pm"), new Label("  5 pm")};
    Label noSectionLabel = new Label("No sections have been added");
    ImageView imageView = new ImageView(new Image(App.class.getResource("!!.jpg").toString(),30,30,false,true)); 
    ArrayList<Section> scheduleSections = new ArrayList<>(); 
    ArrayList<String> colorsList = colorsList();

    public static void main(String[] args) {
        launch();
    }

    public void start(Stage primaryStage){
        saveButton.setOnAction(e -> { //This method must access the stage of the app to make use of the FileChooser methods
            FileChooser savingStage = new FileChooser(); //A JavaFX built in Class for easy file management
            try{
                savingStage.initialFileNameProperty().set("Schedule1"); 
                File appFile = new File(System.getProperty("user.dir")); //This line is just for aesthetics
                savingStage.setInitialDirectory(appFile);
                Schedule userSchedule = new Schedule("221", Section.arrayListToArrayOfSections(sectionsInSchedule));
                File savingFile = savingStage.showSaveDialog(primaryStage); //To avoid error created from existing the FileChooser GUI.
                if(savingFile!=null){
                FileOutputStream saveFile = new FileOutputStream(savingFile);
                ObjectOutputStream objectWriter = new ObjectOutputStream(saveFile);
                objectWriter.writeObject(userSchedule);
                objectWriter.close();
                }
                
            }
            catch(Exception ex){
                System.out.println(ex.getMessage());
            }  
        });
        bottomPane2.setRight(saveButton);
        applyLayoutPrimaryScene();
        applyLayoutScheduleScene();
        applyDarkThemeStyle();
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("Course Offering");
        
    
    
        nextButton.setOnAction(e -> { //Method for producing the user's sections as button in the scrollPane of the second scene

            if(insideBucketButtons.isEmpty()){
                GridPane gridPane = new GridPane();
                gridPane.add(noSectionLabel,0,0);
                gridPane.add(imageView,1,0);
                bucket.setContent(gridPane);
            }
            else{
                GridPane Buttons = new GridPane();
                for(int i = 0; i<insideBucketButtons.size();i++){
                    Buttons.add(insideBucketButtons.get(i), 0, i);
                }
                boolean found = false;
                int position = 0;
                for(int i = 0 ; i < addedSections.length ; i++){
                    for(Section S : sectionsInSchedule){
                        if(S.equals(addedSections[i]))
                            found = true;
                            position = i;
                    }
                    if(!found)
                        insideBucketButtons.get(i).setDisable(false);
                }

                
                bucket.setContent(Buttons);
            }
            primaryStage.setScene(scheduleScene);
            
        });
            
            
            startWithASavedScheduleButton.setOnAction( e ->{ //This method must access the stage of the app to make use of the FileChooser methods
                FileChooser fileChooser = new FileChooser(); //A JavaFX built in Class for easy file management
                File chosenFile = fileChooser.showOpenDialog(primaryStage);
                if(chosenFile!=null){
                try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(chosenFile))){
                    Object obj = objectInputStream.readObject(); //showOpenDialog will show the user's File explorer and help with choosing the file probably
                    if(obj instanceof Schedule){
                        sectionsInSchedule.clear();

                        Schedule schedule = (Schedule) obj;
                        Section[] saved = schedule.getSections(); //We made two methods to avoid dealing with Object arrays and errors from casting
                        sectionsInSchedule.addAll(Section.arrayToArrayListSections(saved));
                        for(int i = 0 ; i < saved.length ; i++ ){
                            append(addedSections,saved[i]);

                        }
             
                        if(insideBucketButtons.isEmpty()){ //Repeating the proccess of the next button, which is producing the buttons of the sections
                            GridPane gridPane = new GridPane();
                            gridPane.add(noSectionLabel,0,0);
                            gridPane.add(imageView,1,0);
                            bucket.setContent(gridPane);
                        }
                        else{
                            GridPane Buttons = new GridPane();
                            for(int i = 0; i<insideBucketButtons.size();i++){
                                // insideBucketButtons.remove(i);
                                insideBucketButtons.get(i).setDisable(true);
                                Buttons.add(insideBucketButtons.get(i), 0, i);
                            }
                            
                            bucket.setContent(Buttons);
                        }
                        setToSchedule(); //adding the saved sections to the schedule
                        startWithASavedScheduleButton.setDisable(true); // The user shouldn't be able to press the buttons of the sections already in the schedule

        
                    }
                    else{
                        Alert notSavedAlert = new Alert(AlertType.ERROR);
                        notSavedAlert.setContentText("There is no schedule has been saved before! ");
                        notSavedAlert.show(); 
                        System.out.println("else");
                    }

                }
                catch(IOException ex){
                    Alert notSavedAlert = new Alert(AlertType.ERROR);
                    notSavedAlert.setContentText("There is no schedule has been saved before! ");
                    notSavedAlert.show();
                    System.out.println("io");
                }
                catch(ClassNotFoundException ex){
                    System.out.println(ex);
                }  
            }
        }
                
            );

            previousButton.setOnAction(e -> { 
                primaryStage.setScene(primaryScene);
                
        });
            
            
            primaryStage.show();
    }

    private void applyLayoutPrimaryScene(){
        // Top Layout
        topPane = new BorderPane();
        root.setTop(topPane);
        topPane.setCenter(basketLabel);
        topPane.setRight(startWithASavedScheduleButton);
        comboBox.getItems().addAll("Light Theme","Dark Theme");
        topPane.setLeft(hBox);
        // Combo Box handler
        comboBox.setOnAction(e -> {
            if(comboBox.getValue().equals("Light Theme")){
                applyLightThemeStyle();
            }
            else if(comboBox.getValue().equals("Dark Theme")){
                applyDarkThemeStyle();
            }
        });


        // Center Layout
        for(int i = 0 ; i < sections.size() ; i++){
            int currentIndex = i;
            addingButtons[i] = new Button("ADD");

            addingButtons[i].setOnAction(e -> {
                boolean alreadyThere = false;
                for( int j = 0 ; j < addedSections.length ; j++){
                    if(addedSections[j].getCourseName().equals(sections.get(currentIndex).getCourseName())){
                        alreadyThere = true; //To prevent the user from adding a section to the bucket if it was already there.
 
                        break;
                    }
                }
                if(!alreadyThere){ 
                    append(addedSections, sections.get(currentIndex));
                    removingButtons[currentIndex].setDisable(false);
                    addingButtons[currentIndex].setDisable(true);
                }
                else{
                    addingButtons[currentIndex].setDisable(true);
                    removingButtons[currentIndex].setDisable(false);
                }
                
        });

                removingButtons[i] = new Button("REMOVE");
                removingButtons[i].setOnAction(e -> {
                    remove(addedSections, sections.get(currentIndex));
                    setToSchedule();
                    removingButtons[currentIndex].setDisable(true);
                    addingButtons[currentIndex].setDisable(false);});
                    
                removingButtons[i].setDisable(true);
        }
        for(int i = 0 ; i < sections.size() ; i++){
            String sec = sections.get(i).toStringOffering();
            HBox sectionDisplay = new HBox();
            sectionDisplay.prefWidth(100);
            sectionDisplay.setPadding(new Insets(10, 8, 8,200));
            sectionDisplay.setSpacing(14);
            Label sectionInfoLabel= new Label(sec);
            sectionInfoLabel.setPrefWidth(615);
            sectionInfoLabel.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD,12));
            addingButtons[i].setAlignment(Pos.CENTER);
            removingButtons[i].setAlignment(Pos.CENTER_RIGHT);
            sectionDisplay.getChildren().addAll(sectionInfoLabel,addingButtons[i],removingButtons[i]);

            centerPane.add(sectionDisplay, 1, i,15,1);
        }
        scrollPane.setContent(centerPane);
        root.setCenter(scrollPane);
        

        // Bottom Layout
        bottomPane = new BorderPane();
        root.setBottom(bottomPane);
        bottomPane.setRight(nextButton);
    }

    private void applyLayoutScheduleScene(){
        // Grid Pane Details
        schedule.setGridLinesVisible(true);
        schedule.add(new Label("               "),0,0);
        for(int i = 0 ; i < days.length ; i++){
            schedule.add(days[i],i+1,0);
        }
        for(int j = 0 ; j < hours.length ; j++){
            schedule.add(hours[j],0,j+1);
        }
        schedule.setHgap(80);
        schedule.setVgap(50);

        bottomPane2.setLeft(previousButton);
        schedulePane.setRight(bucket);
        scheduleScrollPane.setContent(schedule);
        schedulePane.setCenter(scheduleScrollPane);
        schedulePane.setBottom(bottomPane2);
    }

    private void append(Section[] array, Section section ){
            boolean alreadyThere = false;
            for( int j = 0 ; j < addedSections.length ; j++){
                if(addedSections[j].getCourseName().equals(section.getCourseName())){
                    alreadyThere = true; //To prevent the user from adding a section to the bucket if it was already there.
                    break;
                }
            }
            if(!alreadyThere){ 
                Section[] sections = new Section[array.length + 1];
                for(int i = 0 ; i < sections.length - 1 ; i++){
                    sections[i] = array[i];
                }
                
                sections[sections.length - 1] = section;
                addedSections = sections;   
                Button bucketButton= new Button(section.toString());

                insideBucketButtons.add(bucketButton);  
                bucketButton.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
                bucketButton.setTextFill(Color.BLACK);
                bucketButton.setOnMouseEntered(e -> bucketButton.setStyle(" -fx-background-color: rgb(34, 40, 49);"));
                bucketButton.setOnMouseExited(e -> bucketButton.setStyle( " -fx-background-color: rgb(238, 238, 238);"));
                bucketButton.setOnAction(e -> {
                    String conflict = "";
                    for(Section E: sectionsInSchedule){
                        conflict = E.checkConflict(section); // This function returns empty string if there is no conflict. otherwise it returns a string containing the reason of conflict.
                        if(conflict.length() != 0){ // if there is conflict break the loop to keep storing the type of conflict in the string.
                            break;
                        }
                    }
                    if(conflict.length() == 0){
                        sectionsInSchedule.add(section);
                        setToSchedule();
                        bucketButton.setDisable(true);
                    }
                    else{
                        Alert conflictAlert = new Alert(AlertType.ERROR);
                        conflictAlert.setContentText(conflict);
                        conflictAlert.show();
                    }                  
                });
            }
                
            
    }

    private void remove(Section[] array, Section section ){
        Section[] sections = new Section[array.length -1];
        int position =0;
        
        for(int i = 0 ; i < array.length ; i++){
            if(array[i].equals(section)){
                position = i;
                for(int k=0;k<position; k++){
                    sections[k]=array[k];
                }
                for(int j = position + 1 ; j < array.length ; j++){
                    sections[j-1] = array[j];
                }
                insideBucketButtons.remove(position);
                break;
            }
        }  
              
        addedSections = sections;
        for(Section E: sectionsInSchedule){
            if(section.equals(E)){ //To remove the same section even if it has a different hashcode. (Start with a saved schedule)
                sectionsInSchedule.remove(sectionsInSchedule.indexOf(E));
                break;
            }
        }
    }

    private  void setToSchedule(){
        Node gridPaneLines = schedule.getChildren().get(0);
        schedule.getChildren().clear();
        schedule.getChildren().add(0,gridPaneLines);
        schedulePane.setCenter(null);
        

        for(int i = 0 ; i < days.length ; i++){
            schedule.add(days[i],i+1,0);
        }
        for(int j = 0 ; j < hours.length ; j++){
            schedule.add(hours[j],0,j+1);
        }
        for(int j = 0 ; j < sectionsInSchedule.size() ;j++){
            for(int i = 0; i<DAYS.length;i++){
                int position = j;
                Rectangle A = new Rectangle(20,20);
                A.setArcHeight(2);
                A.setArcWidth(2);
                BorderPane Rectangle = new BorderPane();
                Rectangle.setTop(new Label(sectionsInSchedule.get(j).getCourseName()+ "\n" + 
                                    sectionsInSchedule.get(j).getActivity()+ "\n" +
                                    sectionsInSchedule.get(j).getTime()));
                                    
                Button deleteButton = new Button(); 
                deleteButton.setOnAction(e -> {
                    int target = 0;
                    String secName = sectionsInSchedule.get(position).getCourseName();
                    for (int h = 0 ; h < insideBucketButtons.size(); h++ ){
                        if(secName.equals(addedSections[h].getCourseName())){
                            target = h;         //To activate the button that initiated the setToSchedule() Mehtod
                            break;
                        }
                    }

                    sectionsInSchedule.remove(position);

                    insideBucketButtons.get(target).setDisable(false);
                    setToSchedule();
                });
                  Rectangle.setStyle(colorsList.get(j));
                  Rectangle.setShape(A);
                  ImageView delete = new ImageView(new Image(App.class.getResource("delete.jpg").toString(),15,15,false,true));
                  deleteButton.setGraphic(delete);
            

                Rectangle.setBottom(deleteButton);
                Rectangle.setAlignment(deleteButton, Pos.BOTTOM_RIGHT);
                Rectangle.setPrefSize(20, 20);
              
                if(sectionsInSchedule.get(j).getDays().contains(DAYS[i])){
                    
                    String[] time = sectionsInSchedule.get(j).getTime().split("-");
                    int startTime = Integer.parseInt(time[0]);
                    
                    if(sectionsInSchedule.get(j).getEndTime() - sectionsInSchedule.get(j).getStartTime() <= 100 ){
                    Rectangle.setMinHeight(60);
                    Rectangle.setMaxHeight(60);
                    Rectangle.setMinWidth(90);
                    Rectangle.setMaxWidth(90);
                    schedule.add(Rectangle,i+1,startTime/100-6);}
                    else if (sectionsInSchedule.get(j).getEndTime() - sectionsInSchedule.get(j).getStartTime() <= 200 && sectionsInSchedule.get(j).getEndTime() - sectionsInSchedule.get(j).getStartTime() > 100)
                    schedule.add(Rectangle,i+1,startTime/100-6,1,2);
                    else if (sectionsInSchedule.get(j).getEndTime() - sectionsInSchedule.get(j).getStartTime() <= 300 && sectionsInSchedule.get(j).getEndTime() - sectionsInSchedule.get(j).getStartTime() > 100)
                    schedule.add(Rectangle,i+1,startTime/100-6,1,3);                  
                }
            }
        }
        schedule.setHgap(80);
        schedule.setVgap(50);
        scheduleScrollPane.setContent(schedule);
        schedulePane.setCenter(scheduleScrollPane);;

    }
    
    public static String randomColor(){
        Random Rand = new Random();
        return "-fx-background-radius: 50; -fx-background-color: rgb("+Rand.nextInt(256)+","+Rand.nextInt(256)+","+Rand.nextInt(256)+");";
    }

    public static ArrayList<String> colorsList(){ // This method creates an array list containing 20 different colors
        ArrayList<String> colors = new ArrayList<>(); 
        for(int i = 0 ; i < 20 ; i++){
            colors.add(randomColor());
        }
        return colors;
    }

    private void applyLightThemeStyle(){
        /// Primary Scene
        // Buttons and Labels style
        nextButton.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        nextButton.setTextFill(Color.BLACK);
        nextButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(204, 229, 255);");
                
        startWithASavedScheduleButton.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        startWithASavedScheduleButton.setTextFill(Color.BLACK);
        startWithASavedScheduleButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(204, 229, 255);");
                
        // when hovering on buttons, the color changes
        nextButton.setOnMouseEntered(e -> nextButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(0, 102, 51);"));
        nextButton.setOnMouseExited(e -> nextButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(204, 229, 255);"));
        startWithASavedScheduleButton.setOnMouseEntered(e -> startWithASavedScheduleButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(0, 102, 51);"));
        startWithASavedScheduleButton.setOnMouseExited(e -> startWithASavedScheduleButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(204, 229, 255);"));
        basketLabel.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        basketLabel.setTextFill(Color.BLACK);
                
        themeLabel.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        themeLabel.setTextFill(Color.BLACK);
        for(int i = 0 ; i < addingButtons.length ; i++){
            Button currentAddingButton = addingButtons[i]; // we need to assign the value to a variable so we can write it inside the lampda block because i is not defined in the lampda block.
            currentAddingButton.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
            currentAddingButton.setTextFill(Color.BLACK);
            currentAddingButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(204, 229, 255);");
            // When hovering on the button the color changes
            currentAddingButton.setOnMouseEntered(e -> currentAddingButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(0, 102, 51);"));
            currentAddingButton.setOnMouseExited(e -> currentAddingButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(204, 229, 255);"));
                        
            Button currentRemovingButton = removingButtons[i]; // we need to assign the value to a variable so we can write inside the lampda block because i is not defined in the lampda block.
            currentRemovingButton.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
            currentRemovingButton.setTextFill(Color.BLACK);
            currentRemovingButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(204, 229, 255);");
            // When hovering on the button the color changes
            currentRemovingButton.setOnMouseEntered(e -> currentRemovingButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(153, 0, 0);"));
            currentRemovingButton.setOnMouseExited(e -> currentRemovingButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(204, 229, 255);"));
        }

        scrollPane.setStyle("-fx-background: rgb(255, 255, 255)");
        root.setStyle("-fx-background-color: rgb(204, 229, 255)");

        /// Schedule Scene
        for(int i = 0 ; i < days.length ; i++){
            days[i].setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
            days[i].setTextFill(Color.BLACK);
        }
        
        for(int i = 0 ; i < hours.length ; i++){
            hours[i].setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
            hours[i].setTextFill(Color.BLACK);
        }
        
        saveButton.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        saveButton.setTextFill(Color.BLACK);
        saveButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(204, 229, 255);");
        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(0, 102, 51);"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(204, 229, 255);"));
        
        previousButton.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        previousButton.setTextFill(Color.BLACK);
        previousButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(204, 229, 255);");
        previousButton.setOnMouseEntered(e -> previousButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(0, 102, 51);"));
        previousButton.setOnMouseExited(e -> previousButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(204, 229, 255);"));
        
        noSectionLabel.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        noSectionLabel.setTextFill(Color.BLACK);

        schedulePane.setStyle("-fx-background: rgb(204, 229, 255)");
    }

    private void applyDarkThemeStyle(){
        /// Primary Scene
        // Buttons and Labels style
        nextButton.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        nextButton.setTextFill(Color.BLACK);
        nextButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(229, 184, 244);");
        
        startWithASavedScheduleButton.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        startWithASavedScheduleButton.setTextFill(Color.BLACK);
        startWithASavedScheduleButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(229, 184, 244);");
                
        // when hovering on buttons, the color changes
        nextButton.setOnMouseEntered(e -> nextButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(76, 0, 112);"));
        nextButton.setOnMouseExited(e -> nextButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(229, 184, 244);"));
        startWithASavedScheduleButton.setOnMouseEntered(e -> startWithASavedScheduleButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(76, 0, 112);"));
        startWithASavedScheduleButton.setOnMouseExited(e -> startWithASavedScheduleButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(229, 184, 244);"));
        basketLabel.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        basketLabel.setTextFill(Color.WHITE);
                
        themeLabel.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        themeLabel.setTextFill(Color.WHITE);
        for(int i = 0 ; i < addingButtons.length ; i++){
            Button currentAddingButton = addingButtons[i]; // we need to assign the value to a variable so we can write it inside the lampda block because i is not defined in the lampda block.
            currentAddingButton.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
            currentAddingButton.setTextFill(Color.BLACK);
            currentAddingButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(229, 184, 244);");
            // When hovering on the button the color changes
            currentAddingButton.setOnMouseEntered(e -> currentAddingButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(76, 0, 112);"));
            currentAddingButton.setOnMouseExited(e -> currentAddingButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(229, 184, 244);"));
                        
            Button currentRemovingButton = removingButtons[i]; // we need to assign the value to a variable so we can write inside the lampda block because i is not defined in the lampda block.
            currentRemovingButton.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
            currentRemovingButton.setTextFill(Color.BLACK);
            currentRemovingButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(229, 184, 244);");
            // When hovering on the button the color changes
            currentRemovingButton.setOnMouseEntered(e -> currentRemovingButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(76, 0, 112);"));
            currentRemovingButton.setOnMouseExited(e -> currentRemovingButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(229, 184, 244);"));
        }

        noSectionLabel.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        noSectionLabel.setTextFill(Color.WHITE);

        scrollPane.setStyle("-fx-background: rgb(25, 25, 25)"); //middle
        root.setStyle("-fx-background-color: rgb(64, 66, 88)"); // top

        /// Schedule Scene
        for(int i = 0 ; i < days.length ; i++){
            days[i].setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
            days[i].setTextFill(Color.WHITE);
        }
        
        for(int i = 0 ; i < hours.length ; i++){
            hours[i].setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
            hours[i].setTextFill(Color.WHITE);
        }
        
        saveButton.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        saveButton.setTextFill(Color.BLACK);
        saveButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(229, 184, 244);");
        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(76, 0, 112);"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(229, 184, 244);"));
        
        previousButton.setFont(Font.font(BUTTON_FONT, FontWeight.BOLD, BUTTON_FONT_SIZE));
        previousButton.setTextFill(Color.BLACK);
        previousButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(229, 184, 244);");
        previousButton.setOnMouseEntered(e -> previousButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(76, 0, 112);"));
        previousButton.setOnMouseExited(e -> previousButton.setStyle("-fx-background-radius: 50; -fx-background-color: rgb(229, 184, 244);"));
        
        schedulePane.setStyle("-fx-background: rgb(57, 62, 70)");
    } 
}