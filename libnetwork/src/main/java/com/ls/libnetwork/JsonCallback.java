package com.ls.libnetwork;

public abstract class JsonCallback<T> {

    /**
     * 返回类型：返回结果里面除了我们需要的数据，还有响应码和message等字段，所以我们将结果包装成固定的类型：ApiResponse
     * @param response
     */
    public void onSuccess(ApiResponse<T> response){

    }

    public void onError(ApiResponse<T> response){

    }

    public void onCacheSuccess(ApiResponse<T> response){

    }

}
