package com.sds.orchestration.entity;

public enum DataType {
	STRING,INT;
	public static boolean contains(DataType dataType){
		DataType[] values=DataType.values();
		for (DataType dt : values) {
			if(dt==dataType)
				return true;
		}
		return false;
	}
}
