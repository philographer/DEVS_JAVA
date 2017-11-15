package Lab6;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class receiver extends ViewableAtomic
{
  
	protected packet packet;
	protected double processing_time;

	public receiver()
	{
		this("proc", 20);
	}

	public receiver(String name, double Processing_time)
	{
		super(name);  
		addInport("in");
		addOutport("out");
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		packet = new packet("", 0);
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
					packet = (packet)x.getValOnPort("in", i);			
					holdIn("busy", processing_time);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("busy"))
		{
			holdIn("passive", INFINITY); // ���ο� packet�� ���� ���·� ����
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		{
			m.add(makeContent("out", packet)); // ó���� ��Ŷ ���� (Simview���� ������ X)
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "job: " + packet.getName();
	}
}

