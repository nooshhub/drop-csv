package csv.server.sample.http.cors;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * A simple handler which will simple return a successful Http
 * response for any request.
 *
 * @author Neal Shan
 * @since 0.0.1
 */
public class OkResponseHandler extends SimpleChannelInboundHandler<Object> {
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.EMPTY_BUFFER
        );

        response.headers().set("custom-response-header", "Some value");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
