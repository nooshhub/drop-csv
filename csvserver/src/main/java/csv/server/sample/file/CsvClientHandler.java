package csv.server.sample.file;

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
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("C:\\Users\\neals\\Downloads\\new 1.sql");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // In TCP/IP, Netty reads the data sent from a peer into a ByteBuf.
        if(msg instanceof String) {
            System.out.print(msg);
        } else {
            ByteBuf m = (ByteBuf) msg;
            try {
                while (m.isReadable()) {
                    System.out.print((char) m.readByte());
                    System.out.flush();
                }
            } finally {
                m.release();
            }
        }

        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace(); //todo: log
        ctx.close();
    }
}
