package compiler.IR;

import java.util.HashSet;
import java.util.LinkedList;

import compiler.PrettyPrinter;
import compiler.Exceptions.ClassErrorMethod;
import compiler.Exceptions.MethodNotFound;
import compiler.Exceptions.TypeCheckerException;

public class MJNew extends MJExpression {

	private LinkedList<MJExpression> arglist;
	private MJMethod target;

	public MJNew(MJType type) {
		this(type, new LinkedList<MJExpression>());
	}

	public MJNew(MJType type, LinkedList<MJExpression> arglist) {
		this.type = type;
		this.arglist = arglist;
	}

	public void prettyPrint(PrettyPrinter prepri) {
		boolean first = true;

		prepri.print("new " + this.type + "(");
		for (MJExpression arg : arglist) {

			if (!first) {
				prepri.print(", ");
			} else {
				first = false;
			}

			arg.prettyPrint(prepri);
		}
		prepri.print(")");

	}

	MJType typeCheck() throws TypeCheckerException {

		// checks if target is of type class. and typechecks all arguments
		if(this.target.typeCheck().isClass()){
			for (MJExpression arg : arglist) {
				arg.typeCheck();	
			}
			return MJType.getVoidType();
		}
		else{	
			throw new TypeCheckerException(this.target.getName()+" is not of type class");
		}
	}

	void variableInit(HashSet<MJVariable> initialized)
			throws TypeCheckerException {
		
		this.target.variableInit(initialized);
		
		for (MJExpression arg : arglist) {
			arg.variableInit(initialized);	
		}
		// here you should enter the code to check whether all variables are initialized
	}

}
