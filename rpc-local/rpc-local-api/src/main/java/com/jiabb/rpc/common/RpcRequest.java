package com.jiabb.rpc.common;

import lombok.Data;

import java.util.Objects;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/30 22:32
 * @since: 1.0
 */
@Data
public class RpcRequest {

    private String requestId;

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;

}