package Lab9;

import GenCol.entity;
import genDevs.modeling.message;
import simView.ViewableAtomic;

public class evaluator extends ViewableAtomic
{
	int solved; // 처리 완료한 Job의 수
	int loss; // 처리 완료하지 못한 Job의 수
	int arrive; // generator가 전송한 몬든 Job의 수

	public evaluator()
	{
		this("proc");
	}

	public evaluator(String name)
	{
		super(name);
    
		addInport("arrive");
		addInport("solved");
		addInport("loss");

		addOutport("out");
	}
  
	public void initialize()
	{
		arrive = 0;
		solved = 0;
		loss = 0;

		holdIn("active", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("active"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "arrive", i))
				{
				    arrive++;
				} else if (messageOnPort(x, "solved", i))
				{
				    solved++;
				} else if (messageOnPort(x, "loss", i))
				{
				    loss++;
				}
			}
		}
	}
  
	public void deltint()
	{

	}

	public message out()
	{
		message m = new message();
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "arrive" + arrive
		+ "\n" + "solved" + solved
		+ "\n" + "loss" + loss;
	}

}

