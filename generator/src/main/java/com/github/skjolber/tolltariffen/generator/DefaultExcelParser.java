package com.github.skjolber.tolltariffen.generator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class DefaultExcelParser implements ExcelParser {

    private DataFormatter dataFormatter = new DataFormatter();

	private final Iterator<Row> iterator;
	
	public DefaultExcelParser(Sheet sheet) {
		this(sheet.rowIterator());
	}

	public DefaultExcelParser(Iterator<Row> iterator) {
		super();
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public List<String> next() {
		List<String> columns = new ArrayList<>();

		Row next = iterator.next();
		
		Iterator<Cell> cellIterator = next.cellIterator();
		while(cellIterator.hasNext()) {
			Cell c = cellIterator.next();
			
			// iterator is sparse; does not return empty columns
			while(columns.size() < c.getColumnIndex()) {
				columns.add("");
			}
			columns.add(dataFormatter.formatCellValue(c));
		}
		
		return columns;
	}

}
