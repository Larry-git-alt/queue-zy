package cn.queue.imcore.handler;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.queue.cache.ChannelHandlerContextCache;
import cn.queue.domain.entity.FriendsEntity;
import cn.queue.domain.entity.ImMsgEntity;
import cn.queue.domain.valueObj.ImMsgCodeEnum;
import cn.queue.imcore.service.IFriendsService;
import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author: Larry
 * @Date: 2024 /05 /03 / 0:11
 * @Description:
 */
@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Resource
    private ImHandlerFactory imHandlerFactory;
    @Resource
    private  IFriendsService friendsService;
    private static WebSocketHandler webSocketHandler;
    private static Long useId;

//    public void setImHandlerFactory(ImHandlerFactory imHandlerFactory) {
//         this.imHandlerFactory = imHandlerFactory;
//    }
//    public void setFriendsService(IFriendsService friendsService) {
//        this.
//    }
    @PostConstruct     //关键二   通过@PostConstruct 和 @PreDestroy 方法 实现初始化和销毁bean之前进行的操作
    public void init() {
        webSocketHandler = this;
        webSocketHandler.imHandlerFactory = imHandlerFactory;   // 初使化时将已静态化的testService实例化
        webSocketHandler.friendsService = friendsService;
    }
    private Bootstrap bootstrap;
    private EventLoop eventLoop;
    private final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============================start=================");
        logger.info("与客户端建立连接，通道开启！");

        //发送登录包        sendOnlineMessage(ImMsgCodeEnum.IM_LOGIN_MSG.getCode());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //推送离线包
        logger.info("与客户端断开连接，通道关闭！");
        sendOnlineMessage(ImMsgCodeEnum.IM_LOGOUT_MSG.getCode());

    }

    private void sendOnlineMessage(Integer code) {
        List<FriendsEntity> friendsEntityList= webSocketHandler.friendsService.getList(useId);
        friendsEntityList.stream()
                //排除离线用户
                .filter(friendsEntity -> ChannelHandlerContextCache.get(friendsEntity.getToId()) == null)
                .forEach(
                        friendsEntity -> {
                            ImMsgEntity imMsg = ImMsgEntity.builder()
                                    .code(code)
                                    .userId(useId)
                                    .content(useId+"状态变了")
                                    .build();
                             sendMessageToUser(friendsEntity.getToId(),imMsg);
                        }
                );
    }

    private void sendOffOlineMessage(Long id){

    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest httpRequest) {// 处理 HTTP 请求
            System.out.println("Received HTTP request: " + httpRequest.method() + " " + httpRequest.uri());
            // 如果需要，可以在这里处理 WebSocket 握手逻辑
        } else if (msg instanceof WebSocketFrame webSocketFrame) {
            // 处理 WebSocket 消息
            if (webSocketFrame instanceof TextWebSocketFrame frame) {
                ImMsgEntity imMsgEntity = JSON.parseObject(frame.text(), ImMsgEntity.class);
                useId = imMsgEntity.getUserId();
                webSocketHandler.imHandlerFactory.doMsgHandler(ctx,imMsgEntity);

            } else if (webSocketFrame instanceof BinaryWebSocketFrame binaryWebSocketFrame) {
                ByteBuf byteBuf = binaryWebSocketFrame.content();
                // 处理二进制 WebSocket 消息
                // 注意：在处理二进制消息时，需要根据实际情况解码消息内容
            }
        }

        super.channelRead(ctx, msg);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

    }
        public static void sendMessageToUser(Long userId, Object message) {
        ChannelHandlerContext ctx = ChannelHandlerContextCache.get(userId);
        if (ctx != null && ctx.channel().isActive()) {
            ctx.writeAndFlush(message);
        } else {
            System.out.println("User " + userId + " is not connected or channel is not active.");
        }
    }
    private static Integer getUrlParams(String url) {
        if (!url.contains("=")) {
            return null;
        }
        String userId = url.substring(url.indexOf("=") + 1);
        return Integer.parseInt(userId);
    }
}
