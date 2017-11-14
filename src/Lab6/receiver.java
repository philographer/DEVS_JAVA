package Lab6;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class receiver extends ViewableAtomic
{
  
	protected packet packet; //generator 로부터 받은 Job
	protected double processing_time;//JOB의 실행시간 

	public receiver()
	{
		this("proc", 20);
	}

	public receiver(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");//인풋포트 생성 
		addOutport("out");//아웃풋 포트 생성;
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		packet = new packet("",0);
		
		holdIn("passive", INFINITY);//passive 상태, infinity: generator로부터 작업이 지전송 될 때까지 무한정 대기.
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("passive"))//passive 상태이면 
			//phaseIs() : 모델의 상태를 알아보는.
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))//메시지 x가 in 포트에 존재하면 
				{
					packet =(packet) x.getValOnPort("in", i);//in포트로부터 메시지를 받아와
					
					holdIn("busy", processing_time);//busy상태로 변환 후 processing_time만큼 처리한다.
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("busy"))// busy상태이면 
		{
			
			holdIn("passive", INFINITY);//passive 상태로 변환 
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))//busy 상태이면 
		{
			m.add(makeContent("out", packet));//out 포트로 job을 출력
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

