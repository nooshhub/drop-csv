package csv.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvServer {
    private int port;

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
    private void run() throws InterruptedException {
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // used to instantiate a new Channel to accept incoming connections.
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new CsvInfoEncoder(), new CsvServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128) // boss
                    .childOption(ChannelOption.SO_KEEPALIVE, true); //worker

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

    public static void main(String[] args) throws InterruptedException {
        new CsvServer(8088).run();
    }
}
