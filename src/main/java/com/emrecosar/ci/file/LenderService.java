package com.emrecosar.ci.file;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.emrecosar.ci.exception.InsufficientLoanAmountException;
import com.emrecosar.ci.model.Lender;

public interface LenderService {

	List<Lender> readFile(String filePath) throws IOException;

	List<Lender> selectLenders(List<Lender> lenderList, BigDecimal loanAmount) throws InsufficientLoanAmountException;

	void sortLendersByRatingAsc(List<Lender> selectedLenderList);
	
}
