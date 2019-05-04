package com.github.skjolber.tolltariffen.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TollTariffenBuilder {
	
	private InputStream in;
	private ObjectMapper mapper = new ObjectMapper();
	private Set<String> filter = new HashSet<>();

	public static TollTariffenBuilder newBuilder() {
		return new TollTariffenBuilder();
	}

	/**
	 * Top level codes only
	 * 
	 * @param codes code to include
	 * @return this builder
	 */

	public TollTariffenBuilder withCodes(String ... codes) {
		for(String code : codes) {
			filter.add(code);
		}
		return this;
	}
	
	/**
	 * Top level codes only
	 * 
	 * @param codes code to include
	 * @return this builder
	 */
	
	public TollTariffenBuilder withCodes(Set<String> codes) {
		filter.addAll(codes);
		return this;
	}


	public TollTariffenBuilder withInput(InputStream in) {
		this.in = in;
		
		return this;
	}

	public TollTariffenBuilder withInput(File in) throws FileNotFoundException {
		this.in = new FileInputStream(in);
		
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public ToolTariffen build() throws Exception {
		if(filter.isEmpty()) {
			return new ToolTariffen(mapper.readValue(in, Map.class));
		} else {
			JsonFactory jfactory = new JsonFactory();
			JsonParser jParser = jfactory.createParser(in);
			
			Map<String, Object> map = new HashMap<>(filter.size() * 2);;
			
			if(jParser.nextToken() != JsonToken.START_OBJECT) {
				throw new IllegalArgumentException();
			}
			
			int level = 1;
			JsonToken token;
			do {
				token = jParser.nextToken();
				if(token == JsonToken.END_OBJECT) {
					level--;
				} else if(token == JsonToken.FIELD_NAME) {
				    String fieldName = jParser.getCurrentName();
			    	token = jParser.nextToken();
			    	if(token == JsonToken.START_ARRAY) {
				    	List<Map<String, Object>> list = readFilter(jParser);
				    	if(!list.isEmpty()) {
					    	map.put(fieldName, list);
				    	}
			    	} else if(token == JsonToken.VALUE_STRING) {
			    		if(fieldName.equals("year") || filter.contains(fieldName))
			    		map.put(fieldName, jParser.getText());
				    } else {
				    	throw new IllegalArgumentException(token.toString());
				    }
				}
			} while(level > 0);
			
			return new ToolTariffen(map);
		}
	}

	private List<Map<String, Object>> readFilter(JsonParser jParser) throws IOException {
		List<Map<String, Object>> l = new ArrayList<Map<String,Object>>();
		int level = 1;
		JsonToken token;
		do {
			token = jParser.nextToken();
			if(token == JsonToken.END_ARRAY) {
				level--;
			} else {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = mapper.readValue(jParser, Map.class);
				String code = (String) map.get("code");
				if(filter.contains(code)) {
					l.add(map);
				}
			}
		} while(level > 0);
		return l;
	}
}
 