package sample;

import java.util.ArrayList;

public class Ticket{
    private String ticketId, paymentStatus, txnId, flightId, date;
    private ArrayList<Passenger> passengers;
    private int noOfPassengers;
    private ArrayList<Seat> seats;
    private double totalPrice;

    Ticket(ArrayList<Passenger> passengers,double totalPrice, int noOfPassengers, ArrayList<Seat> seats, String flightId, String date){
        this.ticketId = passengers.get(0).getname()+totalPrice+noOfPassengers+flightId+date;
        this.passengers=passengers;
        this.totalPrice=totalPrice;
        this.noOfPassengers=noOfPassengers;
        this.seats=seats;
        this.flightId=flightId;
        this.date=date;
    }

    public boolean cancel(){
        this.paymentStatus= "refunded";
        System.out.println("cancelled!!");
        for(int i = 0; i < passengers.size(); i++) passengers.get(i).cancel();
        for(int i = 0; i < seats.size(); i++) seats.get(i).cancel();
        return true;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getTxnId() {
        return txnId;
    }

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public int getNoOfPassengers() {
        return noOfPassengers;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getDate() { return date; }

    protected boolean confirm(String txnId){
        this.paymentStatus = "paid";
        this.txnId = txnId;
        for(int i = 0; i < passengers.size(); i++) passengers.get(i).confirm();
        for(int i = 0; i < seats.size(); i++) seats.get(i).confirm(passengers.get(i));
        return true;
    }
}
