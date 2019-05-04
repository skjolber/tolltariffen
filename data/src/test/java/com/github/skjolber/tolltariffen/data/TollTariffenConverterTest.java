package com.github.skjolber.tolltariffen.data;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class TollTariffenConverterTest {

	@Test
	public void testParser() throws Exception {
		ToolTariffen tollTariffen = TollTariffenBuilder.newBuilder().withInput(getClass().getResourceAsStream("/tolltariffen/2019/tolltariffen-no.json")).build();
		
		assertThat(tollTariffen.getYear()).isEqualTo("2019");
		
		assertThat(tollTariffen.size()).isAtLeast(100);
		
		String search = tollTariffen.searchValue("01.01.3000");
		assertThat(search).isEqualTo("Esler");
		
		List<Map<String, Object>> searchTree = tollTariffen.searchTree("01.01.3000");
		
		assertThat(searchTree.size()).isEqualTo(1);
	}
	
	@Test
	public void testFilter() throws Exception {
		ToolTariffen tollTariffen = TollTariffenBuilder.newBuilder().withCodes("01.01").withInput(getClass().getResourceAsStream("/tolltariffen/2019/tolltariffen-no.json")).build();
		
		assertThat(tollTariffen.getYear()).isEqualTo("2019");
		
		assertThat(tollTariffen.size()).isEqualTo(2); // year counts as one
		
		String search = tollTariffen.searchValue("01.01.3000");
		assertThat(search).isEqualTo("Esler");
		
		List<Map<String, Object>> searchTree = tollTariffen.searchTree("01.01.3000");
		
		assertThat(searchTree.size()).isEqualTo(1);
	}
	
}
