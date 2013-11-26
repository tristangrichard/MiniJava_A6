package compiler.IR;

import java.util.HashSet;

import compiler.PrettyPrinter;
import compiler.CODE.CODE;
import compiler.CODE.LC3.*;
import compiler.Exceptions.CodeGenException;
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
		if (this.elseblock != null) {
			prepri.println("else");
			this.elseblock.prettyPrint(prepri);
		}
	}
	
	public void rewriteTwo() {
		super.rewriteTwo();
		
		this.elseblock.rewriteTwo();
	}

	MJType typeCheck() throws TypeCheckerException {

		super.typeCheck();
		this.elseblock.typeCheck();

		return MJType.getVoidType();
	}

	void variableInit(HashSet<MJVariable> initialized)
			throws TypeCheckerException {
		
		// check the condition
		this.getCondition().variableInit(initialized);

		// check the then block with a new copy of the set
		HashSet<MJVariable> thenset = (HashSet<MJVariable>) initialized.clone();
		this.getThenblock().variableInit(thenset);

		// check it with a new copy of the set
		HashSet<MJVariable> elseset;
		elseset = (HashSet<MJVariable>) initialized.clone();
		this.elseblock.variableInit(elseset);
			
		//compute the intersection of the then and the else set
		thenset.retainAll(elseset);
			
		// and add it to initialized
		initialized.addAll(thenset);
	}

	public int requiredStackSize() {
		
		int maxstack = this.getThenblock().requiredStackSize();
		
		maxstack = Math.max(maxstack, this.elseblock.requiredStackSize());
		
		return maxstack;
	}

	public void generateCode(CODE code) throws CodeGenException {
		
		LC3label ifFalse = code.newLabel();
		LC3label overElse = code.newLabel();
		super.getCondition().generateCode(code);

		code.comment(" IF/ELSE ");
		code.pop(CODE.TMP0); // Get value of variable
		code.add(new LC3BRZ(ifFalse)); // Branch to ifFalse if 0
		super.getThenblock().generateCode(code);
		code.add(new LC3BR(overElse)); // Branch to ifFalse if 0
		
		code.add(ifFalse);
		this.elseblock.generateCode(code);
		code.add(overElse);
		code.comment(" IF/ELSE END ");
	}
}
