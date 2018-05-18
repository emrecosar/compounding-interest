package com.emrecosar.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emrecosar.calculation.CalculationService;
import com.emrecosar.model.Lender;

@Service
public class LenderServiceImpl implements LenderService {

	CalculationService calculationService;

	@Autowired
	public LenderServiceImpl(CalculationService calculationService) {
		this.calculationService = calculationService;
	}

	@Override
	public List<Lender> readFile(String filePath) {

		List<Lender> lenders = null;
		BufferedReader reader = null;
		try {
			File file = new File(filePath);
			InputStream inputStream = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(inputStream));
			lenders = reader.lines().skip(1).map(mapToLender).collect(Collectors.toList());
		} catch (FileNotFoundException fnfe) {
			System.err.println("File is not found or corrupted! [file : " + filePath + "]");
			return null;
		} catch (NumberFormatException e) {
			System.err.println("File is not well-formed! [file : " + filePath + "]");
			return null;
		} catch (Exception e) {
			System.err.println("File operation error occured! [file : " + filePath + "]");
			return null;
		} finally {
			try {
				reader.close();
			} catch (Exception e) {}
		}
		
		return lenders;

	}

	private Function<String, Lender> mapToLender = (String line) -> {
		String[] details = line.split(",");

		String name = details[0];
		BigDecimal rate = new BigDecimal(details[1]).setScale(3, BigDecimal.ROUND_HALF_UP);
		BigDecimal amount = new BigDecimal(details[2]);

		return new Lender(name, rate, amount);
	};

	@Override
	public List<Lender> selectLenders(List<Lender> lenderList, BigDecimal loanAmount) {
		BigDecimal remainingLoan = loanAmount;
		List<Lender> requiredLenders = new ArrayList<>();
		BigDecimal availableAmountToLend = calculationService.getMaximumLoanAmount(lenderList);

		if (loanAmount.compareTo(availableAmountToLend) > 0) {
			// Insufficient Loan Amount
			System.err.println("Currently, It is impossible to provide a loan for this amount! [Amount :" + loanAmount + "]");
			return null;
		}

		Collections.sort(lenderList);

		for (Lender lender : lenderList) {
			if (remainingLoan.compareTo(BigDecimal.ZERO) <= 0) {
				break;
			}
			if (lender.getAmount().compareTo(remainingLoan) > 0) {
				requiredLenders.add(lender);
				remainingLoan = BigDecimal.ZERO;

			} else {
				remainingLoan = remainingLoan.subtract(lender.getAmount());
				requiredLenders.add(lender);
			}
		}
		return requiredLenders;
	}

	@Override
	public void sortLendersByRatingAsc(List<Lender> selectedLenderList) {
		Collections.sort(selectedLenderList);
	}
}
