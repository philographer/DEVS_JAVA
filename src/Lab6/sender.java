package Lab6;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class sender extends ViewableAtomic
{
	
	protected double int_arr_time;
	protected int count; // ������ packet�� ����
	private int packet_num; // ������ packet�� ����
	

	
	public sender() 
	{
		this("genr", 30);
	}
  
	public sender(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out");
		addInport("in");
		int_arr_time = Int_arr_time;
		

	}
  
	public void initialize()
	{
		count = 1; // ������ packet�� ���� �ʱ�ȭ
		packet_num = 0; // ������ packet�� ���� �ʱ�ȭ
		
		holdIn("active", int_arr_time);
		
		
		
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);

	
		
		
		if (phaseIs("wait"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					holdIn("active", int_arr_time);
				}
			}
		}
	}

	public void deltint()
	{

		if (phaseIs("active"))
		{
			count = count + 1; // packet�� ���� ����
		}
		else if(phaseIs("wait"))
		{
			count = 1; // ������ packet�� ���� �ʱ�ȭ
			
			packet_num = 0; // // ������ packet�� ���� �ʱ�ȭ
		}
	}

	public message out()
	{		

		
		message m = new message();
		
		if(phaseIs("active"))
		{
			if(packet_num < 5) // ������ packet�� ���� 5�� �̸��̸�
			{
				m.add(makeContent("out", new packet("packet" + count, (int)(Math.random() * 5) + 1)));
				// packet(String name, int _arrival)
				// packet�� �����Ͽ� ������ ������(receiver)�� ����
				System.out.println("sender�κ��� packet ����");
				packet_num++;
				// ������ ��Ŷ�� �� ����
			}
		
			if(packet_num == 5)  // ������ packet�� ���� 5�����
			{
				holdIn("wait", INFINITY);
				// wait ���� ����
			}
		}
		
		return m;
	}
  
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time
        + "\n" + " count: " + count
		+ "\n" + " packet_num: " + packet_num;
	}

}
