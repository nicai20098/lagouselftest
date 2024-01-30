package com.jiabb.rpc.common;

import lombok.Data;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/30 22:33
 * @since: 1.0
 */
@Data
public class RpcResponse {

    private String requestId;

    private String error;

    private Object result;

}