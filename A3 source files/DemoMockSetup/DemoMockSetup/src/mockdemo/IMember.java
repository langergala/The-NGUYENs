package mockdemo;

import java.util.List;

public interface IMember {
	
	public static final int   LOAN_LIMIT = 5;	
	public static final float FINE_LIMIT = 10.0f;
	
	public enum State { BORROWING_ALLOWED, BORROWING_DISALLOWED }	

	
	public int     getID();
	public String  getFirstName();	
	public String  getLastName();	
	public String  getContactPhone();	
	public String  getEmailAddress();
		
	public boolean hasOverDueLoans();	
	public boolean hasReachedLoanLimit();	
	public boolean hasFinesPayable();	
	public boolean hasReachedFineLimit();
	
	public void    addFine(float fine);
	public float   getFineAmount();	
	public void    payFine(float payment);
	
	public void        addLoan(ILoan loan);	
	public List<ILoan> getLoans();	
	public void        removeLoan(ILoan loan);
	
	public State   getState();
	
	

}
