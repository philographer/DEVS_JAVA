package Lab6;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class router extends ViewableAtomic
{
	
	protected Queue q; // sender로 부터 들어온 packet을 큐에 담음
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
		
		for(int i=1; i<=5; i++) // receiver로 보낼 5개의 out 포트 생성
		{
			addOutport("out" + i);
		}
		
		addOutport("out"); // sender로 보낼 out 포트 생성
		
		processing_time = Processing_time;
	}
	
	public void initialize()
	{
		call_num = 1;
		q = new Queue(); // queue ����
		packet = new packet("", 0);
		
		holdIn("passive", processing_time); // 초기화시, processing_time만큼 기다리게 만듬 (10)
		call_num = call_num + 1;
	}

	public void deltext(double e, message x)
	{
	
		Continue(e);
		call_num = call_num + 1;
		if (phaseIs("passive"))
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					packet = (packet) x.getValOnPort("in", i);
					holdIn("passive", processing_time);
					q.add(packet); // sender에게서 받은 패킷을 queue에 저장
				}
			}
		}
	}
	
	public void deltint()
	{
		System.out.println("delt init called");
		call_num = call_num + 1;
		if(q.size() == 5) { // queue에 있는 패킷이 5개이면
			holdIn("sending", processing_time);
		}
	}

	public message out()
	{
		call_num = call_num + 1;
		message m = new message();
		
		if (phaseIs("sending"))
		{
			if(!q.isEmpty()) // queue가 비어있지 않으면
			{
				packet = (packet) q.removeFirst();
				// q에서 패킷을 제거
				int portNum = packet.getArrival();	
				// packet에서 어디에 갈 패킷인지 보고
				m.add(makeContent("out" + portNum, packet));					
				// 메세지를 해당 포트에 보냄
				
				holdIn("sending", processing_time);	
				// sending 상태로 천이
			}
			else // queue가 비어있으면
			{
				m.add(makeContent("out", new packet("done", 0)));
				// 완료 메세지를 보냄
				holdIn("passive", processing_time);	
				// passive 상태로 천이
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



