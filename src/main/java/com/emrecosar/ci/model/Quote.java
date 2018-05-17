package com.emrecosar.ci.model;

import java.math.BigDecimal;

public class Quote {

	private BigDecimal loanAmount;
	private BigDecimal rate;
	private BigDecimal monthlyRepayment;
	private BigDecimal totalRepayment;

	public Quote(BigDecimal loanAmount, BigDecimal rate, BigDecimal monthlyRepayment, BigDecimal totalRepayment) {
		this.loanAmount = loanAmount;
		this.rate = rate;
		this.monthlyRepayment = monthlyRepayment;
		this.totalRepayment = totalRepayment;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getMonthlyRepayment() {
		return monthlyRepayment;
	}

	public void setMonthlyRepayment(BigDecimal monthlyRepayment) {
		this.monthlyRepayment = monthlyRepayment;
	}

	public BigDecimal getTotalRepayment() {
		return totalRepayment;
	}

	public void setTotalRepayment(BigDecimal totalRepayment) {
		this.totalRepayment = totalRepayment;
	}

	@Override
	public String toString() {
		return "Requested Amount: £" + loanAmount + "\n" + "Rate:" + rate + "% \n" + "Monthly Repayments: £"
				+ monthlyRepayment + " \n" + "Total Repayment: £" + totalRepayment;
	}
}
