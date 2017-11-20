package Lab9;

import SimpArc.genr;
import SimpArc.proc;
import SimpArc.transd;
import simView.ViewableAtomic;
import simView.ViewableComponent;
import simView.ViewableDigraph;

import java.awt.*;

public class RR extends ViewableDigraph
{

	final int NODE = 5;
	final int QUEUE = 0;
	int [] proc_t = {0, 100, 200, 450, 300, 500, 300}; // processing time


	public RR()
	{
		super("gpt");
    	
		ViewableAtomic g = new genr("g", 10);
		ViewableAtomic s = new scheduler("sched", 0, NODE);
		ViewableAtomic e = new evaluator("evaluator");

		add(g);
		add(s);
		add(e);

		addCoupling(g, "out", e, "arrive");
		addCoupling(g, "out", s, "in");

		for(int i=1; i<=NODE; i++) {
			ViewableAtomic p = new processor("processor"+i, proc_t[i], QUEUE);
			add(p);
			addCoupling(s, "out"+i, p, "in");
			addCoupling(p, "out2", e, "loss");
			addCoupling(p, "out1", e, "solved");
		}
	}

    
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(988, 646);
        ((ViewableComponent)withName("g")).setPreferredLocation(new Point(578, 154));
        ((ViewableComponent)withName("sched")).setPreferredLocation(new Point(465, 476));
        ((ViewableComponent)withName("evaluator")).setPreferredLocation(new Point(59, 148));
    }
}