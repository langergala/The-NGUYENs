package test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.time.LocalDate;
import java.time.Month;

import mockdemo.*;



@RunWith(MockitoJUnitRunner.class)
public class TestLoan3 {
	
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	ILoan sut;
	
	@Mock IMember member;
	@Mock IBook book;
	LocalDate borrowDate;
	LocalDate dueDate;
	LocalDate returnDate;
	
	
	@Before
	public void setUp() throws Exception {
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
		assertEquals(sut.getBorrowDate(), borrowDate);
		assertEquals(sut.getDueDate(), dueDate);
	}


	
	@Test
	public void testConstructorWithNullBook() {
		exception.expect(RuntimeException.class);
		exception.expectMessage("Parameters cannot be null");
		
		sut = new Loan(null, member, borrowDate, dueDate);
		
		fail("Should bave thrown exception");
	}


	
	@Test
	public void testConstructorWithDueDateLessThanBorrowDate() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("Due date cannot be less than borrow date");

		sut = new Loan(book, member, dueDate, borrowDate);		

		fail("Should bave thrown exception");
	}

	
	
	@Test
	public void testCommit() {
		sut.commit(1);
		
		assertEquals(ILoan.State.CURRENT,sut.getState());
	}

	
	
	@Test
	public void testCommitNotPending() {
		//setup
		exception.expect(RuntimeException.class);
		exception.expectMessage("Can only commit in PENDING state");
		sut.commit(1);
		//execute
		sut.commit(2);
		//verifies and asserts
		assertEquals(ILoan.State.CURRENT,sut.getState());
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
