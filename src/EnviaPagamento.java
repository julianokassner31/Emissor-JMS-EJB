import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class EnviaPagamento {

	public static void main(String[] args) {
		System.out.println("Digite o valor do Pagamento");
		Scanner sc = new Scanner(System.in);
		String valor = sc.nextLine();
		System.out.println("Enviando pagamento...");
		
		boolean pagamentoAceito = false;
		
		try{
			
			InitialContext ic = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory) ic.lookup("jms/K19Factory");
			Queue queue = (Queue) ic.lookup("jms/pagamentos");
			Connection conn = factory.createConnection();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer sender = session.createProducer(queue);
			TextMessage message = session.createTextMessage();
			message.setText(valor+";prot123");
			sender.send(message);
			sender.close();
			session.close();
			conn.close();
			System.out.println("Pagamento enviado");
			
			VerificaResponsePagamento rs = new VerificaResponsePagamento();
			pagamentoAceito = rs.verificaPagamento("prot123");
			
			if(pagamentoAceito){
				System.out.println("Pagamento finalizado com sucesso.");
			}else{
				System.out.println("Pagamento não efetuado... tentando novamente");
			}
			
		}catch(Exception e){
			System.out.println("Erro ao enviar pagamento"+e);
		}

	}

}
