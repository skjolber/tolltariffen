package com.github.skjolber.tolltariffen.generator;
import java.util.ArrayList;
import java.util.List;

public class EmptyRowExcelParser implements ExcelParser {

	private final ExcelParser parser;
	
	public EmptyRowExcelParser(ExcelParser parser) {
		this.parser = parser;
	}

	@Override
	public boolean hasNext() {
		return parser.hasNext();
	}

	@Override
	public List<String> next() {
		while(parser.hasNext()) {
			List<String> columns = parser.next();
			
			for (String string : columns) {
				if(!string.trim().isEmpty()) {
					return columns;
				}
			}
		} 
		return new ArrayList<String>();
	}

}
