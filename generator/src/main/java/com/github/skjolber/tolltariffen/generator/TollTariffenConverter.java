package com.github.skjolber.tolltariffen.generator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class TollTariffenConverter {

	private int skipRows;
	private int levels;
	private int languageColumn;

	public TollTariffenConverter(int skipRows, int levels, int languageColumn) {
		super();
		this.skipRows = skipRows;
		this.levels = levels;
		this.languageColumn = languageColumn;
	}

	public void convert(InputStream in, OutputStream outputStream) throws Exception {
        try (
        		Workbook workbook = WorkbookFactory.create(in);
        		JsonConverter c = new JsonConverter("2019", outputStream, levels);
        		) {
            Sheet sheet = workbook.getSheetAt(0);

            ExcelParser parser = new ColumnNormalizerReader(new EmptyRowExcelParser(new SkipExcelParser(new DefaultExcelParser(sheet), skipRows)), languageColumn);

            while (parser.hasNext()) {
                List<String> row = parser.next();

                c.write(row.get(0).trim() + row.get(1).trim() + row.get(2).trim(), Integer.parseInt(row.get(languageColumn)), row.get(languageColumn + 1));
            }
        }
	}
	
	public void convert(File inputFile, File outputFile) throws Exception {
		try (
				OutputStream out = new FileOutputStream(outputFile);
				InputStream in = new FileInputStream(inputFile);
				) {
			convert(in, out);
		} 

	}

}
