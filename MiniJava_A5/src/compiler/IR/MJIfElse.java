package compiler.IR;

import java.util.HashSet;
import java.util.LinkedList;

import compiler.PrettyPrinter;
import compiler.Exceptions.TypeCheckerException;

public class MJIfElse extends MJIf {

	private MJBlock elseblock;

	public MJIfElse(MJExpression condition, MJBlock thenblock, MJBlock elseblock) {
		super(condition, thenblock);
		this.elseblock = elseblock;
	}
	
	public MJBlock getElseblock() {
		return elseblock;
	}

	public void prettyPrint(PrettyPrinter prepri) {
		super.prettyPrint(prepri);
		prepri.println("else");
		this.elseblock.prettyPrint(prepri);
	}

	MJType typeCheck() throws TypeCheckerException {
		
		elseblock.typeCheck();
		// here you should enter the code to type check this class
		
		return MJType.getVoidType();
	}

	void variableInit(HashSet<MJVariable> initialized)
			throws TypeCheckerException {
		
		// gets the variables located both in if and else blocks, and initializes the ones they have in common
		LinkedList<MJVariable> thenList = thenblock.getVariables();
		LinkedList<MJVariable> elseList = elseblock.getVariables();
		for(MJVariable thenb : thenList)
		{
			for(MJVariable elseb : elseList)
			{
				if(thenb.equals(elseb)){
					elseb.variableInit(initialized);
					thenb.variableInit(initialized);
				}
			}
		}
	}

}
