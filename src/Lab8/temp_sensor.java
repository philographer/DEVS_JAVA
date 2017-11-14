package Lab8;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class temp_sensor extends ViewableAtomic
{
  
	
	protected job_msg job; 
	
	double[] current_temp = {15, 15, 19, 24, 25, 24, 22}; // ���� �µ�
	int count;
	
	protected double processing_time;
	
	public temp_sensor()
	{
		this("proc", 10);
	}

	public temp_sensor(String name, double Processing_time)
	{
		super(name);
    
		addInport("in"); // ��ǲ ��Ʈ ����
		addOutport("out"); // �ƿ���Ʈ ����
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		job = new job_msg("off",false);
		count = 0;
		holdIn("off", INFINITY); 
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("off")) // passive �����̸�
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i)) 
					//�޽��� x�� "in" ��Ʈ�� �����ϸ�
				{
					job = (job_msg)x.getValOnPort("in", i); 
					// in : controller�� ���� �޽����� �޴� ��Ʈ			
					holdIn("on", processing_time); 
					// on ���·� ����
				}
			}
		}
		else if(phaseIs("on"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i)) 
					//�޽��� x�� "in" ��Ʈ�� �����ϸ�
				{
					job = (job_msg)x.getValOnPort("in", i); 
					// in : controller�� ���� �޽����� �޴� ��Ʈ			
					if(job.IsWork==false)
					{
						holdIn("off", processing_time); 
					}
				}
			}
			
		}
	}
  
	public void deltint()
	{
		if (phaseIs("on")) 
		{
			holdIn("on", processing_time); 
		}
		
	}

	public message out()
	{
		message m = new message();

		if (phaseIs("on")) // on�����̸�
		{
			m.add(makeContent("out", new job_msg("current_temp : " + current_temp[count],current_temp[count]))); // "out"��Ʈ�� job�� ���
			// ����µ�(current_temp) ���� controller���� ����
			count++;
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

