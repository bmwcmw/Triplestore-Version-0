package queryProcessor;

import java.util.LinkedList;

public class QueryRewriter {
	
	public static Graph rewrite(LinkedList<Pattern> triplepatterns){
		Pattern p;
		int countVar=0;
		while((p = triplepatterns.getFirst()) != null){
			countVar++;
			if (p.getS().isVariable()) 
			countVar = var++;
			else if (NextToken.isLiteral() = true)
			then
			
			countVar = 0;
			/* Re–writing Queries */
			for (countVar(Tp
			) = 0 to n)
			
			Qs
			n = Tp
			;
			g = (Qs
			n
			)
			++;

			triplepatterns.removeFirst();
		}
		return null;
	}
	
	public static boolean isVariable(String elem){
		return elem.startsWith("?");
	}
	
}
