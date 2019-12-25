package csv.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvInfoEncoder extends MessageToByteEncoder<CsvInfo> {

    protected void encode(ChannelHandlerContext ctx, CsvInfo msg, ByteBuf out) throws Exception {
        out.writeInt((int) msg.value());
    }

}
