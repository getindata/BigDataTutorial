package com.getindata.hbase.exercises;

import java.util.Collection;
import java.util.HashSet;

public class GetFiveRowsAtOneShot {
	public static void main(String[] args) {
		Collection<String> rowKeys = null;
		Collection<StreamRockUser> users = getUsers(rowKeys);
		for (StreamRockUser streamRockUser : users) {
			System.out.println(streamRockUser.toString());
		}
	}

	private static Collection<StreamRockUser> getUsers(
			Collection<String> rowKeys) {
		Collection<StreamRockUser> users = new HashSet<StreamRockUser>();
		// ================================
		// TODO put your code here

		// ================================

		return users;
	}

}
