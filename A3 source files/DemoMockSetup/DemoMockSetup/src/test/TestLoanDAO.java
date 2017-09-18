package test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mockdemo.IBook;
import mockdemo.IDateFactory;
import mockdemo.ILoan;
import mockdemo.ILoanFactory;
import mockdemo.IMember;
import mockdemo.LoanDAO;


public class TestLoanDAO {
	
	LoanDAO sut;
	
	Map<Integer, ILoan> loanMap;
	ILoanFactory loanFactory;
	IDateFactory dateFactory;
	IBook book;
	IMember member;
	ILoan loan;
	

	
	@Before
	public void setUp() throws Exception {
		loanMap = spy(new HashMap<Integer, ILoan>());
		loanFactory = mock(ILoanFactory.class);
		dateFactory = mock(IDateFactory.class);
		book = mock(IBook.class);
		member = mock(IMember.class);
		loan = mock(ILoan.class);
		
		sut = new LoanDAO(loanMap, loanFactory, dateFactory);
	}
	

	
	@After
	public void tearDown() throws Exception {
		sut = null;
	}	


	
	@Test
	public void testCreate() {
		//setup
		when(dateFactory.now()).thenReturn(LocalDate.of(2017, Month.AUGUST, 21));
		when(loanFactory.makeLoan(eq(book), eq(member), any(LocalDate.class), any(LocalDate.class)))
			.thenReturn(loan);
		
		//execute
		ILoan actual = sut.createLoan(member, book);
		
		//verify
		verify(dateFactory).now();
		verify(loanFactory).makeLoan(eq(book), eq(member), any(LocalDate.class), any(LocalDate.class));
		assertEquals(loan, actual);		
	}
	

	
	@Test
	public void testCommit() {
		//setup
		
		//execute
		sut.commitLoan(loan);
		
		//verifies and asserts
		verify(loan).commit(anyInt());
		verify(loanMap).put(anyInt(), eq(loan));
		
		assertTrue(loanMap.size() == 1);
		assertEquals(loan, loanMap.get(1));
	}

}
