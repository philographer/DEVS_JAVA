package Lab6;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class sender extends ViewableAtomic
{
	
	protected double int_arr_time; //generator의 Job(작업) 생성시기
	protected int count;//Job이 몇 개 생겼는가를 세기 위한 함수
	private int packet_num;
	
	public sender() //생성자
	{
		this("genr", 30);
	}
  
	public sender(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out");//아웃풋 포트 설정
		addInport("in");//인 포트 설정 변경시 포트 이름이 바뀐다.
    
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()//초기화해주는함수입니다 
	{
		count = 1;//job의 개수를 1부터 셀 것이다.
		packet_num = 0; //개수
		
		holdIn("active", int_arr_time);//active 상태로 변환, int_err_time 이후에 event 발생 하겠다.
	}
  
	public void deltext(double e, message x)//외부에서 메세지가 들어왔을 때의 external함수
	{
		Continue(e);
		if(phaseIs("wait")){
			for(int i =0 ;i<x.getLength();i++){
				if(messageOnPort(x, "in",i)){
					holdIn("active",int_arr_time);
				}
			}
		}
	}

	public void deltint()//내부에서 이벤트 처리 함
	{
		if (phaseIs("active"))//active상태이면
		{
			count = count + 1;//job 카운트 개수 증가 
		}
		else if(phaseIs("wait")){
			count = 1;
			packet_num = 0;
		}
	}

	public message out()//다른 모델로 메시지 보낼때 
	{
		message m = new message();//메시지 생성;
		if(phaseIs("active")) {
			if(packet_num<5) {
				m.add(makeContent("out", new packet("packet" + count,(int)(Math.random()*5+1))));//makeContent : 생성한 개체 등록, new entity: 개체생성;
				packet_num++;
			//out의 이름을 가진 포트로, 개체를 출력(전송)
			}
			if(packet_num==5) {
				holdIn("wait",INFINITY);
			}
		}
		return m;
	}
  
	public String getTooltipText() //없어도 상관없는 메소드지만...툴팁을 표시해줌.
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time
        + "\n" + " count: " + count;
	}

}
/*
 Entity : 그냥 이름만 가지는 객체(택배의 내용물)
 MEssage :  모델끼리 Entity를 주고받고 싶을때, 필요한 정보(포트이름 등)를 담은 객체(신상정보) 
 */
