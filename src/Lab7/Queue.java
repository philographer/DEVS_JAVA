package Lab7;

import GenCol.entity;
import genDevs.modeling.message;
import simView.ViewableAtomic;

public class Queue extends ViewableAtomic
{
	
	protected GenCol.Queue q; // queue 생성
	protected job job;
	protected double processing_time;
	
	public Queue()
	{
		this("procQ", 20);
	}

	public Queue(String name, double Processing_time)
	{
		super(name);

		addInport("in_1"); // generatordㅘ 연결
		addInport("in_2"); // processor와 연결
		addOutport("out");
		
		processing_time = Processing_time;
	}
	
	public void initialize()
	{
		q = new GenCol.Queue();
		job = new job("", false);
		
		holdIn("passive", INFINITY); // 초기상태 : passive
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("passive"))
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in_1", i))
				{
					job = (job)x.getValOnPort("in_1", i);
					
					holdIn("forwarding", 0); // forwarding: processor로 job이 전송이 가능한 상태
				}
			}
		}
		else if (phaseIs("queuing")) // queuing: processor로 job의 전송이 불가능한 상태
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in_1", i)) // generator로 부터 받음
				{
					entity temp = x.getValOnPort("in_1", i);
					q.add(temp);
				}

				if(messageOnPort(x, "in_2", i)) { // processor로 부터 받음
					if(q.isEmpty()) { // queue안에 더이상 Job이 없으면
						holdIn("stop", INFINITY); // stop 상태로 천이하여 모델 중지
					} else {
						holdIn("forwarding", 0); // 그렇지 않을 경우 forwarding 성태로 천이
					}
				}
			}
		}
	}
	
	public void deltint()
	{
		/*
		if (phaseIs("forwarding")) // forwarding 상태일때
		{
			if (!q.isEmpty())
			{
				job = (entity) q.removeFirst();

				holdIn("busy", processing_time);
			}
			else
			{
				job = new entity("");

				holdIn("passive", INFINITY);
			}
		}
		 */
		if (phaseIs("forwarding")) {
			holdIn("queuing", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		
		if (phaseIs("forwarding"))
		{
			if(!q.isEmpty()) {
				job = (job)q.removeFirst();
			}
			m.add(makeContent("out", job));
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



