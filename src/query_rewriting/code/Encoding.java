package query_rewriting.code;

import java.util.ArrayList;

import query_rewriting.generator.NodeContext;
import query_rewriting.query.node.Node;

public class Encoding extends ArrayList<NodeContext>
{

	public ArrayList<Code> generateAllCodes()
	{
		int totalNbState = getTotalNbStates();
		int nbState = size();
		ArrayList<Code> ret = new ArrayList<Code>(totalNbState);
		Code codes[] = new Code[totalNbState];

		// int maxStates[] = new int[nbState];
		//
		// for (int i = 0; i < nbState; i++)
		// {
		// maxStates[i] = this.get(i).getContext().size();
		// }

		for (int i = 0; i < totalNbState; i++)
		{
			codes[i] = new Code(nbState);
			ret.add(codes[i]);
		}
		int restNbState = totalNbState;
		
		for (int pos = 0; pos < nbState; pos++)
		{
			int currNbState = this.get(pos).getContext().size();
			int state = 0;

			for (int i = 0; i < totalNbState;)
			{
				int window = restNbState / currNbState;

				for (int w = 0; w < window; w++, i++)
				{
					codes[i].setCode(pos, state);
				}
				state = (state + 1) % currNbState;
			}
			restNbState /= currNbState;
		}
		return ret;
	}

	public int getTotalNbStates()
	{
		int ret = 1;

		for (NodeContext nc : this)
		{
			ret *= nc.getContext().size();
		}
		return ret;
	}
}