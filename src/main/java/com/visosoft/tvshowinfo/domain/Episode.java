package com.visosoft.tvshowinfo.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="show_episodes")
public class Episode implements Serializable {
	private static final long serialVersionUID = 3146428321902817003L;
	private Long id;
    private Show show;
    private Date airdate;
    private String title;
    private Short season;
    private Short number;
    private static final ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {

		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
    };

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "shwe_id")
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }
    @ManyToOne(targetEntity = Show.class)
    @JoinColumn(name = "show_id", nullable = false)
    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }
    @Column(name = "shwe_date", nullable = false)
    @Temporal(TemporalType.DATE)
    public Date getAirdate() {
        return airdate;
    }

    public void setAirdate(Date airdate) {
        this.airdate = airdate;
    }
    @Column(name = "shwe_title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Column(name = "shwe_season", nullable = false)
    public Short getSeason() {
        return season;
    }

    public void setSeason(Short season) {
        this.season = season;
    }
    @Column(name = "shwe_number", nullable = false)
    public Short getNumber() {
        return number;
    }

    public void setNumber(Short number) {
        this.number = number;
    }
    
    @Transient
    public String getSeasonAndNumber() {
    	return "" + getSeason() + "x" + getFormattedNumber();
    }
    
    @Transient
    public String getAirdateFormatted() {
    	return getAirdate() == null ? "" : sdf.get().format(getAirdate());
    }

	@Override
	public String toString() {
		return "Episode [id=" + id + ", show=" + show + ", airdate=" + airdate
				+ ", title=" + title + ", season=" + season + ", number="
				+ number + "]";
	}
	
	public String toReadableString() {
		StringBuilder b = new StringBuilder();
		return b.append(getShow().getTitle()).append(" - ").append(getSeason()).append("x")
				.append(getFormattedNumber()).append(" ").append(getTitle()).toString();
	}
	
	@Transient
	private String getFormattedNumber() {
		return String.format("%02d", getNumber());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((airdate == null) ? 0 : airdate.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((season == null) ? 0 : season.hashCode());
		result = prime * result + ((show == null) ? 0 : show.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Episode other = (Episode) obj;
		if (airdate == null) {
			if (other.airdate != null)
				return false;
		} else if (!airdate.equals(other.airdate))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (season == null) {
			if (other.season != null)
				return false;
		} else if (!season.equals(other.season))
			return false;
		if (show == null) {
			if (other.show != null)
				return false;
		} else if (!show.equals(other.show))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
}
