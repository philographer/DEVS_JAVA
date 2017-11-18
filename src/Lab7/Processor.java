package Lab7;

import GenCol.entity;
import genDevs.modeling.message;
import simView.ViewableAtomic;

public class Processor extends ViewableAtomic
{
  
	protected job job;
	protected double processing_time;

	public Processor()
	{
		this("proc", 20);
	}

	public Processor(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("out_1"); // Generator에 처리한 job을 반환할
		addOutport("out_2"); // Queue에게 Done을 보낼 포트
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		job = new job("", false);
		
		holdIn("passive", INFINITY); // 초기 상태: passive (Job 처리중이 아닌 상태)
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
					job = (job)x.getValOnPort("in", i);
					holdIn("busy", processing_time);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("busy"))
		{
			job = new job("", false);
			
			holdIn("passive", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		// 다음 이벤트는 busy 상태일때 동작하는 것
		if (phaseIs("busy"))
		{
			if(job.isLast == true) {
				holdIn("stop", INFINITY);
			}
			m.add(makeContent("out_1", job));
			m.add(makeContent("out_2", new job("done", true)));
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

