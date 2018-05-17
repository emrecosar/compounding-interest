package com.emrecosar.ci.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emrecosar.ci.calculation.CalculationService;
import com.emrecosar.ci.exception.InsufficientLoanAmountException;
import com.emrecosar.ci.file.LenderService;
import com.emrecosar.ci.model.Lender;
import com.emrecosar.ci.model.Quote;

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
	public Quote run(String file, BigDecimal loanAmount) throws IOException, InsufficientLoanAmountException {
		
		List<Lender> lenderList = lenderService.readFile(file);
		
		List<Lender> selectedLenderList = lenderService.selectLenders(lenderList, loanAmount);
		lenderService.sortLendersByRatingAsc(selectedLenderList);
		
		BigDecimal amountFromLenders = calculationService.calculateAmountFromLenders(loanAmount, selectedLenderList); 
		BigDecimal rate = calculationService.calculateRate(amountFromLenders, loanAmount);
		BigDecimal rateInDecimal = calculationService.calculateRateInDecimal(rate);
		
		BigDecimal totalRepayment = calculationService.calculateTotalRepayment(rateInDecimal, loanAmount);
        BigDecimal monthlyRepayment = calculationService.calculateMonthlyRepayment(totalRepayment);
        
		return new Quote(loanAmount, rate, monthlyRepayment, totalRepayment);
		
	}

}
