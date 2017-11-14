package Lab8;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class controller extends ViewableAtomic
{
  
	protected job_msg userJob, sensorJob;
	protected double processing_time; // Job�� ����ð�

	int count;
	double current_temp; // ����µ�
	double user_temp; // ����µ�
	boolean IsWork; // ���ۿ���
	
	
	public controller()
	{
		this("proc", 20);
	}

	public controller(String name, double Processing_time)
	{
		super(name);
    
		addInport("in1"); // user�� ����� ��Ʈ
		addInport("in2"); // temp_sensor�� ����� ��Ʈ
		addOutport("out"); // �ƿ���Ʈ ����
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		userJob = new job_msg("off",false);
		sensorJob = new job_msg("off",false);
		
		user_temp = 0;
		current_temp = 0;
		IsWork = false;
		
		holdIn("off", INFINITY); 
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("off"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in1", i)) 
					//�޽��� x�� "in" ��Ʈ�� �����ϸ�
				{
					userJob = (job_msg)x.getValOnPort("in1", i); 
					// in1 : user�� ����� ��Ʈ
					IsWork = userJob.IsWork; // �۵�����(on/off)Ȯ��
					holdIn("wait", processing_time); // wait ���� ����
					
				}
			}
		}
		else
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in1", i)) 
					//�޽��� x�� "in" ��Ʈ�� �����ϸ�
				{
					userJob = (job_msg)x.getValOnPort("in1", i); 
					// in1 : user�� ����� ��Ʈ
					IsWork = userJob.IsWork; // �۵�����(on/off)Ȯ��
					user_temp = userJob.temp; // ����µ� ����
					
				}
				if (messageOnPort(x, "in2", i)) 
					// in2 : temp_sensor�� ����� ��Ʈ
				{
					sensorJob = (job_msg)x.getValOnPort("in2", i);
					current_temp = sensorJob.temp; // ���� �µ� �� ����
					
					System.out.println("current_temp"+current_temp);
				}
				
			}
		}
			
	}
  
	public void deltint()
	{
		if (phaseIs("wait"))
		{	
			holdIn("ready", processing_time); 
	
		}
		else
		{
			if(current_temp !=0.0 && user_temp != 0.0)
			{
				if(current_temp < user_temp) // ����µ� < ����µ�
				{
					holdIn("heating", processing_time);
				}
				else if(current_temp > user_temp) // ����µ� > ����µ�
				{
					holdIn("cooling", processing_time);
				}
			}
		}
		
	}

	public message out()
	{
		message m = new message();
		//���� �̺�Ʈ�� busy�����϶��� ���۵Ǵ� ��
		if (phaseIs("wait")) // 
		{
			m.add(makeContent("out", userJob)); // user�� ���� ���� ���� �޽����� temp_sensor���� ����
		}
		else
		{
			if(IsWork==false)// �۵� �ߴ�
			{
				m.add(makeContent("out", userJob));
				holdIn("off",INFINITY);
			}
		}
		return m;
	}

	
}

