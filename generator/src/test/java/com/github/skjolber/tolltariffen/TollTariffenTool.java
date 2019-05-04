package com.github.skjolber.tolltariffen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.skjolber.tolltariffen.generator.TollTariffenConverter;
import com.github.skjolber.tolltariffen.generator.TollTariffenConverterBuilder;

/**
 * This generates the data files, manually.
 *
 */

public class TollTariffenTool {

	private static final String template1 = "tolltariffen-%1$s.json";
	private static final String template2 = "tolltariffen-%1$s-%2$s.json";
	private static final String outputDirectory = "../data/src/main/resources/tolltariffen/2019";
	
	public static final void main(String[] args) throws Exception {
		int skipRows = 6;
		Map<String, Integer> languageColumns = new HashMap<String, Integer>();
		languageColumns.put("no", 4);
		languageColumns.put("en", 3);
		
		int[] levels = {1, Integer.MAX_VALUE};

		for(int level : levels) {
			for (Entry<String, Integer> entry : languageColumns.entrySet()) {
				TollTariffenConverter converter = TollTariffenConverterBuilder.newBuilder().withLanguageColumn(entry.getValue()).withSkipRows(skipRows).withLevels(level).build();

				String fileName;
				if(level != Integer.MAX_VALUE) {
					fileName = String.format(template2, entry.getKey(), Integer.toString(level));
				} else {
					fileName = String.format(template1, entry.getKey());
				}

				InputStream in = TollTariffenTool.class.getResourceAsStream("/tolltariffen2019_excel.xls");
				File file = new File(outputDirectory, fileName);
				FileOutputStream fout = new FileOutputStream(file);
				try {
					converter.convert(in, fout);
				} finally {
					fout.close();
				}
				System.out.println("Wrote " + file + " size " + file.length()/1024 + "KB");

			}
		}
		

		
	}
			
}
