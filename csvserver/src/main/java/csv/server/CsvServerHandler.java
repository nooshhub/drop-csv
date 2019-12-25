package csv.server;

import io.netty.channel.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;

import java.io.RandomAccessFile;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("HELLO: type the path of the file to retrieve.\n");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Typed path: " + msg);
        RandomAccessFile raf = null;
        long length = -1;
        try {
            raf = new RandomAccessFile((String) msg, "r");
            length = raf.length();
        } catch (Exception e) {
            ctx.writeAndFlush("ERR: " + e.getClass().getSimpleName() + ": " + e.getMessage() + "\n");
            return;
        } finally {
            if (length < 0 && raf != null) {
                raf.close();
            }
        }

        ctx.write("OK: " + raf.length() + "\n");

        if (ctx.pipeline().get(SslHandler.class) == null) {
            // SSL not enabled - can use zero-copy file transfer
            ctx.write(new DefaultFileRegion(raf.getChannel(), 0, length));
        } else {
            // SSL enabled - can not use zero-copy transfer
            ctx.write(new ChunkedFile(raf));
        }
        ctx.writeAndFlush("\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        if (ctx.channel().isActive()) {
            ctx.writeAndFlush("ERR: " +
                    cause.getClass().getSimpleName() + ":" +
                    cause.getMessage() + "\n")
                    .addListener(ChannelFutureListener.CLOSE)
            ;
        }
    }

}
