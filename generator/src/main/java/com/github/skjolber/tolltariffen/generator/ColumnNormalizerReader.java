package com.github.skjolber.tolltariffen.generator;
import java.util.List;

public class ColumnNormalizerReader implements ExcelParser {

	private final int column;
	private final ExcelParser parser;

	public ColumnNormalizerReader(ExcelParser parser, int column) {
		super();
		this.parser = parser;
		this.column = column;
	}

	@Override
	public boolean hasNext() {
		return parser.hasNext();
	}

	@Override
	public List<String> next() {
		List<String> next = parser.next();
		if(next.size() <= column) {
			for(int i = next.size(); i < column + 1; i++) {
				next.add("");
			}
				
		}
		addIndent(next);
		return next;
	}

	private void addIndent(List<String> next) {
		String cellValue = next.get(column);
		
		int start = 0;
		int dashes = 0;
		while(start < cellValue.length()) {
			if(cellValue.charAt(start) == '-') {
				dashes++;
			} else if(!Character.isWhitespace(cellValue.charAt(start))) {
				break;
			}
			start++;
		}

		if(dashes > 0) {
			next.set(column, cellValue.substring(start));
		}
		next.add(column, Integer.toString(dashes));
		
	}

}
