package bcccp.tickets.adhoc;




public class AdhocTicketFactory implements IAdhocTicketFactory {

	@Override
	public IAdhocTicket make(String carparkId, int ticketNo) {
		// I had to add another overloaded method to the AdhocTicket class to make this work.
            AdhocTicket newTicket = new AdhocTicket(carparkId,ticketNo);
            
            return newTicket;
	}


}
