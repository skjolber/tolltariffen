package com.github.skjolber.tolltariffen;

import static com.google.common.truth.Truth.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.skjolber.tolltariffen.generator.TollTariffenConverter;
import com.github.skjolber.tolltariffen.generator.TollTariffenConverterBuilder;
import com.github.skjolber.tolltariffen.generator.ToolTariffen;

public class TollTariffenConverterTest {

	private ObjectMapper mapper = new ObjectMapper();

	private Map<String, Object> parse(byte[] content) throws Exception {
		return parse(new ByteArrayInputStream(content));
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> parse(InputStream in) throws Exception {
		return  mapper.readValue(in, Map.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testParser() throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		
		TollTariffenConverter converter = TollTariffenConverterBuilder.newBuilder().withLanguageColumn(3).withSkipRows(6).build();
		
		InputStream in = getClass().getResourceAsStream("/tolltariffen2019_excel.xls");
		
		converter.convert(in, bout);
		
		Map<String, Object> map = parse(bout.toByteArray());
		
		assertThat(map.get("year")).isEqualTo("2019");
		
		List<Object> codes = (List<Object>) map.get("codes");
		
		assertThat(codes.size()).isAtLeast(100);
		
		// get them asses
		ToolTariffen toolTariffen = new ToolTariffen(map);
		String search = toolTariffen.searchValue("01.01.3000");
		assertThat(search).isEqualTo("Asses");
		
		List<Map<String, Object>> searchTree = toolTariffen.searchTree("01.01.3000");
		
		assertThat(searchTree.size()).isEqualTo(1);
	}
	
}
