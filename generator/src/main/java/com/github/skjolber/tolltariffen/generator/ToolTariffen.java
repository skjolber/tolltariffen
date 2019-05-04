package com.github.skjolber.tolltariffen.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ToolTariffen {
	
	private Map<String, Object> root;

	public ToolTariffen(Map<String, Object> map) {
		this.root = map;
	}

	protected String getValue(Map<String, Object> map, String code) {
		String value = (String) map.get(code);
		if(value != null) {
			return value;
		}
		
		value = (String) map.get("code");
		if(value != null && value.equals(code)) {
			return value;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, Object> search(Map<String, Object> map, String code) {
		String value = getValue(map, code);
		if(value != null) {
			return map;
		}
		
		List<Map<String, Object>> subcodes = (List<Map<String, Object>>) map.get("codes");
		if(subcodes != null) {
			for (Map<String, Object> subcode : subcodes) {
				Map<String, Object> result = search(subcode, code);
				if(result != null) {
					return result;
				}
			}
		}
		
		return null;
	}
	
	protected List<Map<String, Object>> search(String code) {
		return searchTree(root, code);
	}
	
	public String searchValue(String code) throws JsonProcessingException {
		return searchValue(root, code);
	}

	protected String searchValue(Map<String, Object> map, String code) throws JsonProcessingException {
		Map<String, Object> value = search(map, code);
		if(value != null) {
			return getValue(value, code);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> search(List<Map<String, Object>> list, Map<String, Object> map, String code) {
		String value = getValue(map, code);
		if(value != null) {
			list.add(map);
			return map;
		}
		
		List<Map<String, Object>> subcodes = (List<Map<String, Object>>) map.get("codes");
		if(subcodes != null) {
			for (Map<String, Object> subcode : subcodes) {
				Map<String, Object> result = search(subcode, code);
				if(result != null) {
					list.add(map);
					return result;
				}
			}
		}
		
		return null;
	}

	public List<Map<String, Object>> searchTree(String code) {
		return searchTree(root, code);
	}

	protected List<Map<String, Object>> searchTree(Map<String, Object> map, String code) {
		List<Map<String, Object>> list = new ArrayList<>();
		
		if(search(list, map, code) != null) {
			return list;
		}
		
		return null;
	}
	
}
