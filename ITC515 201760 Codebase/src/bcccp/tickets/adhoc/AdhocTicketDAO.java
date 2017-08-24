package bcccp.tickets.adhoc;
import java.util.ArrayList;
import java.util.List;

public class AdhocTicketDAO  implements IAdhocTicketDAO  {

	private IAdhocTicketFactory factory;
	private int currentTicketNo;
        private List<IAdhocTicket> ticketList = new ArrayList<IAdhocTicket>();

	
	
	public AdhocTicketDAO(IAdhocTicketFactory factory) {
		//TODO Implement constructor           
            this.factory = factory;
            
	}



	@Override
	public IAdhocTicket createTicket(String carparkId) {
		// TODO Auto-generated method stub
            IAdhocTicket NewTicket = this.factory.make(carparkId, currentTicketNo);
            ticketList.add(NewTicket);            
            currentTicketNo++;
            return NewTicket;
	}



	@Override
	public IAdhocTicket findTicketByBarcode(String barcode) {
            
                for(int i = 0; i < ticketList.size(); i++){
                    IAdhocTicket t = ticketList.get(i);
                    if(t.getBarcode().equals(barcode)){
                        return t;                    
                    }
                }
                System.out.println("Ticket not in system");
		return null;
	}



	@Override
	public List<IAdhocTicket> getCurrentTickets() {
		
		return this.ticketList;
	}

	
	
}

