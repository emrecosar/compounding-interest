package com.emrecosar.ci;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.emrecosar.ci.exception.InsufficientParameterException;
import com.emrecosar.ci.manager.Manager;
import com.emrecosar.ci.model.Quote;

@Component
public class CICommandLineRunner implements CommandLineRunner {

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
        
		if(args.length < 2) 
			throw new InsufficientParameterException("Application needs 2 parameters to run properly! [args : " + Arrays.toString(args) + "]");
        
        String filePath = args[0];
        BigDecimal loanAmount = new BigDecimal(args[1]);
        
        if (loanAmount.compareTo(loanMinimum) < 0 || loanAmount.compareTo(loanMaximum) > 0 
        		|| loanAmount.remainder(loanStep).compareTo(BigDecimal.ZERO) != 0	) {
            System.err.println(amountNotValidMessage);
            return;
        }
        
        Quote quote = manager.run(filePath, loanAmount);
        System.out.println(quote.toString());
        
        return;
    }
}