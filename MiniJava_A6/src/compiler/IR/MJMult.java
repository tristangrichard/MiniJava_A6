package compiler.IR;

import java.util.HashSet;

import compiler.PrettyPrinter;
import compiler.CODE.CODE;
import compiler.CODE.LC3.*;
import compiler.Exceptions.CodeGenException;
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
		
		// check lhs and rhs
		
		MJType leftType = this.lhs.typeCheck();
		MJType rightType = this.rhs.typeCheck();

		// the types must be the same
		
		if (!leftType.isSame(rightType)) {
			throw new TypeCheckerException("types in * op must be the same ("+leftType.getName()+","+rightType.getName()+","+this.getClass().getName()+")");
		}

		this.type = leftType;
		
		// the arguments must be integers 
		if (!this.type.isInt()) {
			throw new TypeCheckerException("Arguments to * must have type int.");
		}
		return this.type;
	}

	void variableInit(HashSet<MJVariable> initialized)
			throws TypeCheckerException {
		
		// check both arguments
		
		this.lhs.variableInit(initialized);
		this.rhs.variableInit(initialized);
	}

	public void generateCode(CODE code) throws CodeGenException {

		LC3label cont = code.newLabel();
		LC3label retu = code.newLabel();
		
		code.comment(" MULT BEGIN ");
		code.commentline(" lhs ");
		this.lhs.generateCode(code);
		code.commentline(" rhs ");
		this.rhs.generateCode(code);
		
		code.commentline( "Multiply integers");
		code.pop2(CODE.TMP0, CODE.TMP1);
		code.add(new LC3AND(CODE.TMP1, CODE.TMP1, CODE.TMP1));
		code.comment(" Branch to cont if multipier is positive ");
		code.add(new LC3BRP(cont)); // Branch to cont if TMP1 is positive
		code.comment(" Branch to retu if second multipier is zero. ");
		code.add(new LC3BRZ(retu)); // Branch to retu if TMP1 is zero
		code.add(new LC3AND(CODE.TMP0, CODE.TMP0, CODE.TMP0));
		code.comment(" Branch to retu if first multipier is zero. ");
		code.add(new LC3BRZ(retu)); // Branch to retu if TMP0 is zero
		code.comment(" Notting the negative multiplier for the loop count. ");
		code.add(new LC3AND(CODE.CONST1,CODE.CONST1,0)); // Clear CONST1
		code.add(new LC3ADD(CODE.CONST1,CODE.CONST1,CODE.TMP1)); // Copy TMP1 into CONST1
		code.add(new LC3NOT(CODE.TMP1, CODE.TMP1)); // Not CONST1
		code.add(new LC3ADD(CODE.TMP1, CODE.TMP1, 1)); // Adding 1 to the result of not
		
		// Multiplication
		code.commentline(" Multiplying... ");
		code.add(cont);
		code.add(new LC3ADD(CODE.CONST0, CODE.CONST0, CODE.TMP0)); // Adding the number with it's self
		code.add(new LC3ADD(CODE.TMP1, CODE.TMP1, -1)); // Decrementing counter (multiplier)
		code.commentline(" Branch to count as long as TMP1 is positive. ");
		code.add(new LC3BRP(cont)); // if TMP1 positive Branch to cont
		
		// if negativ
		code.commentline(" Checking if multiplier was negative... ");
		code.add(new LC3AND(CODE.CONST1,CODE.CONST1,CODE.CONST1)); // Checking if multiplier was negative
		code.add(new LC3BRP(retu)); // if positive Branch to retu
		code.commentline(" If negative convert the result. ");
		code.add(new LC3NOT(CODE.CONST0,CODE.CONST0)); // if not not the result
		code.add(new LC3ADD(CODE.CONST0, CODE.CONST0, 1)); // Adding 1 to the result of not
		
		// Return
		code.add(retu);
		code.commentline(" Prepating TMP0 by clearing it before adding the result to it ");
		code.add(new LC3AND(CODE.TMP0,CODE.TMP0,0)); // Clear TMP0
		code.add(new LC3ADD(CODE.TMP0,CODE.TMP0,CODE.CONST0)); // ADD the result to TMP0
		code.commentline(" Restoring CONST0 to original value.. ");
		code.add(new LC3AND(CODE.CONST0,CODE.CONST0,0)); // Clear CONST0
		code.commentline(" Restoring CONST1 to original value.. ");
		code.add(new LC3AND(CODE.CONST1,CODE.CONST1,0)); // Cleat CONsT1
		code.add(new LC3ADD(CODE.CONST1,CODE.CONST1,1)); // ADD 1 to CONST1
		code.commentline(" Pushing TMP0... ");
		code.push(CODE.TMP0); // Push result of multiplikation
		
		code.comment(" MULT END ");
	}

}
