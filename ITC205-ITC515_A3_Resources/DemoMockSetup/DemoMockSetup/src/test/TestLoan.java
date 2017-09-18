package test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import mockdemo.*;


public class TestLoan {	
	
	ILoan sut;
	
	IMember member;
	IBook book;
	LocalDate borrowDate;
	LocalDate dueDate;
	LocalDate returnDate;
	
	
	@Before
	public void setUp() throws Exception {
		member = mock(IMember.class);
		book = mock(IBook.class);
		borrowDate = LocalDate.of(2017, Month.AUGUST, 21);
		dueDate = borrowDate.plusDays(ILoan.PERIOD);
		
		sut = new Loan(book, member, borrowDate, dueDate);
	}

	@After
	public void tearDown() throws Exception {
		sut = null;
	}

	
	@Test
	public void testInit() {
		assertTrue(sut instanceof ILoan);
		assertEquals(sut.getBook(), book);
		assertTrue(sut.getBook() instanceof IBook);
		assertEquals(sut.getBorrowDate(), borrowDate);
		assertEquals(sut.getDueDate(), dueDate);
	}

	
	@Test(expected=RuntimeException.class) 
	public void testConstructorWithNullBook() {
		sut = new Loan(null, member, borrowDate, dueDate);		
		fail("Should bave thrown exception");
	}

	
	@Test(expected=RuntimeException.class) 
	public void testConstructorWithDueDateLessThanBorrowDate() {
		sut = new Loan(null, member, dueDate, borrowDate);		
		fail("Should bave thrown exception");
	}
	
	@Test
	public void testCommit() {
		//setup
		
		//execute
		sut.commit(1);
		
		//verifies and asserts
		verify(book).borrow(sut);
		verify(member).addLoan(sut);
		assertEquals(sut.getState(),ILoan.State.CURRENT);
		assertTrue(1 == sut.getId());
	}

	@Test(expected=RuntimeException.class) 
	public void testCommitNotPending() {
		sut.commit(1);
		sut.commit(2);
		
		assertEquals(sut.getState(),ILoan.State.CURRENT);
	}
	
	@Test
	public void testUpdateCurrentDateLessThanDue() {
		sut.commit(1);
		LocalDate currentDate = borrowDate.plusDays(1);
		
		sut.update(currentDate);	
		
		assertEquals(sut.getState(),ILoan.State.CURRENT);
	}

	@Test
	public void testUpdateCurrentDateEqualDue() {
		sut.commit(1);
		LocalDate currentDate = dueDate;
		
		sut.update(currentDate);
		
		assertEquals(sut.getState(),ILoan.State.CURRENT);
	}

	@Test
	public void testUpdateCurrentDateGreaterThanDue() {
		sut.commit(1);
		LocalDate currentDate = dueDate.plusDays(1);
		
		sut.update(currentDate);
		
		assertEquals(sut.getState(),ILoan.State.OVERDUE);
	}

}
