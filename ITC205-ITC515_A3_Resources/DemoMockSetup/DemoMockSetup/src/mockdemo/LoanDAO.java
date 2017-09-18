package mockdemo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoanDAO implements ILoanDAO {
	
	private static int nextLoanId = 1;
	
	private Map<Integer, ILoan> loanMap;
	private ILoanFactory loanFactory;
	private IDateFactory dateFactory;
		
	
	
	public LoanDAO(Map<Integer,ILoan> loanMap, 
		       ILoanFactory loanFactory, 
		       IDateFactory dateFactory) {
	
	if (loanMap == null || loanFactory == null || dateFactory == null) {
		throw new RuntimeException("loanMap, loanFactory, and dateFactory cannot be null");
	}
	this.loanMap = loanMap;
	this.loanFactory = loanFactory;
	this.dateFactory = dateFactory;
}

	
	
	@Override
	public ILoan createLoan(IMember borrower, IBook book) {
		LocalDate borrowDate = dateFactory.now();
		LocalDate dueDate = borrowDate.plusDays(ILoan.PERIOD);
		
		return loanFactory.makeLoan(book, borrower, borrowDate, dueDate);
	}

	
	@Override
	public void commitLoan(ILoan loan) {
		loan.commit(nextLoanId);
		loanMap.put(nextLoanId, loan);
		nextLoanId++;
	}

	
	@Override
	public ILoan getLoanByID(int id) {
		ILoan loan = loanMap.get(id);		
		return loan;
	}

	
	@Override
	public ILoan getLoanByBook(IBook book) {
		for (ILoan loan : loanMap.values()) {
			if (loan.getBook().equals(book)) {
				return loan;
			}
		}
		return null;
	}

	
	@Override
	public List<ILoan> listLoans() {
		return new ArrayList<ILoan>(loanMap.values());
	}

	
	@Override
	public List<ILoan> findLoansByBorrower(IMember member) {
		List<ILoan> list = new ArrayList<>();
		for (ILoan loan : loanMap.values()) {
			if (loan.getMember().equals(member)) {
				list.add(loan);
			}
		}
		return list;
	}

	
	@Override
	public List<ILoan> findLoansByBookTitle(String title) {
		List<ILoan> list = new ArrayList<>();
		for (ILoan loan : loanMap.values()) {
			if (loan.getBook().getTitle().equals(title)) {
				list.add(loan);
			}
		}
		return list;
	}

	
	@Override
	public void updateOverDueStatus(Date currentDate) {
		LocalDate now = dateFactory.now();
		for (ILoan loan : loanMap.values()) {
			if (loan.isCurrent()) {
				loan.update(now);
			}
		}
	}

	
	@Override
	public List<ILoan> findOverDueLoans() {
		List<ILoan> list = new ArrayList<>();
		for (ILoan loan : loanMap.values()) {
			if (loan.isOverDue()) {
				list.add(loan);
			}
		}
		return list;
	}

}
