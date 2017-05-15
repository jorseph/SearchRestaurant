package com.example.currentplacedetailsonmap.data;



public class MarkerHelper {

	private String title;
	private String addr;
	private String tel;
	private Integer pic;
	
	public MarkerHelper(String title, String addr) {
		super();
		this.title = title;
		this.addr = addr;
	}

	public MarkerHelper(LocationInfo locationInfo) {
		super();
		this.title = locationInfo.getName();
		this.addr = locationInfo.getVicinity();
		this.tel = locationInfo.getTel();
		this.pic = locationInfo.getPhoto();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Integer getPic() {
		return pic;
	}

	public void setPic(Integer pic) {
		this.pic = pic;
	}

}
