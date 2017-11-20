package Lab9;

import GenCol.Queue;
import GenCol.entity;
import genDevs.modeling.message;
import simView.ViewableAtomic;

public class processor extends ViewableAtomic
{

	protected Queue q;
	protected entity job;
	protected double processing_time;

	int size; // maximum queue size
	int loss;  // 처리 하지 못 한 Job의 수
	int who; // Job을 처리하지 못한 processor
	double temp_time; // processor의 sigma 값 저장 변수

	public processor()
	{
		this("proc", 20, 30);
	}

	public processor(String name, double Processing_time, int _size)
	{
		super(name);
    
		addInport("in");
		addOutport("out1"); // solved port와 연결 (Job 처리 완료)
		addOutport("out2"); // loss port와 연결(loss msg 전송)

		who = Integer.parseInt(name.substring(9));
		// substring () 지정 위치 다음에서부터 문자열의 끝까지 문자열을 가져오는 메소드
		size = _size; // maximum queue size
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		q = new Queue();
		loss = 0;

		job = new entity("");
		
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
					job = x.getValOnPort("in", i);
					
					holdIn("busy", processing_time);
				}
			}
		} else if(phaseIs("busy")) {
		    for (int i = 0; i < x.getLength(); i++)
			{
				if(q.size() < size) { // queue size < maximum queue size
				    entity job = x.getValOnPort("in", i);
				    q.add(job); // queue에 Job 저장
                } else {
				    loss ++;
				    temp_time = sigma;
				    holdIn("loss", 0);
                }
			}
        }
	}

	// Q. 언제 호출??
	public void deltint()
	{
		if (phaseIs("loss"))
		{
			job = new entity("");
			holdIn("busy", temp_time);
		} else {
		    if(!q.isEmpty()) {
		        job = (entity)q.removeFirst();
		        holdIn("busy", processing_time);
            } else {
		        holdIn("passive", INFINITY);
            }
        }
	}

	// 언제?
	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		{
			m.add(makeContent("out1", job)); // 처리 완료한 Job을 Evaluator에 전송
		}
		if(phaseIs("loss")) { // processor의 queue에 더이상 저장을 하지 못해 job을 처리할 수 없는 경우
		    m.add(makeContent("out2", new loss_msg(who+":"+loss, who, loss))); // 처리 완료한 Job을 Evaluator에 전송
            // loss msg를 evaluator에게 전송
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

