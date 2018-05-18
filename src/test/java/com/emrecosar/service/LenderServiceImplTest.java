package com.emrecosar.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.emrecosar.calculation.CalculationService;
import com.emrecosar.file.LenderServiceImpl;
import com.emrecosar.model.Lender;

@RunWith(SpringRunner.class)
public class LenderServiceImplTest{

	@Mock
	CalculationService calculationService;

	LenderServiceImpl lenderService;
	
	@Before
	public void setup() {
		lenderService = new LenderServiceImpl(calculationService);
	}

	@Test
	public void givenWrongFilePath_whenReadFile_thenReturnNull() {

		String filePath = "/market_data.csvs";
		List<Lender> expected = null;
		
		List<Lender> actual = lenderService.readFile(filePath);
		
		assertThat(actual).isNull();
		assertEquals(expected, actual);
	}
	
	@Test
	public void givenFilePath_whenReadFile_thenFileInfo() {
		
		String filePath = Thread.currentThread().getClass().getResource("/test_market_data.csv").getPath();
		List<Lender> expected = new ArrayList<Lender>();
		Lender lender1 = new Lender("Jane", new BigDecimal(0.069), new BigDecimal(480));
		Lender lender2 = new Lender("Fred", new BigDecimal(0.071), new BigDecimal(520));
		expected.add(lender1);
		expected.add(lender2);
		
		List<Lender> actual = lenderService.readFile(filePath);
		
		assertThat(actual).isNotNull();
		assertEquals(expected.size(), actual.size());
		assertEquals(expected.get(0).getAmount(), actual.get(0).getAmount());
	}
	
	@Test
	public void givenLendes_whenSelectLenders_thenReturnSelectedLendes() {
		
		List<Lender> lenderList = new ArrayList<Lender>();
		Lender lender1 = new Lender("Jane", new BigDecimal(0.069), new BigDecimal(480));
		Lender lender2 = new Lender("Fred", new BigDecimal(0.071), new BigDecimal(520));
		Lender lender3 = new Lender("Joe", new BigDecimal(0.076), new BigDecimal(100));
		lenderList.add(lender1);
		lenderList.add(lender2);
		lenderList.add(lender3);
		
		BigDecimal loanAmount = new BigDecimal(1000);
		
		List<Lender> expected = new ArrayList<Lender>();
		Lender eLender1 = new Lender("Jane", new BigDecimal(0.069), new BigDecimal(480));
		Lender eLender2 = new Lender("Fred", new BigDecimal(0.071), new BigDecimal(520));
		expected.add(eLender1);
		expected.add(eLender2);
		
		when(calculationService.getMaximumLoanAmount(lenderList)).thenReturn(new BigDecimal(10000));
		
		List<Lender> actual = lenderService.selectLenders(lenderList, loanAmount);
		
		assertThat(actual).isNotNull();
		assertEquals(expected.size(), actual.size());
	}
	
}
