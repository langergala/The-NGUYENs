package mockdemo;

import java.time.LocalDate;
import java.util.Date;


public class Loan implements ILoan {
	
	IBook _book;
	IMember _member;
	LocalDate _borrowDate;
	LocalDate _dueDate;
	LocalDate _returnDate;
	LocalDate _completeDate;
	ILoan.State _state;
	int _id;
	
	
	public Loan( IBook book, IMember member, LocalDate borrowDate, LocalDate dueDate) {
		if ( book == null ||
			 member == null ||	
			 borrowDate == null ||	
			 dueDate == null ) {
			throw new IllegalArgumentException("Parameters cannot be null");
		}
		if (dueDate.isBefore(borrowDate)) {
				throw new IllegalArgumentException("Due date cannot be less than borrow date");
			}
			
		_book = book;
		_member = member;
		_borrowDate = borrowDate;
		_dueDate = dueDate;
		_id = 0;
		_state = ILoan.State.PENDING;
	}


	@Override
	public int getId() {
		return _id;
	}


	@Override
	public IMember getMember() {
		return _member;
	}


	@Override
	public IBook getBook() {
		return _book;
	}


	@Override
	public LocalDate getBorrowDate() {
		return _borrowDate;
	}


	@Override
	public LocalDate getDueDate() {
		return _dueDate;
	}


	@Override
	public LocalDate getReturnDate() {
		return _returnDate;
	}


	@Override
	public LocalDate getCompletedDate() {
		return _completeDate;
	}


	@Override
	public void commit(int loanId) {
		if (loanId <= 0) {
			throw new RuntimeException("LoanId must be positive integer");
		}
		switch (_state) {
		case PENDING:
			_id = loanId;
			_book.borrow(this);
			_member.addLoan(this);
			_state = State.CURRENT;	
			break;
		default:
			throw new RuntimeException("Can only commit in PENDING state");			
		}		
	}


	@Override
	public void update(LocalDate currentDate) {
		if (currentDate == null) {
			throw new RuntimeException("Return date cannot be null");
		}
		switch (_state) {
		case CURRENT: 
			if (currentDate.isAfter(_dueDate)) {
				_state = State.OVERDUE;
			}
			break;
			
		case	 OVERDUE:
			break;
			
		default:
			throw new RuntimeException("Can only update CURRENT or OVERDUE loans");
		}
	}


	@Override
	public void discharge(LocalDate returnDate, boolean damaged) {
		if (returnDate == null) {
			throw new RuntimeException("Return date cannot be null");
		}
		if (returnDate.isBefore(_borrowDate)) {
			throw new RuntimeException("Return date cannot be before borrow date");
		}
		switch (_state) {
		case CURRENT: 
			_book.returnBook(damaged);
			if (!returnDate.isAfter(_dueDate) && !damaged) {
				_state = State.COMPLETED;
			}
			else {
				_state = State.FINE_OWING;
			}
			break;
			
		case OVERDUE:
			_book.returnBook(damaged);
			_state = State.FINE_OWING;
			break;
			
		default:
			throw new RuntimeException("Can only discharge CURRENT or OVERDUE loans");
		}
	}


	@Override
	public void payFine() {
		switch (_state) {
		case FINE_OWING: 
			_state = State.COMPLETED;
			break;
			
		default:
			throw new RuntimeException("Can only pay on loans which owe fines");
		}
	}


	@Override
	public boolean isCurrent() {
		return _state == State.CURRENT;
	}


	@Override
	public boolean isOverDue() {
		return _state == State.OVERDUE;
	}


	@Override
	public boolean hasFineOwing() {
		return _state == State.FINE_OWING;
	}


	@Override
	public boolean isComplete() {
		return _state == State.COMPLETED;
	}


	@Override
	public State getState() {
		return _state;
	}

	
	
}
