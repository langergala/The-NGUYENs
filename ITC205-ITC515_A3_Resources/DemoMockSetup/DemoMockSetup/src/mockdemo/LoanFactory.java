package mockdemo;

import java.time.LocalDate;

public class LoanFactory implements ILoanFactory {

	@Override
	public ILoan makeLoan(IBook book, IMember member, LocalDate borrowDate, LocalDate dueDate) {
		return new Loan(book, member, borrowDate, dueDate);
	}

}
