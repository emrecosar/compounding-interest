package com.emrecosar.ci.calculation;

import java.math.BigDecimal;
import java.util.List;

import com.emrecosar.ci.model.Lender;

public interface CalculationService {

	BigDecimal getMaximumLoanAmount(List<Lender> lenders);

	BigDecimal calculateMonthlyRepayment(BigDecimal totalRepayment);

	BigDecimal calculateTotalRepayment(BigDecimal rateInDecimal, BigDecimal loanAmount);

	BigDecimal calculateAmountFromLenders(BigDecimal loanAmount, List<Lender> selectedLenderList);

	BigDecimal calculateRate(BigDecimal amountFromLenders, BigDecimal loanAmount);

	BigDecimal calculateRateInDecimal(BigDecimal rate);

}
