package Lab6;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class router extends ViewableAtomic
{
	
	protected Queue q; // sender�� ���� ���� packet�� ������ ť
	protected packet packet;
	protected double processing_time;
	
	protected int call_num;
	
	public router()
	{
		this("procQ", 20);
	}

	public router(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		
		for(int i=1; i<=5; i++) // (receiver�� ��Ŷ�� �����ϱ� ����)5���� output port����
		{
			addOutport("out" + i);
		}
		
		addOutport("out"); // sender���� �޽����� ������ ���� output port ����
		
		processing_time = Processing_time;
	}
	
	public void initialize()
	{
		call_num = 1;
		q = new Queue(); // queue ����
		packet = new packet("", 0);
		
		holdIn("passive", INFINITY);
	
		System.out.println("ȣ�� init"+call_num);
		call_num = call_num + 1;
	}

	public void deltext(double e, message x)
	{
	
		Continue(e);
		
		System.out.println("ȣ�� ext"+call_num);
		call_num = call_num + 1;
		if (phaseIs("passive"))
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					packet = (packet) x.getValOnPort("in", i);

					q.add(packet); // sender�� ���� ���� ��Ŷ�� ť�� �߰�
					
					if(q.size() == 5) // queue ����� 5�̸�
					{
						holdIn("sending", processing_time);
						// sending ���·� ����
					}
				}
			}
		}
	}
	
	public void deltint()
	{

		System.out.println("ȣ�� int"+call_num);
		call_num = call_num + 1;
		
	}

	public message out()
	{
		System.out.println("ȣ�� out"+call_num);
		call_num = call_num + 1;
		message m = new message();
		
		if (phaseIs("sending"))
		{
			if(!q.isEmpty()) // queue�� ������� ������
			{
				packet = (packet) q.removeFirst();
				// queue���� ��Ŷ�� ������
				int portNum = packet.getArrival();	
				//  �ش� ��Ŷ�� ������ ��ȣ�� ������ 
				m.add(makeContent("out" + portNum, packet));					
				// �ش� ��Ŷ�� ������ ��Ʈ ��ȣ�� packet ����
				
				holdIn("sending", processing_time);	
				// sending ���·� ����
			}
			else // queue�� ����ִٸ�
			{
				m.add(makeContent("out", new packet("done", 0)));
				// sender ���� done �޽��� ����
				holdIn("passive", processing_time);	
				// passive ���·� ����
			}
		}

		return m;
	}
	
	
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + "queue length: " + q.size()
        + "\n" + "queue itself: " + q.toString();
	}

}



