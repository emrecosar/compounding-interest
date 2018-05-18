package com.emrecosar.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.emrecosar.calculation.CalculationService;
import com.emrecosar.file.LenderService;
import com.emrecosar.model.Lender;
import com.emrecosar.model.Quote;

@Service
public class ManagerImpl implements Manager {

	LenderService lenderService;

	CalculationService calculationService;

	@Autowired
	public ManagerImpl(LenderService lenderService, CalculationService calculationService) {
		this.lenderService = lenderService;
		this.calculationService = calculationService;
	}

	@Override
	public Quote run(String filePath, BigDecimal loanAmount) throws IOException {
		
		Quote resultQuote = null;
		
		List<Lender> lenderList = lenderService.readFile(filePath);
		
		if(CollectionUtils.isEmpty(lenderList))
			return resultQuote;
		
		List<Lender> selectedLenderList = lenderService.selectLenders(lenderList, loanAmount);
		if(!CollectionUtils.isEmpty(selectedLenderList)) {
			lenderService.sortLendersByRatingAsc(selectedLenderList);
			
			BigDecimal amountFromLenders = calculationService.calculateAmountFromLenders(loanAmount, selectedLenderList); 
			BigDecimal rate = calculationService.calculateRate(amountFromLenders, loanAmount);
			BigDecimal rateInDecimal = calculationService.calculateRateInDecimal(rate);
			
			BigDecimal totalRepayment = calculationService.calculateTotalRepayment(rateInDecimal, loanAmount);
	        BigDecimal monthlyRepayment = calculationService.calculateMonthlyRepayment(totalRepayment);
	        
	        resultQuote = new Quote(loanAmount, rate, monthlyRepayment, totalRepayment);
		}
		
		return resultQuote;
		
	}

}
