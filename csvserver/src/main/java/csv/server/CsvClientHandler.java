package csv.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * receive a 32-bit integer from the server,
 * translate it into a human-readable format,
 * print the translated time,
 * and close the connection:
 *
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // In TCP/IP, Netty reads the data sent from a peer into a ByteBuf.
        CsvInfo m = (CsvInfo) msg;
        System.out.println(m);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace(); //todo: log
        ctx.close();
    }
}
