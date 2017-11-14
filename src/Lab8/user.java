package Lab8;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class user extends ViewableAtomic
{
	double user_temp; //user�� ����µ�
	int count = 0;
	  
	public user() // �⺻ ������
	{
		this("genr");
	}
  
	public user(String name)
	{
		super(name);

		addOutport("out"); // �ƿ�ǲ ��Ʈ ����
    
	}
  
	public void initialize() // �ʱ�ȭ
	{
		holdIn("READY", 10); 
		user_temp = 24; // user�� ��� �µ� 24

	}
  
	public void deltext(double e, message x) // �ܺο��� �޽����� ������ �� ó��,  external function
	{
		Continue(e);
		
	}

	public void deltint()
	// ���ο����� �̺�Ʈ ó�� �Լ�
	{
		if (phaseIs("READY")) // active �����̸�
		{
			holdIn("SWITCH ON", 10); 
		}
	}
	
	public message out() // �ٸ� �𵨷� �޽���(�̺�Ʈ)�� ���� ��
	{
		message m = new message(); // �޽��� ����
		
		if(phaseIs("READY"))
		{
			m.add(makeContent("out", new job_msg("on", true)));
			// ������ �۵� �޽���(on)�� controller���� ����
		}
		else if(phaseIs("SWITCH ON"))
		{
			m.add(makeContent("out", new job_msg("user_temp : "+user_temp, user_temp)));
			// user�� ��� �µ��� controller���� ����
			holdIn("SET TEMP",60);
			// SET TEMP ���·� ����
		}
		else if(phaseIs("SET TEMP"))
		{
			m.add(makeContent("out", new job_msg("off", false)));
			// ������ �ߴܸ޽���(off)�� controller���� ����
			holdIn("SWITCH OFF", INFINITY);
			// SwITCH OFF ���·� ���� = �� ����
		}
			
		return m;
	}
  
	public String getTooltipText() // ��� ��� ���� �޼ҵ� ����
	{
		return
        super.getTooltipText()
        + "\n" + " count: " + count;
	}

}
