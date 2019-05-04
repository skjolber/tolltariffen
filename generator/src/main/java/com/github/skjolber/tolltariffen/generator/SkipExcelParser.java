package com.github.skjolber.tolltariffen.generator;
import java.util.List;

public class SkipExcelParser implements ExcelParser {

	private final ExcelParser parser;
	
	public SkipExcelParser(ExcelParser parser, int count) {
		this.parser = parser;
		
		while(parser.hasNext() && count > 0) {
			parser.next();
			count--;
		}
	}

	@Override
	public boolean hasNext() {
		return parser.hasNext();
	}

	@Override
	public List<String> next() {
		return parser.next();
	}

}
