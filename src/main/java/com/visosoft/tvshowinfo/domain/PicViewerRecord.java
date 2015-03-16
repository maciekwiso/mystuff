package com.visosoft.tvshowinfo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="pic_viewer_records")
public class PicViewerRecord  implements Serializable {
	private static final long serialVersionUID = 3146428321902817003L;
	private Long id;
	private String url;
	private String title;
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
    
    @Column(name = "picv_title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

	@Override
	public String toString() {
		return "PicViewerRecord [id=" + id + ", title=" + title + ", url=" + url + ", seen=" + seen
				+ "]";
	}
    
}
