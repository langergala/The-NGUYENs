package bcccp.tickets.season;

public class UsageRecord implements IUsageRecord {
	
	String ticketId;
	long startDateTime;
	long endDateTime;
	
	
	
	public UsageRecord(String ticketId, long startDateTime) {
		this.ticketId = ticketId;
                this.startDateTime = startDateTime;
                
	}



	@Override
	public void finalise(long endDateTime) {
		// TODO Auto-generated method stub
		this.endDateTime = endDateTime;
	}



	@Override
	public long getStartTime() {
		// TODO Auto-generated method stub
		return this.startDateTime;
	}



	@Override
	public long getEndTime() {
		// TODO Auto-generated method stub
		return this.endDateTime;
	}



	@Override
	public String getSeasonTicketId() {
		// TODO Auto-generated method stub
		return this.ticketId;
	}
	
	
	
}

