package com.emrecosar.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.emrecosar.calculation.CalculationService;
import com.emrecosar.file.LenderService;
import com.emrecosar.manager.ManagerImpl;
import com.emrecosar.model.Lender;
import com.emrecosar.model.Quote;

@SuppressWarnings("deprecation")
@RunWith(SpringRunner.class)
public class ManagerImplTest{

	@Mock
	LenderService lenderService;

	@Mock
	CalculationService calculationService;

	ManagerImpl manager;
	
	@Before
	public void setup() {
		manager = new ManagerImpl(lenderService, calculationService);
	}

	@Test
	public void givenFileAndLoanAmount_whenNotValidFile_thenReturnNull() throws IOException {

		String filePath = "market_data.csvs";
		BigDecimal loanAmount = new BigDecimal(10000);
		
		Quote expected = null; 
		
		Quote actual = manager.run(filePath, loanAmount);
		
		assertThat(actual).isNull();
		assertEquals(expected, actual);
	}
	
	@Test
	public void givenFileAndLoanAmount_whenValidFile_thenReturnQuote() throws IOException, URISyntaxException {

		String filePath = "market_data.csv";
		BigDecimal loanAmount = new BigDecimal(10000);
		
		List<Lender> lenders = new ArrayList<Lender>();
		Lender lender1 = new Lender("Jane", new BigDecimal(0.069), new BigDecimal(480));
		Lender lender2 = new Lender("Fred", new BigDecimal(0.071), new BigDecimal(520));
		lenders.add(lender1);
		lenders.add(lender2);
		
		Quote expected = new Quote(loanAmount, new BigDecimal(7.0), new BigDecimal(30.78), new BigDecimal(1108.10)); 
		
		when(lenderService.readFile(filePath)).thenReturn(lenders);
		when(lenderService.selectLenders(Matchers.any(), Matchers.any())).thenReturn(lenders);
		
		when(calculationService.calculateAmountFromLenders(Matchers.any(), Matchers.any())).thenReturn(new BigDecimal(1070.04));
		when(calculationService.calculateRate(Matchers.any(), Matchers.any())).thenReturn(new BigDecimal(7.0));
		when(calculationService.calculateRateInDecimal(Matchers.any())).thenReturn(new BigDecimal(0.070));
		when(calculationService.calculateTotalRepayment(Matchers.any(), Matchers.any())).thenReturn(new BigDecimal(1108.10));
		when(calculationService.calculateMonthlyRepayment(Matchers.any())).thenReturn(new BigDecimal(30.78));
		
		Quote actual = manager.run(filePath, loanAmount);
		
		assertThat(actual).isNotNull();
		assertEquals(expected.getLoanAmount(), actual.getLoanAmount());
		assertEquals(expected.getMonthlyRepayment(), actual.getMonthlyRepayment());
		assertEquals(expected.getTotalRepayment(), actual.getTotalRepayment());
		assertEquals(expected.getRate(), actual.getRate());
	}
	
}
