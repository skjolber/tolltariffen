package com.github.skjolber.tolltariffen.generator;
import java.util.List;

public interface ExcelParser {

	boolean hasNext();
	
	List<String> next();
}
