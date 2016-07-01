package com.somnus.pay.payment.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestParameterUtil {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(RequestParameterUtil.class);
	
	/**
	 * 获取B5M APP WAP 等 UA
	 * @param UA
	 * @param key
	 * @return
	 */
	public final static String getValueFromUA(String UA, String key) {
        try{
            String[] head = UA.split("/");
            if (head.length >= 1 && head != null) {
                String keyword = "";
                for (int i = 0; i < head.length; i++) {
                    int count = head[i].lastIndexOf(" ");
                    if (count > 0) {
                        String oKeyword = head[i]
                                .substring(count, head[i].length());
                        if (key.equals(keyword))
                            return head[i].replace(oKeyword, "");
                        keyword = oKeyword;
                    } else {
                        keyword = head[i];
                    }
                }
                String[] splits = head[head.length - 1].split("[&]");
                if (splits.length >= 1 && splits != null) {
                    for (int i = 0, total = splits.length; i < total; i++) {
                        String[] map = splits[i].split("[=]");
                        if (map[0] != null && map[0].equals(key) && map.length > 1) {
                            return map[1];
                        }
                    }
                }
            }
        }catch (Exception e){
        	LOGGER.warn("获取UA信息失败", e);
        }
		return "";
	}
	

	public final static String getParameterForString(HttpServletRequest request, String parameterName, String defaultValue) throws NullPointerException {
		Object parameterValue = RequestParameterUtil.getParameterForObject(
				request, parameterName);
		if (parameterValue == null) {
			return defaultValue;
		}
		return parameterValue.toString();
	}

	public final static Object getParameterForObject(HttpServletRequest request, String parameterName) {
		if (request == null) {
			throw new NullPointerException("HttpServletRequest NULL!");
		}
		Object parameterValue = request.getParameter(parameterName);
		return parameterValue;
	}

	public final static Integer getParameterForInteger(HttpServletRequest request, String parameterName, Integer defaultValue) throws NullPointerException {
		String defValue = "" + defaultValue;
		if (defaultValue == null) {
			defValue = null;
		}
		String parameterValue = RequestParameterUtil.getParameterForString(request, parameterName, defValue);
		try {
			return Integer.parseInt(parameterValue);
		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	public final static Long getParameterForLong(HttpServletRequest request, String parameterName, Long defaultValue) throws NullPointerException {
		String defValue = "" + defaultValue;
		if (defaultValue == null) {
			defValue = null;
		}
		String parameterValue = RequestParameterUtil.getParameterForString(request, parameterName, defValue);
		try {
			return Long.parseLong(parameterValue);
		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	public final static Double getParameterForDouble(HttpServletRequest request, String parameterName, Double defaultValue) throws NullPointerException {
		String defValue = "" + defaultValue;
		if (defaultValue == null) {
			defValue = null;
		}
		String parameterValue = RequestParameterUtil.getParameterForString(
				request, parameterName, defValue);
		try {
			return Double.parseDouble(parameterValue);
		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	public final static Float getParameterForFloat(HttpServletRequest request, String parameterName, Float defaultValue) throws NullPointerException {
		String defValue = "" + defaultValue;
		if (defaultValue == null) {
			defValue = null;
		}
		String parameterValue = RequestParameterUtil.getParameterForString(request, parameterName, defValue);
		try {
			return Float.parseFloat(parameterValue);
		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	public final static Short getParameterForShort(HttpServletRequest request,
			String parameterName, Short defaultValue)
			throws NullPointerException {
		String defValue = "" + defaultValue;
		if (defaultValue == null) {
			defValue = null;
		}
		String parameterValue = RequestParameterUtil.getParameterForString(request, parameterName, defValue);
		try {
			return Short.parseShort(parameterValue);
		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	public final static Byte getParameterForByte(HttpServletRequest request,
			String parameterName, Byte defaultValue)
			throws NullPointerException {
		String defValue = "" + defaultValue;
		if (defaultValue == null) {
			defValue = null;
		}
		String parameterValue = RequestParameterUtil.getParameterForString(
				request, parameterName, defValue);
		try {
			return Byte.parseByte(parameterValue);
		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	public final static Date getParameterForDate(HttpServletRequest request,
			String parameterName, String pattern, Date defaultValue)
			throws NullPointerException {
		if (pattern == null) {
			throw new NullPointerException("pattern NULL!");
		}
		String defValue = "" + defaultValue;
		if (defaultValue == null) {
			defValue = null;
		}
		String parameterValue = RequestParameterUtil.getParameterForString(
				request, parameterName, defValue);
		try {
			return new SimpleDateFormat(pattern).parse(parameterValue);
		} catch (ParseException e) {
		}
		return defaultValue;
	}

	public final static String[] getParameterNames(HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		Enumeration<String> enumeration = request.getParameterNames();
		if (enumeration != null) {
			String[] parameterNames = new String[16];
			int cursor = 0;
			while (enumeration.hasMoreElements()) {
				if (cursor >= parameterNames.length) {
					String[] swap = new String[parameterNames.length
							+ (parameterNames.length / 2)];
					System.arraycopy(parameterNames, 0, swap, 0,
							parameterNames.length);
					parameterNames = swap;
				}
				parameterNames[cursor] = enumeration.nextElement();
				cursor++;
			}
			if (parameterNames.length > cursor) {
				String[] swap = new String[cursor];
				System.arraycopy(parameterNames, 0, swap, 0, cursor);
				parameterNames = swap;
			}
			return parameterNames;
		}
		return null;
	}
	
}
