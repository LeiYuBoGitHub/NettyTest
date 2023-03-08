package com.soft.netty.entity.encoder;

/**
 * @author 野性的呼唤
 * @date 2022/10/31 23:21
 * @description
 */
public abstract class AbstractEncoder {

    /**
     * 转换
     * @return {@link byte[]}
     */
    public abstract byte[] cover();
}
