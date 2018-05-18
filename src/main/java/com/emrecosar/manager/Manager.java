package com.emrecosar.manager;

import java.io.IOException;
import java.math.BigDecimal;

import com.emrecosar.model.Quote;

public interface Manager {

	Quote run(String filePath, BigDecimal loanAmount) throws IOException;

}
