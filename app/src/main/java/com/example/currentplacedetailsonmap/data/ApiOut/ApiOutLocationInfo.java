package com.example.currentplacedetailsonmap.data.ApiOut;

import java.util.List;

import com.example.currentplacedetailsonmap.data.LocationInfo;

public class ApiOutLocationInfo extends ApiOut {

	private List<LocationInfo> locationInfoList;

	public List<LocationInfo> getLocationInfoList() {
		return locationInfoList;
	}

	public void setLocationInfoList(List<LocationInfo> locationInfoList) {
		this.locationInfoList = locationInfoList;
	}
}
