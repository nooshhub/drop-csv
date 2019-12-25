package csv.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvInfoDecoder extends ReplayingDecoder<Void> {
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(new CsvInfo(in.readUnsignedInt()));
    }
}
