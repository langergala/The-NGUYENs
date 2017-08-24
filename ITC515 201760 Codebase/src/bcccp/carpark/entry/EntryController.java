package bcccp.carpark.entry;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarSensorResponder;
import bcccp.carpark.ICarpark;
import bcccp.carpark.ICarparkObserver;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.AdhocTicket;
import bcccp.tickets.adhoc.IAdhocTicket;

public class EntryController 
		implements ICarSensorResponder,
				   ICarparkObserver,
		           IEntryController {
	
	private IGate entryGate;
	private ICarSensor outsideSensor; 
	private ICarSensor insideSensor;
	private IEntryUI ui;
	
	private ICarpark carpark;
	private IAdhocTicket  adhocTicket;
	private long entryTime;
	private String seasonTicketId;
        private String entryType;
	
	

	public EntryController(Carpark carpark, IGate entryGate, 
			ICarSensor os, 
			ICarSensor is,
			IEntryUI ui) {
            this.entryGate = entryGate;
            os.registerResponder(this);
            is.registerResponder(this);
            outsideSensor = os;
            this.carpark = carpark;
            ui.registerController(this);
            this.ui = ui;
            
	}



	@Override
	public void buttonPushed() {
	    //button is pushed, check car is waiting            
            if(!outsideSensor.carIsDetected()){
                //button pushed, no car detected
                ui.display("No car detected");
            }
            //if car is waiting, check to see if carpark is full
            else{
                if(!carpark.isFull()){
                    System.out.println("carpark not full");
                //carpark not full, check to see if ticket already printed                   
                    if(!ui.ticketPrinted()){
                    //car is waiting, carpark not full, ticket not already printed.
                        //create new ticket 
                        adhocTicket = carpark.issueAdhocTicket();
                        entryType = "adhoc";
                        adhocTicket.enter(entryTime);
                        //display the ticket details
                        //carpark.getAdhocTicket(seasonTicketId)
                        ui.printTicket(carpark.getName(), 
                                adhocTicket.getTicketNo(), 
                                adhocTicket.getEntryDateTime(), adhocTicket.getBarcode());
                        ui.display("Take Ticket");
                    }
                    else{
                    //car is waiting, carpark not full, ticket already printed
                        System.out.println("Ticket already printed, please take your ticket.");
                        ui.display("Ticket already printed");
                    }
                }
                else
                    //car is waiting, carpark full
                    while(carpark.isFull() && outsideSensor.carIsDetected())
                        ui.display("Full");
                //If a vehicle leaves the car park, then the ‘Press Button’ display is activated again where there is a vehicle waiting.
                //have not tested this, may need to add call back to buttonpushed() to start the process again ????
                }
                
            
        }
		
	



	@Override
	public void ticketInserted(String barcode) {
            if(!outsideSensor.carIsDetected()){
                //button pushed, no car detected
                ui.display("No car detected");
            }
            else{
                if(barcode != null){
                    if(carpark.isSeasonTicketValid(barcode)){
                        if(carpark.isSeasonTicketInUse(barcode)){
                            System.out.println("Season ticket valid");
                            entryType = "season";
                            ui.display("Take Ticket");
                        }
                        else{
                            System.out.println("Season ticket is in use");
                            ui.display("ticket in use");
                        }
                    }
                    else{
                        System.out.println("Season ticket not valid");
                        ui.display("Ticket Rejected");
                    }
                }
                else{
                    System.out.println("Season ticket button press, no season ticket entered");
                    ui.display("");
                }
                    
                    


            }
        }



	@Override      
	public void ticketTaken() {
            if(entryType != null && (entryType.contentEquals("adhoc") || entryType.contentEquals("season"))){
                ui.display("Thank you");
                entryGate.raise(); 
                if(entryType.contentEquals("adhoc")){
                    carpark.recordAdhocTicketEntry();
                    System.out.println(adhocTicket.getBarcode());
                }
                if(entryType.contentEquals("season")){
                    carpark.recordSeasonTicketEntry(seasonTicketId);
                    
                }
                entryType = "";
            }
            
            
            

            
	}



	@Override
	public void notifyCarparkEvent() {
		// TODO Auto-generated method stub
                IAdhocTicket  adhocTicketTEST = adhocTicket;
                carpark.recordAdhocTicketEntry();
		
	}



	@Override
	public void carEventDetected(String detectorId, boolean detected) {
	    System.out.println(detectorId + " has a car at it: " + detected);
                if(detectorId == "Entry Outside Sensor"){
                //car is detected waiting outside entry
                    if(detected)
                        ui.display("Press Button");
                    else
                        ui.display("");
                }
                if ((detectorId == "Entry Inside Sensor") && !detected){
                //car goes through gate
                
                    if(entryGate.isRaised()){
                        ui.display(""); //clear thank you message from display
                        entryGate.lower();

                        //do we need to auto change the Entry Outside Sensor to false????
                    }
                
                }
		
	}

	
	
}
