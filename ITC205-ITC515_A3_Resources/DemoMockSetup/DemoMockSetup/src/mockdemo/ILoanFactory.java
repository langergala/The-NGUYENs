package mockdemo;

import java.time.LocalDate;

public interface ILoanFactory {
	
	public ILoan makeLoan(IBook book, IMember borrower, LocalDate borrowDate, LocalDate dueDate);

}
