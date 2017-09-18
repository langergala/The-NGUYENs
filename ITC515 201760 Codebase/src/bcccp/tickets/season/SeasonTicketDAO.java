package bcccp.tickets.season;

import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.IUsageRecordFactory;
import java.util.ArrayList;

public class SeasonTicketDAO implements ISeasonTicketDAO {

	private IUsageRecordFactory factory;
  private ISeasonTicket seasonTicket;
	private ArrayList<ISeasonTicket> registeredTicketsList = new ArrayList <ISeasonTicket>();
	
	public SeasonTicketDAO(IUsageRecordFactory factory) {
            this.factory = factory;
	}



	@Override
	public void registerTicket(ISeasonTicket ticket) {

            registeredTicketsList.add(ticket);

	}



	@Override
	public void deregisterTicket(ISeasonTicket ticket) {
            for (ISeasonTicket st : registeredTicketsList) {
                if (st.getId().equals(ticket.getId())) {
                    registeredTicketsList.remove(registeredTicketsList.indexOf(st));
                }
            }
	}



	@Override
	public int getNumberOfTickets() {
            
		return registeredTicketsList.size();
	}



	@Override
	public ISeasonTicket findTicketById(String ticketId) {
            for (ISeasonTicket st : registeredTicketsList) {
                if (st.getId().equals(ticketId)) {
                    return st;
                }
            }
            return null;
	}



	@Override
	public void recordTicketEntry(String ticketId) {

            factory.make(ticketId, System.currentTimeMillis());
  }	




	@Override
	public void recordTicketExit(String ticketId) {

            seasonTicket = findTicketById(ticketId);
            seasonTicket.endUsage(System.currentTimeMillis());
            registeredTicketsList.remove(findTicketById(ticketId));

	}
        
}
