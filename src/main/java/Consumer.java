import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class Consumer {
    public static void main(String[] args)throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("payment_consumer_group");
        //获取NameServer相关group下的topic
        consumer.setNamesrvAddr("192.168.253.144:9876");
        //订阅 order下的所有消息
        consumer.subscribe("order","*");
        //注册消息监听器 ，主要使用匿名内部类进行创建
        consumer.registerMessageListener(new MessageListenerConcurrently()  {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt msg :
                        msgs) {
                    //根据标签主题进行不同业务逻辑匹配
                    if (msg.getTopic().equals("order")){
                        //处理订单创建
                        if (msg.getTags().equals("Create")){
                            //默认是按照默认编码，发送为的数组操作，
                            String consumerInfo = null;
//                            try {
//                                consumerInfo = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
                            System.out.println("处理订单的创建接受消息完成，执行相关逻辑..."+msg);
                            /**
                             * 相关业务逻辑
                             * 订单更新
                             * 数据更新...
                             * */
                        }else if (msg.getTags().equals("Exitshirt")){
                            System.out.println("处理退货的相关业务操作...."+msg);
                        }
                    }
                }
                //返回成功状态 即对消息进行消费完成再返回成功状态
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //消费者是一直处于监听状态
        consumer.start();
    }
}
