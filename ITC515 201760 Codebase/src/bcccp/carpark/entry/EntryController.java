package bcccp.carpark.entry;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarSensorResponder;
import bcccp.carpark.ICarpark;
import bcccp.carpark.ICarparkObserver;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;

public class EntryController 
		implements	ICarSensorResponder,
				ICarparkObserver,
				IEntryController {
	private enum STATE { IDLE, WAITING, FULL, VALIDATED, ISSUED, TAKEN, ENTERING, BLOCKED }
	
	private STATE state_;
	private STATE prevState_;
	private String message;

	private IGate entryGate;
	private ICarSensor outsideSensor; 
	private ICarSensor insideSensor;
	private IEntryUI ui;
	
	private ICarpark carpark;
	private IAdhocTicket  adhocTicket = null;
	private long entryTime;
	private String seasonTicketId = null;
	
	

	public EntryController(Carpark carpark, IGate entryGate, 
			ICarSensor os, 
			ICarSensor is,
			IEntryUI ui) {
		//TODO Implement constructor
		this.carpark = carpark;
		this.entryGate_ = entryGate;
		this.outsideEntrySensor_ = os;
		this.insideEntrySensor_ = is;
		this.ui = ui;
		
		outsideEntrySensor_.registerResponder(this);
		insideEntrySensor_.registerResponder(this);
		ui.registerController(this);
		
		setGate(STATE.IDLE);
		
	}
	private void log(String message) {
		System.out.println("EntryController: " + message);
	}
	
	@Override
	public void carEventDetected(String detectorld, boolean carDetected)
		log("carEventDetected: " + detectorld + ", car Detected: " + carDetected ); 
		switch (state_) { 
		case BLOCKED: 
			if (detectorld.equals(insideEntrySensor_.get!d()) && IcarDetected) { 
				setState (prevState_);
			}
			break; 
		case IDLE:
			log("eventDetecfced: IDLE");
			if (detectorld.equals(outsideEntrySensor_.get!d()) && carDetected) { 
				log('eventDetected: setting state to WAITING"); 
				setState(STATE.WAITING); 
			}
			else if (detectorld.equals(insideEntrySensor_.get!d()) && carDetected) {
				setState(STATE.BLOCKED); 

			} 

			break; 
		
		case WAITING:
		case FULL:
		case VALIDATED:
		case ISSUED: 
			if (detectorld.equals(outsideEntrySensor_.getId()) && !carDetected) {
				setState(STATE.IDLE); 
			}
			else if (detectorld.equals(insideEntrySensor_.getId()) && carDetectecd0 {
				setState(STATE.BLOCKED); 
			}

			break; 

		case TAKEN: 
			if (detectorld.equals(outsideEntrySensor_.getId()) && IcarDetected) {
				setState(STATE.IDLE);
			}
			else if (detectorld.equals(insideEntrySensor_.get!d()) && carDeted) {
				setState(STATE.ENTERING); 
			} 

			break;
		
		case ENTERING:
			if (detectorld.equals(outsideEntrySensor_.getId()) && IcarDetected) {
				setState(STATE.IDLE);
			}
			else if (detectorld.equals(insideEntrySensor_.get!d()) && carDeted) {
				setState(STATE.ENTERING); 
			} 

			break;
		
		case ENTERED:
			if (detectorld.equals(outsideEntrySensor_.getId()) && IcarDetected) {
				setState(STATE.IDLE);
			}
			else if (detectorld.equals(insideEntrySensor_.get!d()) && carDeted) {
				setState(STATE.ENTERING); 
			} 

			break;	 
		default:
			break;
		}
	}

	private void setState(STATE newState) { 
		switch (newState) { 
		
		case BLOCKED:
			log("setState: BLOCKED");
			prevState_ = state_; 
			state = STATE.BLOCKED;
			message = "Blocked"; 
			ui.display(message); 
			break; 
			
		case IDLE: 
			log("setState: IDLE"); 
			if (prevState_ == STATE.ENTERED) { 
				if (adhocTicket != null) { 
					adhocTicket.enter(entryTime); 
					carpark.recordAdhocTicketEntry(); 
					entryTime = 0; 
					log(adhocTicket.toString() ); 
					adhocTicket = null; 
				}	
				else if (seasonTicketId 1= null) {
					carpark.recordSeasonTicketEntry(seasonTicketId); 
					seasonTicketId = null; 

				} 

			} 

			message a "Idle";
			state » STATE.IDLE;
			prevState_ s state_;
			ui.display(message);
			if (outsideEntrySensor^.carIsDetectedO) {
				setState(STATE.WAITING);
			}
				if (entryGate_.isRaised() ) {
					entryGate_.lower(); 
				}
				ui.discardTicketO; 
				break; 
			
		case WAITING: 
			log("sefcState: WAITING"); 
			message = "Push Button"; 
			state_ = STATE.WAITING; 
			ui.display(message);
			if (loutsideEntrySensor_.carIsDetected() ) { 
				setState(STATE.IDLE);
			}
			break; 
				
		case FULL: 
			log("sefcState: FULL");
			message = "Carpark Full";
			state = STATE.FULL;
			prevState_ = state_; 
			ui.display(message); 
			break; 
		
		case VALIDATED: 
			log("setState: VALIDATED"); 
			message = "Ticket Validafced"; 
			state = STATE.VALIDATED; 
			prevState_ = state_; 
			ui.display(message); 
			if (loutsideEntrySensor_.carIsDetected()) { 
				setState(STATE.IDLE); 
			} 
			break; 
			
		case ISSUED: 
			log("setSfcate: ISSUED" ); 
			message = "Take Ticket"; 
			state = STATE.ISSUED; 
			prevState_ = state_; ui.display(message); 
			if (!outsideEntrySensor_.carIsDetected()) { 
				setState(STATE.IDLE); 
			} 
			break; 

		case TAKEN: 
			log("setState: TAKEN"); 
			message = "Ticket Taken" 
			state = STATE.TAKEN; 
			prevState__ = state_; 
			ui.display(message); 
			entryGate_.raise()?
			break;
		
		case ENTERING: 
			log("sefcState: ENTERING"); 
			message = "Entering"; 
			state = STATE.ENTERING; 
			prevState_ = state_; 
			ui.display(message); 
			break; 
		
		case ENTERED: 
			log("sefcState: ENTERED"); 
			message = "Entered"; 
			state = STATE.ENTERED; 
			prevState_ = state_; 
			ui.display(message); 
			break; 

		default: 
			break;
	@Override
	public void buttonPushed() {
		// TODO Auto-generated method stub
		if (state_ == STATE.WAITING) { 
			if (icarpark.isFullO) { 
				adhocTickefc = carpark.issueAdhocTicket0; 

				String carparkld = adhocTicket.getCarparkIdO;
				int ticketNo = adhocTicket.getTicketNo(); 
				entryTime = System.currentTimeMillis(); 
				//entryTime = adhocTickefc.getEntryDateTimeO; 
				String barcode s adhocTicket.getBarcode();
				ui.printTicket(carpark!d, ticketNo, entryTime, barcode) ; 
				setState(STATE.ISSUED);

			} 
			else { 
				setState(STATE.FULL); 
			} 

		} 

		else { 
			ui.beep (); 
			log("ButfconPushed: called while in incorrect state"); 
		}
		
	}



	@Override
	public void ticketInserted(String barcode) {
		// TODO Auto-generated method stub
		if (state_ -= STATE.WAITING) { 
			try { 
				if (carpark.isSeasonTicketValid(barcode) && 
					!carpark.isSeasonTicketInUse(barcode) ) { 
					this.seasonTicketId = barcode; 
					setState(STATE.VALIDATED); 
				} 
				else { 
					ui.beep(); 
					seasonTicketId = null; 
					log("ticketliiserfced: invalid ticket id"); 
				} 
			} 

			catch (NumberFormatException e) { 
				ui.beep() ; 
				seasonTicketld = null; 
				log("fcickefclnserted: invalid fcicket id"); 

			} 

		} 

		else { 
			ui.beep();
			log("tickefclnserted: called while in incorrect state"); 
		}
	}

	
	©Override 
	public void ticketTakenO { 
		if (state_ == STATE.ISSUED II State_ == STATE.VALIDATED ) { 
			setState(STATE.TAKEN); 
		} 

		else { 
			ui.beep 0; 
			log("fcicketTaken: called while in incorrect state"); 

			} 
	}

	©Override 
	public void notifyCarparkEvent() { 
		if (state_ s= STATE.FULL) { 
			if (Icarpark.isFullO) { 
				setState(STATE.WAITING); 

			} 

		} 

	}


