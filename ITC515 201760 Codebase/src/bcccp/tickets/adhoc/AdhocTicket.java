package bcccp.tickets.adhoc;

import bcccp.carpark.Carpark;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class AdhocTicket implements IAdhocTicket {
	
	private String carparkId;
	private int ticketNo;
	private long entryDateTime;
	private long paidDateTime;
	private long exitDateTime;
	private float charge;
	private String barcode;
        
        private boolean isCurrent;
        private boolean hasPaid;
        
        

	
	
	public AdhocTicket(String carparkId, int ticketNo, String barcode) {
            this.carparkId = carparkId;
            this.ticketNo = ticketNo;
            this.entryDateTime = setEntryDateTime();
            this.barcode = barcode;
            this.isCurrent = true; 
            this.hasPaid = false;
            this.exitDateTime = 0L;
            this.charge = 0;
            
            
	}
        
        public AdhocTicket(String carparkId, int ticketNo) {
            this.carparkId = carparkId;
            this.ticketNo = ticketNo;
            this.entryDateTime = setEntryDateTime();
            this.barcode = getEntryDateTime()+ticketNo+"A";
            this.isCurrent = false; 
            this.hasPaid = false;
            this.exitDateTime = 0L;
	    this.charge = 0;	
            
	}


	@Override
	public int getTicketNo() {
            return this.ticketNo;
	}


	@Override
	public String getBarcode() {
            return this.barcode;
	}


	@Override
	public String getCarparkId() {
		return this.carparkId;
	}


	@Override
	public void enter(long dateTime) {
		this.entryDateTime = dateTime;
                this.isCurrent = true;
		
	}


	@Override
	public long getEntryDateTime() {
            
            return this.entryDateTime;
	}
        
        private long setEntryDateTime(){
            //Calendar cal = Calendar.getInstance();
            //SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
            //entryDateTime = Long.parseLong(sdf.format(cal.getTime()));
            //return entryDateTime;
            
            return this.entryDateTime = System.currentTimeMillis();
        }


	@Override
	public boolean isCurrent() {
            return this.isCurrent;
	}


	@Override
	public void pay(long dateTime, float charge) {
		// TODO Auto-generated method stub
                this.paidDateTime = dateTime;
                this.charge = charge;
                this.hasPaid = true;
		
	}
        @Override
        public void pay() {
		this.hasPaid = true;		
	}


	@Override
	public long getPaidDateTime() {
		return this.paidDateTime;
	}


	@Override
	public boolean isPaid() {
                return this.hasPaid;
                
	}


	@Override
	public float getCharge() {
            float shortStayCharge = 5;
                float longStayCharge = 5000;
                if(System.currentTimeMillis()- entryDateTime < 1000){
                    System.out.println(System.currentTimeMillis() + " - " + entryDateTime+" = " + (System.currentTimeMillis() - entryDateTime));
                    System.out.println("charged custmer LONG stay amount");
                    return longStayCharge;
                }
                else{
                    System.out.println(System.currentTimeMillis() + " - " + entryDateTime+" = " + (System.currentTimeMillis() - entryDateTime));
                    System.out.println("charged custmer SHORT stay amount");
                    return shortStayCharge;
                }
            
	}


	@Override
	public void exit(long dateTime) {
		this.isCurrent = false;
                this.exitDateTime = dateTime;
               
		
	}


	@Override
	public long getExitDateTime() {
		return this.exitDateTime;
	}


	@Override
	public boolean hasExited() {
		return !this.isCurrent();
	}

	
	
}
