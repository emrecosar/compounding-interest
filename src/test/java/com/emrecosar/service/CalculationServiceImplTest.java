package com.emrecosar.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.emrecosar.calculation.CalculationServiceImpl;
import com.emrecosar.model.Lender;

@RunWith(BlockJUnit4ClassRunner.class)
public class CalculationServiceImplTest{

	CalculationServiceImpl calculationService;
	
	@Before
	public void setup() {
		calculationService = new CalculationServiceImpl();
	}

	@Test
	public void givenLenders_whenCalculateMaximum_thenReturnAmount() {
		
		List<Lender> lenders = new ArrayList<Lender>();
		Lender lender1 = new Lender("Jane", new BigDecimal(0.069), new BigDecimal(480));
		Lender lender2 = new Lender("Fred", new BigDecimal(0.071), new BigDecimal(520));
		lenders.add(lender1);
		lenders.add(lender2);
		
		BigDecimal expected = new BigDecimal(1000);
		
		BigDecimal actual = calculationService.getMaximumLoanAmount(lenders);
		
		assertThat(actual).isNotNull();
		assertEquals(actual.intValue(), expected.intValue());
	}
	
	@Test
	public void givenLoanAmountAndLenders_whenCalculateAmountFromLenders_thenReturnAmount() {
		
		BigDecimal loanAmount = new BigDecimal(1000);
		List<Lender> lenders = new ArrayList<Lender>();
		Lender lender1 = new Lender("Jane", new BigDecimal(0.069), new BigDecimal(480));
		Lender lender2 = new Lender("Fred", new BigDecimal(0.071), new BigDecimal(520));
		lenders.add(lender1);
		lenders.add(lender2);
		
		BigDecimal expected = new BigDecimal(1070.04);
		
		BigDecimal actual = calculationService.calculateAmountFromLenders(loanAmount, lenders);
		
		assertThat(actual).isNotNull();
		assertEquals(actual.intValue(), expected.intValue());
	}
	
	@Test
	public void givenLoanAmountAndLenders_whenCalculateAmountFromLenders_thenReturnFractionedAmount() {
		
		BigDecimal loanAmount = new BigDecimal(1050);
		List<Lender> lenders = new ArrayList<Lender>();
		Lender lender1 = new Lender("Jane", new BigDecimal(0.069), new BigDecimal(480));
		Lender lender2 = new Lender("Fred", new BigDecimal(0.071), new BigDecimal(520));
		Lender lender3 = new Lender("Joe", new BigDecimal(0.072), new BigDecimal(1000));
		lenders.add(lender1);
		lenders.add(lender2);
		lenders.add(lender3);
		
		BigDecimal expected = new BigDecimal(1123.64);
		
		BigDecimal actual = calculationService.calculateAmountFromLenders(loanAmount, lenders);
		
		assertThat(actual).isNotNull();
		assertEquals(actual.intValue(), expected.intValue());
	}
	
	@Test
	public void givenTotalRepayment_whenCalculateMonthlyRepayment_thenReturnAmount() {
		
		BigDecimal totalRepayment = new BigDecimal(1108.10);
		
		BigDecimal expected = new BigDecimal(30.78);
		
		ReflectionTestUtils.setField(calculationService, "installmentCountToPay", 36);
		
		BigDecimal actual = calculationService.calculateMonthlyRepayment(totalRepayment);
		
		assertThat(actual).isNotNull();
		assertEquals(actual.intValue(), expected.intValue());
	}
	
	@Test
	public void givenLoanAmountAndLendesAmount_whenCalculateRate_thenReturnRate() {
		
		BigDecimal loanAmount = new BigDecimal(1000);
		BigDecimal amountFromLenders = new BigDecimal(1070.04);
		
		BigDecimal expected = new BigDecimal(7.0);
		
		BigDecimal actual = calculationService.calculateRate(amountFromLenders, loanAmount);
		
		assertThat(actual).isNotNull();
		assertEquals(actual.intValue(), expected.intValue());
	}
	
	@Test
	public void givenRate_whenCalculateRateInDecimal_thenReturnRate() {
		
		BigDecimal rate = new BigDecimal(7.0);
		
		BigDecimal expected = new BigDecimal(0.070);
		
		BigDecimal actual = calculationService.calculateRateInDecimal(rate);
		
		assertThat(actual).isNotNull();
		assertEquals(actual.intValue(), expected.intValue());
	}
	
	@Test
	public void givenLoanAmountAndRate_whenCalculateTotalRepayment_thenReturnAmount() {
		
		BigDecimal rateInDecimal = new BigDecimal(0.070);
		BigDecimal loanAmount = new BigDecimal(1000);
		
		ReflectionTestUtils.setField(calculationService, "installmentPerYear", 12);
		ReflectionTestUtils.setField(calculationService, "insterestConstant", "0.9970221");
		ReflectionTestUtils.setField(calculationService, "installmentYear", 3);
		
		BigDecimal expected = new BigDecimal(1108.10);
		
		BigDecimal actual = calculationService.calculateTotalRepayment(rateInDecimal, loanAmount);
		
		assertThat(actual).isNotNull();
		assertEquals(actual.intValue(), expected.intValue());
	}
	
}
