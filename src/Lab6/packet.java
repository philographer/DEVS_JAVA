package Lab6;
import simView.*;
import java.awt.*;
import GenCol.*;

public class packet extends entity{
	int arrival;
	public packet(String name, int _arrival) 
	{
		super(name);
		arrival = _arrival;
	}
	
	public int getArrival() {
		return arrival;
	}
}
