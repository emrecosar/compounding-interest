package com.emrecosar.calculation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.emrecosar.model.Lender;

@Service
public class CalculationServiceImpl implements CalculationService {

	@Value("${installement.count-to-pay}")
	private int installmentCountToPay; 
	
	@Value("${installement.per-year}")
	private int installmentPerYear; 
	
	@Value("${installement.year}")
	private int installmentYear; 
	
	private int rateScale = 1;
	private int rateInDecimalScale = 3;
	private int amountScale = 2;
	private int rateInDecimal10Scale = 10; // not to lose value when divide operation 
	
	private BigDecimal oneHundred = new BigDecimal(100);
	
	@Value("${interest-constant}")
	private String insterestConstant; 

	@Autowired
	public CalculationServiceImpl() {
	}
	
	@Override
	public BigDecimal getMaximumLoanAmount(List<Lender> lenders) {
		return lenders.stream()
		        .map(lender -> lender.getAmount())
		        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

	@Override
	public BigDecimal calculateMonthlyRepayment(BigDecimal totalRepayment) {
		return totalRepayment.divide(new BigDecimal(installmentCountToPay), amountScale, RoundingMode.HALF_UP);
	}

	@Override
    public BigDecimal calculateTotalRepayment(BigDecimal rateInDecimal, BigDecimal loanAmount) {
        
		BigDecimal rateInDecimalPerMonth = rateInDecimal.divide(new BigDecimal(installmentPerYear), rateInDecimal10Scale, RoundingMode.HALF_UP);
		rateInDecimalPerMonth = rateInDecimalPerMonth.add(new BigDecimal(insterestConstant));
        BigDecimal zopaTotalLoanAmount = rateInDecimalPerMonth.pow(installmentPerYear * installmentYear);
        return loanAmount.multiply(zopaTotalLoanAmount).setScale(amountScale, BigDecimal.ROUND_HALF_UP);
    }

	@Override
	public BigDecimal calculateAmountFromLenders(BigDecimal loanAmount, List<Lender> selectedLenderList) {
		
		BigDecimal totalAmountFromLenders = BigDecimal.ZERO;
		BigDecimal amountFromLenders = BigDecimal.ZERO;
		BigDecimal borrowAmountFromLenders = BigDecimal.ZERO;
		
		for(Lender lender : selectedLenderList) {
			BigDecimal amountToBorrow = loanAmount.compareTo(borrowAmountFromLenders.add(lender.getAmount())) < 0
					? loanAmount.subtract(borrowAmountFromLenders)
					: lender.getAmount();
			amountFromLenders = amountToBorrow.add(amountToBorrow.multiply(lender.getRate()));
			totalAmountFromLenders = totalAmountFromLenders.add(amountFromLenders);
			borrowAmountFromLenders = borrowAmountFromLenders.add(amountToBorrow);
		}
		
		return totalAmountFromLenders.setScale(amountScale, RoundingMode.HALF_UP);
	}

	@Override
	public BigDecimal calculateRate(BigDecimal amountFromLenders, BigDecimal loanAmount) {
		return amountFromLenders.subtract(loanAmount).divide(loanAmount).multiply(oneHundred).setScale(rateScale, RoundingMode.HALF_UP);
	}

	@Override
	public BigDecimal calculateRateInDecimal(BigDecimal rate) {
		return rate.divide(oneHundred, rateInDecimalScale, RoundingMode.HALF_UP);
	}
}
