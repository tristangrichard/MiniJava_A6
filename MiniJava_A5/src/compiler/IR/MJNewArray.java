package compiler.IR;

import java.util.HashSet;

import compiler.PrettyPrinter;
import compiler.Exceptions.TypeCheckerException;

public class MJNewArray extends MJNew {

	private MJExpression size;

	public MJNewArray(MJType type, MJExpression size) {
		super(type);
		this.size = size;
	}

	public void prettyPrint(PrettyPrinter prepri) {
		prepri.print("new " + this.type.getBaseType().getName() + "[");
		this.size.prettyPrint(prepri);
		prepri.print("]");
	}

	MJType typeCheck() throws TypeCheckerException {

		// Gets the type of size
		this.type = this.size.typeCheck();
		
		// checks if the size of the array is of type int
		if(this.type.isInt())
		{
			return MJType.getVoidType();
		}
		else
		{
			throw new TypeCheckerException("size must be an integer");
		}
	}

	void variableInit(HashSet<MJVariable> initialized)
			throws TypeCheckerException {
		
		this.size.variableInit(initialized);
		// checks if size is initialized to a value
	}

}
