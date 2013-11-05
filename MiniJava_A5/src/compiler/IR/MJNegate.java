package compiler.IR;

import java.util.HashSet;

import compiler.PrettyPrinter;
import compiler.Exceptions.TypeCheckerException;

public class MJNegate extends MJUnaryOp {

	public MJNegate(MJExpression l) {
		super(l);
	}

	public void prettyPrint(PrettyPrinter prepri) {
		prepri.print("!");
		this.arg.prettyPrint(prepri);
	}

	MJType typeCheck() throws TypeCheckerException {
		// Save the type
		this.type = this.arg.typeCheck();
		// checks if arg is boolean or int, throws exception if not either.
		if (this.type.isInt() || this.type.isBoolean())
		{
			return MJType.getVoidType();
		}
		else{
			throw new TypeCheckerException("arg to negate should be int or boolean");
		}
	}

	void variableInit(HashSet<MJVariable> initialized)
			throws TypeCheckerException {
		
		//throws typechecker expeption if not initialized
		this.arg.variableInit(initialized);

	}

}
