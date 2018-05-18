package com.emrecosar.file;

import java.math.BigDecimal;
import java.util.List;

import com.emrecosar.model.Lender;

public interface LenderService {

	List<Lender> readFile(String filePath);

	List<Lender> selectLenders(List<Lender> lenderList, BigDecimal loanAmount);

	void sortLendersByRatingAsc(List<Lender> selectedLenderList);
	
}
