package mockdemo;

public interface IBook {
	
	public enum State { AVAILABLE, ON_LOAN, LOST, DAMAGED, DISPOSED }

	
	public int getID();
	public String getAuthor();	
	public String getTitle();	
	public String getCallNumber();
	
	public void borrow(ILoan loan);	
	public ILoan getLoan();
	public void returnBook(boolean damaged);
	
	public void lose();	
	public void repair();	
	public void dispose();	
	
	public State getState();
	

}
