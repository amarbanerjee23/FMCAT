package CA;
import java.util.Arrays;

import FSA.FSA;
import FSA.Simulator;
import FSA.Transition;


/**
 * @author Davide Basile
 *
 */
@SuppressWarnings("serial")
public class CA  extends FSA implements java.io.Serializable
{
	private int rank;
	private int[] initial;
	private int[] states;
	private int[][] finalstates; 
	//private CATransition[] tra;
	private static String message = "*** CA simulator ***\n";
	
	/**
	 * Invoke the super constructor and take in input the added new parameters of the automaton
	 */
	public CA()
	{
		super(message);
		try{
			System.out.println();
	        this.rank = 1;
	        this.states = new int[1];
	        this.states[0] = super.getStates();
	        this.initial = new int[1];
	        initial[0] = super.getInitial();
	        finalstates = new int[1][super.getFinalStates().length];
	        finalstates[0]= super.getFinalStates();
	       // this.tra=(CATransition[])super.getTransition();
	        super.write(this);
		}
		catch (Exception e){System.out.println("Errore inserimento");}
	}
	
	public CA(int rank, int[] initial, int[] states, int[][] finalstates,CATransition[] tra)
	{
		super(tra);
		this.rank=rank;
		this.initial=initial;
		this.states=states;
		this.finalstates=finalstates;
	}
	
	/**
	 * Print in output a description of the automaton
	 */
	public void print()
	{
		//super.print();
		/**
		 * Print in output a description of the automaton
		 */
		System.out.println("Contract automaton:");
		System.out.println("Rank: "+this.rank);
		System.out.println("Number of states: "+Arrays.toString(this.getStatesCA()));
		System.out.println("Initial state: " +Arrays.toString(this.getInitialCA()));
		System.out.print("Final states: [");
		for (int i=0;i<finalstates.length;i++)
			System.out.print(Arrays.toString(finalstates[i]));
		System.out.print("]\n");
		//System.out.println("Transitions: "+Arrays.toString(this.getTransition()));		
		System.out.println("Transitions: \n");
		Transition[] t = this.getTransition();
		for (int i=0;i<t.length;i++)
			System.out.println(t[i].toString());		
	}
	
	
	
	/**
	 * Create an instance of the simulator for an FMA
	 */
	protected Simulator createSim()
	{
		//return new FMASimulator(this);
		return null;
	}
	
	/**
	 * 
	 * @param i		the index of the transition to be showed as a message to the user
	 * @return		a new Transition for this automaton
	 */
	protected Transition createTransition(int i)
	{
		return new CATransition(i);
	}
	
	
	/**
	 * 
	 * @return	the array of final states
	 */
	public int[][] getFinalStatesCA()
	{
		return finalstates;
	}
	
	/**
	 * 
	 * @return	the array of states
	 */
	public int[] getStatesCA()
	{
		return states;
	}
	
	/**
	 * 
	 * @return	the array of initial states
	 */
	public int[] getInitialCA()
	{
		return initial;
	}
	
	/**
	 * 
	 * @return the rank of the Contract Automaton
	 */
	public int getRank()
	{
		return rank;
	}
	
	/**
	 * 
	 * @return	the array of transitions
	 */
	public CATransition[] getTransition()
	{
		Transition[] temp = super.getTransition();
		CATransition[] t = new CATransition[temp.length];
		for (int i=0;i<temp.length;i++)
				t[i]=(CATransition)temp[i];
		return t;
	}
	
	public int sumStates()
	{
		int numstates=0;
		for (int i=0;i<states.length;i++)
		{
			numstates+=states[i];
		}
		return numstates;
	}
	
	public int prodStates()
	{
		int prodstates=1;
		for (int i=0;i<states.length;i++)
		{
			prodstates*=states[i];
		}
		return prodstates;
	}
	
	/**
	 * @return a new object CA clone
	 */
	public CA clone()
	{
		CATransition[] at = this.getTransition();
		CATransition[] finalTr = new CATransition[at.length];
		for(int i=0;i<finalTr.length;i++)
		{
			int[] in=at[i].getInitialP();
			int[] l=at[i].getLabelP();
			int[] f= at[i].getFinalP();
			finalTr[i] = new CATransition(Arrays.copyOf(in,in.length),Arrays.copyOf(l,l.length),Arrays.copyOf(f,f.length));
		}
		
		int[][] nf = new int[finalstates.length][];
		for (int i=0;i<finalstates.length;i++)
			nf[i]=Arrays.copyOf(finalstates[i], finalstates[i].length);
		
		return new CA(rank,Arrays.copyOf(initial, initial.length),Arrays.copyOf(states, states.length),finalstates,finalTr);
	}
	
	/**
	 * compute the projection on the i-th principal, or null if rank=1
	 * @param i		index of the CA
	 * @return		the ith principal
	 */
	public CA proj(int i)
	{
		if ((i<0)||(i>rank)) //check if the parameter i is in the rank of the CA
			return null;
		CATransition[] tra = this.getTransition();
		int[] init = new int[1];
		init[0]=initial[i];
		int[] st= new int[1];
		st[0]= states[i];
		int[][] fi = new int[1][];
		fi[0]=finalstates[i];
		CATransition[] t = new CATransition[tra.length];
		int pointer=0;
		for (int ind=0;ind<tra.length;ind++)
		{
			CATransition tt= ((CATransition)tra[ind]);
			int label = tt.getLabelP()[i];
			if(label!=0)
			{
				int source =  tt.getInitialP()[i];
				int dest = tt.getFinalP()[i];
				int[] sou = new int[1];
				sou[0]=source;
				int[] des = new int[1];
				des[0]=dest;
				int[] lab = new int[1];
				lab[0]=label;
				CATransition selected = new CATransition(sou,lab,des);
				boolean skip=false;
				for(int j=0;j<pointer;j++)
				{
					if (t[j].equals(selected))
					{
						skip=true;
						break;
					}
				}
				if (!skip)
				{
					t[pointer]=selected;
					pointer++;
				}
			}
		}
		
		tra = new CATransition[pointer];
		for (int ind=0;ind<pointer;ind++)
			tra[ind]=t[ind];
		return new CA(1,init,st,fi,tra);
	}
	
	/**
	 * compute the most permissive controller for strong agreement
	 * @return KS
	 */
	public CA smpc()
	{
		CA a = this.clone();
		CATransition[] t = a.getTransition();
		for (int i=0;i<t.length;i++)
		{
			if (!t[i].match())
			{
				t[i] = null;
			}
		}
		a.setTransition(t);
		a = CAUtil.removeHangedTransitions(a);
		a = CAUtil.removeUnreachable(a);
		return a;
	}
	
	/**
	 * compute the most permissive controller for agreement
	 * @return
	 */
	public CA mpc()
	{
		CA a = this.clone();
		CATransition[] t = a.getTransition();
		for (int i=0;i<t.length;i++)
		{
			if (t[i].request())
			{
				t[i] = null;
			}
		}
		a.setTransition(t);
		a = CAUtil.removeHangedTransitions(a);
		a = CAUtil.removeUnreachable(a);
		return a;
	}
	
	/**
	 * 
	 * @return  true if the CA is strongly safe
	 */
	public boolean strongSafe()
	{
		CA at = this.clone();
		at = CAUtil.removeHangedTransitions(at);
		at = CAUtil.removeUnreachable(at);
		CA a = this.smpc();
		return (a.getTransition().length == at.getTransition().length);
	}
	
	/**
	 * 
	 * @return true if the CA admits strong agreement
	 */
	public boolean strongAgreement()
	{
		if (this.strongSafe())
			return true;
		CA a = this.smpc();
		return (a.getTransition().length==0);
	}
	
	/**
	 * 
	 * @return true if the CA is safe
	 */
	public boolean safe()
	{
		CA at = this.clone();
		at = CAUtil.removeHangedTransitions(at);
		at = CAUtil.removeUnreachable(at);
		CA a = this.mpc();
		return (a.getTransition().length == at.getTransition().length);
	}
	
	/**
	 * 
	 * @return true if the CA admits agreement
	 */
    public boolean agreement()
    {
    	if (this.safe())
			return true;
		CA a = this.mpc();
		return (a.getTransition().length==0);
    }
	
	/**
	 * 
	 * @return true if the automaton enjoys the branching condition, false otherwise
	 */
	public boolean branchingCondition()
	{
		/**
		 * for all transitions:
		 * 		if t is a match select the state s of the sender and the label l
		 * 			for all reachable states
		 * 				if the sender is in state s
		 * 					if there is no transition from the selected state with label l
		 * 						return false
		 */
		CATransition[] t = this.getTransition();
		int[][] reach = this.reachableStates();
		for (int i=0;i<t.length;i++)
		{
			if (t[i].match())
			{
				int[] l=t[i].getLabelP();
				int s = t[i].sender();
				for (int j=0;j<reach.length;j++)
				{
					if (reach[i][s]==t[i].getInitialP()[s])
					{
						int z=0;
						boolean found = false;
						while ((!found)&&(z<t.length))
						{
							found=Arrays.equals(t[z].getInitialP(), reach[i])&&Arrays.equals(t[z].getLabelP(), l);
							z++;
						}
						if (!found)
							return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @return true if there are mixed choice, false otherwise
	 */
	public boolean mixedChoice()
	{
		/**
		 * for all transitions
		 * 		for all transitions
		 * 			if the two transitions have the same initial state and different sender return false
		 */
		CATransition[] t = this.getTransition();
		for (int i=0;i<t.length;i++)
		{
			for(int j=i+1;j<t.length;j++)
			{
				if (Arrays.equals(t[i].getInitialP(), t[j].getInitialP())&&t[i].sender()!=t[j].sender())
					return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return all the reachable states 
	 */
	private int[][] reachableStates()
	{
		CA aut=this.clone();
		aut = CAUtil.removeUnreachable(aut);
		aut = CAUtil.removeHangedTransitions(aut);
		int[][] s = new int[this.prodStates()][];
		s[0]=aut.getInitialCA();
		CATransition[] t = aut.getTransition();
		int pointer=1;
		for (int i=0;i<t.length;i++)
		{
			int[] p = t[i].getFinalP();
			boolean found=false;
			int j=0;
			while((!found)&&(s[j]!=null))
			{
				found = Arrays.equals(p, s[j]);
				j++;
			}
			if (!found)
			{
				s[pointer]=p;
				pointer++;
			}
		}
	    int[][] f = new int[pointer][];
	    for (int i=0;i<pointer;i++)
	    	f[i]=s[i];
		return f;
	}
}
