package com.jiabb.parsing;

import com.jiabb.mapping.ParameterMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2023/5/6 23:53
 * @since: 1.0
 */
public class ParameterMappingTokenHandler implements TokenHandler{

    private List<ParameterMapping> parameterMappings = new ArrayList<>();
    private Class<?> parameterType;

    public ParameterMappingTokenHandler() {
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    @Override
    public String handleToken(String content) {
        parameterMappings.add(buildParameterMapping(content));
        return "?";
    }

    private ParameterMapping buildParameterMapping(String content) {
//        Map<String, String> propertiesMap = parseParameterMapping(content);
//        String property = propertiesMap.get("property");
//        Class<?> propertyType;
//        if (metaParameters.hasGetter(property)) { // issue #448 get type from additional params
//            propertyType = metaParameters.getGetterType(property);
//        } else if (typeHandlerRegistry.hasTypeHandler(parameterType)) {
//            propertyType = parameterType;
//        } else if (JdbcType.CURSOR.name().equals(propertiesMap.get("jdbcType"))) {
//            propertyType = java.sql.ResultSet.class;
//        } else if (property == null || Map.class.isAssignableFrom(parameterType)) {
//            propertyType = Object.class;
//        } else {
//            MetaClass metaClass = MetaClass.forClass(parameterType, configuration.getReflectorFactory());
//            if (metaClass.hasGetter(property)) {
//                propertyType = metaClass.getGetterType(property);
//            } else {
//                propertyType = Object.class;
//            }
//        }
//        ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, property, propertyType);
//        Class<?> javaType = propertyType;
//        String typeHandlerAlias = null;
//        for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
//            String name = entry.getKey();
//            String value = entry.getValue();
//            if ("javaType".equals(name)) {
//                javaType = resolveClass(value);
//                builder.javaType(javaType);
//            } else if ("jdbcType".equals(name)) {
//                builder.jdbcType(resolveJdbcType(value));
//            } else if ("mode".equals(name)) {
//                builder.mode(resolveParameterMode(value));
//            } else if ("numericScale".equals(name)) {
//                builder.numericScale(Integer.valueOf(value));
//            } else if ("resultMap".equals(name)) {
//                builder.resultMapId(value);
//            } else if ("typeHandler".equals(name)) {
//                typeHandlerAlias = value;
//            } else if ("jdbcTypeName".equals(name)) {
//                builder.jdbcTypeName(value);
//            } else if ("property".equals(name)) {
//                // Do Nothing
//            } else if ("expression".equals(name)) {
//                throw new BuilderException("Expression based parameters are not supported yet");
//            } else {
//                throw new BuilderException("An invalid property '" + name + "' was found in mapping #{" + content + "}.  Valid properties are " + PARAMETER_PROPERTIES);
//            }
//        }
//        if (typeHandlerAlias != null) {
//            builder.typeHandler(resolveTypeHandler(javaType, typeHandlerAlias));
//        }
//        return builder.build();
        return null;
    }

}
