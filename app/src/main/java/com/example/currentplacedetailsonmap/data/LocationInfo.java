package com.example.currentplacedetailsonmap.data;


public class LocationInfo {

	private String lat;
	private String lng;
	private String vicinity;  //addr
	private String tel;
	private String name;
	private String atype;
	private String photo_URL;

	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public LocationInfo(String lat, String lng, String vicinity, String tel,
			String name, String atype, String photo_URL) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.vicinity = vicinity;
		this.tel = tel;
		this.name = name;
		this.atype = atype;
		this.photo_URL = photo_URL;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getVicinity() {
		return vicinity;
	}
	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}
	public String getAtype() {
		return atype;
	}
	public void setAtype(String atype) {
		this.atype = atype;
	}
	public String getPhoto() {
		return photo_URL;
	}
	public void setPhoto(Integer photo) {
		this.photo_URL = photo_URL;
	}
}
