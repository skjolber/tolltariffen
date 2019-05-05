package com.github.skjolber.tolltariffen.data;

import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.CoreMatchers.describedAs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TollTariffenConverterTest {

	@Test
	public void testParser() throws Exception {
		TollTariffen tollTariffen = TollTariffenBuilder.newBuilder().withInput(getClass().getResourceAsStream("/tolltariffen/2019/tolltariffen-no.json")).build();
		
		assertThat(tollTariffen.getYear()).isEqualTo("2019");
		
		assertThat(tollTariffen.size()).isAtLeast(100);
		
		String search = tollTariffen.searchValue("01.01.3000");
		assertThat(search).isEqualTo("Esler");
		
		List<Map<String, Object>> searchTree = tollTariffen.searchTree("01.01.3000");
		
		assertThat(searchTree.size()).isEqualTo(1);
	}
	
	@Test
	public void testFilter() throws Exception {
		TollTariffen tollTariffen = TollTariffenBuilder.newBuilder().withCodes("01.01").withInput(getClass().getResourceAsStream("/tolltariffen/2019/tolltariffen-no.json")).build();
		
		assertThat(tollTariffen.getYear()).isEqualTo("2019");
		
		assertThat(tollTariffen.size()).isEqualTo(2); // year counts as one
		
		String search = tollTariffen.searchValue("01.01.3000");
		assertThat(search).isEqualTo("Esler");
		
		List<Map<String, Object>> searchTree = tollTariffen.searchTree("01.01.3000");
		
		assertThat(searchTree.size()).isEqualTo(1);
	}
	
	@Test
	public void testSerialization() throws Exception {
		TollTariffen tollTariffen = TollTariffenBuilder.newBuilder().withCodes("01.01").withInput(getClass().getResourceAsStream("/tolltariffen/2019/tolltariffen-no.json")).build();
		String search = tollTariffen.searchValue("01.01.3000");
		assertThat(search).isEqualTo("Esler");

	    ObjectMapper objectMapper = new ObjectMapper();

	    StringWriter writer = new StringWriter();
	    
	    objectMapper.writeValue(writer, tollTariffen.getData()); // not elegant but working
	    
	    TollTariffen tollTariffen2 = TollTariffenBuilder.newBuilder().withCodes("01.01").withInput(new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8))).build();
		search = tollTariffen2.searchValue("01.01.3000");
		assertThat(search).isEqualTo("Esler");
	    
	}
	
}
