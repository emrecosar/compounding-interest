package com.emrecosar.model;

import java.math.BigDecimal;

public class Lender implements Comparable<Lender> {
	private String name;
	private BigDecimal rate;
	private BigDecimal amount;

	public Lender(String name, BigDecimal rate, BigDecimal amount) {
		this.name = name;
		this.rate = rate;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public int compareTo(Lender lender) {
		return getRate().compareTo(lender.getRate());
	}
}