package Lab6;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class router extends ViewableAtomic
{
  
	protected packet packet;
	protected Queue q;
	protected double processing_time;//JOB의 실행시간 

	public router()
	{
		this("proc", 20);
	}

	public router(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");//인풋포트 생성 
		for(int i = 1;i<=5;i++) {
			addOutport("out"+i);
			
		}
		addOutport("out");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		packet = new packet("",0);
		q = new Queue();
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
					packet = (packet)x.getValOnPort("in", i);//in포트로부터 메시지를 받아와
					q.add(packet);
					if(q.size()==5) {
						holdIn("sending",processing_time);
					}
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
		if (phaseIs("sending"))//busy 상태이면 
		{
			if(!q.isEmpty()) {
				packet = (packet) q.removeFirst();
				int portNum = packet.getArrival();

				m.add(makeContent("out"+portNum, packet));//out 포트로 job을 출력 
				holdIn("sending",processing_time);
			}
			else {
				m.add(makeContent("out",new packet("done",0)));
				//모두 전송햇을때.
				holdIn("passive",processing_time);
			}
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

