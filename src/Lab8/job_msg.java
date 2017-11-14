package Lab8;
import GenCol.*;

public class job_msg extends entity
{   
	boolean IsWork; // ���ۿ���
	double temp; // �µ�
	
	public job_msg(String name, boolean _work) // �۵� ���� �޽��� (true==on / false==off)
	{  
		super(name);  
		
		IsWork = _work;
		temp = 0;
	}
	
	public job_msg(String name, double _temp) // �µ� ���� �޽��� 
	{  
		super(name);  
		
		IsWork = true;
		temp = _temp;
	}
	
	
}
