package com.github.skjolber.tolltariffen.generator;
public class TollTariffenConverterBuilder {

	public static TollTariffenConverterBuilder newBuilder() {
		return new TollTariffenConverterBuilder();
	}
	
	private int skipRows;
	private int levels = Integer.MAX_VALUE;
	private int languageColumn;

	public TollTariffenConverterBuilder withSkipRows(int skipRows) {
		this.skipRows = skipRows;
		return this;
	}

	public TollTariffenConverterBuilder withLevels(int levels) {
		this.levels = levels;
		return this;
	}

	public TollTariffenConverterBuilder withLanguageColumn(int languageColumn) {
		this.languageColumn = languageColumn;
		return this;
	}

	public TollTariffenConverter build() throws Exception {
		return new TollTariffenConverter(skipRows, levels, languageColumn);
	}
	
}
