package com.getindata.hbase.exercises;

public class ScannerExercise {
	public static void main(String[] args) {
		System.out.println("Starting Program: "+ ScannerExercise.class.getCanonicalName());
		System.out.println("Readning parameters...");
		String tableName="<your-username-here>.user";
		if(args.length>0){
			tableName=args[0];
		}
		System.out.println("tablename:"+tableName);
		printUsers(tableName);
	}

	private static void printUsers(String tableName) {
		// ================================
		// TODO put your code here

		// ================================

	}

	private static  void printUser(StreamRockUser user) {
		System.out.println(user.toString());
	}
}
