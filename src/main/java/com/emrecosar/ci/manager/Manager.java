package com.emrecosar.ci.manager;

import java.io.IOException;
import java.math.BigDecimal;

import com.emrecosar.ci.exception.InsufficientLoanAmountException;
import com.emrecosar.ci.model.Quote;

public interface Manager {

	Quote run(String filePath, BigDecimal loanAmount) throws IOException, InsufficientLoanAmountException;

}
