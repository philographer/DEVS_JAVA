package Lab9;

import GenCol.entity;
import genDevs.modeling.message;
import simView.ViewableAtomic;

public class scheduler extends ViewableAtomic
{

	protected entity job;
	protected double scheduling_time;
	int node;
	int way;

	public scheduler()
	{
		this("proc", 0, 5);
	}

	public scheduler(String name, double Scheduling_time, int _node)
	{
		super(name);

		node = _node;
		addInport("in");

		for(int i=1; i <= _node; i++) {
			addOutport("out"+i); // 아웃포트 생성
		}

		scheduling_time = Scheduling_time;
	}
  
	public void initialize()
	{
		way = 1; // (way값이 증가하면서 1->2->3->4->5->1->2->...  순으로 Job을 전송함

		job = new entity("");
		
		holdIn("passive", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("passive"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					job = x.getValOnPort("in", i);
					holdIn("busy", scheduling_time);
				}
			}
		}
	}

	// out 후에 int
	public void deltint()
	{
		way++;
		if(way > node) {
			way = 1;
		}
		holdIn("passive", INFINITY);
		job = new entity("");
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		{
			m.add(makeContent("out"+way, job)); // RR 스케쥴링 원칙에 따른 Job 전송
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "job: " + job.getName();
	}

}

