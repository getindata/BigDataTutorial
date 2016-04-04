package com.getindata.hbase.exercises;

import java.util.Collection;
import java.util.HashSet;

public class GetFiveRowsAtOneShot {
	public static void main(String[] args) {
		System.out.println("Starting Program: "+ GetFiveRowsAtOneShot.class.getCanonicalName());
		System.out.println("Readning parameters...");
		String tableName="<your-username-here>.user";
		if(args.length>0){
			tableName=args[0];
		}
		System.out.println("tablename:"+tableName);
		Collection<String> rowKeys = null;
		Collection<StreamRockUser> users = getUsers(tableName,rowKeys);
		for (StreamRockUser streamRockUser : users) {
			System.out.println(streamRockUser.toString());
		}
	}

	private static Collection<StreamRockUser> getUsers(
			String tableName, Collection<String> rowKeys) {
		Collection<StreamRockUser> users = new HashSet<StreamRockUser>();
		// ================================
		// TODO put your code here

		// ================================

		return users;
	}

}
