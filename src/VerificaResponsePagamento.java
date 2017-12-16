import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class VerificaResponsePagamento {

	public boolean verificaPagamento(String protocolo) throws JMSException, NamingException{
		
		InitialContext ic = new InitialContext();
		
		ConnectionFactory factory = (ConnectionFactory) ic.lookup("jms/K19DurableFactory");
		
		Topic topic = (Topic) ic.lookup("jms/noticias");
		
		Connection conn = factory.createConnection();
		
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

		MessageConsumer receiver = session.createDurableSubscriber(topic, "(pagamento = '"+protocolo+"')");

		conn.start();
		
		Message receive = receiver.receive();
		
		boolean pagamento = receive.getBooleanProperty(protocolo);
		
		receiver.close();
		
		session.close();
		
		conn.close();
		
		return pagamento; 
	}
}
