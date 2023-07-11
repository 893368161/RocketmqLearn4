/*
*2023年7月10日14:44:31
* 功能：订单生产者模拟创建
* */
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


public class Sender {
    public static void main(String[] args)throws Exception{
        //发送给broker再由broker
        //1.括号内参数指定生产组的名字,创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("order_producer_group");
        //2.指定NameServer地址
        producer.setNamesrvAddr("192.168.253.144:9876");

        producer.setSendMsgTimeout(60000);
        //启动生产（消息）者
        producer.start();
        //订单生成相关信息
        String orderInfo = "Order #225655 购买等相关相关业务信息...";
        String exitInfo = "Exit #225655 退货等相关业务.....";
        //二.具体创建一条消息
        //主题(topic)  标签（topic）[一些描述细节]  第三个参数为传递的信息（并进行编码操作,括号内为编码方式）
        Message meg = new Message("order","Create","user001",orderInfo.getBytes(RemotingHelper.DEFAULT_CHARSET));
        Message meg2 = new Message("order","Exitshirt","user002",exitInfo.getBytes(RemotingHelper.DEFAULT_CHARSET));
        //发送消息并获取相关结果进行打印
        SendResult sendResult = producer.send(meg);
//      sout中默认会使用toString将字节数组转化为toString后的编码
        System.out.println("Order Message Send Success:"+sendResult.getMsgId());
        producer.shutdown();//关闭生产者
    }
}
