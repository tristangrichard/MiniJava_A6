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
		super.typeCheck();
		elseblock.typeCheck();
		// here you should enter the code to type check this class
		
		return MJType.getVoidType();
	}

	void variableInit(HashSet<MJVariable> initialized)
			throws TypeCheckerException {
		
		// Checks if variables are initialized
		HashSet<MJVariable> einit = (HashSet<MJVariable>)initialized.clone();
		elseblock.variableInit(einit);
		
		HashSet<MJVariable> tinit = (HashSet<MJVariable>)initialized.clone();
		thenblock.variableInit(tinit);
		
		tinit.retainAll(einit);
		initialized = tinit;
		
	}
}
