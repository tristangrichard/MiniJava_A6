package compiler.IR;

import java.util.HashSet;

import compiler.PrettyPrinter;
import compiler.Exceptions.TypeCheckerException;

public class MJLess extends MJBinaryOp {

	public MJLess(MJExpression a, MJExpression b) {
		super(a, b);
	}

	public void prettyPrint(PrettyPrinter prepri) {
		this.lhs.prettyPrint(prepri);
		prepri.print(" < ");
		this.rhs.prettyPrint(prepri);
	}

	MJType typeCheck() throws TypeCheckerException {
		
		// here you should enter the code to type check this class
		if (this.lhs.typeCheck().isInt() && this.rhs.typeCheck().isInt())
		{
			return MJType.getVoidType();
		}
		else {
			throw new TypeCheckerException("both lhs and rhs need to be integers");
		}
	}

	void variableInit(HashSet<MJVariable> initialized)
			throws TypeCheckerException {
		
		this.lhs.variableInit(initialized);
		this.rhs.variableInit(initialized);
		// here you should enter the code to check whether all variables are initialized
	}

}
