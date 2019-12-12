package sample;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Flight{
    private String flightId, source,destination,provider,validity,startTime;
    private String[] days;
    private ArrayList<Seat> seatArray;
    private double journeytime, price;
    private int noOfSeat;
    Flight(String flightId, String source, String destination, String provider, String validity, String startTime, double journeytime,double price, int noOfSeat, String... days){
        this.flightId = flightId;
        this.source = source;
        this.destination = destination;
        this.provider = provider;
        this.validity = validity;
        this.startTime = startTime;
        this.journeytime=journeytime;
        this.price=price;
        this.noOfSeat=noOfSeat;
        this.days = days;
        seatArray = new ArrayList<Seat>();
        for(int i = 0; i < noOfSeat; i++) seatArray.add(new Seat(i,price,flightId));
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    public void setSeatArray(ArrayList<Seat> seatArray) {
        this.seatArray = seatArray;
    }

    public void setJourneytime(double journeytime) {
        this.journeytime = journeytime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setNoOfSeat(int noOfSeat) {
        this.noOfSeat = noOfSeat;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getProvider() {
        return provider;
    }

    public String getValidity() {
        return validity;
    }

    public String getStartTime() {
        return startTime;
    }

    public String[] getDays() {
        return days;
    }

    public ArrayList<Seat> getSeatArray() {
        return seatArray;
    }

    public double getJourneytime() {
        return journeytime;
    }

    public double getPrice() {
        return price;
    }

    public int getNoOfSeat() {
        return noOfSeat;
    }

    protected boolean runs(String date){
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
        Date dt1= null;
        try {
            dt1 = format1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format2=new SimpleDateFormat("EEEE");
        String day=format2.format(dt1);
        for(String d:days) if(d.compareToIgnoreCase(day)==0) return true;
        return false;
    }

    protected boolean runs(String source, String dest, String date){
        System.out.println(date);
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
        Date dt1= null;
        try {
            dt1 = format1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(dt1.toString());
        DateFormat format2=new SimpleDateFormat("EEEE");
        String day=format2.format(dt1);
        System.out.println(day);
        if(source.compareToIgnoreCase(this.source)!=0||dest.compareToIgnoreCase(this.destination)!=0) return false;
        for(String d:days) if(d.compareToIgnoreCase(day)==0) return true;
        return false;
    }

    public void printd() {
        String s = " ";
        System.out.println(flightId +  s + source + s + destination + s + provider + s + days[0]);
    }

    public ArrayList<Boolean> getAvailability(String date){
        if(!runs(date)) return null;
        ArrayList<Boolean> av =  new ArrayList<Boolean>();
        for(Seat s : seatArray) av.add(s.isAvailable(date));
        return av;
    }

    public Seat getSeat(int stno) {
        return seatArray.get(stno);
    }
}
