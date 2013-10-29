package compiler.IR;

import java.util.HashSet;

import compiler.PrettyPrinter;
import compiler.Exceptions.TypeCheckerException;

public class MJMult extends MJBinaryOp {

	public MJMult(MJExpression a, MJExpression b) {
		super(a, b);
	}

	public void prettyPrint(PrettyPrinter prepri) {
		this.lhs.prettyPrint(prepri);
		prepri.print(" * ");
		this.rhs.prettyPrint(prepri);
	}

	MJType typeCheck() throws TypeCheckerException {
		this.type = this.lhs.typeCheck();
		
		// checks if both sides are of type int
		if (this.lhs.typeCheck().isInt() && this.rhs.typeCheck().isInt()){
			return MJType.getIntType();
		}
		else{
			throw new TypeCheckerException("both lhs and rhs need to be integers");
		}
	}

	void variableInit(HashSet<MJVariable> initialized)
			throws TypeCheckerException {
		
		//checks if lhs and rhs are initialized
		this.lhs.variableInit(initialized);
		this.rhs.variableInit(initialized);
	}

}
