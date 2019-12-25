package csv.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvClient {

    public static void main(String[] args) throws InterruptedException {
        final String host = "localhost";
        final int port = 8088;

        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(
                                    new StringEncoder(CharsetUtil.UTF_8),
                                    new StringDecoder(CharsetUtil.UTF_8),
                                    new CsvClientHandler()
                            );
                        }
                    });

            // start the clientwait until connection is closed
            b.connect(host, port).sync().channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }
}
