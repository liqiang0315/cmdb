package com.hp.xo.resourcepool.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.hp.xo.resourcepool.ApiConstants;
import com.hp.xo.resourcepool.exception.CloudRuntimeException;
import com.hp.xo.resourcepool.model.ExceptionProxyObject;
import com.hp.xo.resourcepool.vo.WorkOrderReportVO;
import com.hp.xo.resourcepool.request.BaseRequest;
import com.hp.xo.resourcepool.response.AsyncJobResponse;
import com.hp.xo.resourcepool.response.EntityResponse;
import com.hp.xo.resourcepool.response.ErrorResponse;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.response.ResponseObject;
import com.hp.xo.resourcepool.response.SuccessResponse;

public class ResponseSerializer {
    private static final Logger log = Logger.getLogger(ResponseSerializer.class.getName());

    public static String toSerializedString(ResponseObject result, String responseType) {
        log.trace("===Serializing Response===");
        if (BaseRequest.RESPONSE_TYPE_JSON.equalsIgnoreCase(responseType)) {
            return toJSONSerializedString(result);
        } else {
            return toXMLSerializedString(result);
        }
    }

    private static final Pattern s_unicodeEscapePattern = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");

    public static String unescape(String escaped) {
        String str = escaped;
        Matcher matcher = s_unicodeEscapePattern.matcher(str);
        while (matcher.find()) {
            str = str.replaceAll("\\" + matcher.group(0), Character.toString((char) Integer.parseInt(matcher.group(1), 16)));
        }
        return str;
    }

    @SuppressWarnings("rawtypes")
	public static String toJSONSerializedString(ResponseObject result) {
        if (result != null) {
            Gson gson = ResponseGsonHelper.getBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();

            StringBuilder sb = new StringBuilder();

            sb.append("{ \"").append(result.getResponseName()).append("\" : ");
            if (result instanceof ListResponse) {
                List<? extends ResponseObject> responses = ((ListResponse) result).getResponses();
                Integer count = ((ListResponse) result).getCount();
                boolean nonZeroCount = (count != null && count.longValue() != 0);
                if (nonZeroCount) {
                    sb.append("{ \"").append(ApiConstants.COUNT).append("\":").append(count);
                }

                if ((responses != null) && !responses.isEmpty()) {
                    String jsonStr = gson.toJson(responses.get(0));
                    jsonStr = unescape(jsonStr);

                    if (nonZeroCount) {
                    	WorkOrderReportVO vo=(WorkOrderReportVO) responses.get(0);
                        sb.append(" ,\"").append(vo.getObjectName()).append("\" : [  ").append(jsonStr);
                    }

                    for (int i = 1; i < ((ListResponse) result).getResponses().size(); i++) {
                        jsonStr = gson.toJson(responses.get(i));
                        jsonStr = unescape(jsonStr);
                        sb.append(", ").append(jsonStr);
                    }
                    sb.append(" ] }");
                } else  {
                    if (!nonZeroCount){
                        sb.append("{");
                    }

                    sb.append(" }");
                }
            } else if (result instanceof SuccessResponse) {
                sb.append("{ \"success\" : \"").append(((SuccessResponse) result).getSuccess()).append("\"} ");
            } else if (result instanceof ErrorResponse) {
            	String jsonErrorText = gson.toJson((ErrorResponse) result);
            	jsonErrorText = unescape(jsonErrorText);
            	sb.append(jsonErrorText);
            } else {
                String jsonStr = gson.toJson(result);
                if ((jsonStr != null) && !"".equals(jsonStr)) {
                    jsonStr = unescape(jsonStr);
                    if (result instanceof AsyncJobResponse || result instanceof EntityResponse) {
                        sb.append(jsonStr);
                    } else {
                        sb.append(" { \"").append(result.getObjectName()).append("\" : ").append(jsonStr).append(" } ");
                    }
                } else {
                    sb.append("{ }");
                }
            }
            sb.append(" }");
            return sb.toString();
        }
        return null;
    }

    public static void buildResponseFromJson(String jsonString, ResponseObject ro) {
        Gson gson = ResponseGsonHelper.getBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
        JSONObject jo = JSONObject.fromObject(jsonString);
//        ro.getResponseName()
        String response = (String)jo.get("listxxx");//TODO
        
        
    }
        
//        Field[] roFields = ro.getClass().getFields();
//        for (Field field : roFields) {
//        	String fieldName = field.getName();
//        	//fieldName
//        	
//        	
//        }
//        
////        gson.
//            StringBuilder sb = new StringBuilder();
//            // resonsename
//            sb.append("{ \"").append(result.getResponseName()).append("\" : ");
//            if (result instanceof ListResponse) {
//                List<? extends ResponseObject> responses = ((ListResponse) result).getResponses();
//                Integer count = ((ListResponse) result).getCount();
//                boolean nonZeroCount = (count != null && count.longValue() != 0);
//                if (nonZeroCount) {
//                    sb.append("{ \"").append(ApiConstants.COUNT).append("\":").append(count);
//                }
//
//                if ((responses != null) && !responses.isEmpty()) {
//                    String jsonStr = gson.toJson(responses.get(0));
//                    jsonStr = unescape(jsonStr);
//
//                    if (nonZeroCount) {
//                        sb.append(" ,\"").append(responses.get(0).getObjectName()).append("\" : [  ").append(jsonStr);
//                    }
//
//                    for (int i = 1; i < ((ListResponse) result).getResponses().size(); i++) {
//                        jsonStr = gson.toJson(responses.get(i));
//                        jsonStr = unescape(jsonStr);
//                        sb.append(", ").append(jsonStr);
//                    }
//                    sb.append(" ] }");
//                } else  {
//                    if (!nonZeroCount){
//                        sb.append("{");
//                    }
//
//                    sb.append(" }");
//                }
//            } else if (result instanceof SuccessResponse) {
//                sb.append("{ \"success\" : \"").append(((SuccessResponse) result).getSuccess()).append("\"} ");
//            } else if (result instanceof ExceptionResponse) {
//            	String jsonErrorText = gson.toJson((ExceptionResponse) result);
//            	jsonErrorText = unescape(jsonErrorText);
//            	sb.append(jsonErrorText);
//            } else {
//                String jsonStr = gson.toJson(result);
//                if ((jsonStr != null) && !"".equals(jsonStr)) {
//                    jsonStr = unescape(jsonStr);
//                    if (result instanceof AsyncJobResponse || result instanceof CreateCmdResponse) {
//                        sb.append(jsonStr);
//                    } else {
//                        sb.append(" { \"").append(result.getObjectName()).append("\" : ").append(jsonStr).append(" } ");
//                    }
//                } else {
//                    sb.append("{ }");
//                }
//            }
//            sb.append(" }");
//            return sb.toString();
//        }
//        return null;
//    }
    
    private static String toXMLSerializedString(ResponseObject result) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<").append(result.getResponseName()).append(" cloudserver=\"").append("1.0").append("\">");

        if (result instanceof ListResponse) {
            Integer count = ((ListResponse) result).getCount();

            if (count != null && count != 0) {
                sb.append("<").append(ApiConstants.COUNT).append(">").append(((ListResponse) result).getCount()).
                append("</").append(ApiConstants.COUNT).append(">");
            }
            List<? extends ResponseObject> responses = ((ListResponse) result).getResponses();
            if ((responses != null) && !responses.isEmpty()) {
                for (ResponseObject obj : responses) {
                    serializeResponseObjXML(sb, obj);
                }
            }
        } else {
            if (result instanceof EntityResponse || result instanceof AsyncJobResponse) {
                serializeResponseObjFieldsXML(sb, result);
            } else {
                serializeResponseObjXML(sb, result);
            }
        }

        sb.append("</").append(result.getResponseName()).append(">");
        return sb.toString();
    }

    private static void serializeResponseObjXML(StringBuilder sb, ResponseObject obj) {
        if (!(obj instanceof SuccessResponse) && !(obj instanceof ErrorResponse)) {
            sb.append("<").append(obj.getObjectName()).append(">");
        }
        serializeResponseObjFieldsXML(sb, obj);
        if (!(obj instanceof SuccessResponse) && !(obj instanceof ErrorResponse)) {
            sb.append("</").append(obj.getObjectName()).append(">");
        }
    }

    public static Field[] getFlattenFields(Class<?> clz) {
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(clz.getDeclaredFields()));
        if (clz.getSuperclass() != null) {
            fields.addAll(Arrays.asList(getFlattenFields(clz.getSuperclass())));
        }
        return fields.toArray(new Field[] {});
    }

    private static void serializeResponseObjFieldsXML(StringBuilder sb, ResponseObject obj) {
        boolean isAsync = false;
        if (obj instanceof AsyncJobResponse)
            isAsync = true;

        //Field[] fields = obj.getClass().getDeclaredFields();
        Field[] fields = getFlattenFields(obj.getClass());
        for (Field field : fields) {
            if ((field.getModifiers() & Modifier.TRANSIENT) != 0) {
                continue; // skip transient fields
            }

            SerializedName serializedName = field.getAnnotation(SerializedName.class);
            if (serializedName == null) {
                continue; // skip fields w/o serialized name
            }

            field.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = field.get(obj);
            } catch (IllegalArgumentException e) {
                throw new CloudRuntimeException("how illegal is it?", e);
            } catch (IllegalAccessException e) {
                throw new CloudRuntimeException("come on...we set accessible already", e);
            }
            if (fieldValue != null) {
                if (fieldValue instanceof ResponseObject) {
                    ResponseObject subObj = (ResponseObject) fieldValue;
                    if (isAsync) {
                        sb.append("<jobresult>");
                    }
                    serializeResponseObjXML(sb, subObj);
                    if (isAsync) {
                        sb.append("</jobresult>");
                    }
                } else if (fieldValue instanceof Collection<?>) {
                    Collection<?> subResponseList = (Collection<Object>) fieldValue;
                    boolean usedUuidList = false;
                    for (Object value : subResponseList) {
                        if (value instanceof ResponseObject) {
                            ResponseObject subObj = (ResponseObject) value;
                            if (serializedName != null) {
                                subObj.setObjectName(serializedName.value());
                            }
                            serializeResponseObjXML(sb, subObj);
                        } else if (value instanceof ExceptionProxyObject) {
                            // Only exception reponses carry a list of
                            // ExceptionProxyObject objects.
                            ExceptionProxyObject idProxy = (ExceptionProxyObject) value;
                            // If this is the first IdentityProxy field
                            // encountered, put in a uuidList tag.
                            if (!usedUuidList) {
                                sb.append("<" + serializedName.value() + ">");
                                usedUuidList = true;
                            }
                            sb.append("<" + "uuid" + ">" + idProxy.getUuid() + "</" + "uuid" + ">");
                            // Append the new descriptive property also.
                            String idFieldName = idProxy.getDescription();
                            if (idFieldName != null) {
                                sb.append("<" + "uuidProperty" + ">" + idFieldName + "</" + "uuidProperty" + ">");
                            }
                        }
                    }
                    if (usedUuidList) {
                        // close the uuidList.
                        sb.append("</").append(serializedName.value()).append(">");
                    }
                } else if (fieldValue instanceof Date) {
                    sb.append("<").append(serializedName.value()).append(">").append(BaseRequest.getDateString((Date) fieldValue)).
                    append("</").append(serializedName.value()).append(">");
                } else {
                    String resultString = escapeSpecialXmlChars(fieldValue.toString());
                    if (!(obj instanceof ErrorResponse)) {
                        resultString = encodeParam(resultString);
                    }

                    sb.append("<").append(serializedName.value()).append(">").append(resultString).append("</").append(serializedName.value()).append(">");
                }
            }
        }
    }

    private static Method getGetMethod(Object o, String propName) {
        Method method = null;
        String methodName = getGetMethodName("get", propName);
        try {
            method = o.getClass().getMethod(methodName);
        } catch (SecurityException e1) {
            log.error("Security exception in getting ResponseObject " + o.getClass().getName() + " get method for property: " + propName);
        } catch (NoSuchMethodException e1) {
            if (log.isTraceEnabled()) {
                log.trace("ResponseObject " + o.getClass().getName() + " does not have " + methodName + "() method for property: " + propName
                        + ", will check is-prefixed method to see if it is boolean property");
            }
        }

        if (method != null)
            return method;

        methodName = getGetMethodName("is", propName);
        try {
            method = o.getClass().getMethod(methodName);
        } catch (SecurityException e1) {
            log.error("Security exception in getting ResponseObject " + o.getClass().getName() + " get method for property: " + propName);
        } catch (NoSuchMethodException e1) {
            log.warn("ResponseObject " + o.getClass().getName() + " does not have " + methodName + "() method for property: " + propName);
        }
        return method;
    }

    private static String getGetMethodName(String prefix, String fieldName) {
        StringBuffer sb = new StringBuffer(prefix);

        if (fieldName.length() >= prefix.length() && fieldName.substring(0, prefix.length()).equals(prefix)) {
            return fieldName;
        } else {
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
        }

        return sb.toString();
    }

    private static String escapeSpecialXmlChars(String originalString) {
        char[] origChars = originalString.toCharArray();
        StringBuilder resultString = new StringBuilder();

        for (char singleChar : origChars) {
            if (singleChar == '"') {
                resultString.append("&quot;");
            } else if (singleChar == '\'') {
                resultString.append("&apos;");
            } else if (singleChar == '<') {
                resultString.append("&lt;");
            } else if (singleChar == '>') {
                resultString.append("&gt;");
            } else if (singleChar == '&') {
                resultString.append("&amp;");
            } else {
                resultString.append(singleChar);
            }
        }

        return resultString.toString();
    }

    private static String encodeParam(String value) {
//        if (!ApiServer.encodeApiResponse) {
//            return value;
//        }
        try {
            return new URLEncoder().encode(value).replaceAll("\\+", "%20");
        } catch (Exception e) {
            log.warn("Unable to encode: " + value, e);
        }
        return value;
    }

}
