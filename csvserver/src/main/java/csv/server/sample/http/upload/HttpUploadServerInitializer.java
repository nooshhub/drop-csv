package csv.server.sample.http.upload;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class HttpUploadServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();


        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());

        // Remove the following line if you don't want automatic content compression.
        pipeline.addLast(new HttpContentCompressor());

        pipeline.addLast(new HttpUploadServerHandler());
    }
}
