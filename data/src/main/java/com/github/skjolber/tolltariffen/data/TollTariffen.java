package com.github.skjolber.tolltariffen.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TollTariffen {
	
	private Map<String, Object> root;

	public TollTariffen(Map<String, Object> map) {
		this.root = map;
	}
	
	public String getYear() {
		return (String) root.get("year");
	}

	protected String getValue(Map<String, Object> map, String code) {
		String value = (String) map.get(code);
		if(value != null) {
			return value;
		}
		
		value = (String) map.get("code");
		if(value != null && value.equals(code)) {
			return (String) map.get("desc");
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, Object> search(Map<String, Object> map, String code) {
		String mapCode = (String)map.get("code");
		if(mapCode != null && !code.startsWith(mapCode)) {
			return null;
		}

		// get field-value type mappings, or code + desc
		String value = getValue(map, code);
		if(value != null) {
			return map;
		}

		// get mapping in child codes list
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
	
	public String searchValue(String code) {
		return searchValue(root, code);
	}

	protected String searchValue(Map<String, Object> map, String code) {
		Map<String, Object> value = search(map, code);
		if(value != null) {
			return getValue(value, code);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> search(List<Map<String, Object>> list, Map<String, Object> map, String code) {
		String mapCode = (String)map.get("code");
		if(mapCode != null && !code.startsWith(mapCode)) {
			return null;
		}

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

	public int size() {
		return root.size();
	}
	
	public Map<String, Object> getData() {
		return root;
	}
	
	public Set<String> getAllCodes() {
		Set<String> set = new HashSet<>();
		populate(set, root);
		return set;
	}

	private void populate(Set<String> set, Map<String, Object> map) {
		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			switch(key) {
				case "codes": {
					List<Map<String, Object>> subcodes = (List<Map<String, Object>>) entry.getValue();
					for (Map<String, Object> submap : subcodes) {
						populate(set, submap);
					}
					break;
				}
				case "code": {
					set.add((String)entry.getValue());
					break;
				}
				case "desc": {
					break;
				}
				default: {
					set.add(key);
				}
			}
		}
	}

	public Map<String, String> getAllCodesAndDescriptions() {
		Map<String, String> map = new HashMap<>();
		populate(map, root);
		return map;
	}

	private void populate(Map<String, String> set, Map<String, Object> map) {
		String code = null;
		String desc = null;
		
		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			switch(key) {
				case "codes": {
					List<Map<String, Object>> subcodes = (List<Map<String, Object>>) entry.getValue();
					for (Map<String, Object> submap : subcodes) {
						populate(set, submap);
					}
					break;
				}
				case "code": {
					code = (String)entry.getValue();
					break;
				}
				case "desc": {
					desc = (String)entry.getValue();
					break;
				}
				default: {
					set.put(key, (String)entry.getValue());
				}
			}
		}
		if(code != null && desc != null) {
			set.put(code, desc);
		}
	}
	
}
