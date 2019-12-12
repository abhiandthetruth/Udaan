package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.PatternSyntaxException;

class Passenger{
    private String name,date,phno,flightId;
    private int age,seatNo;

    private boolean isConfirmed;
    Passenger(String name, int age, String phno, String date,String flightId, int seatNo){
        this.name = name;
        this.age = age;
        this.phno = phno;
        this.date = date;
        this.seatNo = seatNo;
        this.flightId =  flightId;
        this.isConfirmed=false;
    }

    public String getName() {
        return name;
    }

    public String getPhno() {
        return phno;
    }

    public String getFlightId() {
        return flightId;
    }

    public int getAge() {
        return age;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public String getDate() {
        return date;
    }
    public boolean cancel(){
        this.isConfirmed=false;
        return true;
    }
    protected String getname() {
        return this.name;
    }

    protected boolean confirm() {
        this.isConfirmed=true;
        return isConfirmed;
    }
    protected boolean isConfirmed(){
        return isConfirmed;
    }
}

class Seat{
    private String seatId,flightId;
    private ArrayList<Passenger> passengers;
    private int seatNo;
    private double price;
    Seat(int seatNo , double price, String flightId){
        this.seatNo=seatNo;
        this.price=price;
        this.flightId=flightId;
        this.seatId=flightId+seatNo;
        passengers = new ArrayList<Passenger>();
    }

    public String getSeatId() {
        return seatId;
    }
    public boolean cancel(){
        this.passengers = new ArrayList<Passenger>();
        return true;
    }
    public String getFlightId() {
        return flightId;
    }

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public double getPrice() {
        return price;
    }

    synchronized protected boolean confirm(Passenger passenger){
        if(!isAvailable(passenger.getDate())) return false;
        this.passengers.add(passenger);
        return true;
    }

    protected boolean isAvailable(String date)
    {
        for(Passenger p: passengers) if(p.getDate().compareTo(date)==0 && p.isConfirmed()) return false;
        return true;
    }

}




class User{
    private String userId, name , email, phno, aadharno,password,token;
    private boolean isVerified;
    private ArrayList<Ticket> tickets;
    private int potp,lotp;
    User(String userId, String name, String email, String phno, String password){
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phno = phno;
        this.password = password;
        this.isVerified = false;
        tickets = new ArrayList<Ticket>();
    }
    protected ArrayList<Ticket> getTickets(){
        return tickets;
    }
    protected void generateOtp(){
        this.lotp = (int)(Math.random()*(9999-1000+1));
        System.out.println(this.lotp);
    }

    protected boolean checkLogin(int otp) {
        if(this.lotp==otp) {
            this.isVerified=true;
            return true;
        }
        return false;
    }

    protected boolean cancelTicket(String token, Ticket t){
        String date = t.getDate();
        System.out.println("hi"+date);
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
        Date dt1= null;
        try {
            dt1 = format1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(dt1.compareTo(new Date())<=0) {
            System.out.println("Too Late");
            return false;
        }
        boolean re = t.cancel();
        tickets.remove(t);
        return re;
    }

    private boolean verifyUser(String token){
        if(token==null) return false;
        return this.token.compareTo(token)==0;
    }

    protected boolean changePassword(String pass, int otp){
        if(!checkLogin(otp)) return false;
        this.password=pass;
        return true;
    }

    protected String login(String password){
        if(this.password.compareTo(password)==0 && this.isVerified) {
            this.token = "login";
            return "login";
        }
        return null;
    }

    protected boolean logout(){
        token = null;
        return true;
    }

    protected String addTickets(String token, Ticket t){
        if(!verifyUser(token)) return null;
        this.tickets.add(t);
        this.potp = (int)(Math.random()*(9999-1000+1));
        System.out.println(this.potp);
        return t.getTicketId();
    }

    protected String verifyPayment(String token, String ticketId, int otp){
        if(!verifyUser(token)) return null;
        int i = -1;
        if(this.potp!= otp) return null;
        for(i =  tickets.size() -1; i >=0 ; i--) if(tickets.get(i).getTicketId().compareTo(ticketId)==0) break;
        if(i < 0) return null;
        Ticket t = tickets.get(i);
        String txnId = userId + token + t.getFlightId() + t.getDate() + otp;
        if(tickets.get(i).confirm(txnId)) return txnId;
        return null;
    }

    protected String getUserId() {
        return this.userId;
    }
}

class Userdb{
    private ArrayList<User> userList;
    private ArrayList<Integer> userOtp;
    private ArrayList<User> userListTemp;
    Userdb(){
        userList = new ArrayList<User>();
        userOtp = new ArrayList<Integer>();
        userListTemp = new ArrayList<User>();
    }
    protected int addUser(String uid,User u){
        for(int i = userList.size() -1; i  >=0 ; i--) if(userList.get(i).getUserId().compareTo(uid)==0) return -1;
        userListTemp.add(u);
        u.generateOtp();
        userList.add(u);
        return 1;
    }

    protected boolean changePassword(String uid){
        User u = null;
        for(int i = userList.size() -1; i  >=0 ; i--) if(userList.get(i).getUserId().compareTo(uid)==0) {u = userList.get(i); break;}
        if (u==null) return false;
        u.generateOtp();
        return true;
    }

    protected boolean changePassword(String uid, String password, int otp){
        User u = null;
        for(int i = userList.size() -1; i  >=0 ; i--) if(userList.get(i).getUserId().compareTo(uid)==0) {u = userList.get(i); break;}
        if (u==null) return false;
        return u.changePassword(password,otp);

    }

    protected int addUser(String uid, int otp){
        int i = -1;
        for(i = userListTemp.size() -1; i  >=0 ; i--) if(userListTemp.get(i).getUserId().compareTo(uid)==0) break;
        if(i<0) return -1;
        boolean t = userList.get(i).checkLogin(otp);
        if(t) {
            userListTemp.remove(i);
            return 1;
        }
        return -1;
    }
    protected User getUser(String uid, String password, StringBuilder token){
        int i=-1;
        for(i = userList.size() -1; i  >=0 ; i--) if(userList.get(i).getUserId().compareTo(uid)==0) break;
        if(i<0) return null;
        String tok = userList.get(i).login(password);
        System.out.println(tok);
        if(tok ==null) return null;
        token.append(tok);
        return userList.get(i);
    }
}

class Flightdb{
    private ArrayList<Flight> flightList;
    Flightdb(){
        flightList = new ArrayList<Flight>();
    }
    protected int addFlight(Flight f){
        if(Exists(f.getFlightId())) return -1;
        flightList.add(f);
        return 1;
    }
    protected ArrayList<Flight> getFlights(String source, String dest, String date){
        ArrayList<Flight> fd = new ArrayList<Flight>();

        for(Flight f: flightList) if(f.runs(source,dest,date)) fd.add(f);
        return fd;
    }
    private boolean Exists(String flightId) {
        for(Flight f: flightList) if(f.getFlightId().compareTo(flightId)==0) return true;
        return false;
    }

    public void printflight() {
        for(Flight f: flightList) f.printd();
    }

    public Flight getFlight(String flightId){
        for(Flight f: flightList) if(f.getFlightId().compareTo(flightId)==0) return f;
        return null;
    }
}



public class Main extends Application implements Initializable {

    @FXML
    TableView<Flight> tablesearch;

    @FXML
    TableView<Ticket> tablebooks;

    @FXML
    TableColumn<Ticket,String> ticketid,flightidb,dateb,txnid,status;

    @FXML
    TableColumn<Ticket,Double> priceb;

    @FXML
    TableColumn<Flight,String> flightid,sourcec,destc,starttime,provider;

    @FXML
    TableColumn<Flight,Double> duration,price;

    static Scene mainScene;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        mainScene = new Scene(root, 1400, 780);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        Stage stu = new Stage();
        GridPane pane  = new GridPane();
        TextField tf1=new TextField();
        TextField tf2=new TextField();
        TextField tf3=new TextField();
        DatePicker dt = new DatePicker();
        TextField tf4=new TextField();
        TextField tf5=new TextField();
        TextField tf6=new TextField();
        TextField tf7=new TextField();
        TextField tf8=new TextField();
        Button b1 = new Button("Add Flight");
        b1.setMaxSize(200,20);
        tf1.setPromptText("Enter FlightId");
        tf2.setPromptText("Enter Source");
        tf3.setPromptText("Enter Destination");
        tf4.setPromptText("Enter Provider");
        dt.setPromptText("Enter Validity");
        tf5.setPromptText("Enter StartTime(24 Hour)");
        tf6.setPromptText("Enter Duration in hours");
        tf7.setPromptText("Enter Price/Seat");
        tf8.setPromptText("no of seat");
        PasswordField tf9 = new PasswordField();
        BackgroundFill background_fill = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(background_fill);
        pane.setBackground(background);
        tf9.setPromptText("Enter SuperKey");
        TextField tf10 = new TextField();
        tf10.setPromptText("Enter Space Separated Days");
        pane.addRow(0,tf1);
        pane.addRow(1,tf2);
        pane.addRow(2,tf3);
        pane.addRow(3,tf4);
        pane.addRow(4,tf5);
        pane.addRow(5,dt);
        pane.addRow(6,tf6);
        pane.addRow(7,tf7);
        pane.addRow(8,tf8);
        pane.addRow(10,tf9);
        pane.addRow(9,tf10);
        pane.addRow(11,b1);
        stu.setScene(new Scene(pane,400,500));
        pane.setAlignment(Pos.CENTER);
        pane.setVgap(10);
        stu.show();

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("add->");
                if(!tf5.getText().contains(":"))
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Starting time is incorrect");
                    alert.showAndWait();
                    return;
                }
                else
                {
                    String s[] = tf5.getText().split(":");
                    try{
                        int i = Integer.parseInt(s[0]);
                        int j=  Integer.parseInt((s[1]));
                        if(i>23 || j>59)
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Starting time is incorrect");
                            alert.showAndWait();
                            return;
                        }
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Starting time is incorrect");
                        alert.showAndWait();
                        return;
                    }
                }
                try {
                    String days[] = tf10.getText().split(" ");
                    if (tf9.getText().compareTo("admin") == 0) {
                        System.out.println(fdb.addFlight(new Flight(tf1.getText(), tf2.getText(), tf3.getText(), tf4.getText(), dt.getValue().toString(), tf5.getText(), Double.parseDouble(tf6.getText()), Double.parseDouble(tf7.getText()), Integer.parseInt(tf8.getText()), days)));
                        for(int i=0;i<days.length;i++)
                        {
                            if((days[i].compareToIgnoreCase("Monday")!=0 && days[i].compareToIgnoreCase("Tuesday")!=0 && days[i].compareToIgnoreCase("Wednesday")!=0 && days[i].compareToIgnoreCase("Thursday")!=0 && days[i].compareToIgnoreCase("Friday")!=0 && days[i].compareToIgnoreCase("Saturday")!=0 && days[i].compareToIgnoreCase("Sunday")!=0))
                            {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Day is incorrect");
                                alert.showAndWait();
                                return;
                            }
                        }
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText("Flight is added");
                        alert.showAndWait();
                    }
                    else
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Admin password is incorrect");
                        alert.showAndWait();
                    }
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Incorrect Input");
                    alert.showAndWait();
                }

            }
        });
    }

    static  ArrayList<User> activeUsers = new ArrayList<User>();
    static ArrayList<String> tokens = new ArrayList<String>();
    static ArrayList<User> activePayments = new ArrayList<User>();
    static ArrayList<String> ticketIds = new ArrayList<String>();
    static Userdb udb = new Userdb();
    static Flightdb fdb = new Flightdb();

    public static void main(String args[]){
        launch(args);
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("op-> ");
            switch(sc.next()){
                case "register":

                    break;
                case "verifyme":
                    System.out.println("v-> ");
                    int t = udb.addUser(sc.next(), Integer.parseInt(sc.next()));
                    if(t==1) System.out.println("Register Succesful!");
                    else System.out.println("Wrong");
                    break;
                case "login":
                    System.out.println("l-> ");
                    StringBuilder token= new StringBuilder();
                    User ul = udb.getUser(sc.next(),sc.next(),token);
                    if(ul!=null){
                        activeUsers.add(ul);
                        tokens.add(token.toString());
                        System.out.println("Login Succesful!" + token);
                    }
                    else System.out.println("Wrong");
                    break;
                case "logout":
                    boolean stat = false;
                    System.out.println("out-> ");
                    String userId = sc.next();
                    User up = getUser(userId);
                    if(up!=null){
                        stat = up.logout();
                        tokens.remove(activeUsers.indexOf(up));
                        activeUsers.remove(up);
                    }
                    if(stat) System.out.println("logged out");
                    else System.out.println("Wrong");
                    break;
                case "book":

                case "verifypayment":
                    System.out.print("userId-> ");
                    up = getUser(sc.next());
                    if(up==null) break;
                    String txnId = up.verifyPayment(tokens.get(activeUsers.indexOf(up)), sc.next(),Integer.parseInt(sc.next()));
                    if(txnId==null) System.out.println("Wrong");
                    else System.out.println("booked");
                    break;
                case "addFlight":
                    System.out.println("add->");
                    fdb.addFlight(new Flight(sc.next(), sc.next(), sc.next(), sc.next(), sc.next(), sc.next(), Double.parseDouble(sc.next()),Double.parseDouble(sc.next()), Integer.parseInt(sc.next()), sc.next()));
                    break;
                case "searchflight":
                    System.out.println("search->");
                    ArrayList<Flight> fd = fdb.getFlights(sc.next(),sc.next(),sc.next());
                    for(Flight f: fd) f.printd();
                    break;
                case "printFlight":
                    fdb.printflight();
                    break;
                case "changepassword":
                    System.out.println("chps->");
                    if(udb.changePassword(sc.next())) System.out.println("Otp Sent");
                    break;
                case "change":
                    System.out.println("chv->");
                    if(udb.changePassword(sc.next(),sc.next(),Integer.parseInt(sc.next()))) System.out.println("Password Changed!!");
                case "available":
                    Flight g = fdb.getFlight(sc.next());
                    if(g==null) {System.out.println("Something Went Wrong!"); break;}
                    ArrayList<Boolean> bool = g.getAvailability(sc.next());
                    if(bool==null) {System.out.println("Something Went Wrong!"); break;}
                    for(int i = 0 ; i < bool.size(); i++) System.out.println(i + " " + bool.get(i));
                    break;
                case "end":
                    System.exit(0);
            }
        }

    }
    static User getUser(String userId){
        for(int i = activeUsers.size()-1; i >= 0; i--) if(activeUsers.get(i).getUserId().compareTo(userId)==0) return activeUsers.get(i);
        return null;
    }

    public void handleLogout(ActionEvent e){
        boolean stat = false;
        User up = getUser(activeUsers.get(0).getUserId());
        if(up!=null){
            stat = up.logout();
            Button bt = (Button) mainScene.lookup("#login");
            bt.setText("Login/Register");
            tokens.remove(activeUsers.indexOf(up));
            activeUsers.remove(up);
        }
        if(stat) System.out.println("logged out");
        else System.out.println("Wrong");
    }

    public void handleLogin(ActionEvent e){
        if(activeUsers.size()>0) return;
        Label l1=new Label("   OR");
        TextField tf=new TextField();
        PasswordField pf=new PasswordField();
        tf.setPromptText("User Name");
        pf.setPromptText("Enter Password");
        Button b = new Button("Login");
        Button b1 = new Button("Register");
        Button ch = new Button("Change Paasword");
        Button search = new Button("Search");
        Button mybooking = new Button("My Booking");
        GridPane root = new GridPane();
        b.setStyle("-fx-background-color: #8000ff");
        ch.setStyle("-fx-background-color: #8000ff");
        b1.setStyle("-fx-background-color: #8000ff");
        b.setMaxSize(200,20);
        b1.setMaxSize(200,20);
        ch.setMaxSize(200,20);
        root.addRow(1,tf);
        root.addRow(2,pf);
        root.addRow(3,b);
        root.addRow(5,b1);
        root.addRow(6,ch);
        root.addRow(4,l1);
        l1.setMaxSize(150,20);
        l1.setAlignment(Pos.CENTER);
        b1.setAlignment(Pos.CENTER);
        b.setAlignment(Pos.CENTER);
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        BackgroundFill background_fill = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(background_fill);
        root.setBackground(background);
        // root.getIcons().add(new Image("file:icon.png"));
        root.setPadding(new Insets(10,10,10,10));
        Scene scene=new Scene(root,400,400);
        Stage st = new Stage();

        st.show();
        st.setScene(scene);
        st.setTitle("Udaan");
        ch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane pane = new GridPane();
                TextField tf1 = new TextField();
                PasswordField tf2 = new PasswordField();
                tf1.setPromptText("Enter User Name");
                tf2.setPromptText("Enter New Password");
                Button b = new Button("Get Otp");
                Button v = new Button("Verify");
                TextField otp = new TextField();
                v.setDisable(true);
                otp.setDisable(true);
                pane.addRow(5,tf1);
                pane.addRow(6,tf2);
                pane.addRow(7,b);
                pane.addRow(8,otp);
                pane.addRow(9,v);
                BackgroundFill background_fill = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
                Background background = new Background(background_fill);
                pane.setBackground(background);
                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("chps->");
                        v.setDisable(false);
                        otp.setDisable(false);
                        if(udb.changePassword(tf1.getText())) System.out.println("Otp Sent");
                    }
                });
                v.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("chv->");
                        if(udb.changePassword(tf1.getText(),tf2.getText(),Integer.parseInt(otp.getText()))) {
                            st.close();
                            System.out.println("Password Changed!!");
                        }
                    }
                });
                pane.setPadding(new Insets(10,10,10,10));
                Scene scene=new Scene(pane,400,400);
                st.setScene(scene);
                pane.setAlignment(Pos.CENTER);
            }
        });
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.out.println("l-> ");
                StringBuilder token= new StringBuilder();
                User ul = udb.getUser(tf.getText(),pf.getText(),token);
                if(ul!=null){
                    activeUsers.add(ul);
                    tokens.add(token.toString());
                    System.out.println("Login Succesful!" + token);
                    Button b = (Button) mainScene.lookup("#login");
                    b.setText("Hi " + tf.getText() + "!!");
                    //b.setDisable(true);
                    st.close();
                }
                else System.out.println("Wrong");
            }
        });
        b1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                GridPane pane = new GridPane();
                register(pane);
            }

            private void register(GridPane pane) {
                TextField tf1=new TextField();
                TextField tf2=new TextField();
                TextField tf3=new TextField();
                TextField tf4=new TextField();
                TextField tf5=new TextField();
                TextField tf6=new TextField();
                PasswordField tf7=new PasswordField();
                PasswordField tf8=new PasswordField();
                TextField tf9 = new TextField();
                Button btn1 = new Button("Submit");
                Button btn2 = new Button("Verify");
                //tf1.setDisable(true);
                tf1.setPromptText("User Name");
                tf2.setPromptText("First Name");
                tf3.setPromptText("Last Name");
                tf4.setPromptText("Email ID");
                tf5.setPromptText("Confirm Email ID");
                tf6.setPromptText("Mobile Number");
                tf7.setPromptText("Password");
                tf8.setPromptText("Confirm Password");
                tf9.setPromptText("otp");
                ToggleGroup group = new ToggleGroup();
                Label l1 = new Label("Gender");
                RadioButton button1 = new RadioButton("Male");
                RadioButton button2 = new RadioButton("Female");
                button1.setToggleGroup(group);
                button2.setToggleGroup(group);
                pane.addRow(0,tf1);
                pane.addRow(1,tf2);
                pane.addRow(2,tf3);
                pane.addRow(3,tf4);
                pane.addRow(4,tf5);
                pane.addRow(5,tf6);
                pane.addRow(6,tf7);
                pane.addRow(7,tf8);
                pane.addRow(8,btn1);
                pane.addRow(9,tf9);
                pane.addRow(10,btn2);
                tf9.setDisable(true);
                btn1.setMaxSize(150,10);
                btn2.setMaxSize(150,20);
                pane.setVgap(10);
                BackgroundFill background_fill = new BackgroundFill(Color.WHITE,
                        CornerRadii.EMPTY, Insets.EMPTY);
                Background background = new Background(background_fill);
                pane.setBackground(background);
                btn1.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(!tf4.getText().contains("@")||!tf4.getText().contains("."))
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Incorrect emailID");
                            alert.showAndWait();
                            return;
                        }
                        if(tf4.getText().compareTo(tf5.getText())!=0)
                        {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            alert.setHeaderText("Confirm emailID");
                            alert.showAndWait();
                            return;
                            // alert.setContentText("Are you ok with this?");
                        }
                        if(tf6.getText().length()!=10)
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Incorrect Mobile Number");
                            alert.showAndWait();
                            return;
                        }
                        try{
                            Double temp = Double.parseDouble(tf6.getText());
                        } catch (NumberFormatException ex) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Incorrect Mobile Number");
                            alert.showAndWait();
                            return;
                        }
                        if(tf7.getText().compareTo(tf8.getText())!=0)
                        {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            alert.setHeaderText("Confirm Password");
                            alert.showAndWait();
                            return;
                        }
                        System.out.println("r-> ");
                        String uid = tf1.getText();
                        User u = new User(uid ,tf2.getText()+tf3.getText(),tf4.getText(),tf6.getText(),tf7.getText());
                        Main.udb.addUser(uid, u);
                        System.out.println("Ok!");
                        tf9.setDisable(false);
                        btn1.setDisable(true);
                    }
                });
                btn2.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        System.out.println("v-> ");
                        int t = udb.addUser(tf1.getText(), Integer.parseInt(tf9.getText()));
                        if(t==1) System.out.println("Register Succesful!");
                        else System.out.println("Wrong");
                        st.close();
                    }
                });
                pane.setPadding(new Insets(10,10,10,10));
                Scene scene=new Scene(pane,400,400);
                st.setScene(scene);
                pane.setAlignment(Pos.CENTER);
            }
        } );
    }
    public void handleSearch(ActionEvent e){
        System.out.println("search->");
        TextField tf1 = (TextField)mainScene.lookup("#source");
        TextField tf2 = (TextField)mainScene.lookup("#dest");
        DatePicker dt = (DatePicker)mainScene.lookup("#date");
        ObservableList<Flight> naam = FXCollections.observableArrayList();
        tablesearch.setItems(naam);
        ArrayList<Flight> fd = fdb.getFlights(tf1.getText(),tf2.getText(),dt.getValue().toString());
        naam.addAll(fd);
        tablesearch.setItems(naam);
        for(Flight f: fd) f.printd();
    }

    public void handleMyBooking(ActionEvent e){
        tablesearch.setVisible(false);
        ObservableList<Ticket> naam = FXCollections.observableArrayList();
        tablebooks.setItems(naam);
        Button b1 = (Button) mainScene.lookup("#booktt");
        Button b2 = (Button) mainScene.lookup("#canceltt");
        b1.setVisible(false);
        b2.setVisible(true);
        if(activeUsers.size()==0) return;
        ArrayList<Ticket> tt = activeUsers.get(0).getTickets();
        naam.addAll(tt);

        tablebooks.setItems(naam);
        tablebooks.setVisible(true);
    }
    public void handleCancel(ActionEvent e){
        Ticket t = tablebooks.getSelectionModel().getSelectedItem();
        if(t==null) return;
        if(activeUsers.size()==0) return;
        activeUsers.get(0).cancelTicket(tokens.get(0),t);
        ArrayList<Ticket> tt = activeUsers.get(0).getTickets();
        ObservableList<Ticket> naam = FXCollections.observableArrayList();
        tablebooks.setItems(naam);
        naam.addAll(tt);

        tablebooks.setItems(naam);
        tablebooks.setVisible(true);
    }
    public void handleSearchFlight(ActionEvent e){
        tablebooks.setVisible(false);
        Button b1 = (Button) mainScene.lookup("#booktt");
        Button b2 = (Button) mainScene.lookup("#canceltt");
        b1.setVisible(true);
        b2.setVisible(false);
        tablesearch.setVisible(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        flightid.setCellValueFactory(new PropertyValueFactory<Flight,String>("flightId"));
        sourcec.setCellValueFactory(new PropertyValueFactory<Flight,String>("source"));
        destc.setCellValueFactory(new PropertyValueFactory<Flight, String>("destination"));
        provider.setCellValueFactory(new PropertyValueFactory<Flight, String>("provider"));
        starttime.setCellValueFactory(new PropertyValueFactory<Flight, String>("startTime"));
        duration.setCellValueFactory(new PropertyValueFactory<Flight, Double>("journeytime"));
        price.setCellValueFactory(new PropertyValueFactory<Flight,Double>("price"));
        ticketid.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        dateb.setCellValueFactory(new PropertyValueFactory<>("date"));
        flightidb.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        priceb.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        status.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        txnid.setCellValueFactory(new PropertyValueFactory<>("txnId"));
    }

    public void handleBook(ActionEvent e){
        Flight flight = tablesearch.getSelectionModel().getSelectedItem();
        if(flight == null) return;
        ArrayList<Passenger> p = new ArrayList<Passenger>();
        ArrayList<Seat> seats = new ArrayList<Seat>();
        //System.out.println(flight);
        DatePicker temp = (DatePicker) mainScene.lookup("#date");
        String date = temp.getValue().toString();

        flight.printd();
        GridPane g = new GridPane();

        TextField tf1 = new TextField();
        tf1.setText(flight.getFlightId());
        tf1.setDisable(true);
        g.addRow(0,tf1);

        TextField tf2 = new TextField();
        tf2.setText(date);
        tf2.setDisable(true);
        g.addRow(1,tf2);

        Label l = new Label("Total : Rs 0");
        l.setAlignment(Pos.CENTER);
        l.setTextAlignment(TextAlignment.CENTER);
        g.addRow(3,l);

        ArrayList<Boolean> avail = flight.getAvailability(date);
        Button b = new Button("Select Seats");
        g.addRow(2,b);
        b.setMaxSize(200,20);
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane gh = new GridPane();
                EventHandler<ActionEvent> value = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event1) {
                        GridPane pas = new GridPane();
                        TextField tf1 = new TextField();
                        tf1.setPromptText("Enter Name");
                        pas.addRow(0,tf1);

                        TextField tf2 = new TextField();
                        tf2.setPromptText("Enter Age");
                        pas.addRow(1,tf2);

                        TextField tf3 = new TextField();
                        tf3.setPromptText("Enter Phno");
                        pas.addRow(2,tf3);

                        Button b = new Button("Add Passenger");
                        b.setMaxSize(200,20);
                        pas.setHgap(10);
                        pas.setVgap(10);
                        pas.setAlignment(Pos.CENTER);
                        Stage aurek = new Stage();
                        aurek.setScene(new Scene(pas,200,200));
                        aurek.show();
                        pas.addRow(3,b);

                        b.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                Button bt = (Button) event1.getSource();
                                String s = bt.getText();
                                int stno = Integer.parseInt(s.substring(1));
                                System.out.println(s);

                                //for(Passenger ps : p) if(ps.getSeatNo()==stno) p.remove(ps);
                                try {
                                    p.add(new Passenger(tf1.getText(), Integer.parseInt(tf2.getText()), tf3.getText(), date, flight.getFlightId(), stno));
                                    Seat tempseat = flight.getSeat(stno);
                                    System.out.println(tempseat.getSeatId() + " "+ seats.size());
                                    seats.add(tempseat);
                                    bt.setStyle("-fx-background-color: #00ff00");
                                }catch (Exception e){
                                    bt.setStyle("-fx-background-color: #ffffff");
                                }

                                aurek.close();
                            }
                        });
                    }
                };
                int i =0;
                for(i = 0; i < avail.size(); i++){
                    Button b = new Button("S"+i);
                    b.setOnAction(value);
                    b.setStyle("-fx-background-color: #ffffff");
                    b.setMaxSize(40,20);
                    if(!avail.get(i)) b.setDisable(true);
                    gh.addRow(i/5,b);
                }

                Button done = new Button("Go");
                gh.add(done,3,i/5+1);
                done.setMaxSize(40,20);
                gh.setHgap(10);
                gh.setVgap(10);
                gh.setAlignment(Pos.CENTER);
                Stage sts = new Stage();
                sts.setScene(new Scene(gh, 300,300));
                sts.show();
                done.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        sts.close();
                        b.setDisable(true);
                        l.setText("Total : Rs " + flight.getPrice()*p.size());
                        b.setText(p.size()+ " seats Selected");
                    }
                });

            }
        });

        TextField otp = new TextField();
        otp.setDisable(true);
        otp.setPromptText("Enter Otp");
        g.addRow(6,otp);

        Button verify = new Button("Verify Payment");
        verify.setMaxSize(200,20);
        verify.setDisable(true);
        g.addRow(7,verify);

        Button MakePayment = new Button("Make Payment");
        b.setMaxSize(200,20);
        g.addRow(4,MakePayment);
        StringBuilder ticketId = new StringBuilder();
        MakePayment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Ticket t = new Ticket(p,flight.getPrice()*p.size(),p.size(),seats,flight.getFlightId(),date);
                if(activeUsers.size()==0) return;
                ticketId.append(activeUsers.get(0).addTickets(tokens.get(0),t));
                otp.setDisable(false);
                verify.setDisable(false);
            }
        });

        g.setVgap(10);
        g.setAlignment(Pos.CENTER);
        Scene sc = new Scene(g,400,400);
        Stage st =  new Stage();
        st.setScene(sc);
        st.show();
        verify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String temp = activeUsers.get(0).verifyPayment(tokens.get(0),ticketId.toString(),Integer.parseInt(otp.getText()));
                if(temp!=null) {
                    System.out.println(temp + " Done" );
                    st.close();
                }
            }
        });
    }
}
