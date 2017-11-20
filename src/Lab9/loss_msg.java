package Lab9;

import GenCol.entity;

public class loss_msg extends entity
{
	int loss; // loss 메시지 개수
	int who; // 어떤 프로세서로 부터 loss 메시지 발생
	public loss_msg(String name, int _who, int _loss) {
		super(name);
		loss = _loss;
		who = _who;
	}
	
}
