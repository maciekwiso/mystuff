package com.visosoft.tvshowinfo.domain;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="pic_viewer_records")
public class PicViewerRecord  implements Serializable {
	private static final long serialVersionUID = 3146428321902817003L;
	private Long id;
	private String url;
	private String title;
    private String groupName;
	private boolean seen;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "picv_id")
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }
    
    @Column(name = "picv_url", nullable = false)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    @Column(name = "picv_seen", nullable = false)
    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
    
    @Column(name = "picv_title", nullable = false, length = 65535)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "picv_group_name", nullable = false)
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
	public String toString() {
		return "PicViewerRecord [id=" + id + ", title=" + title + ", url=" + url + ", seen=" + seen
				+ ", groupName=" + groupName + "]";
	}

    @Transient
    public boolean isVideo() {
        return url.endsWith("mp4");
    }
    
}
