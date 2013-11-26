package compiler.IR;

import java.util.HashSet;

import compiler.PrettyPrinter;
import compiler.CODE.CODE;
import compiler.CODE.LC3.*;
import compiler.Exceptions.CodeGenException;
import compiler.Exceptions.TypeCheckerException;

public class MJMinus extends MJBinaryOp {

	public void prettyPrint(PrettyPrinter prepri) {
		this.lhs.prettyPrint(prepri);
		prepri.print(" - ");
		this.rhs.prettyPrint(prepri);
	}

	MJType typeCheck() throws TypeCheckerException {
		
		// check lhs and rhs
		
		MJType leftType = this.lhs.typeCheck();
		MJType rightType = this.rhs.typeCheck();

		// the types must be the same
		
		if (!leftType.isSame(rightType)) {
			throw new TypeCheckerException("types in - op must be the same ("+leftType.getName()+","+rightType.getName()+","+this.getClass().getName()+")");
		}

		this.type = leftType;
		
		// arguments must be int
		
		if (!this.type.isInt()) {
			new TypeCheckerException("Arguments to - must have type int.");
		}
		
		return this.type;
	}

	void variableInit(HashSet<MJVariable> initialized)
			throws TypeCheckerException {		
		
		// just check both sides
		
		this.lhs.variableInit(initialized);
		this.rhs.variableInit(initialized);
	}

	public void generateCode(CODE code) throws CodeGenException {
		code.comment(" MINUS BEGIN ");
		code.commentline(" lhs ");
		this.lhs.generateCode(code);
		code.commentline(" rhs ");
		this.rhs.generateCode(code);
		
		code.commentline("MINUS");
		code.pop2(CODE.TMP0, CODE.TMP1); // get value of left and right hand side
		code.add(new LC3NOT(CODE.TMP1, CODE.TMP1)); // Not right hand side
		code.add(new LC3ADD(CODE.TMP1,CODE.TMP1,1)); // Add 1 to result of NOT opreation
		code.add(new LC3ADD(CODE.TMP0, CODE.TMP0, CODE.TMP1)); // Evalute addition operation
		code.push(CODE.TMP0); // Push result
		
		code.comment(" MINUS END ");
	}

}
