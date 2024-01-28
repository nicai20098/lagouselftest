package com.jiabb.demo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import javafx.scene.SceneAntialiasing;
import sun.applet.resources.MsgAppletViewer;

import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/28 13:41
 * @since: 1.0
 */
public class MessageEncoder extends MessageToMessageEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
        System.out.println("encoder doing");
        String str = (String) o;
        list.add(Unpooled.copiedBuffer(str, CharsetUtil.UTF_8));
    }
}