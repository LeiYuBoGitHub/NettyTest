package com.soft.netty.entity.decoder;

import com.soft.netty.entity.frame.DecoderByteFrame;

/**
 * @author 野性的呼唤
 * @date 2022/7/20 16:15
 */
public abstract class AbstractDecoder {

    /**
     * 转换
     * @param frame 框架
     */
    public abstract void cover(DecoderByteFrame frame);

}
