package Lab7;

import GenCol.entity;

public class job extends entity
{

	boolean isLast; // 마지막인지 나타냄
	
	public job(String name, boolean _isLast)
	{
		super(name);
		isLast = _isLast;
	}
	
}
