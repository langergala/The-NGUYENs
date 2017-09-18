package test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import mockdemo.IBook;
import mockdemo.IDateFactory;
import mockdemo.ILoan;
import mockdemo.ILoanFactory;
import mockdemo.IMember;
import mockdemo.LoanDAO;


@RunWith(MockitoJUnitRunner.class)
public class TestLoanDAO3 {
	
	@Spy  Map<Integer, ILoan> loanMap = new HashMap<Integer, ILoan>();
	@Mock ILoanFactory loanFactory;
	@Mock IDateFactory dateFactory;
	@Mock IBook book;
	@Mock IMember member;
	@Mock ILoan loan;
	
	
	@InjectMocks LoanDAO sut;


	
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
	}

}
