package com.example.currentplacedetailsonmap.data;


import java.io.Serializable;

public class LocationInfo implements Serializable {
	private String placeid;
	private String lat;
	private String lng;
	private String vicinity;  //addr
	private String tel;
	private String name;
	private String atype;
	private String photo_URL;
	private int rating;
    private int score;
	private boolean nowopen;
    private String phone;

	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public LocationInfo(String placeid, String lat, String lng, String vicinity, String tel,
			String name, String atype, String photo_URL,int rating, int score, boolean nowopen, String phone) {
		super();
		this.placeid = placeid;
		this.lat = lat;
		this.lng = lng;
		this.vicinity = vicinity;
		this.tel = tel;
		this.name = name;
		this.atype = atype;
		this.photo_URL = photo_URL;
		this.rating = rating;
        this.score = score;
		this.nowopen = nowopen;
        this.phone = phone;
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
	public String getPlaceid() {return placeid;}
	public void setPhoto(Integer photo) {
		this.photo_URL = photo_URL;
	}
	public int getRating() {return rating;}
	public int getScore() {return score;}
    public void setScore(int score) {this.score = score;}
	public boolean getOpen() {return nowopen;}
	public void setOpen(boolean nowopen) {this.nowopen = nowopen;}
    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}
}
