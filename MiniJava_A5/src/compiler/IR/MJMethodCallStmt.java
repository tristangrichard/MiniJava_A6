package compiler.IR;

import java.util.HashSet;
import java.util.LinkedList;
import compiler.PrettyPrinter;
import compiler.Exceptions.ClassErrorMethod;
import compiler.Exceptions.ClassNotFound;
import compiler.Exceptions.MethodNotFound;
import compiler.Exceptions.TypeCheckerException;
import compiler.Exceptions.VariableNotFound;

public class MJMethodCallStmt extends MJStatement {

	private MJIdentifier method;
	private LinkedList<MJExpression> arglist;

	public MJMethodCallStmt(MJIdentifier m, LinkedList<MJExpression> arglist) {
		this.method = m;
		this.arglist = arglist;
	}

	public MJIdentifier getMethod() {
		return method;
	}

	public LinkedList<MJExpression> getArglist() {
		return arglist;
	}

	public void prettyPrint(PrettyPrinter prepri) {
		boolean first = true;

		this.method.prettyPrint(prepri);
		prepri.print("(");
		for (MJExpression arg : arglist) {

			if (!first) {
				prepri.print(", ");
			} else {
				first = false;
			}

			arg.prettyPrint(prepri);
		}
		prepri.println(");");
	}

	MJType typeCheck() throws TypeCheckerException {

		MJClass decl = null;
		MJClass cla = IR.currentClass;
		MJIdentifier ident = this.method;
		MJMethod meth;

		if(ident instanceof MJSelector){

			MJSelector sel = (MJSelector) ident;
			MJType ty = sel.getObject().typeCheck();

			if(ident.getType().isClass()){
				decl = sel.getObject().typeCheck().getDecl();
				cla = ty.getDecl();
				ident = sel.getField();
			}else throw	new TypeCheckerException(ident.getName()+" is not a Class");
		}else {
			if(ident.getName().equals("this"))
			{
				// Something should happen here
			}else if (ident.getName().equals("super"))
			{
				// Something should happen here
			}
		}

		for(MJExpression args : arglist)
		{
			args.typeCheck();
		}

		try {
			meth = IR.classes.lookupMethod(decl, cla.getName(), arglist);
		} catch (ClassErrorMethod e) {
			throw new TypeCheckerException(e.getMessage());
		} catch (MethodNotFound e) {
			throw new TypeCheckerException(e.getMessage());
		}
		this.method.type = meth.getReturnType();
		return this.method.type;
	}

	void variableInit(HashSet<MJVariable> initialized)
			throws TypeCheckerException {

		this.method.variableInit(initialized);

		for(MJExpression arg : arglist){
			arg.variableInit(initialized);}
	}

}
