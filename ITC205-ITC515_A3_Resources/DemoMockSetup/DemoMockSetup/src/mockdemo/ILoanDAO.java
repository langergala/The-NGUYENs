package mockdemo;

import java.util.Date;
import java.util.List;

public interface ILoanDAO {
		
	public ILoan createLoan(IMember borrower, IBook book);

	public void commitLoan(ILoan loan);
	
	public ILoan getLoanByID(int id);
	
	public ILoan getLoanByBook(IBook book);
	
	public List<ILoan> listLoans();
	
	public List<ILoan> findLoansByBorrower(IMember borrower);

	public List<ILoan> findLoansByBookTitle(String title);
	
	public void updateOverDueStatus(Date currentDate);

	public List<ILoan> findOverDueLoans();

}

