package com.emrecosar;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.emrecosar.manager.Manager;
import com.emrecosar.model.Quote;

@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {

	@Autowired
	Manager manager;
	
	@Value("${loan.step}")
	private BigDecimal loanStep; 

	@Value("${loan.minimum}")
	private BigDecimal loanMinimum;
	
	@Value("${loan.maximum}")
	private BigDecimal loanMaximum;
	
	@Value("${loan.amount-not-valid}")
	private String amountNotValidMessage;
	
	@Override
    public void run(String...args) throws Exception {
        
		if(args.length < 2) { 
			System.err.println("Application needs 2 parameters to run properly! [args : " + Arrays.toString(args) + "]");
			return;
		}
        
		String filePath = null;
		BigDecimal loanAmount = null;
		try {
			filePath = args[0];
			loanAmount = new BigDecimal(args[1]);
		} catch (NumberFormatException nfe) {
			System.err.println("Second parameter should be BigDecimal! [args : " + Arrays.toString(args) + "]");
			return;
		}
        
        if (loanAmount.compareTo(loanMinimum) < 0 || loanAmount.compareTo(loanMaximum) > 0 
        		|| loanAmount.remainder(loanStep).compareTo(BigDecimal.ZERO) != 0	) {
            System.err.println(amountNotValidMessage);
            return;
        }
        
        Quote quote = manager.run(filePath, loanAmount);
        if(quote != null)
        		System.out.println(quote.toString());
        
        return;
    }
}