package com.sds.orchestration.entity;


public enum CompareType {

	EQ("相等"),NEQ("不相等"),GT("大于"),LT("小于");
	private String desc;
	CompareType(String name) {
		this.desc=name;
	}
	public String getDesc(){
		return desc;
	}
	public static boolean contains(CompareType compareType){
		CompareType[] values=CompareType.values();
		for (CompareType ct : values) {
			if(ct==compareType)
				return true;
		}
		return false;
	}
}

