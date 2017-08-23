package bcccp.carpark.exit;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarSensorResponder;
import bcccp.carpark.ICarpark;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;

public class ExitController 
		implements ICarSensorResponder,
		           IExitController {
	private enum STATE { IDLE. WAITING, PROCESSED, REJECTED, TAKEN, EXITING, EXITED, 
			    BLOCKED }
	
	private STATE state;
	private STATE prevState;
	private String message;
	//private String prevMessage
				   
	private IGate exitGate;
	private ICarSensor insideSensor;
	private ICarSensor outsideSensor; 
	private IExitUI ui;
	
	private ICarpark carpark;
	private IAdhocTicket  adhocTicket = null;
	private long exitTime;
	private String seasonTicketId = null;
	
	

	public ExitController(Carpark carpark, IGate exitGate, 
			ICarSensor is,
			ICarSensor os, 
			IExitUI ui) {
		
		//TODO Implement constructor
		this.carpark = carpark; 
		this.exitGate = exitGate; 
		this.is = is; 
		this.os = os; 
		this.ui = ui; 
		
		os.registerResponder(this); 
		is.registerResponder(this) ; 
		ui.registerController(this); 
		
		prevState = STATE.IDLE; 
		setState(STATE.IDLE); 
	}
				   
	private void log(String message) {
		System.out.println ("ExitController: " + message);
	}
	
	©Override 
	public void carEvenDetected (String detectorId, boolean car Detected) {
		log("carEvenDetected: " + detectorId + ", car Detected: " + carDetected);

		switch (state) { 
		
		case BLOCKED: 
			if (detectorld.equals(is.getIdO) && IcarDetected) { 
				setState(prevState); 
			break; 
				
		case IDLE: 
			log("eventDetected: IDLE"); 
			if (detectorld.equals(is.getIdO) && carDetected) { 
				log("eventDetected: setting state to WAITING"); 
				setState(STATE.WAITING); 
			}
			else if (detectorld.equals(os.getIdO) && carDetected) { 
				setState(STATE.BLOCKED); 
			} 
			break; 


		case WAITING: 
		case PROCESSED: 
			if (detectorld.equals(is.getIdO) fic& IcarDetected) { 
				setStafce(STATE.IDLE); 
			}
			else if (detectorld.equals(os.getIdO) && carDetected) { 
			setState(STATE.BLOCKED); 
			} 
			break; 
		
				
		case TAKEN: 
			if (detectorld.equals(is.getIdO) && IcarDetected) { 
				setState(STATE.IDLE); 
			} 

			else if (detectorld.equals(os.getld()) && carDetected) { 
				setState(STATE.EXITING); 
			} 
			break; 

				
		case EXITING: 
			if (defcectorld.equals(is.getIdO) && IcarDetected) { 
				setState(STATE.EXITED);
			}
			else if (detectorld.equals(os.getIdO) && lcarDetected) { 
				setState(STATE.TAKEN); 
			}
			break;
	
				
		case EXITED:
			if (defcectorld.equals(is.getIdO) && IcarDetected) { 
				setState(STATE.EXITED);
			}
			else if (detectorld.equals(os.getIdO) && lcarDetected) { 
				setState(STATE.TAKEN); 
			}
			break;
		
		
		default:
			break;
		}
	}

	private void setState(STATE newState) { 
		switch (newState) { 
		
		case BLOCKED: 
			log ("sefcStafce: BLOCKED"); 
			prevState = state; 
			//prevMessage = message; 
			state = STATE.BLOCKED; 
			message = "Blocked"; 
			ui.display(message); 
			break; 
			
			
		case IDLE; 
			log("setState: IDLE") ; 
			if (prevState == STATE.EXITED) { 
				if (adhocTicket != null) { 
					adhocTicket.exit(exitTime); 
					carpark.recordAdhocTicketExitO; 
					log(adhocTicket.toString() ) ; 
				}
				else if (seasonTicketId 1= null) { 
					carpark.recordSeasonTicketExit(seasonTicketId);
				} 
			}		 

			adhocTicket = null;
			seasonTicketId = null; 

			message a "Idle"; 
			state = STATE.IDLE; 
			//prevMessage = message; 
			prevState = state; 
			ui.display(message); 
			if (is.carIsDetectedO) { s
				etState(STATE.WAITING); 
			}
				if (exitGate.isRaised()) {
					exitGate.lower(); 
				}
				exitTime = 0;
				break;
		
			
		case WAITING: 
			log("setState: WAITING"); 
			message = "Ixiserfc Ticket"; 
			state = STATE.WAITING; 
			//prevMessage = message; 
			prevState = state; 
			ui.display(message); 
			if (!is.carIsDetectedO) { 
				setState(STATE.IDLE); 
			} 
			break; 
			
			
		case PROCESSED: 
			log("setStafce: PROCESSED") ; 
			message = "Take Processed Ticket"; 
			state = STATE.PROCESSED; 
			//prevMessage = message; 
			prevState = state; 
			ui.display(message); 
			if (lis.carlsDetectedO) { 
				setState(STATE.IDLE);
			} 
			break; 
			
		
		case REJECTED: 
			log("setStafce: REJECTED" ) ; 
			message = "Take Rejected Ticket"; 
			State = STATE.REJECTED; 
			//prevMessage = message; 
			prevState = state; 
			ui.display(message);
			if (Jis.carIsDetectedO) { 
				setState(STATE.IDLE);
			} 
			break; 
			
			
		case TAKEN: log("setStafce: TAKEN"); 
			message = "Ticket Taken"; 
			state = STATE.TAKEN; 
			//prevMessage = message; 
			prevState = state; 
			ui.display(message); 
			break; 

				
		case EXITING; 
			log("setStafce: EXITING"); 
			message » "Exiting"; 
			state « STATE.EXITING;
			//prevMessage = message; 
			prevState " stace; 
			ui.display(message); 
			break; 
			
			
		case EXITED: 
			log("setSfcate: EXITED"); 
			message » "Exited"; 
			State = STATE.EXITED; 
			//prevMessage = message; 
			prevState = state; 
			ui.display(message); 
			break; 
			
			
		default: 
			break; 
		}
	}

	
	private boolean isAdhocTicket(String barcode) { 
		return barcode.substring(0,1).equals("A"); 
	}
	

	©Override 
	public void ticketlnserted(String ticketStr) { 
		if (state == STATE.WAIT ING) { 
			if (isAdhocTicket(ticketStr)) { 
				adhocTicket = carpark.getAdhocTicket(ticketStr); 
				exitTime = System.currentTimeMillis(); 
				if (adhocTicket != null && adhocTicket.isPaidO) { 
					setState(STATE.PROCESSED); 
				} 
				else { 
					ui.beep () ; 
					setState(STATE.REJECTED); 
				} 
			} 
			else if (carpark.isSeasonTicketValid(ticketStr) && 
				 carpark.isSeasonTicketInUse(ticketStr)){ 
				seasonTicketId = ticketStr; 
				setState(STATE.PROCESSED); 
			} 
			else { ui. beep (); 
			      setState(STATE.REJECTED); 
			} 
		} 
		else { 
			ui. beep ();
			ui.discardTicket(); 
			log( licketrr.ser^ed: called while in incorrect state"); 
			setStace(STATE.REJECTED); 
		}
	}
			    
			    

	@Override 
	public void ticketTaken() { 
		if (state == STATE.PROCESSED) { 
			exitGate.raise(); 
			setState(STATE.TAKEN); 
		} 
		else if (state == STATE. REJECTED) { 
			etState(STATE.WAITING); 
		}
		else { 
			ui.beep(); 
			log("ticketTaken: called while in incorrect state"); 
		}
	}



	@Override
	public void carEventDetected(String detectorId, boolean detected) {
		// TODO Auto-generated method stub
		
	}

	
	
}
