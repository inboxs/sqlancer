package lama.mysql.ast;

import lama.LikeImplementationHelper;
import lama.Randomly;
import lama.sqlite3.ast.SQLite3Constant;

public class MySQLBinaryComparisonOperation extends MySQLExpression {

	public enum BinaryComparisonOperator {
		EQUALS("=") {
			@Override
			public MySQLConstant getExpectedValue(MySQLConstant leftVal, MySQLConstant rightVal) {
				return leftVal.isEquals(rightVal);
			}
		},
		LIKE("LIKE") {

			@Override
			public MySQLConstant getExpectedValue(MySQLConstant leftVal, MySQLConstant rightVal) {
				if (leftVal.isNull() || rightVal.isNull()) {
					return MySQLConstant.createNullConstant();
				}
				String leftStr = leftVal.castAsString();
				String rightStr = rightVal.castAsString();
				boolean matches = LikeImplementationHelper.match(leftStr, rightStr, 0, 0);
				return MySQLConstant.createBoolean(matches);
			}
			
		}
		;
		// https://bugs.mysql.com/bug.php?id=95908
		/*
		 * IS_EQUALS_NULL_SAFE("<=>") {
		 * 
		 * @Override public MySQLConstant getExpectedValue(MySQLConstant leftVal,
		 * MySQLConstant rightVal) { return leftVal.isEqualsNullSafe(rightVal); }
		 * 
		 * };
		 */

		private final String textRepresentation;

		public String getTextRepresentation() {
			return textRepresentation;
		}

		private BinaryComparisonOperator(String textRepresentation) {
			this.textRepresentation = textRepresentation;
		}

		public abstract MySQLConstant getExpectedValue(MySQLConstant leftVal, MySQLConstant rightVal);

		public static BinaryComparisonOperator getRandom() {
			return Randomly.fromOptions(BinaryComparisonOperator.values());
		}
	}

	private final MySQLExpression left;
	private final MySQLExpression right;
	private final BinaryComparisonOperator op;

	public MySQLBinaryComparisonOperation(MySQLExpression left, MySQLExpression right, BinaryComparisonOperator op) {
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public MySQLExpression getLeft() {
		return left;
	}

	public BinaryComparisonOperator getOp() {
		return op;
	}

	public MySQLExpression getRight() {
		return right;
	}

	@Override
	public MySQLConstant getExpectedValue() {
		return op.getExpectedValue(left.getExpectedValue(), right.getExpectedValue());
	}

}
