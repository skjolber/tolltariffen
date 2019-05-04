package com.github.skjolber.tolltariffen.generator;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter implements Closeable {

	public static final String CODES = "codes";
	public static final String CODE = "code";
	public static final String DESC = "desc";
	
	private final JsonGenerator jGenerator;
	
	private final Stack<Map<String, Object>> stack = new Stack<>();
	private final String year;
	private final int levels;
	
	public JsonConverter(String year, OutputStream out, int levels) throws IOException {
		super();
		this.year = year;
		this.levels = levels;
		JsonFactory jfactory = new JsonFactory();
		jGenerator = jfactory.createGenerator(out, JsonEncoding.UTF8);
		jGenerator.setCodec(new ObjectMapper());
		jGenerator.useDefaultPrettyPrinter();
		
		init();
	}

	public JsonConverter(String year, JsonGenerator jGenerator, int levels) throws IOException {
		super();
		this.year = year;
		this.jGenerator = jGenerator;
		this.levels = levels;
		
		init();
	}
	
	protected void init() throws IOException {
		Map<String, Object> wrapper = new TreeMap<>();
		wrapper.put("year", year);
		
		stack.add(wrapper);
	}
	
	public void close() throws IOException {
		while(stack.size() > 1) {
			popStack();
		}
		jGenerator.writeObject(stack.get(0));

		jGenerator.close();
	}

	public void write(String code, int nextLevel, String content) throws IOException {
		nextLevel++;
		
		if(nextLevel > levels) {
			return;
		}
		Map<String, Object> wrapper = new TreeMap<>();
		if(code != null && !code.trim().isEmpty()) {
			wrapper.put(CODE, code.trim());
		}
		if(content != null) {
			wrapper.put(DESC, normalizeDescription(content));
		}

		Map<String, Object> parent;
		while(nextLevel < stack.size()) {
			popStack();
		}
		parent = stack.peek();

		List<Map<String, Object>> list = (List<Map<String, Object>>) parent.get(CODES);
		if(list == null) {
			list = new ArrayList<>();
			parent.put(CODES, list);
		}
		list.add(wrapper);
		
		if(nextLevel + 1 >= stack.size()) {
			stack.push(wrapper);
		}
	}

	private String normalizeDescription(String content) {
		content = content.trim();
		if(content.endsWith(":")) {
			content = content.substring(0, content.length() - 1).trim();
		}
		
		if(!content.isEmpty()) {
			if(!Character.isUpperCase(content.charAt(0))) {
				content = Character.toUpperCase(content.charAt(0)) + content.substring(1);
			}
		}
		
		return content;
	}

	private void popStack() {
		Map<String, Object> child = stack.pop();
		Map<String, Object> peek = stack.peek();
		
		if(!child.containsKey(CODES)) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) peek.get(CODES);
			
			if(child.containsKey(CODE) && child.containsKey(DESC) && child.size() == 2) {
				list.remove(child);
				
				peek.put((String)child.get(CODE), child.get(DESC));
			} else if(child.containsKey(DESC) && child.size() == 1) {
				// a few descriptions exist without any children
				list.remove(child);
			}
			if(list.isEmpty()) {
				peek.remove(CODES);
			}
		}
	}

}
