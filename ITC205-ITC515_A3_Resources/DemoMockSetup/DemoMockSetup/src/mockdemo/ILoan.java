package mockdemo;

import java.time.LocalDate;

public interface ILoan {
	
	public static final int PERIOD = 14;
	public enum State { PENDING, CURRENT, OVERDUE, FINE_OWING, COMPLETED }

	
	public int getId();
	public IMember getMember();	
	public IBook getBook();
	public LocalDate getBorrowDate();
	public LocalDate getDueDate();
	public LocalDate getReturnDate();
	public LocalDate getCompletedDate();
	
	
	public void commit(int loanId);	
	public void update(LocalDate currentDate);
	public void discharge(LocalDate returnDate, boolean damaged);
	public void payFine();
	
	public boolean isCurrent();
	public boolean isOverDue();	
	public boolean hasFineOwing();	
	public boolean isComplete();	
	
	public State getState();
	

}
