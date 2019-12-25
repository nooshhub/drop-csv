package csv.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * Server that accept the path of a file an echo back its content.
 *
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvServer {

    static final boolean SSL = System.getProperty("ssl") != null;
    private int port = Integer.parseInt(System.getProperty("port", SSL ? "8992" : "8088"));

    public CsvServer(int port) {
        this.port = port;
    }

    /**
     * NioEventLoopGroup is a multithreaded event loop that handles I/O operation.
     * Netty provides various EventLoopGroup implementations for different kind of transports.
     * We are implementing a server-side application in this example, and therefore two NioEventLoopGroup will be used.
     * The first one, often called 'boss', accepts an incoming connection.
     * The second one, often called 'worker', handles the traffic of the accepted connection once the boss accepts the connection
     * and registers the accepted connection to the worker.
     * <p>
     * How many Threads are used and how they are mapped to the created Channels depends on the EventLoopGroup
     * implementation and may be even configurable via a constructor.
     */
    private void run() throws InterruptedException, CertificateException, SSLException {

        // configure SSL
        final SslContext sslCtx;
        if(SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // configure the server
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // used to instantiate a new Channel to accept incoming connections.
                    .option(ChannelOption.SO_BACKLOG, 128) // boss
                    .handler(new LoggingHandler(LogLevel.INFO))
//                    .childOption(ChannelOption.SO_KEEPALIVE, true) //worker
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            if(sslCtx != null) {
                                pipeline.addLast(sslCtx.newHandler(ch.alloc()));
                            }
                            pipeline.addLast(
                                    new StringEncoder(CharsetUtil.UTF_8),
                                    new StringDecoder(CharsetUtil.UTF_8),
                                    new LineBasedFrameDecoder(8192),
                                    new ChunkedWriteHandler(),
                                    new CsvServerHandler()
                            );
                        }
                    })
            ;

            // bind and start to accept inconming connections
            ChannelFuture f = b.bind(port).sync();

            // wait until the server socket is closed
            // gracefully shutdown the server
            f.channel().closeFuture().sync();

        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException, CertificateException, SSLException {
        new CsvServer(8088).run();
    }
}
